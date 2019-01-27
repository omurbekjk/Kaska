package io.jachoteam.kaska.screens.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import io.jachoteam.kaska.PagerAdapter
import io.jachoteam.kaska.R
import io.jachoteam.kaska.screens.common.BaseActivity
import io.jachoteam.kaska.screens.common.ImagesAdapter
import io.jachoteam.kaska.screens.common.setupAuthGuard
import io.jachoteam.kaska.screens.common.setupBottomNavigation
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), TextWatcher {
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mViewModel: SearchViewModel
    private var isSearchEntered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.d(TAG, "onCreate")

        setupAuthGuard {uid ->
            setupBottomNavigation(uid,1)
            mAdapter = ImagesAdapter()
            search_results_recycler.layoutManager = GridLayoutManager(this, 3)
            search_results_recycler.adapter = mAdapter

            mViewModel = initViewModel()
            mViewModel.posts.observe(this, Observer{it?.let{posts ->
                mAdapter.updateImages(posts.map { it.image })
            }})

            search_input.addTextChangedListener(this)
            mViewModel.setSearchText("")
        }

        val tabLayout = findViewById<View>(R.id.tablayout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_likes_border))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home))
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        val adapter = SearchPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.setOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun afterTextChanged(s: Editable?) {
        if (!isSearchEntered) {
            isSearchEntered = true
            Handler().postDelayed({
                isSearchEntered = false
                mViewModel.setSearchText(search_input.text.toString())
            }, 500)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

    companion object {
        const val TAG = "SearchActivity"
    }
}
