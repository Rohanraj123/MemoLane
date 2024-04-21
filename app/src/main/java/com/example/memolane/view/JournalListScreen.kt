package com.example.memolane.view

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.memolane.R
import com.example.memolane.data.Journal
import com.example.memolane.ui.theme.ButtonColor
import com.example.memolane.viewmodel.MyViewModel

@Composable
fun JournalListScreen(
    myViewModel: MyViewModel,
    navHostController: NavHostController
) {
    val journalUiState by myViewModel.journalListUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        WelcomeText(navHostController)
        JournalList(
            journals = journalUiState.journalList,
            myViewModel,
            navHostController
        )
    }
}

@Composable
fun WelcomeText(navHostController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row (){
            Text(
                text = "MEMOLANE",
                modifier = Modifier
                    .padding(5.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(100.dp))
            AddButton(onClick = {
                navHostController.navigate("new_journal_screen")
            })
        }

        Text(
            text = "Welcome",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(5.dp),
            fontSize = 30.sp
        )
    }
}

@Composable
fun JournalList(
    journals: List<Journal>,
    myViewModel: MyViewModel,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(journals) {journal ->
            JournalCard(
                image = journal.backgroundImageUrl?.toUri(),
                soundTrackUrl = journal.soundTrackUrl,
                content = journal.content,
                dateTime = journal.dateTime,
                onEditClick = { navController.navigate("edit_journal_screen/${journal.id}") },
                onDeleteClick = { myViewModel.deleteJournal(journal.id) },
                onCardClick = { navController.navigate("read_journal_screen/${journal.id}") }
            )
            Log.d("JournalListScreen", "Completely fetched updated journal : $journal")
        }
    }
}

@Composable
fun JournalCard(
    image: Uri?,
    soundTrackUrl: String?,
    content: String,
    dateTime: Long,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .clickable { onCardClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (image != null) {
                AsyncImage(
                    model  = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.mountain),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { /* Play sound track */ },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play Sound Track")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = dateTime.toString(),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onEditClick
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor
        )
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Black
        )
    }
}
