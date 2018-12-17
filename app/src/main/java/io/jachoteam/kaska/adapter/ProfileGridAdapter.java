package io.jachoteam.kaska.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import io.jachoteam.kaska.R;
import io.jachoteam.kaska.TabFragment;
import io.jachoteam.kaska.dummy.DummyContent;
import io.jachoteam.kaska.models.FeedPost;
import io.jachoteam.kaska.models.Image;
import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.screens.common.GlideApp;
import io.jachoteam.kaska.screens.postDetails.PostDetailActivity;

/**
 * Created by User on 09.12.2018.
 */
public class ProfileGridAdapter extends RecyclerView.Adapter<ProfileGridAdapter.ViewHolder> {

    private final List<Post> mValues;
    private final TabFragment.OnListFragmentInteractionListener mListener;
    Context context;
    int width;

    public ProfileGridAdapter(List<Post> items, TabFragment.OnListFragmentInteractionListener listener, Context context, int width) {
        mValues = items;
        mListener = listener;
        this.context = context;
        this.width = width;
    }

    @Override
    public ProfileGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_grid, parent, false);
        return new ProfileGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post feedPost = mValues.get(position);

        Image data = (Image) feedPost.getImages().values().toArray()[0];

        GlideApp.with(context)
                .load(data.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .fitCenter()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {



        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.image);
            relativeLayout = view.findViewById(R.id.rel);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,width/3);
            layoutParams.setMargins(5,0,5,0);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent1 = new Intent(context, PostDetailActivity.class);
                    intent1.putExtra("postId",mValues.get(getAdapterPosition()).getId());
                    intent1.putExtra("userId",mValues.get(getAdapterPosition()).getUid());
                    context.startActivity(intent1);
                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

    }
}

