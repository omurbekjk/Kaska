package io.jachoteam.kaska2.data.firebase

import android.arch.lifecycle.LiveData
import io.jachoteam.kaska2.common.toUnit
import io.jachoteam.kaska2.data.SearchRepository
import io.jachoteam.kaska2.data.common.map
import io.jachoteam.kaska2.data.firebase.common.FirebaseLiveData
import io.jachoteam.kaska2.data.firebase.common.database
import io.jachoteam.kaska2.models.SearchPost
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class FirebaseSearchRepository : SearchRepository {
    override fun createPost(post: SearchPost): Task<Unit> =
            database.child("search-posts").push()
                    .setValue(post.copy(caption = post.caption.toLowerCase())).toUnit()

    override fun searchPosts(text: String): LiveData<List<SearchPost>> {
        val reference = database.child("search-posts")
        val query = if (text.isEmpty()) {
            reference
        } else {
            reference.orderByChild("caption")
                    .startAt(text.toLowerCase()).endAt("${text.toLowerCase()}\\uf8ff")
        }
        return FirebaseLiveData(query).map {
            it.children.map { it.asSearchPost()!! }
        }
    }
}

private fun DataSnapshot.asSearchPost(): SearchPost? =
        getValue(SearchPost::class.java)?.copy(id = key)