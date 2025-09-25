package com.example.closegoogleplay

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BlockPlayStoreService : AccessibilityService() {

    // רשימת Activities חסומים במצלמה (כיף / AR Fun)
    private val blockedCameraActivities = setOf("C0.q", "fun")

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        val className = event.className?.toString() ?: ""
        val eventType = event.eventType

        // נעקוב רק אחרי שינויי מסכים
        if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        // --- Google Play ---
        if (packageName == "com.android.vending") {
            Log.d("BlockPlayStoreService", "זוהתה פתיחה של Google Play")
            launchCoverApp()
            return
        }

        // --- מצלמה (מסכי כיף) ---
        if (packageName == "com.sec.android.app.camera") {
            Log.d("BlockPlayStoreService", "Activity במצלמה: $className")

            if (blockedCameraActivities.any { className.contains(it, ignoreCase = true) }) {
                Log.d("BlockPlayStoreService", "זוהה מסך אסור במצלמה → חוסמים")
                launchCoverApp()
            }
        }
    }

    private fun launchCoverApp() {
        val packageName = "com.example.launcherfolder"
        try {
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
                startActivity(launchIntent)
                Log.d("BlockPlayStoreService", "האפליקציה נפתחה עם LaunchIntent")
            } else {
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
