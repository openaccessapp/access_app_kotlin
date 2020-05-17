package dahoum.wales.access_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayout: LinearLayout = findViewById(R.id.buttonsLayout);
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            child.setOnClickListener({
                print("tapped")
            })
        }
    }
}
