package com.example.cooltimer

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar.max = 600
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minutes = progress / 60
                val second = progress - (minutes * 60)
                textView.text = minutes.toString() + ":" + second.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }

    fun Timer(view: View) {
        if (button.text.equals("START")) {
            button.text = "STOP"
            seekBar.isEnabled = false
            timer = object : android.os.CountDownTimer((seekBar.progress * 1000).toLong(), 1000) {
                override fun onFinish() {

                    val preference: SharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    if (preference.getBoolean("enable_sound", true)) {
                        val melodyName = preference.getString("sound_value", "song")
                        when(melodyName){
                            "song" ->   MediaPlayer.create(this@MainActivity, R.raw.song).start()
                            "bibi" ->   MediaPlayer.create(this@MainActivity, R.raw.bibi).start()
                            "sound" ->  MediaPlayer.create(this@MainActivity, R.raw.sound).start()
                        }
                        seekBar.isEnabled = true
                        seekBar.progress = 0
                        button.text = "START"
                    }
                    seekBar.isEnabled = true
                    seekBar.progress = 0
                    button.text = "START"
                }

                override fun onTick(millisUntilFinished: Long) {
                    val minutes = millisUntilFinished / 1000 / 60
                    val second = millisUntilFinished / 1000 - (minutes * 60)
                    textView.text = minutes.toString() + ":" + second.toString()
                }
            }.start()

        } else {
            timer.cancel()
            button.text = "START"
            seekBar.isEnabled = true
            textView.text = "0:0"
            seekBar.progress = 0
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.timer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_setting) {
            val setting = Intent(this, SettingsActivity::class.java)
            startActivity(setting)
        } else {
            val about = Intent(this, AboutActivity::class.java)
            startActivity(about)
        }
        return super.onOptionsItemSelected(item)
    }
}
