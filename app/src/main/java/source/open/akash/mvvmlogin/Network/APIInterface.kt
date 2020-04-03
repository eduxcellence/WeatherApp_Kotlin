package source.open.akash.mvvmlogin.Network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import source.open.akash.mvvmlogin.Model.current.WeatherReport
import source.open.akash.mvvmlogin.Model.nextdayforecast.WeatherNextDaysReport


/**
 * Created by Akash Kumar on 3/29/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */

interface APIInterface {
/*

    @GET("current")
     fun getCurrentWeatherReport(@Query("access_key") accessKey: String, @Query("query") cityName: String): Call<WeatherReport>
*/


    @GET("weather")
     fun getCurrentWeatherReport( @Query("appid") access_key: String,@Query("q") cityName: String): Call<WeatherReport>

    @GET("forecast")
     fun getForecastWeatherReport( @Query("appid") access_key: String,@Query("q") cityName: String): Call<WeatherNextDaysReport>


}