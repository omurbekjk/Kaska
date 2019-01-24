package io.jachoteam.kaska.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.jachoteam.kaska.R;

/**
 * Created by User on 24.01.2019.
 */

public class PostHelper {

    public static void postPopupMenuCaller(final Context context,String postId, String postUid){
        if(Shared.Uid.equals(postUid)){
            PostHelper.postPopUpMenuOwn(context,postId);
        }else {
            PostHelper.postPopUpMenuForeign(context,postId);
        }
    }

    public static void postPopUpMenuForeign(final Context context,String postId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.allert_dialog_foreign, null);

        builder.setView(dialogView);
        TextView unfollowPopupItem = (TextView) dialogView.findViewById(R.id.unfollow_popup_item);



        final AlertDialog dialog = builder.create();

        unfollowPopupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: unfollow
                Toast.makeText(context, "Unfollow", Toast.LENGTH_SHORT).show();
            }
        });



        dialog.show();
    }

    public static void postPopUpMenuOwn(final Context context, String postId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.allert_dialog_more, null);

        builder.setView(dialogView);
        TextView edit_post = (TextView) dialogView.findViewById(R.id.edit_post);
        TextView delete_post = dialogView.findViewById(R.id.delete_post);


        final AlertDialog dialog = builder.create();

        edit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: edit post
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: delete post
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
