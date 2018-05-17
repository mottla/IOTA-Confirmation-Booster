package theman;

import cfb.pearldiver.PearlDiver;
import theman.interfaces.IControlerPanel;
import com.iota.iri.model.Hash;
import com.iota.iri.utils.Converter;
import java.awt.Toolkit;
import jota.dto.response.*;
import jota.error.*;
import jota.model.*;
import jota.pow.ICurl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.logging.Level;
import jota.IotaAPI;
import jota.pow.SpongeFactory;
import jota.utils.Checksum;
import jota.utils.InputValidator;
import jota.utils.StopWatch;
import theman.utils.HashList;

/**
 *
 * @author theman
 */
public class ExtendetIotaAPI extends IotaAPI {

    protected ExtendetIotaAPI(Builder builder) {
        super(builder);
        customCurl = builder.customCurl;
    }

    public static final int SIZE = 1604;
    private static final int TAG_SIZE = 27;

    public static final long SUPPLY = 2779530283277761L; // = (3^33 - 1) / 2

    public static final int SIGNATURE_MESSAGE_FRAGMENT_TRINARY_OFFSET = 0, SIGNATURE_MESSAGE_FRAGMENT_TRINARY_SIZE = 6561;
    public static final int ADDRESS_TRINARY_OFFSET = SIGNATURE_MESSAGE_FRAGMENT_TRINARY_OFFSET + SIGNATURE_MESSAGE_FRAGMENT_TRINARY_SIZE, ADDRESS_TRINARY_SIZE = 243;
    public static final int VALUE_TRINARY_OFFSET = ADDRESS_TRINARY_OFFSET + ADDRESS_TRINARY_SIZE, VALUE_TRINARY_SIZE = 81, VALUE_USABLE_TRINARY_SIZE = 33;
    public static final int OBSOLETE_TAG_TRINARY_OFFSET = VALUE_TRINARY_OFFSET + VALUE_TRINARY_SIZE, OBSOLETE_TAG_TRINARY_SIZE = 81;
    public static final int TIMESTAMP_TRINARY_OFFSET = OBSOLETE_TAG_TRINARY_OFFSET + OBSOLETE_TAG_TRINARY_SIZE, TIMESTAMP_TRINARY_SIZE = 27;
    public static final int CURRENT_INDEX_TRINARY_OFFSET = TIMESTAMP_TRINARY_OFFSET + TIMESTAMP_TRINARY_SIZE, CURRENT_INDEX_TRINARY_SIZE = 27;
    public static final int LAST_INDEX_TRINARY_OFFSET = CURRENT_INDEX_TRINARY_OFFSET + CURRENT_INDEX_TRINARY_SIZE, LAST_INDEX_TRINARY_SIZE = 27;
    public static final int BUNDLE_TRINARY_OFFSET = LAST_INDEX_TRINARY_OFFSET + LAST_INDEX_TRINARY_SIZE, BUNDLE_TRINARY_SIZE = 243;
    public static final int TRUNK_TRANSACTION_TRINARY_OFFSET = BUNDLE_TRINARY_OFFSET + BUNDLE_TRINARY_SIZE, TRUNK_TRANSACTION_TRINARY_SIZE = 243;
    public static final int BRANCH_TRANSACTION_TRINARY_OFFSET = TRUNK_TRANSACTION_TRINARY_OFFSET + TRUNK_TRANSACTION_TRINARY_SIZE, BRANCH_TRANSACTION_TRINARY_SIZE = 243;

    public static final int TAG_TRINARY_OFFSET = BRANCH_TRANSACTION_TRINARY_OFFSET + BRANCH_TRANSACTION_TRINARY_SIZE, TAG_TRINARY_SIZE = 81;
    public static final int ATTACHMENT_TIMESTAMP_TRINARY_OFFSET = TAG_TRINARY_OFFSET + TAG_TRINARY_SIZE, ATTACHMENT_TIMESTAMP_TRINARY_SIZE = 27;
    public static final int ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_OFFSET = ATTACHMENT_TIMESTAMP_TRINARY_OFFSET + ATTACHMENT_TIMESTAMP_TRINARY_SIZE, ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_SIZE = 27;
    public static final int ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_OFFSET = ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_OFFSET + ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_SIZE, ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_SIZE = 27;

