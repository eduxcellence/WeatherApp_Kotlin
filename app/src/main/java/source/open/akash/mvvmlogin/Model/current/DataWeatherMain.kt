package source.open.akash.mvvmlogin.Model.current

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Akash Kumar on 4/1/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class DataWeatherMain {

    @SerializedName("temp")
    @Expose
    var temp: Double? = null
    @SerializedName("feels_like")
    @Expose
    var feels_like: Double? = null

    @SerializedName("pressure")
    @Expose
    var pressure: Int? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Int? = null
    @SerializedName("temp_min")
    @Expose
    var tempMin: Double? = null
    @SerializedName("temp_max")
    @Expose
    var tempMax: Double? = null

}