package io.jachoteam.kaska.screens.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.jachoteam.kaska.R
import io.jachoteam.kaska.adapter.RVFeedAdapter
import io.jachoteam.kaska.common.ValueEventListenerAdapter
import io.jachoteam.kaska.helpers.Shared
import io.jachoteam.kaska.models.Post
import io.jachoteam.kaska.screens.common.BaseActivity
import io.jachoteam.kaska.screens.common.setupAuthGuard
import io.jachoteam.kaska.screens.common.setupBottomNavigation
import io.jachoteam.kaska.screens.postDetails.DefaultPostDetailsImpl
import io.jachoteam.kaska.screens.postDetails.PostDetailsService
import java.util.ArrayList

class HomeActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVFeedAdapter
    private lateinit var defaultPostDetailsService: PostDetailsService

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var postRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (FirebaseAuth.getInstance().currentUser != null) {
            Shared.Uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            postRef = database.getReference("feed-posts/$uid")
            defaultPostDetailsService = DefaultPostDetailsImpl(this)
            recyclerView = findViewById(R.id.feed_recycler)
            recyclerView.layoutManager = LinearLayoutManager(this)

            recyclerView.setHasFixedSize(true)

            adapter = RVFeedAdapter(this)
            recyclerView.adapter = adapter
            updateUserDetails()
        }


    }

    private fun updateUserDetails() {
        val feedPosts:MutableList<Post> = mutableListOf()
        postRef.addValueEventListener(
                ValueEventListenerAdapter(
                        handler = { dataSnapshot ->
                            for (postSnapShot: DataSnapshot in dataSnapshot.children) {
                                var post = postSnapShot.getValue(Post::class.java)
                                post!!.id = postSnapShot.key

                                feedPosts.add(post)
                            }

                            runOnUiThread {

                                adapter.updatePosts(
//                                        feedPosts.sortedWith(compareBy({ it.timestamp }))
                                ArrayList(feedPosts).sortedWith(compareByDescending<Post>{ it.timestamp })
                                )
                            }
                        }))
    }
}