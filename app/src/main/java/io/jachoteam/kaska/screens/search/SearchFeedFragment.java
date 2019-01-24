package io.jachoteam.kaska.screens.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.jachoteam.kaska.R;
import io.jachoteam.kaska.adapter.ProfileGridAdapter;
import io.jachoteam.kaska.adapter.RVFeedAdapter;
import io.jachoteam.kaska.adapter.RVSearchFeedAdapter;
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
public class SearchFeedFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postRef;
    // TODO: Customize parameter argument names
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    int width;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    String uid;
    RecyclerView recyclerView;
    ImageView imageSearch;
    EditText editSearch;
    RVSearchFeedAdapter adapter;


    public SearchFeedFragment() {

    }

    // TODO: Customize parameter initialization

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_feed, container, false);

        editSearch = view.findViewById(R.id.search_input);
        imageSearch = view.findViewById(R.id.search_image);


        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        // Set the adapter

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        adapter = new RVSearchFeedAdapter(getContext());
        recyclerView.setAdapter(adapter);

        initSearch();
        return view;
    }

    private void initSearch() {

        imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSearchResponse(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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




    public void getSearchResponse(String text) {

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

        ElasticSearchApi.getPostByTag(text, listener);

    }


}
