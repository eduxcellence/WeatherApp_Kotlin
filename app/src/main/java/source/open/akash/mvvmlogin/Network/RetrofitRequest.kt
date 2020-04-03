package source.open.akash.mvvmlogin.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Akash Kumar on 3/28/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class RetrofitRequest {



    companion object {
        var BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun getRetrofitInstance(): Retrofit {

            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit
        }
    }
}