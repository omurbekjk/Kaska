package io.jachoteam.kaska.helpers;

import io.jachoteam.kaska.data.firebase.FirebaseUsersRepository;

/**
 * Created by User on 24.01.2019.
 */

public class UserHelper {

    private static FirebaseUsersRepository fur = new FirebaseUsersRepository();

    public static void followUser(String userId) {
        fur.addFollow(Shared.Uid, userId);
        fur.addFollower(Shared.Uid, userId);
    }

    public static void unFollowUser(String userId) {
        fur.deleteFollow(Shared.Uid, userId);
        fur.deleteFollower(Shared.Uid, userId);
    }
}
