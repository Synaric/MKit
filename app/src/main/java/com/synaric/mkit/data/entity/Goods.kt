package com.synaric.mkit.data.entity

import androidx.room.*
import com.synaric.art.BaseApplication
import com.synaric.mkit.R
import java.util.*

@Entity
data class Goods(

    @PrimaryKey(autoGenerate = true) val goodsId: Int?,

    /**
     * 商品型号正式名。
     */
    @ColumnInfo(name = "model") var model: String,

    /**
     * 商品型号本地化正式名。
     */
    @ColumnInfo(name = "modelLocale") var modelLocale: String,

    /**
     * 商品型号别名。
     */
    @ColumnInfo(name = "modelAlias") var modelAlias: String,

    /**
     * 商品唯一标识。
     */
    @ColumnInfo(name = "brandId") val brandId: Int?,

    /**
     * 商品类型。
     */
    @ColumnInfo(name = "goodsType") val goodsType: GoodsType,

    @ColumnInfo(name = "isDelete", defaultValue = "false") var isDelete: Boolean,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

    @ColumnInfo(name = "deleteTime") var deleteTime: Date?,

    )

/**
 * 商品类型的枚举。
 * @property type Int
 * @property alias String
 * @constructor
 */
enum class GoodsType(val type: Int, val alias: String) {

    SOURCE(0, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[0]),
    DAC(1, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[1]),
    HEADPHONE_AMP(2, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[2]),
    HEADPHONE(3, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[3]),
    SPEAKER(4, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[4]),
    PRE_AMP(5, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[5]),
    POWER_AMP(6, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[6]),
    AMP(7, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[7]),
    ALL_IN_ONE(8, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[8]),
    CABLE(9, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[9]),
    VIBRATION(10, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[10]),
    POWER(11, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[11]),
    DIGITAL(12, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[12]),
    LP(13, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[13]),
    CD(14, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[14]),
    OTHER(15, BaseApplication.Instance.resources.getStringArray(R.array.goodsType)[15]),
}

/*
<string-array name="goodsType">
        <item>音源</item>
        <item>解码</item>
        <item>耳放</item>
        <item>耳机</item>
        <item>音箱</item>
        <item>前级</item>
        <item>后级</item>
        <item>合并功放</item>
        <item>综合一体机</item>
        <item>线材周边</item>
        <item>避震周边</item>
        <item>供电周边</item>
        <item>数播周边</item>
        <item>黑胶周边</item>
        <item>CD周边</item>
        <item>其他周边</item>
    </string-array>
 */
