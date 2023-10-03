package com.comunidadedevspace.joaovazstudio.data.database

import androidx.room.TypeConverter

class ExerciseIdListConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<Int> {
        if (value.isEmpty()) {
            return emptyList()
        }
        return value.split(",").map { it.toInt() }
    }
}
