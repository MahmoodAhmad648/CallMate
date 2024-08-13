package com.mahmood.callmate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String,
    val phoneNumber: String,
    val email: String,
    val dateOfCreation: Long,
    val isActive: Boolean,
    val image: ByteArray? = null
)