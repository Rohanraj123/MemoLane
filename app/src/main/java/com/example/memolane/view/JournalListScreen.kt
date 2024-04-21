package com.example.memolane.view

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.memolane.R
import com.example.memolane.data.Journal
import com.example.memolane.ui.theme.ButtonColor
import com.example.memolane.ui.theme.GrayColor
import com.example.memolane.viewmodel.MyViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalListScreen(
    myViewModel: MyViewModel,
    navHostController: NavHostController
) {
    val journalUiState by myViewModel.journalListUiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GrayColor)
    ) {
        item {
            WelcomeText()
        }

        items(journalUiState.journalList) {journal ->
            JournalCard(
                image = journal.backgroundImageUrl?.toUri(),
                soundTrackUrl = journal.soundTrackUrl,
                content = journal.content,
                dateTime = journal.dateTime,
                onEditClick = { navHostController.navigate("edit_journal_screen/${journal.id}") },
                onDeleteClick = { myViewModel.deleteJournal(journal.id) },
                onCardClick = { navHostController.navigate("read_journal_screen/${journal.id}") }
            )
        }
    }
    CustomFloatingButton { navHostController.navigate("new_journal_screen") }
}

@Composable
fun WelcomeText() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = "MEMOLANE",
            modifier = Modifier
                .padding(5.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
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



@RequiresApi(Build.VERSION_CODES.O)
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
    
    val localDateTime = LocalDateTime
                        .ofInstant(Instant
                            .ofEpochMilli(dateTime),
                            ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(color = Color.White)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(width = 1.dp, color = Color.Black)
            ) {

                if (image != null) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Text(text = "No selected Image : (")
                }

            }

            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(GrayColor)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatter.format(localDateTime),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(10.dp),
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row {

                        CustomButton(
                            onClick = onEditClick,
                            icon = Icons.Default.Edit
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        CustomButton(
                            onClick = onDeleteClick,
                            icon = Icons.Default.Delete
                        )

                    }

                }
            }
    }
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(10.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }

}


@Composable
fun CustomFloatingButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.padding(16.dp),
            containerColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 16.dp
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}