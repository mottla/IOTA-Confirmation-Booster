/*
 * Copyright 2017 theman.
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

import theman.interfaces.IControlerPanel;
import cfb.pearldiver.PearlDiverLocalPoW;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import jota.dto.response.GetBalancesAndFormatResponse;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.GetTransactionsToApproveResponse;
import jota.error.BroadcastAndStoreException;
import jota.error.InvalidAddressException;
import jota.error.InvalidSecurityLevelException;
import theman.interfaces.IControler;
import theman.interfaces.IPublish;
import jota.model.Input;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;
import theman.utils.HashList;
import jota.utils.IotaAPIUtils;
import jota.utils.StopWatch;

/**
 *
 * @author theman
 */
public class Controler implements IControler {

    private static final String TEST_SEED1 = "KQSJONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULM99";
    private static final String TEST_SEED2 = "KQSJONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULM9A";
    private static final String TEST_SEED3 = "KQSJONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULM9B";
    private static final String TEST_SEED4 = "KQSJONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULM9C";
    private static final String Seed1k = "KQSJONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULMCC";
    private static final String Seed1k_reciver = "AAAAONBIQVYEFXBZYUNYVLHXWSLVJKTPTEBPYGTRGAEA9XJVGLHWCHU9C9NUTSJWADENSIIQYQ9YULMCC";
    String Seed1k_reciver_address = "FJRDZ9HHXRQUCZOIYDLBOUVHUPZLCBWBXKIDAZQXOJMKOGFBVEICHOCVDGSQPKPGEEERYAPWWOSZKGNCZFTTKQYGP9";
    private static final String tangleBloxAddress = "WJWFNLVTTFIFDFSBUWKBEFCHEEBTAXM9HVJJXJXV9DPVLSLPYLKU9CRFHESHEJZYIGWBNI9UYYWFXRJHDEXSGUZKKD";
    private final List<ExtendetIotaAPI> dolmetschers;
    private final Stack<String[]> tipsToApprove;
    private final Random rand = new Random();

    private final IControlerPanel UIControler;

    private List<String[]> found;
    private final List<Input> inputs = null;
    private final HashList hashestobuildon;
    private final IPublish publisher;
    private boolean shutdown = false;

    Controler(IControlerPanel cp, IPublish publisher) {
        this.UIControler = cp;
        this.tipsToApprove = new NotifierStack();
        dolmetschers = Collections.synchronizedList(new ArrayList<>());
        hashestobuildon = new HashList((IControler) this);
        this.publisher = publisher;
        new Thread(new TipsUpdater(), "TipsUpdater Thread").start();

        //test();
    }

    private class NotifierStack extends Stack<String[]> {

        @Override
        public String[] pop() {
            if (this.size() < 2) {
                log("need to find some new tips now.. wait a little");
                findGoodTips(1, 0).forEach((tip) -> {
                    //add at index 0, so the best two tips will be the first ones to pop()
                    this.add(0, tip);
                });
            }
            return super.pop();
        }
    }

    private class TipsUpdater implements Runnable {

