package com.mahmood.callmate.presentation.ui_layer

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mahmood.callmate.R
import com.mahmood.callmate.navigation.Screen
import com.mahmood.callmate.presentation.states.ContactStates
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    state: ContactStates,
    onEvent: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        if (uri != null) {
            val inputStream = uri.let { context.contentResolver.openInputStream(it) }
            val bytes = inputStream?.readBytes()
            if (bytes != null) {
                state.image.value = bytes
            }
        }
    }
    val bitmap = state.image.value.let { BitmapFactory.decodeByteArray(it, 0, it.size) }


    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.height(48.dp),
                title = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)){
                        Text(
                            text = "Contacts",
                            fontSize = 18.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    bitmap = bitmap?.asImageBitmap() ?: BitmapFactory.decodeResource(context.resources, R.drawable.man).asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch("image/*")
                        }.align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.userName.value,
                    onValueChange = { state.userName.value = it },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Name", color = Color.Black) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                    placeholder = { Text(text = "Enter Your Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF0057),
                        unfocusedBorderColor = Color(0xFFDD1E5F),
                        errorBorderColor = Color.Red,
                    )
                )

                OutlinedTextField(
                    value = state.phoneNumber.value,
                    onValueChange = { state.phoneNumber.value = it },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Phone", color = Color.Black) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                    placeholder = { Text(text = "Enter Your Phone Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF0057),
                        unfocusedBorderColor = Color(0xFFDD1E5F),
                        errorBorderColor = Color.Red,
                    )
                )

                OutlinedTextField(
                    value = state.email.value,
                    onValueChange = { state.email.value = it },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Email", color = Color.Black) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
                    placeholder = { Text(text = "Enter Your Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF0057),
                        unfocusedBorderColor = Color(0xFFDD1E5F),
                        errorBorderColor = Color.Red,
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if(state.userName.value.isEmpty()||state.phoneNumber.value.isEmpty()||state.email.value.isEmpty()){
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }else{
                            onEvent.invoke()
                            navController.navigate(Screen.HomeScreen.route)
                        }

                              },
                    modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(10.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFDD1E5F))
                ) {
                    Text(
                        text = if (state.id.value == 0) "Add Contact" else "Edit Contact",
                        fontSize = 16.sp
                    )
                }
            }
        }
    )
}
