package io.jachoteam.kaska.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.jachoteam.kaska.R;
import io.jachoteam.kaska.models.Image;
import io.jachoteam.kaska.models.Likes;
import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.screens.common.GlideApp;
import io.jachoteam.kaska.screens.home.FeedSlidingImageAdapter;
import io.jachoteam.kaska.screens.postDetails.PostDetailActivity;


public class RVSearchFeedAdapter extends RecyclerView.Adapter<RVSearchFeedAdapter.PersonViewHolder> {
    Context context;
    Post vse;


    public class PersonViewHolder extends RecyclerView.ViewHolder {


        CircleImageView imageUser;
        TextView textCaption, textUsername;


        PersonViewHolder(final View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.user_photo_image);
            textCaption = itemView.findViewById(R.id.caption_text);
            textUsername = itemView.findViewById(R.id.user);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("postId", listVse.get(getAdapterPosition()).getId());
                    intent.putExtra("userId", listVse.get(getAdapterPosition()).getUid());
                    context.startActivity(intent);
                }
            });

        }

    }




    ArrayList<Post> listVse;
    HashMap<Integer, Likes> likes = new HashMap<Integer, Likes>();

    public RVSearchFeedAdapter(Context context) {

        this.context = context;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        try {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_feed, viewGroup, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {

        if (listVse.size() > 0) {
            vse = listVse.get(i);

            if (!vse.getImage().isEmpty()) {
                GlideApp.with(context).load(vse.getImage()).into(holder.imageUser);
            }

            holder.textUsername.setText(vse.getUsername());
            holder.textCaption.setText(vse.getCaption());


        }

    }


    public void initPagerAdapter(Map<String, Image> images, String id, String uid, ViewPager viewPager){

        List<Image> im = new ArrayList<>(images.values());

        FeedSlidingImageAdapter adapter = new FeedSlidingImageAdapter(context,im,uid,id);


    }

    public void updatePosts(ArrayList<Post> posts) {
        listVse = posts;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return listVse == null ? 0 : listVse.size();
    }




}