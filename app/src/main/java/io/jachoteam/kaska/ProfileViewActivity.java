package io.jachoteam.kaska;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.jachoteam.kaska.data.FeedPostsRepository;
import io.jachoteam.kaska.data.firebase.FirebaseFeedPostsRepository;
import io.jachoteam.kaska.data.firebase.FirebaseUsersRepository;
import io.jachoteam.kaska.dummy.DummyContent;
import io.jachoteam.kaska.helpers.Shared;
import io.jachoteam.kaska.helpers.UserHelper;
import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.addfriends.AddFriendsViewModel;
import io.jachoteam.kaska.screens.common.GlideApp;

public class ProfileViewActivity extends AppCompatActivity implements TabFragment.OnListFragmentInteractionListener,
        Tab2Fragment.OnFragmentInteractionListener, Tab3Fragment.OnFragmentInteractionListener {
    public String uid;
    public String username;
    public TextView followersCountTextView;
    public TextView followingCountTextView;
    private Button followBtn, unfollowBtn, sendMessageBtn;
    String TAG = "ProfileViewActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    DatabaseReference postRef;
    FirebaseUsersRepository usersRepository = new FirebaseUsersRepository();
    FirebaseFeedPostsRepository feedPostRepository = new FirebaseFeedPostsRepository();
    User user;
    boolean userLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("Profile","dsdfd");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        username = intent.getStringExtra("username");
        Log.d(TAG, uid + ": " + username);

        userRef = database.getReference("users/" + uid);
        postRef = database.getReference("images/" + uid);
        followersCountTextView = (TextView) findViewById(R.id.followers_count_text);
        followingCountTextView = (TextView) findViewById(R.id.following_count_text);
        followBtn = findViewById(R.id.follow_btn);
        unfollowBtn = findViewById(R.id.unfollow_btn);
        sendMessageBtn = findViewById(R.id.send_message_btn);

//        messageBtn.setOnClickListener(sendMessage());

        followersCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "followers count clicked");
                Intent intent = new Intent(getApplicationContext(), FollowersListActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        followingCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "Followings count clicked");
                Intent intent = new Intent(getApplicationContext(), FollowingsListActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        countPosts();
        updateUserDetails();

        sendMessage();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_likes_border));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),uid);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void sendMessage() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(ProfileViewActivity.this, ChatActivity.class);
                startActivity(chatIntent);
            }
        });

        sendMessageBtn = findViewById(R.id.send_message_btn);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(ProfileViewActivity.this, ChatActivity.class);
                startActivity(chatIntent);
            }
        });
    }

    private void updateUserDetails() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userLoaded = true;
                updateView(user);
                System.out.println(user);

                displayFollowButton();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void countPosts() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                TextView postsCount = (TextView) findViewById(R.id.posts_count_text);
                postsCount.setText(count + "");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        postRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateView(User user) {
        ImageView imageView = (ImageView) findViewById(R.id.profile_image);
        TextView username = (TextView) findViewById(R.id.username_text);
        TextView followersCount = (TextView) findViewById(R.id.followers_count_text);
        TextView followingCount = (TextView) findViewById(R.id.following_count_text);

        followersCount.setText(user.getFollowers().size() + "");
        followingCount.setText(user.getFollows().size() + "");
        username.setText(user.getUsername());

        GlideApp.with(this).load(user.getPhoto()).fallback(R.drawable.person).into(imageView);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i("INTERFACE_CALLEDD", "HREHEHE");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("INTERFACE_CALLEDD", "URI URI URI");
    }

    public void displayFollowButton(){
        if(!user.getFollowers().containsKey(Shared.Uid)){
            followBtn.setVisibility(View.VISIBLE);
            unfollowBtn.setVisibility(View.GONE);
        }else{
            followBtn.setVisibility(View.GONE);
            unfollowBtn.setVisibility(View.VISIBLE);
        }
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelper.followUser(uid);
            }
        });
        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelper.unFollowUser(uid);
            }
        });
    }
}