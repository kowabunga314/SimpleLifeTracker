package com.kaiju.simplelifetracker.options

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import com.kaiju.simplelifetracker.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.show()

    }

    companion object {
        /**
         * preference value change listener that updates preference to reflect summary's new value
         */

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->

            val stringValue = value.toString()

            if (preference is ListPreference) {
                // Get correct display value in entries list
                val index = preference.findIndexOfValue(stringValue)

                preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null
                )
            } else {
                // For other types just set summary to value's string representation
                preference.summary = stringValue
            }

            true

        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set listener to watch for value changes
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger listener immediately with pref's current value
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
            PreferenceManager
                .getDefaultSharedPreferences(preference.context)
                .getString(preference.key, ""))
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.root_preferences)
//
////            findPreference<Preference>("display_mode")?.let { bindPreferenceSummaryToValue(it) }
////            findPreference<Preference>("starting_life")?.let { bindPreferenceSummaryToValue(it) }
//        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            // Set starting life preference to only accept numeric values
            val editTextPreference = preferenceManager.findPreference<EditTextPreference>("starting_life")
            editTextPreference?.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }


        }

    }
}