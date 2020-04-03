package source.open.akash.mvvmlogin.Model.nextdayforecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Akash Kumar on 4/1/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class WeatherNextDaysReport {

    @SerializedName("cod")
    @Expose
    var cod: String? = null
    @SerializedName("message")
    @Expose
    var message: Int? = null
    @SerializedName("cnt")
    @Expose
    var cnt: Int? = null

    @SerializedName("list")
    @Expose
    var list: List<ListData> = ArrayList()
    @SerializedName("city")
    @Expose
    var city: City? = null

}

/* */