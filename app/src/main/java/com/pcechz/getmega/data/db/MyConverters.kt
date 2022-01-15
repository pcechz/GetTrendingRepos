package com.pcechz.getmega.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pcechz.getmega.data.model.Owner

class MyConverters {
    @TypeConverter
    fun fromOwner(owner: Owner?): String? {
        val type = object : TypeToken<Owner>() {}.type
        return Gson().toJson(owner, type)
    }

}