package com.crypto.tranxact.data

import arrow.core.Either
import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange
import com.crypto.tranxact.domain.Repository
import java.lang.Exception

class RepositoryImpl constructor(private val retrofitService: RetrofitService) : Repository {

    override suspend fun assets(): Either<String, List<Asset>> {
        return try {
            val response = retrofitService.assets()

            if (response.isSuccessful) {
                Either.Right(response.body() ?: listOf())
            } else {
                Either.Left("An error occurred fetching assets")
            }
        } catch (_: Exception) {
            Either.Left("An error occurred fetching assets")
        }
    }

    override suspend fun exchanges(): Either<String, List<Exchange>> {
        return try {
            val response = retrofitService.exchanges()

            if (response.isSuccessful) {
                Either.Right(response.body() ?: listOf())
            } else {
                Either.Left("An error occurred fetching exchanges")
            }
        } catch (e: Exception) {
            Either.Left("An error occurred fetching exchanges")
        }

    }

    companion object {

        val instance = RepositoryImpl(RetrofitService.instance())
    }
}