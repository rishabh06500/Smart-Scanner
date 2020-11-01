package tv.rishabh.homescreenactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar? = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
    }
}
