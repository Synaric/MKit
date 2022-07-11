package com.synaric.mkit.base.const

import androidx.paging.PagingConfig

class AppConfig {

    companion object {

        /**
         * 分页中每页的条数。
         */
        const val PagingSize = 10

        /**
         * 最大分页条数。
         */
        const val PagingMax = PagingConfig.MAX_SIZE_UNBOUNDED

        /**
         * 内存储子目录，用于存放数据库导出的json数据。
         */
        const val InTypeJson = "json"

        const val ExportJsonBrandPrefix = "brand"
        const val ExportJsonGoodsPrefix = "goods"
        const val ExportJsonTradeRecordPrefix = "trade_record"
        const val ExportJsonTradeRecordIndexPrefix = "trade_record_index"

        /**
         * 外部导出数据库文件名。
         */
        const val ExportZIPFileName = "sy.zip"

        const val MainActivityActionImport = 0
        const val MainActivityActionExport = 1
    }
}