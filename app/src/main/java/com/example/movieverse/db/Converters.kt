package com.example.movieverse.db

import androidx.room.TypeConverter

internal class Converters {

    // genresIds list of Int
    @TypeConverter
    fun fromListToString(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }

    fun fromStringToList(value: String): List<Int> {
        val list = mutableListOf<Int>()
        value.takeIf { it.isNotBlank() }
            ?.split(",")
            ?.map { id: String ->
                list.add(id.toInt())
            }
        return list
    }
}