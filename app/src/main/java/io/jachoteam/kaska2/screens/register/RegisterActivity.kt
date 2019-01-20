package io.jachoteam.kaska2.screens.register

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import io.jachoteam.kaska2.R
import io.jachoteam.kaska2.screens.common.BaseActivity
import io.jachoteam.kaska2.screens.home.HomeActivity

class RegisterActivity : BaseActivity(), EmailFragment.Listener, NamePassFragment.Listener {
    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mViewModel = initViewModel()
        mViewModel.goToNamePassScreen.observe(this, Observer {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, NamePassFragment())
                    .addToBackStack(null)
                    .commit()
        })
        mViewModel.goToHomeScreen.observe(this, Observer {
            startHomeActivity()
        })
        mViewModel.goBackToEmailScreen.observe(this, Observer {
            supportFragmentManager.popBackStack()
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment())
                    .commit()
        }
    }

    override fun onNext(email: String) {
        mViewModel.onEmailEntered(email)
    }

    override fun onRegister(fullName: String, password: String) {
        mViewModel.onRegister(fullName, password)
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}