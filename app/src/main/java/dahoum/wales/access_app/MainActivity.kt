package dahoum.wales.access_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
//
//    private val TAG: String? = MainActivity::class.java.simpleName
//    lateinit var calendarFrom: Calendar
//    lateinit var calendarTo: Calendar
//    lateinit var service: RetrofitService2
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        calendarFrom = Calendar.getInstance()
//        calendarTo = Calendar.getInstance()
//
//        fromDateTv.setOnClickListener {
//            showDateTimePicker(calendarFrom)
//        }
//        toDateTv.setOnClickListener {
//            showDateTimePicker(calendarTo)
//        }
//
//        service = RetrofitClientInstance2().getRetrofitInstance()!!.create(RetrofitService2::class.java)
//        val getDemo: Call<JsonObject> = service.getDemo()
//        getDemo.enqueue(object: Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//                val viewsDateFormat = SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault())
//                try {
//                    val fromDate =
//                        serverDateFormat.parse(response.body()!!.asJsonObject["fromDate"].asString)
//                    val toDate =
//                        serverDateFormat.parse(response.body()!!.asJsonObject["toDate"].asString)
//                    calendarFrom.time = fromDate
//                    calendarTo.time = toDate
//                    toDateTv.text = viewsDateFormat.format(toDate)
//                    fromDateTv.text = viewsDateFormat.format(fromDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    fun showDateTimePicker(calendar: Calendar) {
//        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view1, hourOfDay, minute ->
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                calendar.set(Calendar.MINUTE, minute)
//                updateOnServer()
//            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
//            timePickerDialog.show()
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//        datePickerDialog.show()
//    }
//
//    fun updateOnServer() {
//        val body = hashMapOf<String, String>()
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//        body["fromDate"] = simpleDateFormat.format(calendarFrom.time)
//        body["toDate"] = simpleDateFormat.format(calendarTo.time)
//        val updateTime: Call<JsonObject> = service.updatePlace("demo", body)
//        updateTime.enqueue(object : Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//                val viewsDateFormat = SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault())
//                try {
//                    val fromDate =
//                        serverDateFormat.parse(response.body()!!.asJsonObject["fromDate"].asString)
//                    val toDate =
//                        serverDateFormat.parse(response.body()!!.asJsonObject["toDate"].asString)
//                    calendarFrom.time = fromDate
//                    calendarTo.time = toDate
//                    toDateTv.text = viewsDateFormat.format(toDate)
//                    fromDateTv.text = viewsDateFormat.format(fromDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}
