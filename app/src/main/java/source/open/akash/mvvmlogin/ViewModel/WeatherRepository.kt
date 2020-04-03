package source.open.akash.mvvmlogin.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import source.open.akash.mvvmlogin.Network.APIInterface
import source.open.akash.mvvmlogin.Network.RetrofitRequest
import com.google.gson.Gson
import source.open.akash.mvvmlogin.Model.current.WeatherReport
import source.open.akash.mvvmlogin.Model.nextdayforecast.WeatherNextDaysReport


/**
 * Created by Akash Kumar on 3/30/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class WeatherRepository : ViewModel() {


     val TAG: String?=WeatherRepository::class.java.simpleName
     private val apiRequest: APIInterface  = RetrofitRequest.getRetrofitInstance().create(APIInterface::class.java)


    fun getCurrentWeatherReport(access_key: String,query: String): LiveData<WeatherReport> {


        Log.d(TAG, "onResponse response:: $access_key  $query")
        val data = MutableLiveData<WeatherReport>()
        apiRequest.getCurrentWeatherReport(access_key, query)
            .enqueue(object : Callback<WeatherReport> {


                override fun onResponse(call: Call<WeatherReport>, response: Response<WeatherReport>) {
                   //   Log.d(TAG, "onResponse response:: ${response.isSuccessful}")
               /*     Log.d(TAG, "onResponse response:: ${Gson().toJson(response)}")
*/


                    if (response.body() != null) {
                        data.value=response.body()
                        //     Log.d(TAG, "onResponse response:: ${data.value?.main?.temp}")
                    }
                }

                override fun onFailure(call: Call<WeatherReport>, t: Throwable) {
//                    data.value=null
                }
            })
        return data
    }
    fun getForecastWeatherReport(access_key: String,query: String): LiveData<WeatherNextDaysReport> {
        Log.d(TAG, "onResponse response:: $access_key  $query")
        val data = MutableLiveData<WeatherNextDaysReport>()
        apiRequest.getForecastWeatherReport(access_key, query)
            .enqueue(object : Callback<WeatherNextDaysReport> {

                override fun onResponse(call: Call<WeatherNextDaysReport>, response: Response<WeatherNextDaysReport>) {
           Log.d(TAG, "onResponse response:: ${Gson().toJson(response)}")
                    if (response.body() != null) {
                        data.value=response.body()
                    }
                }

                override fun onFailure(call: Call<WeatherNextDaysReport>, t: Throwable) {
                   // data.value=null
                }
            })
        return data
    }
}