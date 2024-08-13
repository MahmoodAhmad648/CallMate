package com.mahmood.callmate.presentation.ui_layer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.mahmood.callmate.R
import com.mahmood.callmate.navigation.Screen
import com.mahmood.callmate.presentation.states.ContactStates
import com.mahmood.callmate.presentation.viewModel.ContactViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

//@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactViewModel,
    navController: NavHostController,
    state: ContactStates
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Launcher to request permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Permission granted!")
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Permission denied.")
            }
        }
    }

    fun checkAndRequestCallPermission(onPermissionGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted
                onPermissionGranted()
            }
            else -> {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.height(48.dp),
                title = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)) {
                        Text(
                            text = "Contacts",
                            fontSize = 18.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.changeSortType() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sort),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDD1E5F),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(10.dp),
                contentColor = Color.White,
                containerColor = Color.Black,
                onClick = { navController.navigate(Screen.AddContact.route) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) } // Set SnackbarHost
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF))
        ) {
            items(
                items = state.contact,
                key = { it.id }
            ) { contact ->
                SwipeToDeleteContainer(item = contact, onDelete = {
                    state.id.value = contact.id
                    state.userName.value = contact.userName
                    state.phoneNumber.value = contact.phoneNumber
                    state.email.value = contact.email
                    state.dateOfCreation.value = contact.dateOfCreation
                    viewModel.deleteContact()
                }) { contactItem ->
                    ContactCard(
                        name = contactItem.userName,
                        image = contactItem.image,
                        onClick = {
                            state.id.value = contactItem.id
                            state.userName.value = contactItem.userName
                            state.phoneNumber.value = contactItem.phoneNumber
                            state.email.value = contactItem.email
                            state.dateOfCreation.value = contactItem.dateOfCreation
                            state.image.value = contactItem.image!!
                            navController.navigate(Screen.DetailScreen.route)
                        },
                        onCallClick = {
                            checkAndRequestCallPermission {
                                val intent = Intent(Intent.ACTION_CALL)
                                intent.data = android.net.Uri.parse("tel:${contactItem.phoneNumber}")
                                context.startActivity(intent)
                            }
                        }
                    )
                }
            }
        }
    }
}
//@Composable
//fun ContactCard(
//    name: String,
//    phoneNumber: String,
//    email: String,
//    onClick: () -> Unit
//) {
//
//    Card(modifier = Modifier
//        .fillMaxWidth()
//        .padding(8.dp)
//        .clickable(onClick = onClick),
//        elevation = CardDefaults.cardElevation(10.dp),
//        shape = RoundedCornerShape(10.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//        ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row (modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween){
//
//                    Text(text = name,
//                        fontSize = 17.sp,
//                        fontWeight = FontWeight.SemiBold
//                        )
//
//
//                    Text(text = phoneNumber,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold)
//
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = email)
//
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = { DeleteBackground(dismissState) },
            directions = setOf(SwipeToDismissBoxValue.EndToStart),
            dismissContent = { content(item) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(dismissState: SwipeToDismissBoxState) {


    val backgroundColor = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        SwipeToDismissBoxValue.Settled -> Color.Transparent
        SwipeToDismissBoxValue.StartToEnd -> TODO()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}

@Composable
fun ContactCard(
    name: String,
    image: ByteArray?,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
        .clickable(onClick = onClick),
        border = BorderStroke(1.dp, Color(0xFFDD1E5F)),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val bitmap = image?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Display the first letter of the name in a circle when the image is null
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(randomColor())
                ) {
                    Text(
                        text = name.first().uppercaseChar().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDD1E5F))
                    .clickable(onClick = onCallClick)
            ){
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                    )
            }
        }
    }
}
fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}