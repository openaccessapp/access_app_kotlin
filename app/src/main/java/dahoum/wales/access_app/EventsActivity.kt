package dahoum.wales.access_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_events.*

class EventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        card_history_museum.setOnClickListener {
            startActivityForResult(Intent(this@EventsActivity, PlanVisitActivity::class.java), 1)
        }
    }
}
