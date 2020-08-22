package com.kaiju.simplelifetracker.options

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.*
import com.kaiju.simplelifetracker.R
import com.kaiju.simplelifetracker.game.GameActivity
import com.kaiju.simplelifetracker.game.GameFragment
import kotlinx.android.synthetic.main.game_fragment_game.*

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

    class SettingsFragment : PreferenceFragmentCompat() {

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

                true
            }

        }

    }

}