        @Override
        public void run() {

            while (!shutdown) {
                if (tipsToApprove.size() <= 2) {
                    GetTransactionsToApproveResponse gattr = getGoodApiConnection().getTransactionsToApprove(5, null, 1);
                    log("Auto Attaching : " + gattr.getTrunkTransaction() + " and " + gattr.getBranchTransaction());
                    tipsToApprove.add(new String[]{gattr.getTrunkTransaction(), gattr.getBranchTransaction()});
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void shutdown() {

    }

//    public void test() {
//        //String[] k = new String[]{"http", "service.iotasupport.com", "14265"};
//        String[] k = new String[]{"http", "eugene.iota.community", "14265"};
//        IotaAPI api = new IotaAPI.Builder(customCurl).localPoW(pow)
//                .protocol(k[0])
//                .host(k[1])
//                .port(k[2]).build();
//
//        String[] s = new String[]{"WJWFNLVTTFIFDFSBUWKBEFCHEEBTAXM9HVJJXJXV9DPVLSLPYLKU9CRFHESHEJZYIGWBNI9UYYWFXRJHDEXSGUZKKD"};
//        FindTransactionResponse tr = api.findTransactionsByAddresses(s);
//        for (int i = 0; i < tr.getHashes().length; i++) {
//            log(tr.getHashes()[i]);
//        }
//        List<Transaction> gt = api.getTransactionsObjects(tr.getHashes());
//        gt.forEach(g -> log(g.toString()));
////        
////        String[] s2 = new String[]{tangleBloxAddress};
////        GetBalancesResponse getBalancesResponse = api.getBalances(100, Arrays.asList(s2));
////        List<String> balances = Arrays.asList(getBalancesResponse.getBalances());
////
////        balances.forEach(t -> {
////            System.out.println(t);
////
////        });
//    }
//    public synchronized void addListener(IPublish p) throws Exception {
//        this.publisher = p;
//        log("Listening added..");
//        if (dolmetschers.isEmpty()) {
//            connect();
//        }
//        String listen = UIControler.getListentoHash();
//        ticker.addSpy(listen, UIControler.getBlockerType());
//
//    }
    public void clearHistory() {
        hashestobuildon.clear();
    }

    public void reloadConnections() {
        connect();

    }

    //this method causes huge memory leak due to a few million int[].. profiling should be done 
    public void loadInfos() {

        if (dolmetschers.isEmpty()) {
            connect();
        }
        log("Gathering Infos started..");
        ExtendetIotaAPI api = getGoodApiConnection();
        switch (UIControler.getInputType()) {
            case Address:
                break;
            case AddressH:
                break;
            case Seed:
                try {
                    String[] adds = new String[35];
                    int startAddScann = 0;
                    GetBalancesAndFormatResponse g = null;
                    String input = UIControler.getTransactionSeed();
                    for (int i = startAddScann; i < adds.length + startAddScann; i++) {
                        try {
                            adds[i - startAddScann] = IotaAPIUtils.newAddress(input, 2, i, false, SpongeFactory.create(SpongeFactory.Mode.KERL));
                        } catch (InvalidAddressException ex) {
                            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    List<String> addresses = Arrays.asList(adds);
                    g = api.getBalanceAndFormat(addresses, 0, 0, 0, new StopWatch(), 2);
                    showBalance("Total balance found: " + g.getTotalBalance() + "i");
                    log("Balance scann done.");
                    log("Balance on addresses from " + startAddScann + " to " + (startAddScann + adds.length) + " show a total balance of: " + String.valueOf(g.getTotalBalance()));
                    //inputs = g.getInput();
                    log("The following inputs where found:");
                    g.getInput().forEach(i -> log(i.toString()));
                } catch (InvalidSecurityLevelException ex) {
                    Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case BundleH:
                break;
            default:
                log("how the fuck is it even possible to get here dude");
                break;
        }
        api.done();
    }

    private void startSpam() {

        log("-----Start zero value transactions------");
        ExtendetIotaAPI current = null;
        StopWatch stopWatch = new StopWatch();
        int counter = 0;
        int pyramid = 0;

        while (counter < UIControler.getZeroValueSpamCount()) {
            stopWatch.reStart();
            try {

                current = getGoodApiConnection();

                float sum = 0;
                float[] vals = UIControler.getSliderValues();
                float sel = rand.nextFloat();
                for (int i = 0; i < vals.length; i++) {
                    sum += (vals[i]);
                }
                float asdf = 0;
                int next = 0;
                for (int i = 0; i < vals.length; i++) {
                    asdf += vals[i] / sum;
                    if (asdf > sel) {
                        next = i;
                        break;
                    }
                }
                int oldsize = hashestobuildon.size();
                switch (next) {
                    case 0:

                        log("Create spam referencing <zero tip + new tip> ");

                        hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(rand.nextInt(oldsize)).getHash(), tipsToApprove.pop()[1], stopWatch)
                        );
                        break;
                    case 1:
                        log("Create spam referencing <zero tip + zero tip> ");

                        hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(rand.nextInt(oldsize)).getHash(), hashestobuildon.get(rand.nextInt(oldsize)).getHash(), stopWatch)
                        );
                        break;
                    case 2:
                        log("Create spam referencing <start tip + zero> ");

                        hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(0).getHash(), hashestobuildon.get(rand.nextInt(oldsize)).getHash(), stopWatch)
                        );
                        break;
                    case 3:

                        log("Create spam referencing <start tip + new tip> ");
                        hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(0).getHash(), tipsToApprove.pop()[1], stopWatch));
                        break;
                    case 4:
                        if (hashestobuildon.size() >= 2) {
                            log("Create spam referencing <last tip + second last tip> ");
                            hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(oldsize - 1).getHash(), hashestobuildon.get(oldsize - 2).getHash(), stopWatch));
                        } else {
                            log("Error. Could not create spam referencing <last tip + second last tip> ");
                        }
                        break;
                    case 5:
                        String[] costum = UIControler.getHashesToBuildOn();
                        if (costum[0].equals("") || costum[1].equals("")) {
                            log("Error. Could not create spam referencing manualy selected hashes ");
                            break;
                        } else {
                            log("Create spam referencing manually selected hashes ");
                            hashestobuildon.addNew(current.makeZeroTransaction(this, costum[0], costum[1], stopWatch));
                        }
                        break;
                    case 6:

