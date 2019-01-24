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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.jachoteam.kaska.adapter.RVFeedAdapter;
import io.jachoteam.kaska.helpers.ElasticSearchApi;
import io.jachoteam.kaska.helpers.ElasticSearchPostListener;
import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.screens.home.FeedAdapter;
import io.jachoteam.kaska.screens.home.HomeViewModel;
import io.jachoteam.kaska.screens.postDetails.DefaultPostDetailsImpl;
import io.jachoteam.kaska.screens.postDetails.PostDetailsService;


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
       // postRef = database.getReference("feed-posts/" + uid);

        defaultPostDetailsService = new DefaultPostDetailsImpl(getActivity());
        recyclerView = view.findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);





        adapter = new RVFeedAdapter(getContext());
        recyclerView.setAdapter(adapter);
        //updateUserDetails();
        getPost();

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


    private void getPost() {

        ElasticSearchPostListener listener = new ElasticSearchPostListener() {
            @Override
            public void onRequest(boolean isOk, final ArrayList<Post> posts) {
                if (isOk) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updatePosts(posts);
                        }
                    });
                }
            }
        };

        ElasticSearchApi.getPostByUserId(uid,listener);

    }

}
