package com.example.closegoogleplay
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.closegoogleplay.ui.theme.CloseGooglePlayTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CloseGooglePlayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SetupScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenAccessibility = { openAccessibilitySettings() }
                    )
                }
            }
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        Toast.makeText(
            this,
            "גלול למטה, מצא בתוך ישומים מותקנים את BlockPlayStore והפעל אותו",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun isAccessibilityServiceEnabled(
        context: Context,
        service: Class<out android.accessibilityservice.AccessibilityService>
    ): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
        val serviceName = ComponentName(context, service).flattenToString()
        return enabledServices?.contains(serviceName) == true
    }
}

@Composable
fun SetupScreen(modifier: Modifier = Modifier, onOpenAccessibility: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "כדי שהאפליקציה תחסום את Google Play,\nצריך להפעיל שירות נגישות.",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "במסך הבא: \n1. גלול למטה\n2. לחץ על יישומים מותקנים\n3. מצא את BlockPlayStore\n4.לחץ עליו והפעל אותו",
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onOpenAccessibility() }) {
            Text("פתח הגדרות נגישות")
        }
    }
}