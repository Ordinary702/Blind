package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glowstudio.android.blindsjn.ui.MainScreen
import com.glowstudio.android.blindsjn.ui.screens.AddScheduleScreen
import com.glowstudio.android.blindsjn.ui.screens.LoginScreen
import com.glowstudio.android.blindsjn.ui.screens.SignupScreen
import com.glowstudio.android.blindsjn.ui.screens.HomeScreen
import com.glowstudio.android.blindsjn.ui.screens.BoardScreen
import com.glowstudio.android.blindsjn.ui.screens.BoardDetailScreen
import com.glowstudio.android.blindsjn.ui.screens.PopularScreen
import com.glowstudio.android.blindsjn.ui.screens.MessageScreen
import com.glowstudio.android.blindsjn.ui.screens.ProfileScreen
import com.glowstudio.android.blindsjn.ui.screens.WritePostScreen
import com.glowstudio.android.blindsjn.ui.screens.PostDetailScreen
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarState
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 로그인 화면
        composable("login") {
            LoginScreen(
                navController = navController,
                onSignupClick = { navController.navigate("signup") },
                onForgotPasswordClick = { /* TODO: 비밀번호 찾기 화면 이동 */ }
            )
        }

        // 회원가입 화면
        composable("signup") {
            SignupScreen(
                onSignupClick = { phoneNumber, password ->
                    // TODO: 서버와 연동하여 회원가입 처리
                    navController.navigate("home") // 회원가입 후 홈 화면으로 이동
                },
                onBackToLoginClick = { navController.navigateUp() }
            )
        }

        // 메인 화면: 아래 5개 페이지의 상위 관리
        composable("main") {
            MainScreen() // MainScreen은 네비게이션의 컨테이너 역할
        }

        // 홈 화면
        composable("home") {
            // 홈 화면에서는 TopBar를 단순하게 설정
            topBarViewModel.updateState(TopBarState("홈 화면", false, false))
            HomeScreen()
        }

        // 게시판 목록 화면
        composable("board") {
            topBarViewModel.updateState(TopBarState("게시판 목록", false, true))
            BoardScreen(navController = navController)
        }

        // 게시판 상세 화면
        composable("boardDetail/{title}") { backStackEntry ->
            val postTitle = backStackEntry.arguments?.getString("title") ?: "게시글"
            topBarViewModel.updateState(TopBarState(postTitle, true, true))
            BoardDetailScreen(navController = navController, title = postTitle)
        }

        // 게시글 쓰기 화면
        composable("writePost") {
            topBarViewModel.updateState(TopBarState("게시글 작성", true, false))
            WritePostScreen(navController = navController)
        }

        // 상세 게시물 화면
        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: "1"
            // 만약 PostDetailScreen에서 topBar 상태 변경이 필요하다면, 여기서 updateState 호출
            topBarViewModel.updateState(TopBarState("게시글 상세", true, false))
            PostDetailScreen(navController = navController, postId = postId)
        }

        // 인기글 화면
        composable("popular") {
            topBarViewModel.updateState(TopBarState("인기글", false, false))
            PopularScreen()
        }

        // 메시지 (캘린더) 화면
        composable("message") {
            topBarViewModel.updateState(TopBarState("캘린더", false, true))
            MessageScreen(navController = navController)
        }

        // 캘린더 일정 추가 화면
        composable("addSchedule") {
            topBarViewModel.updateState(TopBarState("일정 추가", true, false))
            AddScheduleScreen(
                onCancel = { navController.navigateUp() },
                onSave = { schedule ->
                    // schedule 정보 처리 로직
                    navController.navigateUp()
                }
            )
        }

        // 프로필 화면
        composable("profile") {
            topBarViewModel.updateState(TopBarState("프로필", false, false))
            ProfileScreen(onLogoutClick = {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
    }
}
