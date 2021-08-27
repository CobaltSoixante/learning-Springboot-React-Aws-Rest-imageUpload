package com.rosewine.awsimageupload.datastore;

import com.rosewine.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository     // Indicates that this is the 3rd tier - database-access - is our backend.
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();   // hmmm - I guess because it static-final we name it with capitals.

    static {    // I remember now: this is a static block: performed only once, regardless of how many times the class is instantiated. Good for static-final, like the above variable:
        // I reckon:
        // 'final' stuff should be set by the time we are in the constructor.
        // 'static final' stuff should be set in the 'static {}' block.
        //USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "janetjones", null));
        //USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "antoniojunior", null));

        // 22 - Set user profile image link - 01:53:41
        // We hardcode the UUID's now in our in-memory database - because we want STABLE UUID to store a PREDICTABLE/STABLE link to the user's image that IS NOT gonna be regenerated every time
        // (this would not have been an issue with a real database, where persistence would have been guaranteed - und these shenanigans not required).
        USER_PROFILES.add(new UserProfile(UUID.fromString("5855f756-5373-4ca0-a426-175aaff996df"), "janetjones", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("20790f74-03ab-453a-9434-15429e8492bc"), "antoniojunior", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
