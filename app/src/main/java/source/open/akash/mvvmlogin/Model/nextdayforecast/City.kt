package source.open.akash.mvvmlogin.Model.nextdayforecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by Akash Kumar on 4/1/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class City {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("population")
    @Expose
    var population: Int? = null
    @SerializedName("timezone")
    @Expose
    var timezone: Int? = null
    @SerializedName("sunrise")
    @Expose
    var sunrise: Int? = null
    @SerializedName("sunset")
    @Expose
    var sunset: Int? = null

}



