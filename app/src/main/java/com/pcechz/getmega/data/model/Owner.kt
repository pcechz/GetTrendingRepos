package com.pcechz.getmega.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "owner_table")
@Parcelize
data class Owner(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    val owner_id: Int,
    @SerializedName("login")
    @Expose
    val login: String,
    @SerializedName("avatar_url")
    @Expose
    val avatar_url: String
): Parcelable