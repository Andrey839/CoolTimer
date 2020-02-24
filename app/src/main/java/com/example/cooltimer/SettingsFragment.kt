package com.example.cooltimer

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.preference.*
import java.lang.Exception
import java.lang.NumberFormatException

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener{
//    Preference
//.OnPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.timer_preference)

        val preferenceFragment: SharedPreferences = preferenceScreen.sharedPreferences
        val preferenceScreen = preferenceScreen
        val count = preferenceScreen.preferenceCount

        for (i in count.downTo(0)) {
            val preference: Preference = preferenceScreen.getPreference(1)

            if ((preference !is CheckBoxPreference)) {
                val value = preferenceFragment.getString(preference.key, "")

                setPreferenceLabel(preference, value)
            }


        }

//        val preference: Preference? = findPreference("timer_default")
//        preference?.onPreferenceChangeListener = this
    }

    fun setPreferenceLabel(preference: Preference, value: String?) {
        if (preference is ListPreference) {
            val listPreference: ListPreference = preference
            val index = listPreference.findIndexOfValue(value)
            if (index >= 0) listPreference.summary = listPreference.entries[index]
        } else if (preference is EditTextPreference) {
            preference.summary = value
        }

    }

    companion object CREATOR : Parcelable.Creator<SettingsFragment> {
        override fun createFromParcel(parcel: Parcel): SettingsFragment {
            return SettingsFragment()
        }

        override fun newArray(size: Int): Array<SettingsFragment?> {
            return arrayOfNulls(size)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        val preference: Preference? = key?.let { findPreference(it) }
        if (preference !is CheckBoxPreference) {
            val value = sharedPreferences?.getString(preference?.key, "")
            preference?.let { setPreferenceLabel(it, value) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

//   override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
//
//       val toast = Toast.makeText(context, "Не верное значение", Toast.LENGTH_LONG)
//       if (preference?.key.equals("timer_default")) {
//           newValue.toString()
//           try {
//
//               newValue as Int
//           }
//
//           catch (e: NumberFormatException){
//               toast.show()
//               return false
//           }
//       }
//       return true
//
//   }
//
}
