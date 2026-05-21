package com.scholarlyapps.pathlingo.ui.auth

import android.content.Context
import android.content.Intent
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object LogoutHelper {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @JvmStatic
    fun logout(context: Context) {
        ServiceLocator.init(context)
        scope.launch { ServiceLocator.authRepository.logout() }
        val intent = Intent(context, AuthActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}
