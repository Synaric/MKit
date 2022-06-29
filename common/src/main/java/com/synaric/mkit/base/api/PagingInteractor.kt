package com.synaric.mkit.base.api

import androidx.paging.PagingData

abstract class PagingInteractor<P : Parameters<T>, T : Any> : SubjectInteractor<P, PagingData<T>>() {

}