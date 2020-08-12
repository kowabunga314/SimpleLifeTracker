package com.kaiju.simplelifetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val STARTING_LIFE = "starting_life"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var startingLife: String? = "20"

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
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val positiveButton = view.findViewById<Button>(R.id.game_button_positive)
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        val negativeButton = view.findViewById<Button>(R.id.game_button_negative)

        positiveButton.setOnClickListener { handleIncrementScore(scoreTextView) }
        positiveButton.setOnLongClickListener {
            handleIncrementScore(scoreTextView, 5)
            return@setOnLongClickListener true
        }

        negativeButton.setOnClickListener { handleDecrementScore(scoreTextView) }
        negativeButton.setOnLongClickListener {
            handleDecrementScore(scoreTextView, 5)
            return@setOnLongClickListener true
        }

    }

    private fun handleIncrementScore(view: View, value: Int = 1) {
        // Get TextView
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        // Get score as integer from TextView
        val scoreText = scoreTextView.text.toString()
        // Calculate new score
        val newScore = scoreText.toInt() + value
        // Set TextView value to new score
        scoreTextView.text = newScore.toString()
    }

    private fun handleDecrementScore(view: View, value: Int = 1) {
        // Get TextView
        val scoreTextView = view.findViewById<TextView>(R.id.game_textview_score)
        // Get current score from TextView
        val scoreText = scoreTextView.text.toString()
        // Calculate new score
        val newScore = scoreText.toInt() - value

        // Check to see if this action will send player life into negative range
        if (newScore < 0) {
            // STOP RESISTING
            val toast = Toast.makeText(context, "He's dead, Jim", Toast.LENGTH_SHORT)
            toast.show()
        }

        // Set TextView value to new score
        scoreTextView.text = newScore.toString()

    }

    private fun handleResetScore(view: View) {
        // Get the default starting life value
        val newScore = startingLife
        // Set both scores to starting value
        view.findViewById<TextView>(R.id.game_textview_score).text = newScore
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