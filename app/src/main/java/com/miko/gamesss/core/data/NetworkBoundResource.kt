package com.miko.gamesss.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.miko.gamesss.core.data.source.remote.network.ApiResponse
import com.miko.gamesss.core.vo.StatusResponse
import com.miko.gamesss.core.utils.AppExecutors

abstract class NetworkBoundResource<ResultType, RequestType>(private val executor: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        @Suppress("LeakingThis")
        val dbSource = loadFromDB()

        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.value = Resource.success(newData)
                }
            }
        }
    }

    protected abstract fun loadFromDB(): LiveData<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    protected abstract fun saveCallResult(data: RequestType)

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()

        result.addSource(dbSource) { newData ->
            result.value = Resource.loading(newData)
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response.status) {
                StatusResponse.SUCCESS -> executor.diskIO().execute {
                    saveCallResult(response.body)
                    executor.mainThread().execute {
                        result.addSource(loadFromDB()) { newData ->
                            result.value = Resource.success(newData)
                        }
                    }
                }
                StatusResponse.EMPTY -> executor.mainThread().execute {
                    result.addSource(loadFromDB()) { newData ->
                        result.value = Resource.error(response.message, newData)
                    }
                }
                StatusResponse.ERROR -> {
                    result.addSource(dbSource) { newData ->
                        result.value = Resource.error(response.message, newData)
                    }
                }
            }
        }

    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

}