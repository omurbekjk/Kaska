package io.jachoteam.kaska2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.jachoteam.kaska2.R;
import io.jachoteam.kaska2.data.FeedPostLike;
import io.jachoteam.kaska2.helpers.Shared;
import io.jachoteam.kaska2.models.FeedPost;
import io.jachoteam.kaska2.models.Image;
import io.jachoteam.kaska2.models.Likes;
import io.jachoteam.kaska2.models.Post;
import io.jachoteam.kaska2.screens.comments.CommentsActivity;
import io.jachoteam.kaska2.screens.common.GlideApp;
import io.jachoteam.kaska2.screens.home.FeedAdapter;
import io.jachoteam.kaska2.screens.home.FeedSlidingImageAdapter;


public class RVFeedAdapter extends RecyclerView.Adapter<RVFeedAdapter.PersonViewHolder> {
    Context context;
    Post vse;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postRef;


    public class PersonViewHolder extends RecyclerView.ViewHolder {


        ViewPager viewPager;
        ImageView imageNext, imagePrev, imageMore, imageLike, imageComment, imageShare, imageAudio;
        CircleImageView imageUser;
        TextView textLike, textCaption, textUsername, textAddress;


        PersonViewHolder(final View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.feed_slider_pager);
            imageNext = itemView.findViewById(R.id.feed_sliding_image_next);
            imagePrev = itemView.findViewById(R.id.feed_sliding_image_prev);
            imageMore = itemView.findViewById(R.id.more_image);
            imageComment = itemView.findViewById(R.id.comment_image);
            imageLike = itemView.findViewById(R.id.like_image);
            imageShare = itemView.findViewById(R.id.share_image);
            imageAudio = itemView.findViewById(R.id.has_audio_indicator);
            imageUser = itemView.findViewById(R.id.user_photo_image);
            textLike = itemView.findViewById(R.id.likes_text);
            textCaption = itemView.findViewById(R.id.caption_text);
            textUsername = itemView.findViewById(R.id.username_text);
            textAddress = itemView.findViewById(R.id.user_address_text);

            imageLike.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                   Likes likes1 =  likes.get(getAdapterPosition());
                   if (likes1!=null) {
                       if (likes1.isYourLike()) {
                           DatabaseReference ref = database.getReference("likes/" + listVse.get(getAdapterPosition()).getId());
                           ref.child(Shared.Uid).removeValue();
                       } else {

                           DatabaseReference ref = database.getReference("likes/" + listVse.get(getAdapterPosition()).getId());
                           ref.child(Shared.Uid).setValue(true);
                       }
                   }else {
                       DatabaseReference ref = database.getReference("likes/" + listVse.get(getAdapterPosition()).getId());
                       ref.child(Shared.Uid).setValue(true);
                   }

                   updateUserDetails(listVse.get(getAdapterPosition()).getId(),getAdapterPosition());

                }
            });

            imageComment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, CommentsActivity.class).putExtra("POST_ID",listVse.get(getAdapterPosition()).getId()));
                }
            });


            imagePrev.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = viewPager.getCurrentItem()-1;

                    viewPager.setCurrentItem(pos);

                    ArrayList<Image> images = new ArrayList<>(listVse.get(getAdapterPosition()).getImages().values());

                    setImages(imageNext,imagePrev,pos,images);

                }
            });

            imageNext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = viewPager.getCurrentItem()+1;

                    viewPager.setCurrentItem(pos);

                    ArrayList<Image> images = new ArrayList<>(listVse.get(getAdapterPosition()).getImages().values());

                    setImages(imageNext,imagePrev,pos,images);

                }
            });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    ArrayList<Image> images = new ArrayList<>(listVse.get(getAdapterPosition()).getImages().values());

                    setImages(imageNext,imagePrev,position,images);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }


    public void setImages(ImageView next,ImageView prev,int position, ArrayList<Image> images){
        Log.e("Pos",position+"");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
        if (position > 0) {
            prev.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(images.get(position - 1).getUrl())
                    .centerCrop()
                    .apply(requestOptions)
                    .override(120,120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(prev);
        }else {
            prev.setVisibility(View.GONE);
        }
        if (position < images.size() - 1) {
            next.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(images.get(position + 1).getUrl())
                    .centerCrop()
                    .apply(requestOptions)
                    .override(120,120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(next);
        }else {
            next.setVisibility(View.GONE);
        }
    }

    ArrayList<Post> listVse;
    HashMap<Integer, Likes> likes = new HashMap<Integer, Likes>();

    public RVFeedAdapter(Context context) {

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
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
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
            Likes like = likes.get(i);

            List<Image> im = new ArrayList<>(vse.getImages().values());

            FeedSlidingImageAdapter adapter = new FeedSlidingImageAdapter(context,im,vse.getUid(),vse.getId());
            holder.viewPager.setAdapter(adapter);

            setImages(holder.imageNext,holder.imagePrev,holder.viewPager.getCurrentItem(), (ArrayList<Image>) im);
            if (!vse.getImage().isEmpty()) {
                GlideApp.with(context).load(vse.getImage()).into(holder.imageUser);
            }

            holder.textAddress.setText(vse.getAddress());
            holder.textUsername.setText(vse.getUsername());
            holder.textCaption.setText(vse.getCaption());

            if (vse.getAudioUrl().isEmpty()){
                holder.imageAudio.setVisibility(View.VISIBLE);
                holder.imageAudio.setImageResource(R.drawable.audio_icon);
            }else {
                holder.imageAudio.setVisibility(View.GONE);

            }

            updateUserDetails(vse.getId(), i);
            if (like != null) {
                holder.textLike.setVisibility(View.VISIBLE);
                holder.textLike.setText(like.getCount() + " like");
                if (like.isYourLike()){
                    holder.imageLike.setImageResource(R.drawable.ic_likes_active);
                }else {
                    holder.imageLike.setImageResource(R.drawable.ic_likes_border);
                }
            }else {
                holder.textLike.setVisibility(View.GONE);
            }


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

    public void updateLikes(Likes like, int pos) {

        Likes k = likes.get(pos);

        if (k!=null){
            if (like.getCount()!=k.getCount()){
                likes.put(pos, like);
                notifyItemChanged(pos);
            }
        }else {
            likes.put(pos, like);
            notifyItemChanged(pos);
        }


    }


    @Override
    public int getItemCount() {
        return listVse == null ? 0 : listVse.size();
    }

    private void updateUserDetails(final String postid, final int position) {
        postRef = database.getReference("likes/" + postid);
        final ArrayList<FeedPost> feedPosts = new ArrayList<>();
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean bool = false;
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.e("ddd", postSnapshot.toString() + "  " + position + " " + postid);
                    if (Objects.equals(postSnapshot.getKey(), Shared.Uid)) {
                        bool = true;
                    }

                    keys.add(postSnapshot.getKey());

                }
                if (dataSnapshot.getChildrenCount() > 0) {
                    updateLikes(new Likes((int) dataSnapshot.getChildrenCount(), bool,keys), position);
                    // Log.e("Size",feedPosts.size()+"");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


}