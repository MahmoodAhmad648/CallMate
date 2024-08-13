package com.mahmood.callmate.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmood.callmate.data.database.Contact
import com.mahmood.callmate.data.database.ContactDatabase
import com.mahmood.callmate.presentation.states.ContactStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactViewModel @Inject constructor(var database: ContactDatabase) : ViewModel() {

    private val isSortByName = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val contact = isSortByName.flatMapLatest {
        if (it){
            database.dao.getContactOrderByName()
        }else{
            database.dao.getContactOrderByDate()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    private val _state = MutableStateFlow(ContactStates())

    val state = combine(_state,contact,isSortByName){
        state,contact,isSortByName ->
        state.copy(contact = contact)
    }.stateIn(
        viewModelScope,started = SharingStarted.WhileSubscribed(5000),ContactStates()
    )

    fun saveContact(){
        val contact = Contact(
            id = _state.value.id.value,
            userName = _state.value.userName.value,
            image = _state.value.image.value,
            phoneNumber = _state.value.phoneNumber.value,
            email = _state.value.email.value,
            dateOfCreation = System.currentTimeMillis(),
            isActive = true

        )
        viewModelScope.launch {
            database.dao.upsertContact(contact)

        }

        state.value.id.value = 0
        state.value.userName.value = ""
        state.value.phoneNumber.value = ""
        state.value.email.value = ""
        state.value.dateOfCreation.value = 0
        state.value.image.value = ByteArray(0)

    }

    fun changeSortType(){
        isSortByName.value = !isSortByName.value

    }

    fun deleteContact(){
        val contact = Contact(
            id = _state.value.id.value,
            userName = _state.value.userName.value,
            phoneNumber = _state.value.phoneNumber.value,
            email = _state.value.email.value,
            dateOfCreation = _state.value.dateOfCreation.value,
            isActive = true

        )

        viewModelScope.launch {
            database.dao.deleteContact(contact)
        }

        state.value.id.value = 0
        state.value.userName.value = ""
        state.value.phoneNumber.value = ""
        state.value.email.value = ""
        state.value.dateOfCreation.value = 0


    }


}