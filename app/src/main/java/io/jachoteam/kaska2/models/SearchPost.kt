package io.jachoteam.kaska2.models

import com.google.firebase.database.Exclude

data class SearchPost(val image: String = "", val caption: String = "", val postId: String = "",
                      @get:Exclude val id: String = "")