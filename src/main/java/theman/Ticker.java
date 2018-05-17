/*
 * Copyright 2017 Mottl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theman;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import theman.utils.Pair;
import jota.dto.response.GetBalancesResponse;
import theman.interfaces.EBlockerInputs;
import theman.interfaces.IPublish;
import jota.model.Transaction;
import theman.utils.HashList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author theman
 */
public class Ticker implements Runnable {

    private Controler con;
    private List<BalanceRecorder> addressesToListenTo;
    HashMap<String, Pair< EBlockerInputs, List<String>>> spyMap = new HashMap<>();
    private boolean shuttingDown = false;
    private Thread balanceTicker;
    private final Logger log = LoggerFactory.getLogger(Ticker.class);
    private HashList hashestobuildon;
    private IPublish publisher;

//    public Ticker(Controler con, List<BalanceRecorder> addressesToListenTo) {
//        this.con = con;
//        this.addressesToListenTo = addressesToListenTo;
//    }
//
//    public Ticker(Controler con, BalanceRecorder addressToListenTo) {
//        this.con = con;
//        addressesToListenTo = new ArrayList<>();
//        addressesToListenTo.add(addressToListenTo);
//    }
    public Ticker(Controler con,IPublish publisher) {
        this.con = con;
        addressesToListenTo = new ArrayList<>();
        this.hashestobuildon = con.getHashesToBuildOn();
        this.publisher=publisher;
    }

    private Stack<BalanceRecorder> recordersToAdd = new Stack<>();

    public void addBalanceRecorder(BalanceRecorder addressToListenTo) {
        recordersToAdd.push(addressToListenTo);

    }

    public boolean removeBalanceRecorder(BalanceRecorder addressToListenTo) {
        return addressesToListenTo.remove(addressToListenTo);
    }

    private Stack<Pair<String, EBlockerInputs>> toAdd = new Stack<>();

    public void addSpy(String key, EBlockerInputs Type) {
        this.toAdd.push(new Pair<>(key, Type));

    }

    public boolean removeSpy(String hash) {
        EBlockerInputs e = spyMap.remove(hash).left;
        return e != null;
    }

    public void run() {
        

        while (!shuttingDown) {
            try {
                while (!recordersToAdd.empty()) {
                    BalanceRecorder addressToListenTo = recordersToAdd.pop();
                    this.addressesToListenTo.add(addressToListenTo);
                    ExtendetIotaAPI api = con.getGoodApiConnection();
//con.getGoodApiConnection().findTransactionsByDigests(digests)

                    GetBalancesResponse getBalancesResponse = api.getBalances(100, new String[]{addressToListenTo.getAddress()});
                    String[] balances = getBalancesResponse.getBalances();

                    log("Added a new addres which will be watched for change continuosly!");
                    long balance = 0;
                    for (String s : balances) {
                        balance += Long.parseLong(s);
                        //cp.log(s);
                    }
                    addressToListenTo.setBalance(balance);
                    log("its balance is " + addressToListenTo.getBalance());
                    api.done();

                }

                while (!toAdd.empty()) {
                    Pair<String, EBlockerInputs> pair = toAdd.pop();
                    String key = pair.left;
                    EBlockerInputs type = pair.right;
                    ExtendetIotaAPI current = con.getGoodApiConnection();
                    switch (type) {
                        case Address:
                            List<String> start = new ArrayList<>();
                            log("Added a new " + type.text() + " which will be watched for apperence on the tangle continuosly!");
                            String[] ss = current.findTransactionsByAddresses(key).getHashes();
                            for (String s : ss) {
                                if (!start.contains(s)) {
                                    start.add(s);
                                }
                            }
                            log(start.size() + " hashes were found already containing it.");
                            this.spyMap.put(key, new Pair(type, start));
                            break;
                        default:
                            log("case not implemented yet");
                    }
                    current.done();
                }

                if (!addressesToListenTo.isEmpty()) {
                    List<String> addresses = addressesToListenTo.stream().map(BalanceRecorder::getAddress).collect(Collectors.toList());
                    ExtendetIotaAPI current = con.getGoodApiConnection();
                    GetBalancesResponse getBalancesResponse = current.getBalances(100, addresses);
//con.getGoodApiConnection().findTransactionsByDigests(digests)
                    String[] balances = getBalancesResponse.getBalances();
                    for (int i = 0; i < addressesToListenTo.size(); i++) {
                        BalanceRecorder br = addressesToListenTo.get(i);
                        if (Long.parseLong(balances[i]) >= (br.getBalance() + br.getAlertWhenChangedByValueOf())) {
                            log("Address " + br.getAddress() + " changed from " + br.getBalance() + " to " + Long.parseLong(balances[i]));
                            br.setBalance(Long.parseLong(balances[i]));
                            br.setValidated();
                            addressesToListenTo.remove(br);
                            //notify later..
                        }
                    }
                    current.done();
                }
                if (!spyMap.isEmpty()) {
                    ExtendetIotaAPI current = con.getGoodApiConnection();
                    spyMap.forEach((k, v) -> {
                        switch (v.left) {
                            case Address:
                                List<String> start = new ArrayList<String>(v.right);
                                List<String> newa = new ArrayList<String>();
                                String[] ss = current.findTransactionsByAddresses(k).getHashes();
                                for (String s : ss) {
                                    if (!start.contains(s)) {
                                        newa.add(s);
                                    }
                                }
                                if (!newa.isEmpty()) {                                    
                                    newa.forEach(s -> {
                                        log("A new transaction on address: " + k + " came in! Hash: " + s);
                                        hashestobuildon.add(
                                                new BalanceRecorder("Block Hash", s, "")
                                        );
                                    });
                                    List<Transaction> gt = current.getTransactionsObjects(newa.toArray(new String[0]));
                                    gt.forEach(g -> {
                                        log(g.toString());
                                        hashestobuildon.add(
                                                new BalanceRecorder("Block Trunk", g.getTrunkTransaction(), "")
                                        );
                                        hashestobuildon.add(new BalanceRecorder("Block Branch", g.getBranchTransaction(), ""));
                                    }
                                    );
                                    start.addAll(newa);
                                    v.right = start;
                                }

                                break;
                            default:
                                log("case not implemented yet");
                        }
                    });
                    current.done();

                }
            } catch (IllegalStateException ex) {
                log("Balance Changed Listener got a bad responce. Try again now..");
            }
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                log.error("Balance rescan interrupted.");
            }

        }

    }

    public void shutdown() throws InterruptedException {
        shuttingDown = true;
        try {
            if (balanceTicker != null && balanceTicker.isAlive()) {
                balanceTicker.join();
            }
        } catch (Exception e) {
            log.error("Error in shutdown", e);
        }

    }

    public void log(String s) {
        finalizePublication(new Publish().setLog(s));

    }

    public void addHashToSwing(BalanceRecorder b) {
        finalizePublication(new Publish().setBalanceRecorder(b));
    }

    private Stack<Publish> temp = new Stack<>();

    private void finalizePublication(Publish topublish) {
        if (publisher != null) {
            publisher.publish(topublish);
            while (!temp.empty()) {
                publisher.publish(temp.pop());
            }

        } else {
            temp.push(topublish);
        }

    }

}
