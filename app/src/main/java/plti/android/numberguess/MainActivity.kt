package plti.android.numberguess

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import plti.android.numberguess.common.Constants
import plti.android.numberguess.common.GameException
import plti.android.numberguess.model.GameUser
import plti.android.numberguess.random.RandomNumberGenerator
import plti.android.numberguess.random.impl.StdRandom
import kotlin.collections.ArrayList

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import plti.android.numberguess.statistics.Statistics


class MainActivity : AppCompatActivity() {

    var started = false
    var number = 0
    var tries = 0


    val rnd: RandomNumberGenerator = StdRandom()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fetchSavedInstanceData(savedInstanceState)
        doGuess.setEnabled(started)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        putInstanceData(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.statistics -> {
            openStatistics()
            true
        }
        else -> {super.onOptionsItemSelected(item) }
    }

    //버튼 함수 ------------------------------------------------------------------------------------//

    fun start(v: View) {
        log("game started")
        num.setText("")
        started=true
        doGuess.setEnabled(true)
        status.text = getString(R.string.guess_hint, Constants.LOWER_BOUND, Constants.UPPER_BOUND)
        val span = Constants.UPPER_BOUND + Constants.LOWER_BOUND + 1
        number = rnd.rnd(Constants.LOWER_BOUND,Constants.UPPER_BOUND)
        tries = 0
    }

    fun guess(v:View) {
        if(num.text.toString() == "") return

        try {
            val g = num.text.toString().toInt()

            if(g < Constants.LOWER_BOUND) throw GameException("Must guess a number >= ${Constants.LOWER_BOUND}")
            if(g > Constants.UPPER_BOUND) throw GameException("Must guess a number <= ${Constants.UPPER_BOUND}")

            tries++
            log("Guessed ${num.text} (tries:${tries}")

            if(g < number) {
                status.setText(R.string.status_too_low)
                num.setText("")

            } else if(g > number) {
                status.setText(R.string.status_too_high)
                num.setText("")
            } else {
                Statistics.register(number, tries)
                status.text = getString(R.string.status_hit, tries)
                started = false
                doGuess.setEnabled(false)
            }
        } catch(e:GameException) {
            Toast.makeText(this, "Guessable numbers: ${Constants.LOWER_BOUND} to ${Constants.UPPER_BOUND}", Toast.LENGTH_LONG).show()
            log(e.message ?: "null")
        }
    }

    fun colorChange(v:View) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> { AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES) } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> { AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO) } // Night mode is active, we're using dark theme
        }

    }

    //pritvate 함수 ------------------------------------------------------------------------------//

    private fun putInstanceData(outState:Bundle?) {
        if(outState != null) with(outState) {
            putBoolean("started", started)
            putInt("number", number)
            putInt("tries", tries)
            putString("statusMsg", status.text.toString())
            putStringArrayList("logs", ArrayList(console.text.split("\n")))
        }
    }

    private fun fetchSavedInstanceData(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) with(savedInstanceState) {
            started = getBoolean("started")
            number = getInt("number")
            tries = getInt("tries")
            status.text = getString("statusMsg")
            console.text = getStringArrayList("logs")!!.joinToString("\n")
        }
    }

    private fun log(msg:String) {
        Log.d("LOG", msg)
        console.log(msg)
    }

    private fun openStatistics() {
        val intent: Intent = Intent(this,StatisticsActivity::class.java)
        startActivity(intent)
    }
}









