package io.jachoteam.kaska.helpers;

import java.util.ArrayList;

import io.jachoteam.kaska.models.Post;

/**
 * Created by User on 30.12.2018.
 */

public interface ElasticSearchPostListener {

    void onRequest(boolean isOk, ArrayList<Post> posts);

}
