package io.jachoteam.kaska2.screens.comments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.jachoteam.kaska2.data.FeedPostsRepository
import io.jachoteam.kaska2.data.UsersRepository
import io.jachoteam.kaska2.models.Comment
import io.jachoteam.kaska2.models.User
import io.jachoteam.kaska2.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class CommentsViewModel(private val feedPostsRepo: FeedPostsRepository,
                        usersRepo: UsersRepository,
                        onFailureListener: OnFailureListener) :
        BaseViewModel(onFailureListener) {
    lateinit var comments: LiveData<List<Comment>>
    private lateinit var postId: String
    val user: LiveData<User> = usersRepo.getUser()

    fun init(postId: String) {
        this.postId = postId
        comments = feedPostsRepo.getComments(postId)
    }

    fun createComment(text: String, user: User) {
        val comment = Comment(
                uid = user.uid,
                username = user.username,
                photo = user.photo,
                text = text)
        feedPostsRepo.createComment(postId, comment).addOnFailureListener(onFailureListener)
    }
}