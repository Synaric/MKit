package com.synaric.mkit.data.db

import androidx.room.TypeConverter
import com.synaric.mkit.data.entity.CableType
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun cableTypeToInt(cableType: CableType?): Int {
        return cableType?.type ?: 0
    }

    @TypeConverter
    fun intToCableType(value: Int?): CableType? {
        val arrayOfCableTypes = CableType.values()
        for (ct: CableType in arrayOfCableTypes) {
            if (ct.type == value) {
                return ct
            }
        }

        return null
    }
}