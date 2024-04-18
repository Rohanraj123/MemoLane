package com.example.memolane.view

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.memolane.R
import com.example.memolane.data.Journal
import com.example.memolane.ui.theme.ButtonColor
import com.example.memolane.ui.theme.LightGrey
import com.example.memolane.viewmodel.MyViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun NewJournalScreen(
    myViewModel: MyViewModel,
    navController: NavHostController
) {
    var textValue = remember{ mutableStateOf("") }
    val dataTime = System.currentTimeMillis()
    val content = textValue.value
    val backgroundImageUrl = ""
    val soundTrackUrl = ""
    val journal = Journal(0, dataTime, content, backgroundImageUrl, soundTrackUrl)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = LightGrey)){
        Header(onClick = {})
        ExpandableTextField(
            text = textValue,
            onValueChange = {textValue.value = it},
            onSaveJournal = { myViewModel.saveJournal(journal) },
            myViewModel,
            navController
        )
    }
}

@Composable
fun Header(
    onClick: () -> Unit
) {
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
    myViewModel: MyViewModel,
    onSaveJournal: () -> Unit,
    navController: NavHostController
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomButton(
            icon = painterResource(id = R.drawable.gallery),
            onClick = {

            },
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CustomButton(
            icon = painterResource(id = R.drawable.camera),
            onClick = {

                      },
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CustomButton(
            icon = painterResource(id = R.drawable.microphone),
            onClick = {/*TODO*/},
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    onSaveJournal()
                    navController.navigate("journal_list_screen")
                }
                .clip(RoundedCornerShape(10.dp))
                .background(color = ButtonColor)
                .padding(16.dp)
        ) {
            Text(
                text = "SAVE",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
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
            .background(color = LightGray)
            .then(modifier)
    ) {
        Icon(painter = icon, contentDescription = null, modifier = Modifier.size(30.dp))
    }
}

@Composable
fun ExpandableTextField(
    text: MutableState<String>,
    onValueChange: (String) -> Unit,
    onSaveJournal: () -> Unit,
    myViewModel: MyViewModel,
    navController: NavHostController
) {
    val label: String = "Write your memories..."
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Header2(
                myViewModel = myViewModel,
                onSaveJournal = onSaveJournal,
                navController = navController
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            TextField(
                value = text.value,
                onValueChange = onValueChange,
                label = { Text(text = label)},
                singleLine = false,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle done action if needed */ }
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                minLines = 30,
                maxLines = Int.MAX_VALUE
            )
        }
    }
}
