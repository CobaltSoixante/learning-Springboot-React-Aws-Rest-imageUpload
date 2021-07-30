package com.rosewine.awsimageupload.profile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Actual CLASS for the "user" - the core "protagonist" of our application.
 */
public class UserProfile {
    private UUID userProfileId;
    private String username;
    private String userProfileImageLink;    // S3 key

    public UserProfile(
            UUID userProfileId
            , String username
            , String userProfileImageLink
    ) {
        this.userProfileId = userProfileId;
        this.username = username;
        this.userProfileImageLink = userProfileImageLink;
    }

    public UUID getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(UUID userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * In section
     * 8 - Create in-memory database - 00:30:24
     * we change signature from
     * public String getUserProfileImageLink() {
     * to -
     * public Optional<String> getUserProfileImageLink() {
     * BECAUSE 'userProfileImageLink' can be pre-initialized to 'null' - especially when adding a user to the database BEFORE there is a viable photo of him in out database.
     */
//    public String getUserProfileImageLink() {
//        return userProfileImageLink;
//    }
    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        // The following is the default code from the right-click GENERATE: there is some additional suggested code by Nelson in 7 - User Profile Model - 00:25:34
        return Objects.equals(userProfileId, that.userProfileId) && Objects.equals(username, that.username) && Objects.equals(userProfileImageLink, that.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, username, userProfileImageLink);
    }
}
