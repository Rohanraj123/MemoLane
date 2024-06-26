package com.example.memolane.view.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memolane.view.EditJournalScreen
import com.example.memolane.view.JournalListScreen
import com.example.memolane.view.NewJournalScreen
import com.example.memolane.view.ReadJournalScreen
import com.example.memolane.viewmodel.ImageSelectionViewModel
import com.example.memolane.viewmodel.MyViewModel
import com.example.memolane.viewmodel.NewJournalEditScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    myViewModel: MyViewModel,
    newJournalEditScreenViewModel: NewJournalEditScreenViewModel,
    activity: Activity,
    onImageButtonClicked: () -> Unit,
    imageSelectionViewModel: ImageSelectionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "journal_list_screen"
    ) {
        composable("journal_list_screen") {
            JournalListScreen(
                myViewModel,
                navController
            )
        }
        composable("edit_journal_screen/{journalId}") {backStackEntry ->
            val journalId = backStackEntry
                .arguments
                ?.getString("journalId")
                ?.toLong()
            EditJournalScreen(myViewModel,
                                navController,
                                backStackEntry)
        }
        composable("new_journal_screen") {
            NewJournalScreen(
                newJournalEditScreenViewModel,
                navController,
                activity,
                onImageButtonClicked,
                imageSelectionViewModel
            )
        }
        composable("read_journal_screen/{journalId}") {backStackEntry ->
            val journalId = backStackEntry
                .arguments
                ?.getString("journalId")
                ?.toLong()
            ReadJournalScreen(myViewModel,
                navController,
                backStackEntry)
        }
    }
}
