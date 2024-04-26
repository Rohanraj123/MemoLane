package com.example.memolane.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.memolane.ui.theme.GrayColor
import com.example.memolane.viewmodel.MyViewModel
import kotlinx.coroutines.launch


@Composable
fun EditJournalScreen(
    myViewModel: MyViewModel,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val journalId = navBackStackEntry.arguments?.getString("journalId")
    val journalUiState by myViewModel.journalListUiState.collectAsState()

    val selectedJournal = journalUiState.journalList.find { it.id == journalId?.toLong() }
    val backgroundImageUri = selectedJournal?.backgroundImageUrl?.toUri()


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = GrayColor)
    ){

        EditJournalScreenHeader(onClick = {navController.popBackStack()})

        EditJournal(
            myViewModel,
            journalId,
            navController,
            backgroundImageUri
        )
    }
}

@Composable
fun EditJournalScreenHeader(onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = Color.White)
                .clickable { onClick() }
                .padding(10.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Edit Memory",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    }
}

@Composable
fun EditJournal(
    myViewModel: MyViewModel,
    journalId: String?,
    navController: NavHostController,
    uri: Uri?
) {

    val coroutineScope = rememberCoroutineScope()
    var journalContent by remember(journalId) { mutableStateOf("") }

    LaunchedEffect(journalId) {
        if (journalId != null) {
            val content = myViewModel.readJournal(journalId.toLong())
            journalContent = content
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface (
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            color = Color.White,
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(modifier = Modifier.padding(10.dp)) {

                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier) {

                    BasicTextField(
                        value = journalContent,
                        onValueChange = { journalContent = it },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        minLines = 30,
                        maxLines = Int.MAX_VALUE
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        Button(
            onClick = { coroutineScope.launch {

                if (journalId != null) {
                    val journal = myViewModel.getJournalById(journalId.toLong())
                    journal.content = journalContent
                    myViewModel.editJournal(journal)
                    navController.navigate("journal_list_screen")
                    { popUpTo("edit_journal_screen") { inclusive = true } }
                }
            } },

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 10.dp, end = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),

            ) {
            Text(text = "Save Changes")
        }
    }
}
