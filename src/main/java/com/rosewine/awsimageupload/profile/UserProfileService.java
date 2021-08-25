package com.rosewine.awsimageupload.profile;

import com.rosewine.awsimageupload.bucket.BucketName;
import com.rosewine.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

//import org.apache.http.entity.ContentType;
import static org.apache.http.entity.ContentType.*; //  static import feature was introduced into the language in version 5.0 .
    // It allows members which have been scoped within their container class as public static, to be used in Java code without specifying the class in which the field has been defined.
    // This is used for Nelson's implementation of point#2 in the business logic:
    // 2. Check if file is an image

/**
 * This is where all the main-work/business-logic happens.
 */
@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;  // For sec-5 of bus. logic.

    @Autowired  // Enable injecting the UserProfileDataAccessService into this class.
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    /**
     * NOTE: If u EVER restart this IntelliJ backend (8080) - you must ALSO RELOAD your 3000 client at the browser end - to get the latest UUID's.
     * This is because our "fake" database generates new random UserProfile UUID's whenever we restart this 8080 backend, and the 3000 frontend remain "in sync" and know about the new UUID values.
     *
     * @param userProfileId
     * @param file
     */
    /*public - is default*/ void uploadUserProfileImage(UUID userProfileId, MultipartFile file) throws IOException {
        // 1. Check if image is not empty
        // 2. Check if file is an image
        // 3. Check if the user exists in our database
        // 4. Grab some metadata from file - if any - 01:37:55
        // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link - 01:39:40

        final boolean useAWBusinessLogic = true;
            // true - use the business logic I implemented as exercise for this routine.
            // false - use Amigoscode logic from section 20 - Lets Implement uploadUserProfileImage() - 01:31:02

        // 1. Check if image is not empty
        if (useAWBusinessLogic) {
            if (file.getSize() <= 0)
                throw new IllegalStateException(String.format("file '%s' is empty.", file.getOriginalFilename()));
        } else {
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file { " + file.getSize() + "]");
            }
        }

        // 2. Check if file is an image
        if (useAWBusinessLogic) {
            final boolean isImage =
                file.getOriginalFilename().endsWith(".jpg")
                || file.getOriginalFilename().endsWith(".jpeg")
                || file.getOriginalFilename().endsWith(".png")
                ;

            if (!isImage)
                throw new IllegalStateException(String.format("file '%s' is not an image.", file.getOriginalFilename()));
        } else {
            if (!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())) {
                throw new IllegalStateException("file must be an image");
            }
        }

        // 3. Check if the user exists in our database
        // Not using AW logic because we need actual "user" object for step-5 of business logic.
        UserProfile user;
        if (! useAWBusinessLogic)
        {
            //FakeUserProfileDataStore userProfileDataStore = new FakeUserProfileDataStore();
            //List<UserProfile> userProfiles = userProfileDataStore.getUserProfiles();
            //
            //List<UserProfile> userProfiles = userProfileDataAccessService.getUserProfiles();
            List<UserProfile> userProfiles = getUserProfiles();

            // Find if the UUID exists.
            int index = 0;
            for (index = 0; index < userProfiles.size(); ++index)
            {
                //if (userProfiles.get(index).getUserProfileId().toString().equals(userProfileId.toString()))
                UUID curUserProfileId = userProfiles.get(index).getUserProfileId();
                if (curUserProfileId.equals(userProfileId))
                    break;
            }
            if (index >= userProfiles.size())
            {
                throw new IllegalStateException(String.format("User UUID '%s' does not exist in database.", userProfileId.toString()));
            }
        }
        else {
            user = userProfileDataAccessService
                    .getUserProfiles()  // returns List<UserProfile>: this is the list we are going through.
                    .stream()   // Nelson: streams are very powerful, and we should all be using them.
                    .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                    .findFirst()    // Just find the 1st one (should be only one)
                    .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
        }

        // 4. Grab some metadata from file - if any - 01:37:55
        // (just using Nelson's code here)
        // NOTE: this metadata is REALLY important - it is a requirement of HTTP protocol to successfully post our image-file to our S3 bucket!
        Map<String, String> metadata  = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link - 01:39:40
        // Not using AW logic: it WORKS! - but: Nelson's stuff will be more consistent with the rest of the course.
        if (! useAWBusinessLogic) {
            fileStore.save(
             "rosewine-image-upload-123/" + userProfileId,  // path: in the S3 bucket we have a folder per user, and inside that folder we can see the files for every specific user.
                    file.getOriginalFilename(),
                    Optional.of(metadata),
                    file.getInputStream()
            );
        } else {
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
            String filename = String.format("%s-%s-%s",
                    file.getOriginalFilename(),
                    UUID.randomUUID(),   // tacking on a random ID means we don't OVERWRITE the previous file - thus, we have a HISTORY of files downloaded in our S3 bucket.
                    file.getOriginalFilename()  // Tacking this on IN ADDITION to Nelson's original spec - so file-name ends with .jpg/.png etc - easier to directly MANUALLY download from the S3 bucket.
            );

            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

        }

    }

}
