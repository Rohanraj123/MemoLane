package com.example.memolane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.memolane.ui.theme.MemoLaneTheme
import com.example.memolane.view.navigation.Navigation
import com.example.memolane.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoLaneTheme {
                val myViewModel = hiltViewModel<MyViewModel>()
                val navController = rememberNavController()
                Navigation(navController = navController, myViewModel)
            }
        }
    }
}
