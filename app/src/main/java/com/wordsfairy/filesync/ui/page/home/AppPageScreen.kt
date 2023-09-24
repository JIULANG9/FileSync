package com.wordsfairy.filesync.ui.page.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.wordsfairy.filesync.constants.NavigateRouter


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

/**
 * @Description:首页
 * @Author: JIULANG
 * @Data: 2023/6/29 14:37
 */

@ExperimentalAnimationApi
fun NavGraphBuilder.homePageScreenNav(navController: NavHostController) {
    composable(
        NavigateRouter.HomePage.AppNav
    ) {
        HomePageScreen()
    }
}

@Composable
fun HomePageScreen(
    viewModel: AppPageViewModel = hiltViewModel()
) {

    Box(
        Modifier
            .fillMaxSize()

    ) {
        HomeUI()

    }

}
