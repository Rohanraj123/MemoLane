package com.example.memolane.view

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.memolane.R
import com.example.memolane.data.Journal
import com.example.memolane.ui.theme.GrayColor
import com.example.memolane.util.PermissionUtil
import com.example.memolane.viewmodel.ImageSelectionViewModel
import com.example.memolane.viewmodel.NewJournalEditScreenViewModel

@Composable
fun NewJournalScreen(
    newJournalEditScreenViewModel: NewJournalEditScreenViewModel,
    navController: NavHostController,
    activity: Activity,
    onImageButtonClicked: () -> Unit,
    imageSelectionViewModel: ImageSelectionViewModel
) {
    val textValue = remember{ mutableStateOf("") }

    val dataTime = System.currentTimeMillis()
    val content = textValue.value
    val soundTrackUrl = ""
    var backgroundImageUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(backgroundImageUri) {
        val observer = Observer<Uri?> {uri ->
            backgroundImageUri = uri
        }

        imageSelectionViewModel.selectedImageUri.observeForever(observer)
        onDispose {
            imageSelectionViewModel.selectedImageUri.removeObserver(observer)
        }
    }

    val journal = Journal(0,
        dataTime,
        content,
        backgroundImageUri.toString(),
        soundTrackUrl
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GrayColor)
    ){
        Header(onClick = {navController.popBackStack()})
        ExpandableTextField(
            text = textValue,
            onValueChange = {textValue.value = it},
            onSaveJournal = {
                newJournalEditScreenViewModel.saveJournal(journal)
                            },
            newJournalEditScreenViewModel,
            navController,
            activity,
            onImageButtonClicked,
            backgroundImageUri = backgroundImageUri
        )
    }
}

@Composable
fun Header(onClick: () -> Unit) {
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
            text = "New Memory",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    }
}

@Composable
fun Header2(
    newJournalEditScreenViewModel: NewJournalEditScreenViewModel,
    onSaveJournal: () -> Unit,
    navController: NavHostController,
    activity: Activity,
    onImageButtonClicked: () -> Unit
) {

    Box(
        modifier = Modifier
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

            CustomButton(
                icon = painterResource(id = R.drawable.gallery),
                onClick = {
                    val isPermissionGranted = PermissionUtil.requestStoragePermission(activity)
                    if (isPermissionGranted) {
                        onImageButtonClicked()
                    }
                },
                modifier = Modifier.padding(10.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 10.dp, end = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                onClick = {
                    onSaveJournal()
                    navController.navigate("journal_list_screen")
                }
            ) {
                Text(
                    text = "SAVE",
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
fun CustomButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .clip(CircleShape)
            .background(color = White)
            .then(modifier)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun ExpandableTextField(
    text: MutableState<String>,
    onValueChange: (String) -> Unit,
    onSaveJournal: () -> Unit,
    newJournalEditScreenViewModel: NewJournalEditScreenViewModel,
    navController: NavHostController,
    activity: Activity,
    onImageButtonClicked: () -> Unit,
    backgroundImageUri: Uri?
) {
    val label: String = "Write your memories..."

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Header2(
                newJournalEditScreenViewModel = newJournalEditScreenViewModel,
                onSaveJournal = onSaveJournal,
                navController = navController,
                activity,
                onImageButtonClicked
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(width = 1.dp, color = Color.Black),
                contentAlignment = Alignment.Center
            ) {

                if (backgroundImageUri != null) {
                    AsyncImage(
                        model = backgroundImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            TextField(
                value = text.value,
                onValueChange = onValueChange,
                label = { Text(text = label)},
                singleLine = false,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
                keyboardActions = KeyboardActions(
                    onNext = {
                        
                    }
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                minLines = 30,
                maxLines = Int.MAX_VALUE,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = White,
                    unfocusedContainerColor = Color.White,
                )
            )
        }
    }
}
