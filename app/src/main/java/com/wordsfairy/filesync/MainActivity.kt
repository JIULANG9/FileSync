package com.wordsfairy.filesync


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wordsfairy.filesync.constants.EventBus
import com.wordsfairy.filesync.constants.NavigateRouter
import com.wordsfairy.filesync.ext.flowbus.observeEvent
import com.wordsfairy.filesync.ui.page.home.AppPageWidget
import com.wordsfairy.filesync.ui.theme.WordsFairyTheme
import com.wordsfairy.filesync.ui.widgets.toast.PillButton
import com.wordsfairy.filesync.ui.widgets.toast.PillButtonModel
import com.wordsfairy.filesync.ui.widgets.toast.PillButtonState
import com.wordsfairy.filesync.ui.widgets.toast.ToastModel
import com.wordsfairy.filesync.ui.widgets.toast.ToastModelSuccess
import com.wordsfairy.filesync.ui.widgets.toast.ToastModelWarning
import com.wordsfairy.filesync.ui.widgets.toast.ToastUI
import com.wordsfairy.filesync.ui.widgets.toast.ToastUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 在这里处理结果
            if (result.resultCode == RESULT_OK) {
                ToastModelSuccess("权限已授权")
            } else {
                ToastModelWarning("权限被拒绝")
            }
        }

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        CONTEXT = this
        setContent {
            val navController = rememberAnimatedNavController()
            val toastState = remember { ToastUIState() }
            val pillButtonState = remember { PillButtonState() }

            WordsFairyTheme {

                Box(modifier = Modifier.fillMaxSize()) {
                    AppPageWidget(navController)
                    ToastUI(toastState)
                    PillButton(pillButtonState)
                }
            }
            observeEvent(key = EventBus.NavController) {
                val route = it as String
                if (route.isEmpty()) {
                    return@observeEvent
                }

                if (route == NavigateRouter.HomePage.AppNav) {
                    navController.popBackStack(route, true)
                }
                navController.navigate(route)
            }
            /** toast */
            observeEvent(key = EventBus.ShowToast) {
                lifecycleScope.launch {
                    val data = it as ToastModel
                    toastState.show(data)
                }
            }
            /** PillButton */
            observeEvent(key = EventBus.ShowPillButton) {
                lifecycleScope.launch {
                    val data = it as PillButtonModel
                    if (data.show) {
                        pillButtonState.show(data)
                    } else {
                        pillButtonState.hide()
                    }
                }
            }
        }

        checkAndRequestAllFilePermissions()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkAndRequestAllFilePermissions() {

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.setData(Uri.parse("package:$packageName"))
            launcher.launch(intent)
        }
    }

    companion object {
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: MainActivity

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordsFairyTheme {
        Greeting("Android")
    }
}