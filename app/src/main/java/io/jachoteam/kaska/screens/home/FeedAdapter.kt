package io.jachoteam.kaska.screens.home

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.FeedPost
import io.jachoteam.kaska.models.Image
import io.jachoteam.kaska.screens.common.*
import io.jachoteam.kaska.screens.postDetails.PostDetailsService
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(private val listener: Listener,
                  private var context: Context,
                  private val postDetailsService: PostDetailsService)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    interface Listener {
        fun toggleLike(postId: String)
        fun loadLikes(postId: String, position: Int)
        fun openComments(postId: String)
        fun openProfile(username: String, uid: String)
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        init {



            view.feed_sliding_image_prev.setOnClickListener {
                val position = view.feed_slider_pager.currentItem
                view.feed_slider_pager.currentItem = position - 1
                Log.e("Pos", adapterPosition.toString())
            }
            view.feed_sliding_image_next.setOnClickListener {
                val position = view.feed_slider_pager.currentItem
                view.feed_slider_pager.currentItem = position + 1
                Log.e("Pos", adapterPosition.toString())
            }
        }

    }


    private var posts = listOf<FeedPost>()
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private val defaultPostLikes = FeedPostLikes(0, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)

    }

    fun updatePostLikes(position: Int, likes: FeedPostLikes) {
        postLikes += (position to likes)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = posts[position]
        val likes = postLikes[position] ?: defaultPostLikes
        with(holder.view) {
            user_photo_image.loadUserPhoto(post.photo)
            username_text.text = post.username
            init(post.images, feed_slider_pager, post.uid, post.id, position, feed_sliding_image_prev, feed_sliding_image_next)
            if (likes.likesCount == 0) {
                likes_text.visibility = View.GONE
            } else {
                likes_text.visibility = View.VISIBLE
                val likesCountText = holder.view.context.resources.getQuantityString(
                        R.plurals.likes_count, likes.likesCount, likes.likesCount)
                likes_text.text = likesCountText
            }



            if (post.audioUrl.length > 0) {
                has_audio_indicator.setImageResource(R.drawable.audio_icon)
            } else {
                has_audio_indicator.setImageResource(R.drawable.navigation_empty_icon)
            }

            if (post.address.length > 0) {
                user_address_text.text = post.address
            }

            caption_text.setCaptionText(post.username, post.caption)
            like_image.setOnClickListener { listener.toggleLike(post.id) }
            username_text.setOnClickListener { listener.openProfile(post.username, post.uid) }
            like_image.setImageResource(
                    if (likes.likedByUser) R.drawable.ic_likes_active
                    else R.drawable.ic_likes_border)
            comment_image.setOnClickListener { listener.openComments(post.id) }
            listener.loadLikes(post.id, position)


        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<FeedPost>) {
        Log.e("dsf", "dsf")
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.posts, newPosts) { it.id })
        this.posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }

    private fun init(imagesMap: Map<String, Image>, sliderPager: ViewPager, postUserId: String, postId: String, position: Int, imageViewPrev: ImageView, imageViewNext: ImageView) {
        var imagesList: MutableList<Image> = mutableListOf()
        imagesMap.forEach { (key, value) ->
            run {
                imagesList.add(value)
            }
        }


        var pos = sliderPager.currentItem


        imagesList.sortBy { i -> i.order }

        if (pos > 0) {
            GlideApp.with(context)
                    .load(imagesList[pos - 1].url)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .override(120, 120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewPrev)
        }
        if (pos < imagesList.size - 1) {
            GlideApp.with(context)
                    .load(imagesList[pos + 1].url)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .override(120, 120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewNext)
        }

        HomeActivity.mPager = sliderPager
        HomeActivity.mPager.adapter = FeedSlidingImageAdapter(context, imagesList, postUserId, postId)
    }



    fun setImage(pos : Int,position: Int,imageViewPrev: ImageView,imageViewNext: ImageView){

        var imagesMap = posts[position].images

        var imagesList: MutableList<Image> = mutableListOf()
        imagesMap.forEach { (key, value) ->
            run {
                imagesList.add(value)
            }
        }





        imagesList.sortBy { i -> i.order }

        if (pos > 0) {
            GlideApp.with(context)
                    .load(imagesList[pos - 1].url)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .override(120, 120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewPrev)
        }
        if (pos < imagesList.size - 1) {
            GlideApp.with(context)
                    .load(imagesList[pos + 1].url)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .override(120, 120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewNext)
        }
    }



}