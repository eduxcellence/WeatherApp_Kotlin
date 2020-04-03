package source.open.akash.mvvmlogin.Model.current

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Akash Kumar on 4/1/2020.
 * https://github.com/eduxcellence
 * akkr2017@gmail.com
 */
class Coord {

    @SerializedName("lon")
    @Expose
    var lon: Double? = null
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

}