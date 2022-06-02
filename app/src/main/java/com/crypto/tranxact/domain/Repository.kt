package com.crypto.tranxact.domain

import arrow.core.Either
import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange

interface Repository {

    suspend fun assets() :  Either<String, List<Asset>>

    suspend fun exchanges() :  Either<String, List<Exchange>>

}