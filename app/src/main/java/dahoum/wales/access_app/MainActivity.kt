package dahoum.wales.access_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG: String? = MainActivity::class.java.simpleName
    lateinit var calendarFrom: Calendar
    lateinit var calendarTo: Calendar
    lateinit var service: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarFrom = Calendar.getInstance()
        calendarTo = Calendar.getInstance()

        fromDateTv.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendarFrom.set(Calendar.YEAR, year)
                    calendarFrom.set(Calendar.MONTH, month)
                    calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    showTimePicker()
                },
                calendarFrom.get(Calendar.YEAR),
                calendarFrom.get(Calendar.MONTH),
                calendarFrom.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        toDateTv.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendarFrom.set(Calendar.YEAR, year)
                    calendarFrom.set(Calendar.MONTH, month)
                    calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    showTimePicker()
                },
                calendarFrom.get(Calendar.YEAR),
                calendarFrom.get(Calendar.MONTH),
                calendarFrom.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService::class.java)
    }

    fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarFrom.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarFrom.set(Calendar.MINUTE, minute)
            updateOnServer()
        }, calendarFrom.get(Calendar.HOUR_OF_DAY), calendarFrom.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    fun updateOnServer() {
        val body = hashMapOf<String, String>()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        body["fromDate"] = simpleDateFormat.format(calendarFrom.time)
        body["toDate"] = simpleDateFormat.format(calendarTo.time)
        val updateTime: Call<JsonObject> = service.updatePlace("demo", body)
        updateTime.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
            }
        })
    }
}
