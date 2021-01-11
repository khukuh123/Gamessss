package com.miko.gamesss.model.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.remote.response.GameListItem
import com.miko.gamesss.model.source.remote.response.GameListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource{

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null
         fun getInstance(): RemoteDataSource = instance?: synchronized(this){
             instance ?: RemoteDataSource()
         }
    }

    fun getListGame(): LiveData<ApiResponse<GameListResponse>> {
        val client = ApiConfig.getApiService().getGameList()
        val result = MutableLiveData<ApiResponse<GameListResponse>>()
        client.enqueue(object : Callback<GameListResponse>{
            override fun onResponse(
                call: Call<GameListResponse>,
                response: Response<GameListResponse>
            ) {
                val gameListResponse = response.body()
                if(response.isSuccessful && gameListResponse != null) {
                    if(gameListResponse.results.isNotEmpty()){
                        result.postValue(ApiResponse.success(gameListResponse))
                    }else{
                        result.postValue(ApiResponse.empty("Game not Found", gameListResponse))
                    }
                }
            }

            override fun onFailure(call: Call<GameListResponse>, t: Throwable) {
                result.postValue(ApiResponse.error(t.message.toString(), GameListResponse(listOf(
                    GameListItem("", 0.0, 0, "")
                ))))
            }

        })

        return result
    }

    fun searchGame(query: String): LiveData<ApiResponse<GameListResponse>>{
        val result = MutableLiveData<ApiResponse<GameListResponse>>()

        val client = ApiConfig.getApiService().getGameList(query)
        client.enqueue(object : Callback<GameListResponse>{
            override fun onResponse(
                call: Call<GameListResponse>,
                response: Response<GameListResponse>
            ) {
                val gameListResponse = response.body()
                if(response.isSuccessful && gameListResponse != null){
                    if(gameListResponse.results.isNotEmpty()){
                        result.postValue(ApiResponse.success(gameListResponse))
                    }else{
                        result.postValue(ApiResponse.empty("Game not Found", gameListResponse))
                    }
                }
            }

            override fun onFailure(call: Call<GameListResponse>, t: Throwable) {
                result.postValue(ApiResponse.error(t.message.toString(), GameListResponse(listOf(
                    GameListItem("", 0.0, 0, "")
                ))))
            }
        })

        return result
    }

}