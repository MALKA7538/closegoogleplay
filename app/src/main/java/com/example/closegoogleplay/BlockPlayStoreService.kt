package com.example.closegoogleplay

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BlockPlayStoreService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("BlockPlayStoreService", "אירוע: ${event?.packageName} סוג: ${event?.eventType}")

        if (event?.packageName == "com.android.vending" &&
            (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
                    event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        ) {
            Log.d("BlockPlayStoreService", "זוהתה פתיחה של Google Play")
            launchCoverApp()
        }
    }

    private fun launchCoverApp() {
        val packageName = "com.example.launcherfolder"
        try {
            // קודם ננסה להשיג אינטנט רגיל (MAIN/LAUNCHER)
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launchIntent)
                Log.d("BlockPlayStoreService", "האפליקציה נפתחה עם LaunchIntent")
            } else {
                // אם נכשל, ננסה לפתוח Activity ידנית
                val intent = Intent()
                intent.setClassName(packageName, "$packageName.MainActivity")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Log.d("BlockPlayStoreService", "האפליקציה נפתחה ידנית עם ClassName")
            }
        } catch (e: Exception) {
            Log.e("BlockPlayStoreService", "שגיאה בפתיחת האפליקציה: ${e.message}")
        }
    }

    override fun onInterrupt() {
        Log.d("BlockPlayStoreService", "שירות הנגישות הופסק")
    }
}
