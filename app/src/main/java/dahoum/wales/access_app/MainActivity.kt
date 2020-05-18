package dahoum.wales.access_app

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG: String? = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttons: List<Button> = listOf(button1, button2, button3)

        for (button in buttons) {
            button.setOnClickListener {
                val color: Int = (button.background as ColorDrawable).color
                if (color == ContextCompat.getColor(this, R.color.red)) {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                } else {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                }
            }
        }
    }
}
