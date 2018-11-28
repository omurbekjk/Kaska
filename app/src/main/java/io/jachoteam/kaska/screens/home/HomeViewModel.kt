package io.jachoteam.kaska.screens.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.jachoteam.kaska.common.SingleLiveEvent
import io.jachoteam.kaska.data.FeedPostsRepository
import io.jachoteam.kaska.data.common.map
import io.jachoteam.kaska.models.FeedPost
import io.jachoteam.kaska.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(onFailureListener: OnFailureListener,
                    private val feedPostsRepo: FeedPostsRepository) : BaseViewModel(onFailureListener) {
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    lateinit var feedPostsList: List<FeedPost>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            feedPosts = feedPostsRepo.getFeedPosts(uid).map {
                it.sortedByDescending { it.timestampDate() }
            }
        }
    }

    fun init(feedPostsList: List<FeedPost>, uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
        }
        this.feedPostsList = feedPostsList
        Log.d("HomeViewModel","init()")
        Log.d("HomeViewModel","postList(): " + feedPostsList)
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (existingLoadedLikes == null) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                        likesCount = likes.size,
                        likedByUser = likes.find { it.userId == uid } != null)
            }
            loadedLikes += postId to liveData
            return liveData
        } else {
            return existingLoadedLikes
        }
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }
}