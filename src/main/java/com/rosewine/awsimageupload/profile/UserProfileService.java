package com.rosewine.awsimageupload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * This is where all the main-work/business-logic happens.
 */
@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;

    @Autowired  // Enable injecting the UserProfileDataAccessService into this class.
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService) {
        this.userProfileDataAccessService = userProfileDataAccessService;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    /**
     *
     * @param userProfileId
     * @param file
     */
    /*public - is default*/ void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        // 2. Check if file is an image
        // 3. Check if the user exists in our database
        // 4. Grab some metadata from file - if any
        // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link
    }
}
