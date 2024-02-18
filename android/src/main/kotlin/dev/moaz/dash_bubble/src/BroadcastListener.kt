package dev.moaz.dash_bubble.src

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.MethodChannel

/** This broadcast listener is used to listen for the bubble callbacks */
class BroadcastListener(channel: MethodChannel) : BroadcastReceiver() {
    private val channel = channel

    override fun onReceive(context: Context?, intent: Intent) {
        when (intent.action) {

            /** This method is called when the bubble is tapped */
            Constants.ON_TAP -> {
                channel.invokeMethod(Constants.ON_TAP, getAppLaunched(context!!, intent))
            }

            /** This method is called when the bubble is tapped down (pressed) */
            Constants.ON_TAP_DOWN -> {
                channel.invokeMethod(
                    Constants.ON_TAP_DOWN, getCoordinatesValues(intent)
                )
            }

            /** This method is called when the bubble is tapped up (released) */
            Constants.ON_TAP_UP -> {
                channel.invokeMethod(
                    Constants.ON_TAP_UP, getCoordinatesValues(intent)
                )
            }

            /** This method is called when the bubble is moved */
            Constants.ON_MOVE -> {
                channel.invokeMethod(
                    Constants.ON_MOVE, getCoordinatesValues(intent)
                )
            }
        }
    }

    /** This method is used to get the coordinates from the intent extras. */
    private fun getCoordinatesValues(intent: Intent): HashMap<String, Double> {
        return hashMapOf(
            Constants.X_AXIS_VALUE to intent.getDoubleExtra(Constants.X_AXIS_VALUE, 0.0),
            Constants.Y_AXIS_VALUE to intent.getDoubleExtra(Constants.Y_AXIS_VALUE, 0.0)
        )
    }

    private fun getAppLaunched(context: Context, intent: Intent): Boolean {
//    val appLaunched = intent.getBooleanExtra(Constants.APP_LAUNCHED, true)

        //Check if the app is in the foreground
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        val foregroundProcess = runningAppProcesses[0].processName
        val packageName = context.packageName

        val appLaunched = foregroundProcess == packageName

    if (!appLaunched) {
        // Bring the app to the foreground by launching the main activity
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        launchIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(launchIntent)
    }

    return appLaunched
}

}