    private static long MAX_TIMESTAMP_VALUE = (3 ^ 27 - 1) / 2;
    private boolean inUse = false;
    public static final int MWN = 14;
    private static final Logger log = LoggerFactory.getLogger(ExtendetIotaAPI.class);
    private ICurl customCurl;
    private static final String TEST_MESSAGE = "JOTA";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
//    private final PearlDiverLocalPoW pow = new PearlDiverLocalPoW();
    private int usedCounter = 0;
    private long usedTime = 0;

    public int getUsedCounter() {
        return usedCounter;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void incrementUsage(long usedTime) {
        this.usedCounter += 1;
        this.usedTime += usedTime;
    }

//    public static void prepareTransaction(IControlerPanel cp, Controler con, List<Input> inputs) {
//        ExtendetIotaAPI current = con.getGoodApiConnection();
//        con.log("Transaction preparation started");
//        //List<TransactionViewModel> tvms = initdontaion(current, tips.get(1), donationadd, fromSeed, amount);
//        int donationAmount = cp.getTransactionAmount();
//        List<String> trytes = prepareTransaction(current, cp.getReciverAddress(), cp.getTransactionSeed(), donationAmount, inputs);
//        Converter.
//    }
    public void done() {
        this.inUse = false;
    }

    public ExtendetIotaAPI inUse() {
        this.inUse = true;
        return this;
    }

    public boolean isinUse() {
        return inUse;
    }

    public List<String> prepareTransaction(String donationadd, String donatorSeed, int amount, List<Input> inputs) {
        try {
            List<Transfer> transfers = new ArrayList<>();
            transfers.add(new jota.model.Transfer(donationadd, amount, getRandTrytesString(14), getRandTrytesString(27)));
            List<String> trytes = prepareTransfers(amount > 0 ? donatorSeed : getRandTrytesString(81), 2, transfers, null, (inputs == null || inputs.isEmpty()) ? null : inputs);
            return trytes;
        } catch (InvalidSecurityLevelException | InvalidAddressException | InvalidTransferException | NotEnoughBalanceException cxvb) {
            java.util.logging.Logger.getLogger(ExtendetIotaAPI.class.getName()).log(Level.SEVERE, null, cxvb);

        }
        return null;
    }

    private List<TransactionViewModel> makeTransaction(String[] startTips, List<String> trytes) {
        List<TransactionViewModel> tvms = attachToTangleStatement(new Hash(startTips[0]), new Hash(startTips[1]), 14, trytes);
        return tvms;

    }

    public BalanceRecorder makeTransaction(Controler con, IControlerPanel UIControler, List<String[]> tips, List<Input> inputs) {

        con.log("Transaction initiation started");
        int donationAmount = UIControler.getTransactionAmount();
        con.log("Transaction reference hashes are: " + Arrays.toString(tips.get(0)));
        List<String> trytes = prepareTransaction(UIControler.getReciverAddress(), UIControler.getTransactionSeed(), donationAmount, inputs);
        List<TransactionViewModel> tvms = makeTransaction(tips.get(0), trytes);
        con.log("Transaction preparation and PoW done.");
        final List<String> elements = new LinkedList<>();
        BalanceRecorder b = null;

        for (int i = 0; i < tvms.size(); i++) {
            elements.add(com.iota.iri.utils.Converter.trytes(tvms.get(i).trits()));
            if (tvms.get(i).getCurrentIndex() == 0) {
                con.log("New branch address hash: " + tvms.get(i).getHash());
                b = new BalanceRecorder("Transaction Hash", tvms.get(i).getHash().toString(), tvms.get(i).getAddressHash().toString(), donationAmount);
            }
        }

        StopWatch sw = new StopWatch();
        try {
            broadcastAndStore(elements.stream().toArray(String[]::new));
            con.log("Transaction broadcast done in " + sw.getElapsedTimeSecs() + " seconds");
            if (UIControler.getBeepStat()) {
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (BroadcastAndStoreException ex) {
            java.util.logging.Logger.getLogger(ExtendetIotaAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.log("Transaction completed");
        done();
        return b;
    }

    public String[] makeZeroTransaction(Controler con, String trunk, String branch, StopWatch sw) {

        sw.reStart();
        try {
            List<Transfer> transfers = new ArrayList<>();
            Transfer tf = new jota.model.Transfer(getRandTrytesString(81), 0, getRandTrytesString(14), getRandTrytesString(27));
            transfers.add(tf);
            List<String> prp = prepareTransfers(getRandTrytesString(81), 2, transfers, null, null);
            List<TransactionViewModel> tvms = attachToTangleStatement(new Hash(trunk), new Hash(branch), 14, prp);
            con.log("Prepare and Pow took " + sw.getElapsedTimeSecs() + " seconds");
            final List<String> elements = new LinkedList<>();
            String[] hashToBuildOn = null;
            for (int i = 0; i < tvms.size(); i++) {
                // for (int i = tvms.size(); i-- > 0;) {
                elements.add(com.iota.iri.utils.Converter.trytes(tvms.get(i).trits()));
                if (tvms.get(i).getCurrentIndex() == 0) {
                    con.log("new hash -> " + tvms.get(i).getHash());
                    hashToBuildOn = new String[]{"Zero val", tvms.get(i).getHash().toString()};
                }
            }
            sw.reStart();
            broadcastAndStore(elements.stream().toArray(String[]::new));
            con.log("Broadcasting took" + sw.getElapsedTimeSecs() + " seconds");

            return hashToBuildOn;
        } catch (InvalidSecurityLevelException | InvalidAddressException | InvalidTransferException ex) {
            java.util.logging.Logger.getLogger(ExtendetIotaAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotEnoughBalanceException ex) {
            java.util.logging.Logger.getLogger(ExtendetIotaAPI.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (BroadcastAndStoreException ex) {
            java.util.logging.Logger.getLogger(ExtendetIotaAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int getRandInt(int min, int max) {

        return new Random().nextInt((max - min) + 1) + min;
    }
    private static final Random rand = new Random();

    public static String getRandTrytesString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ9";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rand.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String generateSeed() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ9";
        int length = 81;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rand.nextInt(characters.length()));
        }
        return new String(text);
    }

    public  List<TransactionViewModel> attachToTangleStatement(final Hash trunkTransaction, final Hash branchTransaction,
            final int minWeightMagnitude, final List<String> trytes) {
        final List<TransactionViewModel> transactionViewModels = new LinkedList<>();

        Hash prevTransaction = null;
        PearlDiver pearlDiver = new PearlDiver();

        for (final String tryte : trytes) {
            long startTime = System.nanoTime();
            long timestamp = System.currentTimeMillis();

            final int[] transactionTrits = Converter.trits(tryte);
            //branch and trunk
            System.arraycopy((prevTransaction == null ? trunkTransaction : prevTransaction).trits(), 0,
                    transactionTrits, TransactionViewModel.TRUNK_TRANSACTION_TRINARY_OFFSET,
                    TransactionViewModel.TRUNK_TRANSACTION_TRINARY_SIZE);
            System.arraycopy((prevTransaction == null ? branchTransaction : trunkTransaction).trits(), 0,
                    transactionTrits, TransactionViewModel.BRANCH_TRANSACTION_TRINARY_OFFSET,
                    TransactionViewModel.BRANCH_TRANSACTION_TRINARY_SIZE);

            //attachment fields: tag and timestamps
            //tag - copy the obsolete tag to the attachment tag field only if tag isn't set.
            if (Arrays.stream(transactionTrits, TransactionViewModel.TAG_TRINARY_OFFSET, TransactionViewModel.TAG_TRINARY_OFFSET + TransactionViewModel.TAG_TRINARY_SIZE).allMatch(s -> s == 0)) {
                System.arraycopy(transactionTrits, TransactionViewModel.OBSOLETE_TAG_TRINARY_OFFSET,
                        transactionTrits, TransactionViewModel.TAG_TRINARY_OFFSET,
                        TransactionViewModel.TAG_TRINARY_SIZE);
            }

            Converter.copyTrits(timestamp, transactionTrits, TransactionViewModel.ATTACHMENT_TIMESTAMP_TRINARY_OFFSET,
                    TransactionViewModel.ATTACHMENT_TIMESTAMP_TRINARY_SIZE);
            Converter.copyTrits(0, transactionTrits, TransactionViewModel.ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_OFFSET,
                    TransactionViewModel.ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_SIZE);
            Converter.copyTrits(MAX_TIMESTAMP_VALUE, transactionTrits, TransactionViewModel.ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_OFFSET,
                    TransactionViewModel.ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_SIZE);

            if (!pearlDiver.search(transactionTrits, minWeightMagnitude, 0)) {
                transactionViewModels.clear();
                break;
            }
            //validate PoW - throws exception if invalid
            final TransactionViewModel transactionViewModel = validate(transactionTrits, MWN);

            transactionViewModels.add(transactionViewModel);
            prevTransaction = transactionViewModel.getHash();
        }
        return transactionViewModels;
//        final List<String> elements = new LinkedList<>();
//        for (int i = transactionViewModels.size(); i-- > 0;) {
//            elements.add(Converter.trytes(transactionViewModels.get(i).trits()));
//        }
//        return elements;
    }

    public  TransactionViewModel validate(final int[] trits, int minWeightMagnitude) {

        Hash h = Hash.calculate(trits, 0, trits.length, SpongeFactory.create(SpongeFactory.Mode.CURLP81));
        System.out.println("validate hash " + h.toString());
        TransactionViewModel transactionViewModel = new TransactionViewModel(trits, h);
        runValidation(transactionViewModel, minWeightMagnitude);
        return transactionViewModel;
    }

    private  void runValidation(TransactionViewModel transactionViewModel, final int minWeightMagnitude) {
        transactionViewModel.setMetadata();
        if (transactionViewModel.getTimestamp() < 1508760000 && !transactionViewModel.getHash().equals(Hash.NULL_HASH)) {
            throw new RuntimeException("Invalid transaction timestamp.");
        }
        for (int i = VALUE_TRINARY_OFFSET + VALUE_USABLE_TRINARY_SIZE; i < VALUE_TRINARY_OFFSET + VALUE_TRINARY_SIZE; i++) {
            if (transactionViewModel.trits()[i] != 0) {
                //log.error("Transaction trytes: "+Converter.trytes(transactionViewModel.trits()));
                throw new RuntimeException("Invalid transaction value");
            }
        }

        int weightMagnitude = transactionViewModel.weightMagnitude;
        if (weightMagnitude < minWeightMagnitude) {
            /*
            log.error("Hash found: {}", transactionViewModel.getHash());
            log.error("Transaction trytes: "+Converter.trytes(transactionViewModel.trits()));
             */
            throw new RuntimeException("Invalid transaction hash");
        }
    }

    /**
     * Prepares transfer by generating bundle, finding and signing inputs.
     *
     * @param seed 81-tryte encoded address of recipient.
     * @param security The security level of private key / seed.
     * @param transfers Array of transfer objects.
     * @param remainder If defined, this address will be used for sending the
     * remainder value (of the inputs) to.
     * @param inputs The inputs.
     * @param validateInputs whether or not to validate the balances of the
     * provided inputs
     * @return Returns bundle trytes.
     * @throws InvalidAddressException is thrown when the specified address is
     * not an valid address.
     * @throws NotEnoughBalanceException is thrown when a transfer fails because
     * their is not enough balance to perform the transfer.
     * @throws InvalidSecurityLevelException is thrown when the specified
     * security level is not valid.
     * @throws InvalidTransferException is thrown when an invalid transfer is
     * provided.
     */
//    public Bundle prepareBundle(String seed, int security, final List<Transfer> transfers, String remainder, List<Input> inputs) throws NotEnoughBalanceException, InvalidSecurityLevelException, InvalidAddressException, InvalidTransferException {
//
//        // Input validation of transfers object
//        if (!InputValidator.isTransfersCollectionValid(transfers)) {
//            throw new InvalidTransferException();
//        }
//
//        // validate seed
//        if ((!InputValidator.isValidSeed(seed))) {
//            throw new IllegalStateException("Invalid Seed");
//        }
//
//        if (security < 1 || security > 3) {
//            throw new InvalidSecurityLevelException();
//        }
//
//        // Create a new bundle
//        final Bundle bundle = new Bundle();
//        final List<String> signatureFragments = new ArrayList<>();
//
//        long totalValue = 0;
//        String tag = "";
//
//        //  Iterate over all transfers, get totalValue
//        //  and prepare the signatureFragments, message and tag
//        for (final Transfer transfer : transfers) {
//            // If address with checksum then remove checksum
//            if (Checksum.isAddressWithChecksum(transfer.getAddress())) {
//                transfer.setAddress(Checksum.removeChecksum(transfer.getAddress()));
//            }
//
//            int signatureMessageLength = 1;
//
//            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
//            if (transfer.getMessage().length() > 2187) {
//
//                // Get total length, message / maxLength (2187 trytes)
//                signatureMessageLength += Math.floor(transfer.getMessage().length() / 2187);
//
//                String msgCopy = transfer.getMessage();
//
//                // While there is still a message, copy it
//                while (!msgCopy.isEmpty()) {
//
//                    String fragment = StringUtils.substring(msgCopy, 0, 2187);
//                    msgCopy = StringUtils.substring(msgCopy, 2187, msgCopy.length());
//
//                    // Pad remainder of fragment
//                    fragment = StringUtils.rightPad(fragment, 2187, '9');
//
//                    signatureFragments.add(fragment);
//                }
//            } else {
//                // Else, get single fragment with 2187 of 9's trytes
//                String fragment = StringUtils.substring(transfer.getMessage(), 0, 2187);
//
//                fragment = StringUtils.rightPad(fragment, 2187, '9');
//
//                signatureFragments.add(fragment);
//            }
//
//            // get current timestamp in seconds
//            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);
//
//            // If no tag defined, get 27 tryte tag.
//            tag = transfer.getTag().isEmpty() ? "999999999999999999999999999" : transfer.getTag();
//
//            // Pad for required 27 tryte length
//            tag = StringUtils.rightPad(tag, 27, '9');
//
//            // Add first entry to the bundle           
//            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), tag, timestamp);
//            // Sum up total value
//            totalValue += transfer.getValue();
//        }
//
//        // Get inputs if we are sending tokens
//        if (totalValue != 0) {
//         
//            //  Case 1: user provided inputs
//            //  Validate the inputs by calling getBalances
//          
//            if (inputs != null && !inputs.isEmpty()) {
//
//                // Get list if addresses of the provided inputs
//                List<String> inputsAddresses = new ArrayList<>();
//                for (final Input i : inputs) {
//                    inputsAddresses.add(i.getAddress());
//                }
//
//                GetBalancesResponse balancesResponse = getBalances(100, inputsAddresses);
//                String[] balances = balancesResponse.getBalances();
//
//                List<Input> confirmedInputs = new ArrayList<>();
//                int totalBalance = 0;
//                int i = 0;
//                for (String balance : balances) {
//                    long thisBalance = Integer.parseInt(balance);
//
//                    // If input has balance, add it to confirmedInputs
//                    if (thisBalance > 0) {
//                        totalBalance += thisBalance;
//                        Input inputEl = inputs.get(i++);
//                        inputEl.setBalance(thisBalance);
//                        confirmedInputs.add(inputEl);
//
//                        // if we've already reached the intended input value, break out of loop
//                        if (totalBalance >= totalValue) {
//                            log.info("Total balance already reached ");
//                            break;
//                        }
//                    }
//                }
//
//                // Return not enough balance error
//                if (totalValue > totalBalance) {
//                    throw new IllegalStateException("Not enough balance");
//                }
//
//                return addRemainder(seed, security, confirmedInputs, bundle, tag, totalValue, remainder, signatureFragments);
//            } //  Case 2: Get inputs deterministically
//            //
//            //  If no inputs provided, derive the addresses from the seed and
//            //  confirm that the inputs exceed the threshold
//            else {
//
//                @SuppressWarnings("unchecked")
//                GetBalancesAndFormatResponse newinputs = getInputs(seed, security, 0, 15, 0);
//                // If inputs with enough balance
//                return addRemainder(seed, security, newinputs.getInput(), bundle, tag, totalValue, remainder, signatureFragments);
//            }
//        } else {
//
//            // If no input required, don't sign and simply finalize the bundle
//            bundle.finalize(customCurl.clone());
//            bundle.addTrytes(signatureFragments);
//
//            List<Transaction> trxb = bundle.getTransactions();
//            List<String> bundleTrytes = new ArrayList<>();
//
//            for (Transaction trx : trxb) {
//                bundleTrytes.add(trx.toTrytes());
//            }
//            Collections.reverse(bundleTrytes);
//            return bundleTrytes;
//        }
//    }
    public List<Transaction> prepareZeroTransfers(final List<Transfer> transfers) throws NotEnoughBalanceException, InvalidSecurityLevelException, InvalidAddressException, InvalidTransferException {

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new InvalidTransferException();
        }

        // Create a new bundle
        final Bundle bundle = new Bundle();
        final List<String> signatureFragments = new ArrayList<>();

        String tag = "";

        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {

            // If address with checksum then remove checksum
            if (Checksum.isAddressWithChecksum(transfer.getAddress())) {
                transfer.setAddress(Checksum.removeChecksum(transfer.getAddress()));
            }

            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > 2187) {
                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / 2187);
                String msgCopy = transfer.getMessage();
                // While there is still a message, copy it
                while (!msgCopy.isEmpty()) {
                    String fragment = StringUtils.substring(msgCopy, 0, 2187);
                    msgCopy = StringUtils.substring(msgCopy, 2187, msgCopy.length());
                    // Pad remainder of fragment
                    fragment = StringUtils.rightPad(fragment, 2187, '9');
                    signatureFragments.add(fragment);
                }
            } else {
                // Else, get single fragment with 2187 of 9's trytes
                String fragment = StringUtils.substring(transfer.getMessage(), 0, 2187);
                fragment = StringUtils.rightPad(fragment, 2187, '9');
                signatureFragments.add(fragment);
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);
            // If no tag defined, get 27 tryte tag.
            tag = transfer.getTag().isEmpty() ? "999999999999999999999999999" : transfer.getTag();
            // Pad for required 27 tryte length
            tag = StringUtils.rightPad(tag, 27, '9');
            // Add first entry to the bundle
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), tag, timestamp);
        }
        // If no input required, don't sign and simply finalize the bundle
        bundle.finalize(customCurl.clone());
        bundle.addTrytes(signatureFragments);
        List<Transaction> trxb = bundle.getTransactions();
//        List<String> bundleTrytes = new ArrayList<>();
//        for (Transaction trx : trxb) {
//            bundleTrytes.add(trx.toTrytes());
//        }
//        Collections.reverse(bundleTrytes);
//          return bundleTrytes
        return trxb;
    }

//    public static class Builder extends ExtendetIotaAPICore.Builder<Builder> {
//
//        private Sponge customCurl;
//
//        Builder(Sponge curl) {
//            customCurl = curl;
//        }
//
//        public Builder withCustomCurl(ICurl curl) {
//            customCurl = curl;
//            return this;
//        }
//
//        public ExtendetIotaAPI build() {
//            super.build();
//            return new ExtendetIotaAPI(this);
//        }
//    }
}
