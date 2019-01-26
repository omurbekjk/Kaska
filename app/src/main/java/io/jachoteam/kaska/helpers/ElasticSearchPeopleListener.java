package io.jachoteam.kaska.helpers;

import java.util.ArrayList;

import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.models.UserJava;

/**
 * Created by User on 30.12.2018.
 */

public interface ElasticSearchPeopleListener {

    void onRequest(boolean isOk, ArrayList<UserJava> users);

}
