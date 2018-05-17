package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'getTransactionsToApprove'.
 **/
public class IotaGetTransactionsToApproveRequest extends IotaCommandRequest {
// this was the original code
//    private Integer depth;
//
//    /**
//     * Initializes a new instance of the IotaGetTransactionsToApproveRequest class.
//     */
//    private IotaGetTransactionsToApproveRequest(final Integer depth) {
//        super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
//        this.depth = depth;
//    }
//
//    /**
//     * Create a new instance of the IotaGetTransactionsToApproveRequest class.
//     */
//    public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(Integer depth) {
//        return new IotaGetTransactionsToApproveRequest(depth);
//    }
//
//    /**
//     * Gets the depth.
//     *
//     * @return The depth.
//     */
//    public Integer getDepth() {
//        return depth;
//    }
//
//    /**
//     * Sets the depth.
//     *
//     * @param depth The depth.
//     */
//    public void setDepth(Integer depth) {
//        this.depth = depth;
//    }
       public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getNumWalks() {
        return numWalks;
    }

    public void setNumWalks(Integer numWalks) {
        this.numWalks = numWalks;
    }

    private Integer depth;
    private String reference;
    private Integer numWalks;

    /**
     * Initializes a new instance of the IotaGetTransactionsToApproveRequest
     * class.
     */
    private IotaGetTransactionsToApproveRequest(final Integer depth, final String reference, final Integer numWalks) {
        super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
        this.depth = depth;
        this.reference = reference;
        this.numWalks = numWalks;

    }

    /**
     * Create a new instance of the IotaGetTransactionsToApproveRequest class.
     */
    public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(Integer depth, String refernece, Integer numWalks) {
        return new IotaGetTransactionsToApproveRequest(depth,refernece,numWalks);
    }

    /**
     * Gets the depth.
     *
     * @return The depth.
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * Sets the depth.
     *
     * @param depth The depth.
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }
}
