package com.crypto.tranxact.data

import arrow.core.Either
import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange
import com.crypto.tranxact.domain.Repository
import kotlinx.coroutines.delay

class RepositoryImpl constructor(private val retrofitService: RetrofitService) : Repository{

    override suspend fun assets(): Either<String, List<Asset>> {
        val response = retrofitService.assets()

        return if(response.isSuccessful){
            Either.Right(response.body() ?: listOf() )
        }else{
            Either.Left("An error occurred fetching assets" )
        }
    }

    override suspend fun exchanges(): Either<String, List<Exchange>> {
        val response = retrofitService.exchanges()

        return if(response.isSuccessful){
            Either.Right(response.body() ?: listOf() )
        }else{
            Either.Left("An error occurred fetching exchanges" )
        }
    }

    companion object{

        val instance = RepositoryImpl(RetrofitService.instance())
    }
}