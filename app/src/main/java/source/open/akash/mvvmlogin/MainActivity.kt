package source.open.akash.mvvmlogin

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import source.open.akash.mvvmlogin.Model.current.WeatherReport
import source.open.akash.mvvmlogin.Model.nextdayforecast.ListData
import source.open.akash.mvvmlogin.Model.nextdayforecast.WeatherNextDaysReport
import source.open.akash.mvvmlogin.ViewModel.WeatherRepository
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val TAG: String? = MainActivity::class.java.simpleName
    var access_key: String = BuildConfig.access_key /*"fc7ca27cdb74949f62f5b936e50db2c3"*/
    var query: String = "Lucknow,Uttar Pradesh,IN"
    var isUp = false

    //  Check Internet Continuosly
    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0
    var snackbar: Snackbar? = null
    val coordinatorLayout: CoordinatorLayout? = null
    var internetConnected = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create a current date string.
        val date_n = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        //set current date.
        tv_current_date.text = date_n
        //set current location.
        var currentCity = query.split(",")
        tv_current_location.text = currentCity[0]

if(checkConnectivity(applicationContext))
{
    //Fetch Current Weather and Next Day Forecasting repository data
    getWeatherReport()
}else
{
    val builder = AlertDialog.Builder(this@MainActivity)
    //set title for alert dialog
    builder.setTitle("Network Issues / Internet Connection Lost")
    //set message for alert dialog
    builder.setMessage("Turn on Network Data")
    builder.setIcon(R.drawable.ic_no_internet)

    //performing positive action
    builder.setPositiveButton("Retry") { dialogInterface, which ->
        if(checkConnectivity(applicationContext))
        {
            //Fetch Current Weather and Next Day Forecasting repository data
            getWeatherReport()
        }
    }

    //performing negative action
    builder.setNegativeButton("Exit") { dialogInterface, which ->
        finish()
    }
    // Create the AlertDialog and set Properties
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}


        //declaring swipe refresh listener
        swipeRefresh.setOnRefreshListener {

            if(checkConnectivity(applicationContext))
            {
                //Fetch Current Weather and Next Day Forecasting repository data
                getWeatherReport()
            }else
            {
                val builder = AlertDialog.Builder(this@MainActivity)
                //set title for alert dialog
                builder.setTitle("Network Issues / Internet Connection Lost")
                //set message for alert dialog
                builder.setMessage("Turn on Network Data")
                builder.setIcon(R.drawable.ic_no_internet)

                //performing positive action
                builder.setPositiveButton("Retry") { dialogInterface, which ->
                    if(checkConnectivity(applicationContext))
                    {
                        //Fetch Current Weather and Next Day Forecasting repository data
                        getWeatherReport()
                    }

                }

                //performing negative action
                builder.setNegativeButton("Exit") { dialogInterface, which ->
                    finish()
                }
                // Create the AlertDialog and set Properties
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }


    }


    private fun getWeatherReport() {

        val weatherRepository = ViewModelProvider(this).get(WeatherRepository::class.java)

        GetCurrentWeather(weatherRepository)

        NextDayWeather(weatherRepository)


    }

    fun GetCurrentWeather(weatherRepository: WeatherRepository) {
        swipeRefresh.isRefreshing = true
        weatherRepository.getCurrentWeatherReport(access_key, query).observe(this, object : Observer<WeatherReport> {

            override fun onChanged(weatherReport: WeatherReport?) {


                if (weatherReport == null) {
                    swipeRefresh.isRefreshing = false
                    val builder = AlertDialog.Builder(this@MainActivity)
                    //set title for alert dialog
                    builder.setTitle(R.string.dialogTitle)
                    //set message for alert dialog
                    builder.setMessage(R.string.dialogMessage)
                    builder.setIcon(R.drawable.ic_error)

                    //performing positive action
                    builder.setPositiveButton("Retry") { dialogInterface, which ->
                        GetCurrentWeather(weatherRepository)
                    }

                    //performing negative action
                    builder.setNegativeButton("Exit") { dialogInterface, which ->
                        finish()
                    }
                    // Create the AlertDialog and set Properties
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                } else {
                    val currentTempKelvin = weatherReport?.main?.temp
                    val currentTempCelsius = currentTempKelvin?.minus(273.15)

                    //set current temperature.
                    tv_current_temperature.text = currentTempCelsius?.toInt().toString()

                    //   tvStatus.setText(weatherReport.getWeather().get(0).getDescription())

                    tvHumidity.text = "" + weatherReport?.main?.humidity + " %"
                    tvSensible.text =
                        "" + (weatherReport?.main?.feels_like?.minus(273.15))?.toInt().toString() + Html.fromHtml(" \u2103")
                    tvPressure.text = "" + weatherReport?.main?.pressure + " hPa"
                    tvWind.text = "" + weatherReport?.wind?.speed + " km/h"
                    if (weatherReport != null) {
                        //set current weather description.
                        tv_current_description.text = weatherReport.weather?.get(0)?.description
                        //set current weather description image.
                        Picasso.get()
                            .load("http://openweathermap.org/img/wn/" + weatherReport.weather?.get(0)?.icon + "@2x.png")
                            .into(iv_current_description)
                        tv_today_temp_minmax.text =
                            weatherReport?.main?.tempMax?.minus(273.15)?.toInt().toString() + " / " + weatherReport?.main?.tempMin?.minus(
                                273.15
                            )?.toInt().toString()
                        val formatter = SimpleDateFormat("HH:mm:ss a ", Locale.US)
                        formatter.timeZone = TimeZone.getDefault()/* TimeZone.getTimeZone("UTC")*/
                        /* val text = formatter.format(Date((weatherReport.sys?.sunset)!!*1000))
                         println(text)*/

                        tv_sunrise.text = formatter.format(Date((weatherReport.sys?.sunrise)!! * 1000))
                        tv_sunset.text = formatter.format(Date((weatherReport.sys?.sunset)!! * 1000))
                        swipeRefresh.isRefreshing = false
                    }

                }


            }


        })
    }

    fun NextDayWeather(weatherRepository: WeatherRepository) {

        swipeRefresh.isRefreshing = true
        weatherRepository.getForecastWeatherReport(access_key, query)
            .observe(this, object : Observer<WeatherNextDaysReport> {
                override fun onChanged(weatherReport: WeatherNextDaysReport?) {
                    if (weatherReport == null) {
                        swipeRefresh.isRefreshing = false
                        val builder = AlertDialog.Builder(this@MainActivity)
                        //set title for alert dialog
                        builder.setTitle(R.string.dialogTitle)
                        //set message for alert dialog
                        builder.setMessage(R.string.dialogMessage)
                        builder.setIcon(R.drawable.ic_error)

                        //performing positive action
                        builder.setPositiveButton("Retry") { dialogInterface, which ->
                            NextDayWeather(weatherRepository)
                        }

                        //performing negative action
                        builder.setNegativeButton("Exit") { dialogInterface, which ->
                            finish()
                        }
                        // Create the AlertDialog and set Properties
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    } else {


                        val stringListDataHashMap = HashMap<String, ListData>()

                        if (weatherReport.list.isEmpty()) {

                            swipeRefresh.isRefreshing = false

                        } else {

                            //
                            for (y in 0 until weatherReport.list.size) {

                                stringListDataHashMap[weatherReport.list.get(y).getDtTxt()!!] =
                                    weatherReport.list.get(y)

                                //  System.out.println("Value = " + weatherReport.getList().get(y).getDtTxt());
                            }


                            val listData = ArrayList<ListData>()

                            for (value in stringListDataHashMap.values) {
                                System.out.println("Ascending before Value = " + value.getDtTxt())
                                listData.add(value)

                            }

                            // Sort date in assending order
                            Collections.sort(listData, object : Comparator<ListData> {
                                @SuppressLint("SimpleDateFormat")
                                var f: DateFormat = SimpleDateFormat("yyyy-MM-dd")

                                override fun compare(lhs: ListData, rhs: ListData): Int {
                                    try {
                                        return f.parse(lhs.getDtTxt()).compareTo(f.parse(rhs.getDtTxt()))
                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                        throw IllegalArgumentException(e)
                                    }

                                }
                            })

                            for (i in listData.indices) {
                                System.out.println("Ascending after Value = " + listData[i].getDtTxt())


                            }

                            rvNextDays.adapter = WeatherNextAdapter(applicationContext, listData)
                            rvNextDays.layoutManager = LinearLayoutManager(this@MainActivity)
                            swipeRefresh.isRefreshing = false
                        }
                    }
                }

            })
    }


    class WeatherNextAdapter(var context: Context, var listDataList: List<ListData>) :
        RecyclerView.Adapter<WeatherNextAdapter.WeatherView>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeatherView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_next_forecast, parent, false)
        )


        override fun onBindViewHolder(holder: WeatherView, position: Int) {
            val listData = listDataList[position]
            if (position != 0) {

                Picasso.get()
                    .load("http://openweathermap.org/img/wn/" + listData.weather.get(0).icon + "@2x.png")
                    .into(holder.ivWeatherStatus)

                if (position == 1) {
                    holder.tvDate.text = context.getString(R.string.tomorrow)

                } else {
                    holder.tvDate.setText(DateConvert(listData.getDtTxt()!!))

                }

                holder.tvDescription.text = listData.weather[0].description
                holder.tvDayName.setText(DayConvert(listData.getDtTxt()!!))
                holder.tvTemp.text =
                    String.format(
                        "%d%s",
                        (listData.main?.temp?.minus(273.15))?.toInt(),
                        Html.fromHtml(" \u2103")
                    )


            } else {
                holder.itemView.visibility = View.GONE
            }
        }


        override fun getItemCount(): Int {
            return listDataList.size
        }

        class WeatherView(itemView: View) : RecyclerView.ViewHolder(itemView) {


            val tvDate: TextView
            val tvDayName: TextView
            val tvTemp: TextView
            val tvDescription: TextView
            val ivWeatherStatus: ImageView


            init {
                println("hittingh")
                tvDate = itemView.findViewById(R.id.tvDate)
                tvDayName = itemView.findViewById(R.id.tvDayName)
                ivWeatherStatus = itemView.findViewById(R.id.ivWeatherStatus)
                tvTemp = itemView.findViewById(R.id.tvTemp)
                tvDescription = itemView.findViewById(R.id.tvDescription)


            }
        }

        //convert date format in dd-MM
        @SuppressLint("SimpleDateFormat")
        fun DateConvert(date: String): String {
            var dateString = date
            try {

                val sdf2 = SimpleDateFormat("dd-MM")
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                dateString = sdf2.format(sdf.parse(dateString))

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return dateString
        }

        //convert date format in day name
        @SuppressLint("SimpleDateFormat")
        fun DayConvert(dateString: String): String? {

            val sdf = SimpleDateFormat("yyyy-MM-dd")


            val sdf_ = SimpleDateFormat("EEEE")

            var dayName: String? = null
            try {
                dayName = sdf_.format(sdf.parse(dateString))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return dayName
        }
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            view.height.toFloat(), // fromYDelta
            0f
        )                // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            0f, // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        Handler().postDelayed({
            view.visibility = View.GONE
        }, 501)

    }

    fun onSlideViewButtonClick(view: View) {
        if (isUp) {

            slideDown(lay_nextdays)
            iv_bottom_slide_up.setImageResource(R.drawable.ic_slideup)
        } else {

            slideUp(lay_nextdays)
            iv_bottom_slide_up.setImageResource(R.drawable.ic_slidedown)
        }
        isUp = !isUp
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private fun registerInternetCheckReceiver() {
        val internetFilter = IntentFilter()
        internetFilter.addAction("android.net.wifi.STATE_CHANGE")
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(broadcastReceiver, internetFilter)
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = getConnectivityStatusString(context)
            setSnackbarMessage(status, false)
        }
    }

    fun getConnectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled"
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled"
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet"
        }
        return status
    }

    private fun setSnackbarMessage(status: String?, showBar: Boolean) {
        var internetStatus = ""

        try {
            if (status!!.equals("Wifi enabled", ignoreCase = true) || status.equals(
                    "Mobile data enabled",
                    ignoreCase = true
                )
            ) {
                internetStatus = "Internet Connected"
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                snackbar =
                    Snackbar.make(findViewById(android.R.id.content), internetStatus, Snackbar.LENGTH_LONG)
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                internetStatus = "Lost Internet Connection"
                snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    internetStatus,
                    Snackbar.LENGTH_INDEFINITE
                )

            }

            val sbView = snackbar!!.getView()
            val textView = sbView.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            if (internetStatus.equals("Lost Internet Connection", ignoreCase = true)) {
                if (internetConnected) {
                    snackbar!!.show()
                    internetConnected = false
                }
            } else {
                if (!internetConnected) {
                    internetConnected = true
                    snackbar!!.show()
                }
            }
        } catch (e: Exception) {
            print(e)
        }

    }

    override fun onPause() {

        super.onPause()
        unregisterReceiver(broadcastReceiver)

    }

    override fun onResume() {

        super.onResume()
        registerInternetCheckReceiver()
    }
//Check internet connectivity
    fun checkConnectivity(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (activeNetwork?.isConnected != null) {
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}
