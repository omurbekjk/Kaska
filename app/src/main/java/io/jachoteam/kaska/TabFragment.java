package io.jachoteam.kaska;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.jachoteam.kaska.adapter.ProfileGridAdapter;
import io.jachoteam.kaska.dummy.DummyContent.DummyItem;
import io.jachoteam.kaska.helpers.ElasticSearchApi;
import io.jachoteam.kaska.helpers.ElasticSearchPostListener;
import io.jachoteam.kaska.models.Post;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TabFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postRef;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    int width;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    String uid;
    RecyclerView recyclerView;

    public TabFragment() {

    }

    // TODO: Customize parameter initialization

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_list, container, false);

        uid = getArguments().getString("uid");
        postRef = database.getReference("feed-posts/" + uid);
        getPost();
        //updateUserDetails();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));


        }
        return view;
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



   private void getPost() {

       ElasticSearchPostListener listener = new ElasticSearchPostListener() {
           @Override
           public void onRequest(boolean isOk, final ArrayList<Post> posts) {
               if (isOk) {
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           recyclerView.setAdapter(new ProfileGridAdapter(posts,mListener,getContext(),width));
                       }
                   });
               }
           }
       };

       ElasticSearchApi.getPostByUserId(uid,listener);

   }
}
