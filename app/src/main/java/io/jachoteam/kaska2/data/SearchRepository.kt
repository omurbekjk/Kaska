package io.jachoteam.kaska2.data

import android.arch.lifecycle.LiveData
import io.jachoteam.kaska2.models.SearchPost
import com.google.android.gms.tasks.Task

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}