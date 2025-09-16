package com.example.closegoogleplay

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BlockPlayStoreService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.android.vending" &&
            (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
                    event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        ) {
            closePlayStore()
        }
    }

    private fun closePlayStore() {
        try {
            val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses("com.android.vending")

            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(homeIntent)

            Log.d("BlockPlayStoreService", "Google Play נסגר")
        } catch (e: Exception) {
            Log.e("BlockPlayStoreService", "שגיאה בסגירת Google Play: ${e.message}")
        }
    }

    override fun onInterrupt() {
        Log.d("BlockPlayStoreService", "שירות הנגישות הופסק")
    }
}
