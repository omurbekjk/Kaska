package io.jachoteam.kaska;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import io.jachoteam.kaska.adapter.ProfileGridAdapter;
import io.jachoteam.kaska.adapter.RVFeedAdapter;
import io.jachoteam.kaska.models.FeedPost;
import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.screens.home.FeedAdapter;
import io.jachoteam.kaska.screens.home.HomeViewModel;
import io.jachoteam.kaska.screens.postDetails.DefaultPostDetailsImpl;
import io.jachoteam.kaska.screens.postDetails.PostDetailsService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Tab2Fragment extends Fragment implements FeedAdapter.Listener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postRef;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String uid;

    RecyclerView recyclerView;
    RVFeedAdapter adapter;
    PostDetailsService defaultPostDetailsService;
    HomeViewModel homeViewModel;
    public Tab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Tab2Fragment.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        Log.e("DDDDD","sadsd");
        uid = getArguments().getString("uid");
        postRef = database.getReference("feed-posts/" + uid);

        defaultPostDetailsService = new DefaultPostDetailsImpl(getActivity());
        recyclerView = view.findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        getCategory();


        adapter = new RVFeedAdapter(getContext());
        recyclerView.setAdapter(adapter);
        updateUserDetails();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void toggleLike(@NotNull String postId) {

    }

    @Override
    public void loadLikes(@NotNull String postId, int position) {

    }

    @Override
    public void openComments(@NotNull String postId) {

    }

    @Override
    public void openProfile(@NotNull String username, @NotNull String uid) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateUserDetails() {
        final ArrayList<Post> feedPosts = new ArrayList<>();
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    post.setId(postSnapshot.getKey());
                    Log.e("Get Data", post.getAddress());
                    feedPosts.add(post);


                }
                Log.e("Size",feedPosts.size()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updatePosts(feedPosts);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void getCategory() {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("param", "для")
                .build();


        final Request request = new Request.Builder()
                .url("https://us-central1-fir-elastic-ab0bd.cloudfunctions.net/getPostListByTag")


                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.e("RESPONSE_Finish",json);





            }
        });
    }

}
