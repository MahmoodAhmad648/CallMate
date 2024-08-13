package com.mahmood.callmate.presentation.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.mahmood.callmate.data.database.Contact

data class ContactStates(

    val contact: List<Contact> = emptyList(),
    val id: MutableState<Int> = mutableIntStateOf(0),
    val image: MutableState<ByteArray> = mutableStateOf(ByteArray(0)),
    val userName: MutableState<String> = mutableStateOf(""),
    val phoneNumber: MutableState<String> = mutableStateOf(""),
    val email: MutableState<String> = mutableStateOf(""),
    val dateOfCreation: MutableState<Long> = mutableLongStateOf(0),



    )
