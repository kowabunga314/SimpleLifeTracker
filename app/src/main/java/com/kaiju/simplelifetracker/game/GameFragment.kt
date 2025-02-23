package com.kaiju.simplelifetracker.game

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.kaiju.simplelifetracker.Dice.Die
import com.kaiju.simplelifetracker.R
import com.kaiju.simplelifetracker.options.SettingsActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.game_fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle options button
        configureOptionsButton(view)

        // Handle roll dialog button
        configureRollDialogButton(view)

        // Change scores to value set in prefs
        resetScores(view)
    }

    override fun onResume() {
        super.onResume()

        // Check for reset flag
        if (getResetFlag() == true) {
            view?.let { resetScores(it) }
            clearResetFlag()
        }
    }

    private fun configureOptionsButton(view: View) {
        val optionsButton = view.findViewById<Button>(R.id.game_button_menu)

        // Set menu behavior
        optionsButton.setOnClickListener {

            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)

        }

        // Set reset shortcut behavior
        optionsButton.setOnLongClickListener {
            resetScores(view)

            true
        }
    }

    private fun configureRollDialogButton(view: View) {
        val rollDialogButton = view.findViewById<Button>(R.id.game_button_roll_die)

        // Set roll die behavior
        rollDialogButton.setOnClickListener {
            val dialog = activity?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            // Clicking outside of dialog or OS back button closes dialog
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.die_roll_dialog_die_roll)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Set status to let us know we're setting up the dialog
            var initializing = true
            // Get prefs, including die sides
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            var dieSides = prefs.getString("die_sides", "6") ?: "6"

            // Set dialog title line
            val titleView = dialog?.findViewById<TextView>(R.id.layout_die_roll_title)
            if (titleView != null) {
                setDialogTitle(titleView, dieSides)
            }

            // Set dialog dismiss button behavior
            val dialogDismissButton = dialog?.findViewById(R.id.layout_die_roll_dismiss) as Button
            dialogDismissButton.setOnClickListener {
                dialog.dismiss()
            }

            // Set die roll spinner behavior
            val spinner: Spinner = dialog.findViewById(R.id.spinner_die_sides)
            context?.let { it1 -> ArrayAdapter.createFromResource(
                it1,
                R.array.die_side_labels,
                R.layout.spinner_layout
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val pass: String

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        if (!initializing) {
                            // Write new value to prefs
                            val sides = resources.getStringArray(R.array.die_side_values)[pos]
                            val editor = prefs.edit()
                            editor.putString("die_sides", sides)
                            editor.apply()

                            // Set title (die vs coin)
                            if (titleView != null) {
                                setDialogTitle(titleView, sides)
                            }
                        } else {
                            // Get position of selected die type and set spinner value
                            val dieTypes = resources.getStringArray(R.array.die_side_values)
                            val selectedIndex = dieTypes.indexOf(dieSides)
                            spinner.setSelection(selectedIndex)
                            val sides = resources.getStringArray(R.array.die_side_values)[selectedIndex]

                            if (titleView != null) {
                                setDialogTitle(titleView, sides)
                            }
                        }
                        initializing = false
                    }
                }
            } }

            val rollButton = dialog.findViewById<Button>(R.id.layout_die_roll_image)
            rollButton.setOnClickListener {
                dieSides = prefs.getString("die_sides", "6") ?: "6"
                // Get and set die roll value
                val die = Die(dieSides.toInt())
                onRollButtonClicked(dialog, rollButton, die)
            }

            dialog.show()
        }
    }

    private fun getResetFlag(): Boolean? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val resetFlag = prefs.getBoolean("key_flag_reset_game", false)
        return resetFlag
    }

    private fun clearResetFlag() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity) ?: return
        with (sharedPref.edit()) {
            putBoolean("key_flag_reset_game", false)
            commit()
        }
    }

    private fun setDialogTitle(titleView: TextView, sides: String) {
        // Get title from string resources
        var title = getString(R.string.title_dialog_die_roll)

        // Check whether coin or die
        if (sides == "2") {
            title = getString(R.string.title_dialog_coin_flip)
        }

        titleView.text = title
    }

    fun resetScores(view: View) {

        // Get player fragment views
        val player1Fragment = FragmentManager.findFragment<PlayerFragment>(view.findViewById(
            R.id.fragment_player_one
        ))
        val player2Fragment = FragmentManager.findFragment<PlayerFragment>(view.findViewById(
            R.id.fragment_player_two
        ))

        // Set reset score handler for each fragment
        player1Fragment.handleResetScore(view.findViewById(R.id.fragment_player_one))
        player2Fragment.handleResetScore(view.findViewById(R.id.fragment_player_two))

    }

    private fun onRollButtonClicked(dialog: Dialog, rollButton: Button, die: Die) {
        // Animate die roll
        val buttonAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_die_roll)
        rollButton.startAnimation(buttonAnimation)

        // Set new value halfway through the animation
        val handler = Handler()
        handler.postDelayed({
            // Roll die
            var rollResult = die.roll()

            // Handle result string if coin toss
            if (die.getSides() == 2) {
                rollResult = if (rollResult == "1") getString(R.string.heads_display) else getString(
                                    R.string.tails_display)
            }

            // Set die roll result
            val rollResultDisplay = dialog.findViewById<Button>(R.id.layout_die_roll_image)
            rollResultDisplay.text = rollResult
        }, getString(R.string.die_roll_animation_duration).toLong()/2)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun resetGame(view: View) {
            GameFragment().resetScores(view)
        }
    }
}