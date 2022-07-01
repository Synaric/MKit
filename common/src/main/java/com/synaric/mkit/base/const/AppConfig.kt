package com.synaric.mkit.base.const

import androidx.paging.PagingConfig

class AppConfig {

    companion object {

        /**
         * 分页中每页的条数
         */
        const val PagingSize = 10

        /**
         * 最大分页条数
         */
        const val PagingMax = PagingConfig.MAX_SIZE_UNBOUNDED

        /**
         * SD外存储根目录
         */
        const val SDRoot = "com.synaric.mkit"

        /**
         * SD外存储子目录，用于存放数据库导出的json数据。
         */
        const val SDTypeDB = "json"

        /**
         * 内存储子目录，用于存放数据库导出的json数据。
         */
        const val InTypeJson = "json"

        const val ExportJsonBrandPrefix = "brand"
        const val ExportJsonGoodsPrefix = "goods"
        const val ExportJsonTradeRecordPrefix = "trade_record"
        const val ExportJsonTradeRecordIndexPrefix = "trade_record_index"
    }
}