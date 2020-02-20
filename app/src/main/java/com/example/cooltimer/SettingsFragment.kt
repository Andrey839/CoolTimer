package com.example.cooltimer

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.preference.*

class SettingsFragment(parcel: Parcel) : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.timer_preference)

        val preferenceFragment: SharedPreferences = this.preferenceScreen.sharedPreferences
        val preferenceScreen = this.preferenceScreen
        val count = preferenceScreen.preferenceCount

        for (i in count.rangeTo(Int.MAX_VALUE)) {
            val preference: Preference = preferenceScreen.getPreference(i)

            if ((preference !is CheckBoxPreference)) {
                val value = preferenceFragment.getString(preference.key, "")

                setPreferenceLabel(preference, value)
            }
        }
    }

    fun setPreferenceLabel(preference: Preference, value: String?) {
        if (preference is ListPreference) {
            val listPreference: ListPreference = preference
            val index = listPreference.findIndexOfValue(value)
            if (index >= 0) listPreference.summary = listPreference.entries[index]
        }

    }

    companion object CREATOR : Parcelable.Creator<SettingsFragment> {
        override fun createFromParcel(parcel: Parcel): SettingsFragment {
            return SettingsFragment(parcel)
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
}
