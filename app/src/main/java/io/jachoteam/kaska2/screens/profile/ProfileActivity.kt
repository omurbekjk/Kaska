package io.jachoteam.kaska2.screens.profile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import io.jachoteam.kaska2.FollowersListActivity
import io.jachoteam.kaska2.FollowingsListActivity
import io.jachoteam.kaska2.PagerAdapter
import io.jachoteam.kaska2.R
import io.jachoteam.kaska2.helpers.Shared
import io.jachoteam.kaska2.screens.addfriends.AddFriendsActivity
import io.jachoteam.kaska2.screens.common.*
import io.jachoteam.kaska2.screens.editprofile.EditProfileActivity
import io.jachoteam.kaska2.screens.profilesettings.ProfileSettingsActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
    private lateinit var mAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.e(TAG, "onCreate")

        edit_profile_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        settings_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        add_friends_image.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }
        val tabLayout = findViewById<TabLayout>(R.id.tablayout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"))
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"))
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        Log.i("USI",Shared.Uid)
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount, Shared.Uid)
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid,4)
            val viewModel = initViewModel<ProfileViewModel>()
            viewModel.init(uid)

            followers_count_text.setOnClickListener {
                val intent = Intent(this, FollowersListActivity::class.java)
                intent.putExtra("uid", uid);
                startActivity(intent)
            }

            following_count_text.setOnClickListener {
                val intent = Intent(this, FollowingsListActivity::class.java)
                intent.putExtra("uid", uid);
                startActivity(intent)
            }

            viewModel.user.observe(this, Observer {
                it?.let {
                    profile_image.loadUserPhoto(it.photo)
                    username_text.text = it.username
                    followers_count_text.text = it.followers.size.toString()
                    following_count_text.text = it.follows.size.toString()
                }
            })
            viewModel.images.observe(this, Observer {
                it?.let { images ->
                    posts_count_text.text = images.size.toString()
                }
            })
        }
    }

    companion object {
        const val TAG = "ProfileActivity"
    }
}