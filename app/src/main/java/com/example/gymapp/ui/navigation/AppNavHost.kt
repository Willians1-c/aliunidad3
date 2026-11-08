package com.example.gymapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymapp.routines.RoutineDetailScreen
import com.example.gymapp.routines.RoutineFormScreen
import com.example.gymapp.routines.RoutineListScreen
import com.example.gymapp.ui.auth.LoginScreen
import com.example.gymapp.ui.auth.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ROUTINE_CREATE = "routines/create"
    const val ROUTINE_DETAIL = "routines/{routineId}"
    const val ROUTINE_EDIT = "routines/edit/{routineId}"

    fun routineDetail(id: Int) = "routines/$id"
    fun routineEdit(id: Int) = "routines/edit/$id"
}

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            RoutineListScreen(
                onRoutineClick = { id ->
                    navController.navigate(Routes.routineDetail(id))
                },
                onAddRoutineClick = {
                    navController.navigate(Routes.ROUTINE_CREATE)
                },
                onLogoutSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.ROUTINE_DETAIL,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) {
            RoutineDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditRoutineClick = { id ->
                    navController.navigate(Routes.routineEdit(id))
                },
                onDeleteSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.ROUTINE_CREATE) {
            RoutineFormScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ROUTINE_EDIT,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) {
            RoutineFormScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}
