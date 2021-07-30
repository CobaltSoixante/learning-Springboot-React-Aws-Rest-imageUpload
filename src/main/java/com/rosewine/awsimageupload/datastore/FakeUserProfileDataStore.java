package com.rosewine.awsimageupload.datastore;

import com.rosewine.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository     // Indicates that this is the 3rd tier - database-access - is our backend.
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();   // hmmm - I guess because it static-final we name it with capitals.

    static {    // I remember now: this is a static block: performed only once, regardless of how many times the class is instantiated. Good for static-final, like the above variable.
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "janetjones", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "antoniojunior", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
