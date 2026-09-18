package com.campus.lostfound.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Claim implements Serializable {

    private final String claimId;
    private final String itemId;
    private final String claimantId;
    private final String proof;
    private final LocalDateTime submittedAt;

    private ClaimStatus status;

    public Claim(String claimId, String itemId, String claimantId, String proof) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.proof = proof;
        this.submittedAt = LocalDateTime.now();
        this.status = ClaimStatus.PENDING;
    }

    public String getClaimId() {
        return claimId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getClaimantId() {
        return claimantId;
    }

    public String getProof() {
        return proof;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return claimId + " | Item: " + itemId
                + " | Claimant: " + claimantId
                + " | Status: " + status
                + " | Submitted: " + submittedAt;
    }
}