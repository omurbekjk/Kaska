package io.jachoteam.kaska;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.jachoteam.kaska.data.firebase.FirebaseFeedPostsRepository;
import io.jachoteam.kaska.dummy.DummyContent;
import io.jachoteam.kaska.dummy.DummyContent.DummyItem;
import io.jachoteam.kaska.models.FeedPost;
import io.jachoteam.kaska.retrofit.Request;
import io.jachoteam.kaska.screens.comments.CommentsActivity;
import io.jachoteam.kaska.screens.common.ViewModelFactory;
import io.jachoteam.kaska.screens.home.FeedAdapter;
import io.jachoteam.kaska.screens.home.FeedPostLikes;
import io.jachoteam.kaska.screens.home.HomeViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TabFragment extends Fragment  implements FeedAdapter.Listener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private FeedAdapter mAdapter;
    private HomeViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TabFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TabFragment newInstance(int columnCount) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mAdapter = new FeedAdapter(this, this.getContext());
        mViewModel = new HomeViewModel(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }, new FirebaseFeedPostsRepository());
//        mViewModel.retrieveForProfile(ProfileViewActivity.uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_item, container, false);
        Log.i("VIEW_DATA_IS", view.getContext().toString());



        Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://us-central1-kgkaska.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        Request request = retrofit.create(Request.class);

        JsonObject userData = new JsonObject();

        userData.addProperty("params",ProfileViewActivity.uid);
        Call<Map<String,List<FeedPost>>> call = request.getPostListByUserId(userData);

        call.enqueue(new Callback<Map<String,List<FeedPost>>>() {
            @Override
            public void onResponse(Call<Map<String,List<FeedPost>>> call, Response<Map<String,List<FeedPost>>> response) {
                List<FeedPost> posts = response.body().get("message");
                mViewModel.init(posts, FirebaseAuth.getInstance().getCurrentUser().getUid());
                mAdapter.updatePosts(mViewModel.feedPostsList);
                observePosts();
//                try {
//                    mViewModel.feedPosts = new LiveData<List<FeedPost>>(){
//
//                    };
//                    mViewModel.feedPosts.getValue().addAll(posts.get("message"));
//                } catch (Exception ex){
//                    // do smth
//                }
//                observePosts();
//                Log.i("SUCCESS_RESPONSE_DATA", posts.toString());
            }

            @Override
            public void onFailure(Call<Map<String,List<FeedPost>>> call, Throwable t) {
                Log.i("FAIL_RESPONSE_DATA", t.toString());
            }
        });


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }


        return view;
    }

    private void observePosts(){
        final Context context = this.getContext();
        mViewModel.getGoToCommentsScreen().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                CommentsActivity.Companion.start(context, s);
            }


        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void toggleLike(@NotNull String postId) {
        mViewModel.toggleLike(postId);
    }

    @Override
    public void loadLikes(@NotNull String postId, final int position) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, new Observer<FeedPostLikes>() {
                @Override
                public void onChanged(@Nullable FeedPostLikes feedPostLikes) {
                    mAdapter.updatePostLikes(position, feedPostLikes);

                }
            });
        }
    }

    @Override
    public void openComments(@NotNull String postId) {
        mViewModel.openComments(postId);
    }

    @Override
    public void openProfile(@NotNull String username, @NotNull String uid) {
        // do nothing
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
