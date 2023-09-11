package com.akilimo.rya.updates

import android.R
import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdate(private val parentActivity: Activity) {
    private val appUpdateManager: AppUpdateManager?
    private var updateType = AppUpdateType.FLEXIBLE
    private val UPDATE_REQUEST_CODE = 500
    private val RESULT_CANCELLED = 0

    init {
        appUpdateManager = AppUpdateManagerFactory.create(parentActivity)
    }


    private var updatedListener = InstallStateUpdatedListener { installState: InstallState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            //notify user update is done
            notifyUpdateCompleteSnackBar()
        }
    }

    fun checkForUpdates() {
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { updateInfo: AppUpdateInfo ->
            val isUpdateAvailable =
                updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val updateAllowed = updateInfo.isUpdateTypeAllowed(updateType)
            if (isUpdateAvailable && updateAllowed) {
                val updateOptions = AppUpdateOptions
                    .newBuilder(updateType)
                    .build()
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        parentActivity,
                        updateOptions,
                        UPDATE_REQUEST_CODE
                    )
                } catch (ex: SendIntentException) {
                    throw RuntimeException(ex)
                }
            }
        }
        appUpdateManager.registerListener(updatedListener)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELLED) {
                Toast.makeText(parentActivity, "Update cancelled by user", Toast.LENGTH_SHORT)
                    .show()
            } else if (resultCode != AppCompatActivity.RESULT_OK) {
                checkForUpdates()
            }
        }
    }

    private fun notifyUpdateCompleteSnackBar() {
        Snackbar.make(
            parentActivity.findViewById(R.id.content),
            "Update completed",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("RESTART") { view: View? -> appUpdateManager?.completeUpdate() }.show()
    }

    fun onResume() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { info: AppUpdateInfo ->
            if (info.installStatus() == InstallStatus.DOWNLOADED) {
                notifyUpdateCompleteSnackBar()
            }
        }
    }

    fun onDestroy() {
        appUpdateManager?.unregisterListener(updatedListener)
    }
}