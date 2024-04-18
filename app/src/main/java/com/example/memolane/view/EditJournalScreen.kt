package com.example.memolane.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.memolane.R
import com.example.memolane.ui.theme.ButtonColor
import com.example.memolane.viewmodel.MyViewModel
import kotlinx.coroutines.launch


@Composable
fun EditJournalScreen(
    myViewModel: MyViewModel,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val journalId = navBackStackEntry.arguments?.getString("journalId")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightGray)
    ){
        EditJournalScreenHeader(onClick = {/*TODO*/})
        EditJournal(
            image = painterResource(id = R.drawable.ic_launcher_background),
            myViewModel,
            journalId,
            navController
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "Update Edit")
        }
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
    image: Painter,
    myViewModel: MyViewModel,
    journalId: String?,
    navController: NavHostController
) {

    val coroutineScope = rememberCoroutineScope()

    var journalContent by remember(journalId) { mutableStateOf("") }

    LaunchedEffect(journalId) {
        if (journalId != null) {
            val content = myViewModel.readJournal(journalId.toLong())
            journalContent = content ?: ""
        }
    }

    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        color = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Row(modifier = Modifier
                    .width(300.dp)
                    .padding(start = 50.dp)
                    .background(color = LightGray)
                    .clip(RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = Color.White)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null,
                            modifier = Modifier.size(50.dp))
                    }
                    Text(text = "00:12",
                        modifier = Modifier.padding(10.dp),
                        color = Color.Gray
                    )
                    Image(
                        painter = painterResource(id = R.drawable.vertical_bars),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp)
                    )
                }
            }
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
                Button(
                    onClick = { coroutineScope.launch {
                        if (journalId != null) {

                            val journal = myViewModel.getJournalById(journalId.toLong())
                            journal.content = journalContent
                            myViewModel.editJournal(journal)
                            navController.navigate("journal_list_screen")
                            {popUpTo("edit_journal_screen") {inclusive = true} }

                        }
                    } },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColor,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Update the change")
                }
            }
        }
    }
}