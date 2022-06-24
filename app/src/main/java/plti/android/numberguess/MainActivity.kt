package plti.android.numberguess

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import plti.android.numberguess.common.Constants
import plti.android.numberguess.common.GameException
import plti.android.numberguess.databinding.ActivityMainBinding
import plti.android.numberguess.model.GameUser
import plti.android.numberguess.random.RandomNumberGenerator
import plti.android.numberguess.random.impl.StdRandom
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity() {

    private lateinit var mv: ActivityMainBinding

    var started = false
    var number = 0
    var tries = 0


    val rnd: RandomNumberGenerator = StdRandom()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mv = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mv.root)

        fetchSavedInstanceData(savedInstanceState)
        mv.doGuess.setEnabled(started)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        putInstanceData(outState)
    }

    //버튼 함수 ------------------------------------------------------------------------------------//

    fun start(v: View) {
        log("game started")
        mv.num.setText("")
        started=true
        mv.doGuess.setEnabled(true)
        mv.status.text = getString(R.string.guess_hint, Constants.LOWER_BOUND, Constants.UPPER_BOUND)
        val span = Constants.UPPER_BOUND + Constants.LOWER_BOUND + 1
        number = rnd.rnd(Constants.LOWER_BOUND,Constants.UPPER_BOUND)
        tries = 0
    }

    fun guess(v:View) {
        if(mv.num.text.toString() == "") return

        try {
            val g = mv.num.text.toString().toInt()

            if(g < Constants.LOWER_BOUND) throw GameException("Must guess a number >= ${Constants.LOWER_BOUND}")
            if(g > Constants.UPPER_BOUND) throw GameException("Must guess a number <= ${Constants.UPPER_BOUND}")

            tries++
            log("Guessed ${mv.num.text} (tries:${tries}")

            if(g < number) {
                mv.status.setText(R.string.status_too_low)
                mv.num.setText("")

            } else if(g > number) {
                mv.status.setText(R.string.status_too_high)
                mv.num.setText("")
            } else {
                mv.status.text = getString(R.string.status_hit, tries)
                started = false
                mv.doGuess.setEnabled(false)
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
            putString("statusMsg", mv.status.text.toString())
            putStringArrayList("logs", ArrayList(mv.console.text.split("\n")))
        }
    }

    private fun fetchSavedInstanceData(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) with(savedInstanceState) {
            started = getBoolean("started")
            number = getInt("number")
            tries = getInt("tries")
            mv.status.text = getString("statusMsg")
            mv.console.text = getStringArrayList("logs")!!.joinToString("\n")
        }
    }

    private fun log(msg:String) {
        Log.d("LOG", msg)
        mv.console.log(msg)
    }
}









