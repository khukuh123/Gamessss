package com.miko.core.data

import com.miko.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = flow<Resource<ResultType>> {
        emit(Resource.Loading(null))

        val dbSource = loadFromDB().first()

        if (shouldFetch(dbSource)) {
            emit(Resource.Loading(null))

            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)

                    emitAll(loadFromDB().map { Resource.Success(it) })
                }

                is ApiResponse.Error -> {
                    emit(Resource.Error(null, apiResponse.errorMessage))
                }

                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
            }
        } else {
            emitAll(loadFromDB().map { Resource.Success(it) })
        }
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result

}