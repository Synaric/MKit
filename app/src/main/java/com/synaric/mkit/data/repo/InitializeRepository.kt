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
import com.synaric.mkit.data.entity.Brand
import com.synaric.mkit.data.entity.Goods
import com.synaric.mkit.data.entity.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex
import java.io.File


/**
 * APP数据初始化repo。
 * @property appDatabase AppDatabase
 */
class InitializeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.Instance)

    /**
     * 数据库外部导入。
     * @param from Uri 用户选定的数据库zip文件。
     * @return Unit
     */
    suspend fun importDBFromZIP(from: Uri) = execute {
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
     * 数据库初始化导入。
     * @return Unit
     */
    suspend fun importDBFromAsset() = execute {
        val isInitDatabase = SPUtil.Instance.getSpValue(SPKey.InitDatabase, false)
        if (!isInitDatabase) {
            importTableFromAsset(AppConfig.ExportJsonBrandPrefix, object : TypeToken<List<Brand>>() {}) {
                appDatabase.brandDao().insertAll(it)
                AppLog.d(this, "import db ${AppConfig.ExportJsonBrandPrefix}: ${it.size}")
            }
            importTableFromAsset(AppConfig.ExportJsonGoodsPrefix, object : TypeToken<List<Goods>>() {}) {
                appDatabase.goodsDao().insertAll(it)
                AppLog.d(this, "import db ${AppConfig.ExportJsonGoodsPrefix}: ${it.size}")
            }
            importTableFromAsset(
                AppConfig.ExportJsonTradeRecordPrefix,
                object : TypeToken<List<TradeRecord>>() {}) {
                appDatabase.tradeRecordDao().insertAll(it)
                AppLog.d(this, "import db ${AppConfig.ExportJsonTradeRecordPrefix}: ${it.size}")
            }
            importTableFromAsset(
                AppConfig.ExportJsonTradeRecordIndexPrefix,
                object : TypeToken<List<TradeRecordSearchIndex>>() {}) {
                appDatabase.tradeRecordDao().insertAllSearchIndex(it)
                AppLog.d(this, "import db ${AppConfig.ExportJsonTradeRecordIndexPrefix}: ${it.size}")
            }

            SPUtil.Instance.putSpValue(SPKey.InitDatabase, true)
        } else {
            AppLog.d(this, "skip insert")
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

    private fun <T> importTableFromAsset(
        table: String,
        typeToken: TypeToken<T>,
        doQuery: (list: T) -> Unit
    ) {
        val gson = Gson()
        context.assets.list("")?.forEach { filename ->
            if (filename.endsWith("json") && filename.startsWith(table)) {
                val content = FileUtil.readAssetFile(context, filename)
                val tempList =
                    gson.fromJson<T>(content, typeToken.type)
                doQuery(tempList)
            }
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
}