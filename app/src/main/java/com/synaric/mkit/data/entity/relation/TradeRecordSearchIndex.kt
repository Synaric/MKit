package com.synaric.mkit.data.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions

@Entity
@Fts4(tokenizer = FtsOptions.TOKENIZER_UNICODE61)
data class TradeRecordSearchIndex(

    @ColumnInfo(name = "tradeRecordId") var tradeRecordId: Int?,

    @ColumnInfo(name = "indexText") var indexText: String,

    @ColumnInfo(name = "fullText") var fullText: String,
)