package io.jachoteam.kaska.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import io.jachoteam.kaska.ProfileViewActivity;
import io.jachoteam.kaska.helpers.Shared;
import io.jachoteam.kaska.screens.profile.ProfileActivity;

public class ProfileOpener implements View.OnClickListener {
    private String uid, username;
    private Context context;

    public ProfileOpener(Context context, String userId, String username){
        this.uid = userId;
        this.username = username;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent profileIntent = null;
        if (Shared.Uid.equals(uid)) {
            profileIntent = new Intent(context, ProfileActivity.class);
        } else {
            profileIntent = new Intent(context, ProfileViewActivity.class);
        }
        profileIntent.putExtra("uid", uid);
        profileIntent.putExtra("username", username);
        context.startActivity(profileIntent);
    }
}