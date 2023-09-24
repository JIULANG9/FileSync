package com.wordsfairy.filesync.ui.page.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.wordsfairy.filesync.constants.NavigateRouter
import com.wordsfairy.filesync.ui.widgets.SlideAnimatedNavHost

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/9/18 18:57
 */
@ExperimentalAnimationApi
@Composable
fun AppPageWidget(navController: NavHostController) {

    SlideAnimatedNavHost(
        navController,
        startDestination = NavigateRouter.HomePage.AppNav,
    ) {
        homePageScreenNav(navController)
    }
}