package com.rosewine.awsimageupload.profile;

import com.rosewine.awsimageupload.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService {

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    /**
     * The @Autowired annotation promotes dependency injection - so if we changed the database from being "fake" to MySQL oe POSTGRES -
     * only one line of code would need to be changed.
     * @param fakeUserProfileDataStore
     */
    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    List<UserProfile> getUserProfiles() {
        return fakeUserProfileDataStore.getUserProfiles();
    }
}
