package com.rosewine.awsimageupload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController // 1st tier (API) of the app
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")   // Addresses a CORS problem we encounter at before 01:03:41 : this will allow strange hosts (other than the client) to communicate with us.
    // Is not something we should normally do in production - but will enable our React component's backend to communicate with us for now.
    // Instead of "*" (everything) we could have limited this to just "localhost:3000" (our React backend) - but - what the hell, we're just testing, lets enable everything.
public class UserProfileController {

    private final UserProfileService userProfileService;    // NOTE: it seems to me there is a pattern here: classes/objects that are "injected" are generally "final".

    @Autowired  // Enable injecting of the "UserProfileService"
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * At browser:
     * http://localhost:8080/api/v1/user-profile
     * @return All the user profiles.
     */
    @GetMapping // The "get" verb for our REST app. Is magic: the name of the method could be anything: it is just the "@GetMapping"annotation that counts.
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }

    /**
     *
     * @param userProfileId - We are getting the userProfileId from the actual path.
     * @param file
     */
    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                       @RequestParam("file") MultipartFile file) {
        userProfileService.uploadUserProfileImage(userProfileId, file);
    }
}
