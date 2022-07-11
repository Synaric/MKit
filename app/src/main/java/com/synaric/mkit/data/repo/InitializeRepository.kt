package com.synaric.mkit.data.repo

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.art.util.AppLog
import com.synaric.art.util.FileUtil
import com.synaric.art.util.FileUtil.Companion.saveFileToInternalFile
import com.synaric.art.util.SPUtil
import com.synaric.mkit.base.const.AppConfig
import com.synaric.mkit.base.const.SPKey
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.*
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex
import com.synaric.mkit.util.StringUtil
import java.io.File
import java.util.*


/**
 * APP数据初始化repo。
 * @property appDatabase AppDatabase
 */
class InitializeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.Instance)

    /**
     * 数据库初始化插入。
     * @return Unit
     */
    suspend fun initInsert() = execute {
        val isInitDatabase = SPUtil.INSTANCE.getSpValue(SPKey.InitDatabase, false)

        if (!isInitDatabase) {
            val initDate = Date()
            initSearchIndex(
                initInsertBrand(initDate),
                initInsertGoods(initDate),
                initInsertTradeRecord(initDate)
            )

            SPUtil.INSTANCE.putSpValue(SPKey.InitDatabase, true)
        } else {
            AppLog.d(this, "skip insert")
        }
    }

    /**
     * 数据库初始化导入。
     * @param from Uri 用户选定的数据库zip文件。
     * @return Unit
     */
    suspend fun importDB(from: Uri) = execute {
        val to = Uri.fromFile(
            File(
                FileUtil.getInternalFilePath(
                    context,
                    AppConfig.InTypeJson,
                    AppConfig.ExportZIPFileName
                )
            )
        )
        // 删除旧文件
        FileUtil.clearInternalFile(context, AppConfig.InTypeJson)
        // 创建空文件用于拷贝
        FileUtil.createFileToInternalFile(
            context,
            AppConfig.InTypeJson,
            AppConfig.ExportZIPFileName
        )
        // 将选定的zip复制到内部存储
        FileUtil.copyFile(context, from, to)
        // 解压
        FileUtil.unzipFileToInternalFile(context, AppConfig.InTypeJson, AppConfig.ExportZIPFileName)
        // 解析并插入数据库
        importTable(AppConfig.ExportJsonBrandPrefix, object : TypeToken<List<Brand>>() {}) {
            appDatabase.brandDao().insertAll(it)
            AppLog.d(this, "import db ${AppConfig.ExportJsonBrandPrefix}: ${it.size}")
        }
        importTable(AppConfig.ExportJsonGoodsPrefix, object : TypeToken<List<Goods>>() {}) {
            appDatabase.goodsDao().insertAll(it)
            AppLog.d(this, "import db ${AppConfig.ExportJsonGoodsPrefix}: ${it.size}")
        }
        importTable(
            AppConfig.ExportJsonTradeRecordPrefix,
            object : TypeToken<List<TradeRecord>>() {}) {
            appDatabase.tradeRecordDao().insertAll(it)
            AppLog.d(this, "import db ${AppConfig.ExportJsonTradeRecordPrefix}: ${it.size}")
        }
        importTable(
            AppConfig.ExportJsonTradeRecordIndexPrefix,
            object : TypeToken<List<TradeRecordSearchIndex>>() {}) {
            appDatabase.tradeRecordDao().insertAllSearchIndex(it)
            AppLog.d(this, "import db ${AppConfig.ExportJsonTradeRecordIndexPrefix}: ${it.size}")
        }
    }

    /**
     * 将数据库导出到外置SD卡，存储为json格式。
     * @return Unit
     */
    suspend fun exportDB(onCreateFile: (sourceFile: Uri, filename: String) -> Unit) = execute {
        exportTable(AppConfig.ExportJsonBrandPrefix) { start ->
            appDatabase.brandDao().getBrandList(start, 1000)
        }
        exportTable(AppConfig.ExportJsonGoodsPrefix) { start ->
            appDatabase.goodsDao().getGoodsList(start, 1000)
        }
        exportTable(AppConfig.ExportJsonTradeRecordPrefix) { start ->
            appDatabase.tradeRecordDao().getTradeRecordList(start, 1000)
        }
        exportTable(AppConfig.ExportJsonTradeRecordIndexPrefix) { start ->
            appDatabase.tradeRecordDao().getSearchIndexList(start, 1000)
        }

        val parent = File("${context.filesDir}/${AppConfig.InTypeJson}")
        parent.listFiles()?.forEach {
            AppLog.d(this, "export db: $it")
        }

        parent.listFiles()
            ?.filter {
                it.name.endsWith(".json")
            }?.let {
                val zipFile = FileUtil.zipFileToInternalFile(
                    context,
                    it.toList(),
                    AppConfig.InTypeJson,
                    AppConfig.ExportZIPFileName
                )
                    ?: return@let
                FileUtil.saveFileToSD(zipFile, AppConfig.ExportZIPFileName, onCreateFile)
            }
    }

    private fun <T> importTable(
        table: String,
        typeToken: TypeToken<T>,
        doQuery: (list: T) -> Unit
    ) {
        val gson = Gson()
        for (i in 0..10) {
            val file = File("${context.filesDir}/${AppConfig.InTypeJson}/${table}_$i.json")
            if (!file.exists() || !file.isFile) {
                break
            }
            val content = FileUtil.readFile(context, file)
            val tempList =
                gson.fromJson<T>(content, typeToken.type)
            doQuery(tempList)
        }
    }

    private fun <T> exportTable(
        table: String,
        doQuery: (s: Int) -> List<T>
    ) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        var exportSuccess = false
        var start = 0

        while (!exportSuccess) {
            val list = doQuery(start)
            if (list.isEmpty()) {
                exportSuccess = true
            } else {
                val json = gson.toJson(list)
                saveFileToInternalFile(
                    context,
                    AppConfig.InTypeJson,
                    "${table}_$start.json",
                    json,
                )
            }
            start++
        }
    }

    private fun initSearchIndex(
        brandList: List<Brand>,
        goodsList: List<Goods>,
        tradeRecordList: List<TradeRecord>
    ) {
        val searchIndexList = mutableListOf<TradeRecordSearchIndex>()
        tradeRecordList.forEach { record ->
            if (record.tradeRecordId == null) {
                return
            }
            val goods = goodsList.findLast { it.goodsId?.equals(record.goodsId) ?: false }
            var brand: Brand? = null
            if (goods != null) {
                brand = brandList.findLast { it.brandId?.equals(goods.brandId) ?: false }
            }
            val searchIndex =
                StringUtil.formatTradeRecordSearchIndex(record, goods, brand)
            val fullText =
                StringUtil.formatTradeRecordFullText(record, goods, brand)
            val tradeRecordSearchIndex =
                TradeRecordSearchIndex(record.tradeRecordId, searchIndex, fullText)
            searchIndexList.add(tradeRecordSearchIndex)
        }
        appDatabase.tradeRecordDao().insertAllSearchIndex(searchIndexList)
    }

    private fun initInsertBrand(initDate: Date): List<Brand> {
        val brandList = listOf(
            Brand(
                0,
                "Nordost",
                "音乐丝带",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                1,
                "Esoteric",
                "第一极品",
                "二嫂",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                2,
                "Van den Hul",
                "范登豪",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                3,
                "Siltech",
                "银彩",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                4,
                "Live Cable",
                "现场拉阔",
                "LC",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                5,
                "Crystal Connect",
                "晶彩",
                "Crystal Cable",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                6,
                "Kubala Sosna",
                "金威",
                "KS",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                7,
                "Argento",
                "雅图",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                8,
                "Accuphase",
                "金嗓子",
                "喉宝|嗓子",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                9,
                "Creaktiv",
                "创新",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                10,
                "WK Audio",
                "",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                11,
                "STAGE III CONCEPTS",
                "银圣",
                "S3C",
                false,
                initDate,
                initDate,
                null,
            )
        )
        appDatabase.brandDao().insertAll(brandList)
        return brandList
    }

    private fun initInsertGoods(initDate: Date): List<Goods> {


        val goodsList = listOf(
            Goods(
                0,
                "Odin2",
                "奥丁2",
                "",
                0,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                1,
                "Grandioso K1X",
                "",
                "",
                1,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                2,
                "Mainsstream",
                "大主流",
                "VDH",
                2,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                3,
                "Triple Crown",
                "三皇冠",
                "",
                3,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                4,
                "Signature XLR",
                "签名XLR",
                "",
                4,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                5,
                "Valhalla2 RCA",
                "瓦哈拉2 RCA",
                "",
                0,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                6,
                "Da vinci",
                "达芬奇",
                "Davinci",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                7,
                "Monet",
                "莫奈",
                "",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                8,
                "Realization",
                "钻星",
                "",
                6,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                9,
                "Ultimate Dream",
                "终极梦幻",
                "",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                10,
                "Flow",
                "",
                "",
                7,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                11,
                "C-3900",
                "",
                "",
                8,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                12,
                "Grandioso G1X",
                "",
                "",
                1,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                13,
                "Big Reference",
                "大参考",
                "",
                9,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                15,
                "DP-750",
                "",
                "",
                8,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                16,
                "The Ray",
                "",
                "",
                10,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                17,
                "Kraken",
                "",
                "",
                11,
                false,
                initDate,
                initDate,
                null,
            ),
        )
        appDatabase.goodsDao().insertAll(goodsList)
        return goodsList
    }

    private fun initInsertTradeRecord(initDate: Date): List<TradeRecord> {
        val tradeRecordList = listOf(
            TradeRecord(
                0,
                0,
                58000,
                142000,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                1,
                0,
                45000,
                142000,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                2,
                1,
                158000,
                240000,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                3,
                2,
                2370,
                3340,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "名线名声",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-6-19"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                4,
                3,
                41000,
                45000,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2021-9-6"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                5,
                4,
                21800,
                33000,
                Condition.NEW,
                Change.NEW,
                SalesChannel.PARALLEL,
                "Hifiboy",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.INTERCONNECT
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                6,
                5,
                40000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "名线名声",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.INTERCONNECT
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                7,
                0,
                69000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                "",
                GoodsExtendInfo(
                    2.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                8,
                6,
                58000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "熊猫视听",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-3-24"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                9,
                7,
                18000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-12"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                10,
                8,
                18500,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                "20A",
                GoodsExtendInfo(
                    2f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-11"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                11,
                10,
                12000,
                18500,
                Condition.CONDITION_95,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "V2Hifi",
                "",
                GoodsExtendInfo(
                    2f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-6-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                12,
                9,
                35000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "",
                "换过头尾",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-3-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                13,
                11,
                125900,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.PARALLEL,
                "乐海音响",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-5-27"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                14,
                12,
                110000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "淘乐音响",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-6-24"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                15,
                13,
                35000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                "4层",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2020-6-24"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                16,
                15,
                80000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.PARALLEL,
                "淘乐音响",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-6-25"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                17,
                16,
                29000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "古典音乐相对论",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-4-29"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                18,
                17,
                27500,
                null,
                Condition.CONDITION_85,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-7-8"),
                false,
                initDate,
                initDate,
                null,
            ),
        )
        appDatabase.tradeRecordDao().insertAll(tradeRecordList)
        return tradeRecordList
    }
}