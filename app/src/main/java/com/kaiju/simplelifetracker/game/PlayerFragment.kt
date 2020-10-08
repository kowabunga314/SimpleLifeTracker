package com.kaiju.simplelifetracker.game

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.kaiju.simplelifetracker.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val STARTING_LIFE = "starting_life"
private const val PREFS = "prefs"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var startingLife: String? = "20"
    private var mToast: Toast? = null
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startingLife = it.getString(STARTING_LIFE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.game_fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        val positiveButton = view.findViewById<Button>(R.id.game_button_positive)
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        val negativeButton = view.findViewById<Button>(R.id.game_button_negative)

        positiveButton.setOnClickListener {
            // Get value from prefs
            var incrementString = prefs?.getString("key_small_increment", "1") ?: "1"
            var incrementValue = incrementString.toInt()

            handleIncrementScore(scoreTextView, incrementValue)
        }

        positiveButton.setOnLongClickListener {
            // Get value from prefs
            var incrementString = this.prefs?.getString("key_large_increment", "5") ?: "5"
            var incrementValue = incrementString.toInt()

            handleIncrementScore(scoreTextView, incrementValue)
            return@setOnLongClickListener true
        }

        negativeButton.setOnClickListener {
            // Get value from prefs
            var incrementString = prefs?.getString("key_small_increment", "1") ?: "1"
            var incrementValue = incrementString.toInt()


            handleDecrementScore(scoreTextView, incrementValue)
        }

        negativeButton.setOnLongClickListener {
            // Get increment from preferences
            var incrementString = prefs?.getString("key_large_increment", "5") ?: "5"
            var incrementValue = incrementString.toInt()

            handleDecrementScore(scoreTextView, incrementValue)
            return@setOnLongClickListener true
        }

    }

    private fun handleIncrementScore(view: View, value: Int = 1) {
        // Get TextView
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        // Get score as integer from TextView
        var scoreText = scoreTextView.text.toString()

        // Check for scores that are too large to be held in integer val
        if (scoreText.length > 9) {
            scoreText = getString(R.string.max_score)
            if (mToast != null) mToast?.cancel()
            mToast = Toast.makeText(context, getString(R.string.error_score_too_high), Toast.LENGTH_SHORT)
            mToast?.show()
        }

        // Get increment value from preferences

        // Calculate new score
        try {
            var newScore = scoreText.toInt() + value

            // Don't exceed limit
            if (newScore > getString(R.string.max_score).toInt()) {
                newScore = getString(R.string.max_score).toInt()
            }

            // Set TextView value to new score
            scoreTextView.text = newScore.toString()
        } catch (e: Exception) {
            return
        }
    }

    private fun handleDecrementScore(view: View, value: Int = 1) {
        // Get TextView
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        // Get current score from TextView
        var scoreText = scoreTextView.text.toString()

        // Check length of score before casting to int
        if (scoreText.length > 9) {
            scoreText = getString(R.string.max_score)
            if (mToast != null) mToast?.cancel()
            mToast = Toast.makeText(context, getString(R.string.error_score_too_high), Toast.LENGTH_SHORT)
            mToast?.show()
        }

        // Calculate new score
        val newScore = scoreText.toInt() - value

        // Check to see if this action will send player life into negative range
        if (newScore < 0) {
            // STOP RESISTING
            if (mToast != null) mToast?.cancel()
            mToast = Toast.makeText(context, "He's dead, Jim", Toast.LENGTH_SHORT)
            mToast?.show()
        }

        // Set TextView value to new score
        scoreTextView.text = newScore.toString()

    }

    fun handleResetScore(view: View) {

        // Get starting life value from preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val startingLife = prefs.getString("starting_life", "20")

        // Set both scores to starting value
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)

        scoreTextView.text = startingLife
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param startingLife The starting life value for games.
         * @return A new instance of fragment PlayerFragment.
         */
        @JvmStatic
        fun newInstance(startingLife: String) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(STARTING_LIFE, startingLife)
                }
            }
    }
}