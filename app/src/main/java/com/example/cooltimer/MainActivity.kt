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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var timer: CountDownTimer
    var defaultInterval: Int = 0
    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharePref = PreferenceManager.getDefaultSharedPreferences(this)

        seekBar.max = 600
        setIntervalTimer(PreferenceManager.getDefaultSharedPreferences(this))
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

        sharePref.registerOnSharedPreferenceChangeListener(this)

    }

    fun Timer(view: View) {
        if (button.text.equals("START")) {
            button.text = "STOP"
            seekBar.isEnabled = false
            timer = object : android.os.CountDownTimer((seekBar.progress * 1000).toLong(), 1000) {
                override fun onFinish() {


                    if (sharePref.getBoolean("enable_sound", true)) {
                        val melodyName = sharePref.getString("sound_value", "song")
                        when (melodyName) {
                            "song" -> MediaPlayer.create(this@MainActivity, R.raw.song).start()
                            "bibi" -> MediaPlayer.create(this@MainActivity, R.raw.bibi).start()
                            "sound" -> MediaPlayer.create(this@MainActivity, R.raw.sound).start()
                        }
                        seekBar.isEnabled = true
                        setIntervalTimer(sharePref)
                        button.text = "START"
                    }
                    seekBar.isEnabled = true
                    setIntervalTimer(sharePref)
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
            setIntervalTimer(sharePref)
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

    fun setIntervalTimer(sharedPreferences: SharedPreferences) {
        try {
            defaultInterval = (sharedPreferences.getString("timer_default", "0"))!!.toInt()
        }
        catch (e: Exception){
            Toast.makeText(this, "Не правельное значение",Toast.LENGTH_LONG).show()
        }
        textView.text = defaultInterval.toString()
        seekBar.progress = defaultInterval
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals("timer_default")) {
            setIntervalTimer(sharePref)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharePref.unregisterOnSharedPreferenceChangeListener(this)
    }
}
