package com.kaiju.simplelifetracker.options

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kaiju.simplelifetracker.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        supportActionBar?.title = getString(R.string.options_title)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity_menu)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize ads module
        MobileAds.initialize(this) {}
        // Get ad view
        val adView = findViewById<AdView>(R.id.settings_ad_view)
        // Formulate request for ad
        val adRequest = AdRequest.Builder().build()
        // load ad
        adView.loadAd(adRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var mToast: Toast? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            // Set starting life preference to only accept numeric values
            val editTextPreference = preferenceManager.findPreference<EditTextPreference>("starting_life")
            editTextPreference?.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }

            // Define behavior for reset preference
            preferenceManager.findPreference<Preference>(
                "key_reset_game")?.setOnPreferenceClickListener {

                val resetFlag = preferenceManager.findPreference<CheckBoxPreference>("key_flag_reset_game")
                if (resetFlag != null) {
                    resetFlag.isChecked = true
                }

                activity?.finish()
                if (mToast != null) mToast?.cancel()
                mToast = Toast.makeText(context, "Game reset!", Toast.LENGTH_SHORT)
                mToast?.show()

                true
            }

            // Set theme preference
            val listPreference = findPreference<ListPreference>(getString(R.string.key_display_mode))
            listPreference?.setOnPreferenceChangeListener { preference, newValue ->

                when (newValue) {
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    "system_default" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }

                true
            }

            val startingLifePreference = findPreference<Preference>(getString(R.string.key_starting_life))
            startingLifePreference?.summary = preferenceManager.sharedPreferences.getString(getString(R.string.key_starting_life), "20")
            startingLifePreference?.setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = newValue.toString()

                true
            }

            val smallIncrementPreference = findPreference<Preference>(getString(R.string.key_small_increment))
            // Set summary to include score
            smallIncrementPreference?.summary =
                "${smallIncrementPreference?.summary.toString()}\n\n${
                    preferenceManager.sharedPreferences.getString(
                        getString(R.string.key_small_increment),
                        getString(R.string.small_increment_value)
                    )
                }"

            val largeIncrementPreference = findPreference<Preference>(getString(R.string.key_large_increment))
            // Set summary to include score
            largeIncrementPreference?.summary =
                "${largeIncrementPreference?.summary.toString()}\n\n${
                    preferenceManager.sharedPreferences.getString(
                        getString(R.string.key_large_increment),
                        getString(R.string.large_increment_value)
                    )
                }"

            val dieSidesPreference = findPreference<Preference>(getString(R.string.key_die_sides))
            dieSidesPreference?.summary = preferenceManager.sharedPreferences.getString(getString(R.string.key_die_sides), "6")
            dieSidesPreference?.setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = newValue.toString()

                true
            }

        }

    }

}
