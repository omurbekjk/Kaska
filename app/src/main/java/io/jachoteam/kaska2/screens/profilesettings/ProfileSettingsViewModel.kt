package io.jachoteam.kaska2.screens.profilesettings

import io.jachoteam.kaska2.common.AuthManager
import io.jachoteam.kaska2.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class ProfileSettingsViewModel(private val authManager: AuthManager,
                               onFailureListener: OnFailureListener) :
        BaseViewModel(onFailureListener),
        AuthManager by authManager