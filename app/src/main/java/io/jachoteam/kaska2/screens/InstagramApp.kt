package io.jachoteam.kaska2.screens

import android.app.Application
import io.jachoteam.kaska2.common.firebase.FirebaseAuthManager
import io.jachoteam.kaska2.data.firebase.FirebaseFeedPostsRepository
import io.jachoteam.kaska2.data.firebase.FirebaseNotificationsRepository
import io.jachoteam.kaska2.data.firebase.FirebaseSearchRepository
import io.jachoteam.kaska2.data.firebase.FirebaseUsersRepository
import io.jachoteam.kaska2.screens.notifications.NotificationsCreator
import io.jachoteam.kaska2.screens.search.SearchPostsCreator

class InstagramApp : Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val searchRepo by lazy { FirebaseSearchRepository() }

    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostsCreator(searchRepo)
    }
}