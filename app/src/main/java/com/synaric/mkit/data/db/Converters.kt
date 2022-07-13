package com.synaric.mkit.data.db

import androidx.room.TypeConverter
import com.synaric.mkit.data.entity.CableType
import com.synaric.mkit.data.entity.Change
import com.synaric.mkit.data.entity.Condition
import com.synaric.mkit.data.entity.SalesChannel
import java.util.*

/**
 * 定义了应用自定义数据到数据库支持类型之间的转换。
 */
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
    fun intToCableType(value: Int?): CableType {
        val arrayOfCableTypes = CableType.values()
        for (ct: CableType in arrayOfCableTypes) {
            if (ct.type == value) {
                return ct
            }
        }

        return CableType.UNKNOWN
    }

    @TypeConverter
    fun conditionToInt(condition: Condition?): Int {
        return condition?.type ?: -1
    }

    @TypeConverter
    fun intToCondition(value: Int?): Condition {
        val arrayOfConditions = Condition.values()
        for (c: Condition in arrayOfConditions) {
            if (c.type == value) {
                return c
            }
        }

        return Condition.UNKNOWN
    }

    @TypeConverter
    fun changeToInt(change: Change?): Int {
        return change?.type ?: -1
    }

    @TypeConverter
    fun intToChange(value: Int?): Change {
        val arrayOfChanges = Change.values()
        for (c: Change in arrayOfChanges) {
            if (c.type == value) {
                return c
            }
        }

        return Change.UNKNOWN
    }

    @TypeConverter
    fun salesChannelToInt(salesChannel: SalesChannel?): Int {
        return salesChannel?.type ?: 0
    }

    @TypeConverter
    fun intToSalesChannel(value: Int?): SalesChannel {
        val arrayOfSalesChannels = SalesChannel.values()
        for (c: SalesChannel in arrayOfSalesChannels) {
            if (c.type == value) {
                return c
            }
        }

        return SalesChannel.UNKNOWN
    }
}