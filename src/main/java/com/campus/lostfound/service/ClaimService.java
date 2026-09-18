package com.campus.lostfound.service;

import com.campus.lostfound.model.Claim;
import com.campus.lostfound.model.ClaimStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemStatus;
import com.campus.lostfound.util.AppLogger;
import com.campus.lostfound.util.Validate;

import java.util.ArrayList;
import java.util.List;

public class ClaimService {

    private DataStore database;

    public ClaimService(DataStore database) {
        this.database = database;
    }

    // Submit a claim for a found item
    public Claim submitClaim(
            String itemId,
            String claimantId,
            String proof) {

        Validate.requiredtext(itemId);
        Validate.requiredtext(claimantId);
        Validate.requiredtext(proof);

        itemId = itemId.trim();
        claimantId = claimantId.trim();
        proof = proof.trim();

        Item item = database.getItems().get(itemId);

        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        // Lost items cannot be claimed
        if (item.getType().equals("LOST")) {
            throw new IllegalArgumentException(
                    "You can only claim a found item."
            );
        }

        // Closed items cannot be claimed
        if (item.getStatus().equals(ItemStatus.CLOSED)) {
            throw new IllegalArgumentException(
                    "This item is already closed."
            );
        }

        // Check if the claimant exists
        if (!database.getUsers().containsKey(claimantId)) {
            throw new IllegalArgumentException("User not found.");
        }

        String claimId = createClaimId();

        Claim claim = new Claim(
                claimId,
                itemId,
                claimantId,
                proof
        );

        database.getClaims().put(claimId, claim);

        // Mark the item as waiting for claim decision
        item.setStatus(ItemStatus.CLAIM_PENDING);

        database.saveClaims();
        database.saveItems();

        AppLogger.info("New claim added: " + claimId);

        return claim;
    }

    // Approve or reject a claim
    public void decideClaim(
            String claimId,
            boolean approve) {

        Validate.requiredtext(claimId);

        claimId = claimId.trim();

        Claim claim = database.getClaims().get(claimId);

        if (claim == null) {
            throw new IllegalArgumentException("Claim not found.");
        }

        if (!claim.getStatus().equals(ClaimStatus.PENDING)) {
            throw new IllegalArgumentException(
                    "This claim has already been decided."
            );
        }

        Item item = database.getItems().get(claim.getItemId());

        if (item == null) {
            throw new IllegalArgumentException(
                    "Item connected to this claim was not found."
            );
        }

        if (approve) {
            claim.setStatus(ClaimStatus.APPROVED);
            item.setStatus(ItemStatus.RETURNED);
        } else {
            claim.setStatus(ClaimStatus.REJECTED);
            item.setStatus(ItemStatus.ACTIVE);
        }

        database.saveClaims();
        database.saveItems();

        AppLogger.info(
                "Claim decision recorded: " + claimId
        );
    }

    // Close an item after it has been returned
    public void closeItem(String itemId) {

        Validate.requiredtext(itemId);

        itemId = itemId.trim();

        Item item = database.getItems().get(itemId);

        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        if (!item.getStatus().equals(ItemStatus.RETURNED)) {
            throw new IllegalArgumentException(
                    "Only returned items can be closed."
            );
        }

        item.setStatus(ItemStatus.CLOSED);

        database.saveItems();

        AppLogger.info(
                "Item case closed: " + itemId
        );
    }

    // Get all claims that are waiting for approval
    public List<Claim> getPending() {

        List<Claim> pendingClaims = new ArrayList<>();

        for (Claim claim : database.getClaims().values()) {

            if (claim.getStatus().equals(ClaimStatus.PENDING)) {
                pendingClaims.add(claim);
            }
        }

        return pendingClaims;
    }

    // Get all claims submitted by a particular user
    public List<Claim> getClaimsByUser(String userId) {

        Validate.requiredtext(userId);

        userId = userId.trim();

        List<Claim> userClaims = new ArrayList<>();

        for (Claim claim : database.getClaims().values()) {

            if (claim.getClaimantId().equals(userId)) {
                userClaims.add(claim);
            }
        }

        return userClaims;
    }

    // Generate a unique claim ID
    private String createClaimId() {

        int count = 1;
        String claimId;

        do {
            claimId = "C" + String.format("%04d", count);
            count++;

        } while (database.getClaims().containsKey(claimId));

        return claimId;
    }
}