                        log("Create spam referencing <latest tip + new tip> ");
                        hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(oldsize - 1).getHash(), tipsToApprove.pop()[1], stopWatch));

                        break;
                    case 7:

                        log("Create spam referencing <new tip + new tip> ");
                        String[] s = tipsToApprove.pop();
                        hashestobuildon.addNew(current.makeZeroTransaction(this, s[0], s[1], stopWatch));

                        break;
                    case 8:
                        log("Building Pyramid");
                        if (pyramid + 1 < oldsize) {
                            hashestobuildon.addNew(current.makeZeroTransaction(this, hashestobuildon.get(pyramid).getHash(), hashestobuildon.get(++pyramid).getHash(), stopWatch));
                            pyramid++;
                        } else {
                            log("Error. Not enough hashes in store, to build a reference pyramid.");
                            break;
                        }
                        break;
                    case 9:

                        String[] costum2 = UIControler.getHashesToBuildOn();
                        if (costum2[0].equals("")) {
                            log("Error. Could not create spam referencing manually selected hashes ");
                            break;
                        } else {
                            log("Create spam referencing <manual trunk + new tip> ");
                            hashestobuildon.addNew(current.makeZeroTransaction(this, costum2[0], tipsToApprove.pop()[1], stopWatch));
                        }
                        break;
                }
                if (UIControler.getBeepStat() && (oldsize < hashestobuildon.size())) {
                    Toolkit.getDefaultToolkit().beep();

                }
                if (oldsize < hashestobuildon.size()) {
                    counter++;
                    log(counter + " zero value transaction made");
                    current.incrementUsage(stopWatch.getElapsedTimeSecs());
                    if (UIControler.getBeepStat()) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    log("No new zero value transaction added");
                    counter = Integer.MAX_VALUE;
                }
                current.done();
            } catch (IllegalAccessError | NullPointerException ex) {
                removeAPI(current);
            }
        }

    }

    private void connect() {

        dolmetschers.clear();
        log("Connecting to full nodes..");
        if (UIControler.getQuick()) {
            //String[] k = new String[]{"http", "service.iotasupport.com", "14265"};
            //String[] k = new String[]{"http", "iota.bitfinex.com", "80"};
            //String[] k = new String[]{"http", "eugene.iota.community", "14265"};
            String[] k = new String[]{"http", "node.lukaseder.de", "14265"};
            Builder b = new Builder(k[0], k[1], k[2]);
            ExtendetIotaAPI api = new ExtendetIotaAPI(b);
            dolmetschers.add(api);
        } else {
            dolmetschers.addAll(getAPIConnections(getNodes()));
        }
        log("Connected to " + dolmetschers.size() + " full nodes");
    }

    public synchronized void transaction() {

        log("Transaction started..");
        if (dolmetschers.isEmpty()) {
            connect();
        }
        if (UIControler.getQuick()) {
            GetTransactionsToApproveResponse gattr = getGoodApiConnection().getTransactionsToApprove(5, null, 1);
            found = new ArrayList<>();
            log("Attaching to: " + gattr.getTrunkTransaction() + " and " + gattr.getBranchTransaction());
            found.add(new String[]{gattr.getTrunkTransaction(), gattr.getBranchTransaction()});
        } else {
            found = findGoodTips(1, 0);
        }

        //donationadd = IotaAPIUtils.newAddress(Seed1k_reciver, 2, 0, false, customCurl.clone());
        if (found.size() >= 1) {
            hashestobuildon.add(getGoodApiConnection().makeTransaction(this, UIControler, found, inputs));

        } else {
            log("no tips found! ");
        }

    }

    @Override
    public void log(String s) {
        finalizePublication(new Publish().setLog(s));

    }

    private void showBalance(String b) {
        finalizePublication(new Publish().setBalance(b));
    }

    @Override
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

    public HashList getHashesToBuildOn() {
        return hashestobuildon;
    }

    public synchronized void prepare() {

    }

    public synchronized void spam() {

        if (dolmetschers.isEmpty()) {
            connect();
        }

        startSpam();
    }

    private List<String[]> getNodes() {
        List<String[]> nodes = new ArrayList<>();
        nodes.add(new String[]{"https", "n1.iota.nu", "443"});
        nodes.add(new String[]{"https", "node.tangle.works", "443"});
        nodes.add(new String[]{"http", "5.9.137.199", "14265"});
        nodes.add(new String[]{"http", "node02.iotatoken.nl", "14265"});
        nodes.add(new String[]{"http", "iota.bitfinex.com", "80"});
        nodes.add(new String[]{"http", "node01.iotatoken.nl", "14265"});
        nodes.add(new String[]{"http", "iota.digits.blue", "14265"});
        nodes.add(new String[]{"http", "mainnet.necropaz.com", "14500"});
        nodes.add(new String[]{"http", "eugene.iotasupport.com", "14999"});
        //nodes.add(new String[]{"http", "5.9.149.168", "14265"});
        nodes.add(new String[]{"http", "5.9.118.112", "14265"});
        nodes.add(new String[]{"http", "wallets.iotamexico.com", "80"});
        nodes.add(new String[]{"http", "eugeneoldisoft.iotasupport.com", "14265"});
        nodes.add(new String[]{"http", "88.198.230.98", "14265"});
        nodes.add(new String[]{"http", "service.iotasupport.com", "14265"});
        nodes.add(new String[]{"http", "node.lukaseder.de", "14265"});
        nodes.add(new String[]{"http", "eugene.iota.community", "14265"});
        nodes.add(new String[]{"http", "176.9.3.149", "14265"});
        nodes.add(new String[]{"http", "node03.iotatoken.nl", "15265"});
        return nodes;
    }

    private synchronized List<ExtendetIotaAPI> getAPIConnections(List<String[]> nodes) {
        List<ExtendetIotaAPI> dolis = new ArrayList<>();
        StopWatch sw = new StopWatch();
        List<apiIdentifi> runnables = new ArrayList<>();
        nodes.forEach((k) -> {
            apiIdentifi runnable = new apiIdentifi(nodes.indexOf(k), k);
            runnables.add(runnable);
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        runnables.stream().map((r) -> {
            if (r.done()) {
                dolis.add(r.getAPI());
            } else {
                log("Error! Kicked out node: " + r.api.getHost());
            }
            return r;
        }).forEachOrdered((r) -> {
            r.stop();
        });

        runnables.clear();
        log("it took " + sw.getElapsedTimeSecs() + " to connect to " + dolis.size() + " dolmetschers");
        sw.stop();
        return dolis;
    }

    public void removeAPI(ExtendetIotaAPI api) {
        log("Node " + api.getHost() + "failed. Breaking up connection now...");
        dolmetschers.remove(api);
        if (dolmetschers.isEmpty()) {
            log("No Fullnode Connections left. Programm ends now.");
            while (true) {
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Controler.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public ExtendetIotaAPI getGoodApiConnection() {

        if (dolmetschers.isEmpty()) {
            connect();
        }
        if (!dolmetschers.isEmpty()) {
            if (!UIControler.getQuick()) {
                int ind = rand.nextInt(dolmetschers.size());
                float avgtime = dolmetschers.stream().map((t) -> {
                    return t.getUsedCounter() > 0 ? (float) t.getUsedTime() / (float) t.getUsedCounter() : null; //To change body of generated lambdas, choose Tools | Templates.
                }).filter(Objects::nonNull)
                        .reduce((a, b) -> {
                            return ((float) a + (float) b) / 2f;
                        }).orElse(0f);

                ExtendetIotaAPI current = dolmetschers.get(ind);

                float speed;
                while (((speed = (current.getUsedCounter() > 0 ? current.getUsedTime() / current.getUsedCounter() : 0))
                        > (avgtime + Math.sqrt(avgtime))) || current.isinUse()) {
                    if (speed > (avgtime + Math.sqrt(avgtime))) {
                        System.out.println("I piked a Lazy Node " + current.getHost() + "which has avg. of: " + speed);
                        System.out.println("the avg working time is " + avgtime);
                        current.incrementUsage((long) avgtime + (long) ((float) avgtime * 0.2f));
                    } else {
                        System.out.println("I piked a Node that is currently in use. Picking another one..");
                    }
                    ind = rand.nextInt(dolmetschers.size());
                    current = dolmetschers.get(ind);
                }
                return current.inUse();
            } else {
                return dolmetschers.get(0);
            }
        } else {
            log("ERROR! Check internet connection!");
        }
        return null;
    }

    private List<String[]> findGoodTips(int repeats, int minFound) {
        return findGoodTips(repeats, minFound, UIControler.getQuick());
    }

    private List<String[]> findGoodTips(int repeats, int minFound, boolean quick) {
        if (!quick) {
            int si = dolmetschers.size();
            log("searching for new tips.. this may take a while");
            HashMap<String[], Integer> trunks = new HashMap<String[], Integer>();

            List<tipsIdentifi> runnables = new ArrayList<>();
            for (int i = 0; i < repeats; i++) {
                dolmetschers.forEach(d -> {
                    if (!d.isinUse()) {
                        tipsIdentifi runnable = new Controler.tipsIdentifi(d);
                        runnables.add(runnable);
                    }
                });
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                for (tipsIdentifi r : runnables) {

                    if (r.done()) {
                        String[] st = new String[]{r.getTrunk(), r.getBranch()};
                        boolean contained = false;
                        for (String[] key : trunks.keySet()) {
                            if (st[0].equals(key[0]) && st[1].equals(key[1])) {
                                trunks.put(key, trunks.get(key) + 1);
                                contained = true;
                                break;
                            }
                        }
                        if (!contained) {
                            trunks.put(st, 1);
                        }
                    } else {
                        System.out.println("Error! API connection returned <null> as tips. I fire him.");
                        dolmetschers.remove(r.getAPI());
                        r.stop();
                    }

                }

            }
            //runnables.clear();
            List<String[]> results = new ArrayList<>();
            trunks.forEach((k, v) -> {
                System.out.println(k[0] + " " + k[1] + " " + v);
                if (v > minFound) {
                    results.add(k);
                }
            });
            String[] best = trunks.keySet().stream().reduce((a, b) -> {
                if (trunks.get(a) > trunks.get(b)) {
                    return a;
                } else {
                    return b;
                }
            }).orElse(null);

            trunks.remove(best);
            String[] secondbest = trunks.keySet().stream().reduce((a, b) -> {
                if (trunks.get(a) > trunks.get(b)) {
                    return a;
                } else {
                    return b;
                }
            }).orElse(null);
            log("Search done. Found " + results.size() + " fresh tips.\nClosed " + (si - dolmetschers.size()) + " fullnode connections because they were lazy");
            if (best != null && secondbest != null) {
                //.orElse(new String[]{"", ""});
                log("Best Tips: " + best[0] + " " + best[1]);
                log("Second Best Tips: " + secondbest[0] + " " + secondbest[1]);
                results.add(0, best);
                results.add(1, secondbest);
            } else {
                log("Error. No best and second best tip returned... result size=" + results.size());
            }

            return results;
        } else {
            GetTransactionsToApproveResponse gattr = dolmetschers.get(0).getTransactionsToApprove(5, null, 1);
            found = new ArrayList<>();
            log("Attaching to: " + gattr.getTrunkTransaction() + " and " + gattr.getBranchTransaction());
            found.add(new String[]{gattr.getTrunkTransaction(), gattr.getBranchTransaction()});
            return found;
        }

    }

    private class tipsIdentifi implements Runnable {

        private Thread thread;
        private ExtendetIotaAPI api;
        private String trunk = null;
        private String branch = null;

        public String getTrunk() {
            return trunk;
        }

        public String getBranch() {
            return branch;
        }

        tipsIdentifi(ExtendetIotaAPI api) {
            this.api = api;
            this.thread = new Thread(this);
            thread.start();
        }

        public boolean done() {
            return !(trunk == null || branch == null);
        }

        @Override
        public void run() {
            try {
                StopWatch sw = new StopWatch();
                GetTransactionsToApproveResponse tips = api.getTransactionsToApprove(5, null, 1);
                String tempa = tips.getTrunkTransaction();
                String teampb = tips.getBranchTransaction();
                trunk = tempa;
                branch = teampb;
                api.incrementUsage(sw.getElapsedTimeSecs());
            } catch (IllegalAccessError | Exception ex) {
                trunk = null;
                branch = null;
                System.err.println("exception in first tip search " + ex.getMessage());
            }
        }

        public ExtendetIotaAPI getAPI() {
            return api;
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                // Interrupt the thread so it unblocks any blocking call
                thread.interrupt();
                // Change the states of variable
                thread = null;
                trunk = null;
                branch = null;
            }
        }
    }

    private class apiIdentifi implements Runnable {

        private int index;
        private ExtendetIotaAPI api;
        private GetNodeInfoResponse info = null;
        private String[] k;
        private Thread thread;

        public ExtendetIotaAPI getAPI() {
            return api;
        }

        public boolean done() {
            return info != null ? true : false;
        }

        apiIdentifi(int index, String[] k) {
            this.index = index;
            this.k = k;
            this.thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                // Interrupt the thread so it unblocks any blocking call
                thread.interrupt();
                // Change the states of variable
                thread = null;
                info = null;
            }
        }

        @Override
        public void run() {
            Builder b = new Builder(k[0], k[1], k[2]);
            this.api = new ExtendetIotaAPI(b);

            try {
                GetNodeInfoResponse asdf = api.getNodeInfo();
                info = asdf;
            } catch (Exception ex) {
                info = null;
                log("Exception thrown: " + ex.toString());
            }
        }

    }

}
