package theman;

import java.util.*;

import com.iota.iri.model.*;
import com.iota.iri.utils.Converter;

public class TransactionViewModel {

    private final TransactionIRI transaction;

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
    private static final int NONCE_TRINARY_OFFSET = ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_OFFSET + ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_SIZE, NONCE_TRINARY_SIZE = 81;

    public static final int TRINARY_SIZE = NONCE_TRINARY_OFFSET + NONCE_TRINARY_SIZE;

    public static final int ESSENCE_TRINARY_OFFSET = ADDRESS_TRINARY_OFFSET, ESSENCE_TRINARY_SIZE = ADDRESS_TRINARY_SIZE + VALUE_TRINARY_SIZE + OBSOLETE_TAG_TRINARY_SIZE + TIMESTAMP_TRINARY_SIZE + CURRENT_INDEX_TRINARY_SIZE + LAST_INDEX_TRINARY_SIZE;

    private TransactionViewModel trunk;
    private TransactionViewModel branch;
    private final Hash hash;

    public final static int GROUP = 0; // transactions GROUP means that's it's a non-leaf node (leafs store transaction value)
    public final static int PREFILLED_SLOT = 1; // means that we know only hash of the tx, the rest is unknown yet: only another tx references that hash
    public final static int FILLED_SLOT = -1; //  knows the hash only coz another tx references that hash

    private int[] trits;
    public int weightMagnitude;

    public TransactionViewModel(final int[] trits, Hash hash) {
        transaction = new TransactionIRI();
        this.trits = new int[trits.length];
        System.arraycopy(trits, 0, this.trits, 0, trits.length);
        transaction.bytes = Converter.bytes(trits);
        this.hash = hash;

        transaction.type = FILLED_SLOT;

        weightMagnitude = this.hash.trailingZeros();
        transaction.validity = 0;
        transaction.arrivalTime = 0;
    }

    public static int[] trits(byte[] transactionBytes) {
        int[] trits;
        trits = new int[TRINARY_SIZE];
        if (transactionBytes != null) {
            Converter.getTrits(transactionBytes, trits);
        }
        return trits;
    }

    public synchronized int[] trits() {
        return (trits == null) ? (trits = trits(transaction.bytes)) : trits;
    }

    public final int getType() {
        return transaction.type;
    }

    public void setArrivalTime(long time) {
        transaction.arrivalTime = time;
    }

    public long getArrivalTime() {
        return transaction.arrivalTime;
    }

    public byte[] getBytes() {
        if (transaction.bytes == null || transaction.bytes.length != SIZE) {
            transaction.bytes = trits == null ? new byte[SIZE] : Converter.bytes(trits());
        }
        return transaction.bytes;
    }

    public Hash getHash() {
        return hash;
    }

    public Hash getAddressHash() {
        if (transaction.address == null) {
            transaction.address = new Hash(trits(), ADDRESS_TRINARY_OFFSET);
        }
        return transaction.address;
    }

    public Hash getObsoleteTagValue() {
        if (transaction.obsoleteTag == null) {
            transaction.obsoleteTag = new Hash(Converter.bytes(trits(), OBSOLETE_TAG_TRINARY_OFFSET, OBSOLETE_TAG_TRINARY_SIZE), 0, TAG_SIZE);
        }
        return transaction.obsoleteTag;
    }

    public Hash getBundleHash() {
        if (transaction.bundle == null) {
            transaction.bundle = new Hash(trits(), BUNDLE_TRINARY_OFFSET);
        }
        return transaction.bundle;
    }

    public Hash getTrunkTransactionHash() {
        if (transaction.trunk == null) {
            transaction.trunk = new Hash(trits(), TRUNK_TRANSACTION_TRINARY_OFFSET);
        }
        return transaction.trunk;
    }

    public Hash getBranchTransactionHash() {
        if (transaction.branch == null) {
            transaction.branch = new Hash(trits(), BRANCH_TRANSACTION_TRINARY_OFFSET);
        }
        return transaction.branch;
    }

    public Hash getTagValue() {
        if (transaction.tag == null) {
            transaction.tag = new Hash(Converter.bytes(trits(), TAG_TRINARY_OFFSET, TAG_TRINARY_SIZE), 0, TAG_SIZE);
        }
        return transaction.tag;
    }

    public long getAttachmentTimestamp() {
        return transaction.attachmentTimestamp;
    }

    public long getAttachmentTimestampLowerBound() {
        return transaction.attachmentTimestampLowerBound;
    }

    public long getAttachmentTimestampUpperBound() {
        return transaction.attachmentTimestampUpperBound;
    }

    public long value() {
        return transaction.value;
    }

    public int getValidity() {
        return transaction.validity;
    }

    public long getCurrentIndex() {
        return transaction.currentIndex;
    }

    public int[] getSignature() {
        return Arrays.copyOfRange(trits(), SIGNATURE_MESSAGE_FRAGMENT_TRINARY_OFFSET, SIGNATURE_MESSAGE_FRAGMENT_TRINARY_SIZE);
    }

    public long getTimestamp() {
        return transaction.timestamp;
    }

    public byte[] getNonce() {
        return Converter.bytes(trits(), NONCE_TRINARY_OFFSET, NONCE_TRINARY_SIZE);
    }

    public long lastIndex() {
        return transaction.lastIndex;
    }

    public void setAttachmentData() {
        getTagValue();
        transaction.attachmentTimestamp = Converter.longValue(trits(), ATTACHMENT_TIMESTAMP_TRINARY_OFFSET, ATTACHMENT_TIMESTAMP_TRINARY_SIZE);
        transaction.attachmentTimestampLowerBound = Converter.longValue(trits(), ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_OFFSET, ATTACHMENT_TIMESTAMP_LOWER_BOUND_TRINARY_SIZE);
        transaction.attachmentTimestampUpperBound = Converter.longValue(trits(), ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_OFFSET, ATTACHMENT_TIMESTAMP_UPPER_BOUND_TRINARY_SIZE);

    }

    public void setMetadata() {
        transaction.value = Converter.longValue(trits(), VALUE_TRINARY_OFFSET, VALUE_USABLE_TRINARY_SIZE);
        transaction.timestamp = Converter.longValue(trits(), TIMESTAMP_TRINARY_OFFSET, TIMESTAMP_TRINARY_SIZE);
        //if (transaction.timestamp > 1262304000000L ) transaction.timestamp /= 1000L;  // if > 01.01.2010 in milliseconds
        transaction.currentIndex = Converter.longValue(trits(), CURRENT_INDEX_TRINARY_OFFSET, CURRENT_INDEX_TRINARY_SIZE);
        transaction.lastIndex = Converter.longValue(trits(), LAST_INDEX_TRINARY_OFFSET, LAST_INDEX_TRINARY_SIZE);
        transaction.type = transaction.bytes == null ? TransactionViewModel.PREFILLED_SLOT : TransactionViewModel.FILLED_SLOT;
    }

    public boolean updateSolid(boolean solid) throws Exception {
        if (solid != transaction.solid) {
            transaction.solid = solid;
            return true;
        }
        return false;
    }

    public boolean isSolid() {
        return transaction.solid;
    }

    public int snapshotIndex() {
        return transaction.snapshot;
    }

    public long getHeight() {
        return transaction.height;
    }

    private void updateHeight(long height) throws Exception {
        transaction.height = height;
    }

    public void updateSender(String sender) throws Exception {
        transaction.sender = sender;
    }

    public String getSender() {
        return transaction.sender;
    }
}
