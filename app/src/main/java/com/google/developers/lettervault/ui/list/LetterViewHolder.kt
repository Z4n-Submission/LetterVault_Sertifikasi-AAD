package com.google.developers.lettervault.ui.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter
import java.text.SimpleDateFormat
import java.util.*

/**
 * View holds a letter for RecyclerView.
 */
class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var letter: Letter
    private val context = itemView.context
    private val simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())
    private val subjectText: TextView = itemView.findViewById(R.id.subjectList)
    private val messageText: TextView = itemView.findViewById(R.id.messageList)
    private val letterState: TextView = itemView.findViewById(R.id.letterStateList)
    private val lockView: ImageView = itemView.findViewById(R.id.lockView)
    private val lock: ImageView = itemView.findViewById(R.id.lock)

    fun bindData(letter: Letter, clickListener: (Letter) -> Unit) {
        this.letter = letter
        itemView.setOnClickListener { clickListener(letter) }

        if (letter.expires < System.currentTimeMillis() && letter.opened != 0L) {
            val opened =
                context.getString(R.string.title_opened, simpleDate.format(letter.opened))
            letterState.text = opened
            lockView.visibility = View.GONE
        } else {
            if (letter.expires < System.currentTimeMillis()) {
                val ready = context.getString(R.string.letter_ready)
                letterState.text = ready
                lock.visibility = View.GONE
                messageText.visibility = View.GONE
            } else {
                val opening =
                    context.getString(R.string.letter_opening, simpleDate.format(letter.expires))
                letterState.text = opening
                lock.visibility = View.GONE
                messageText.visibility = View.GONE
            }
        }

        subjectText.text = letter.subject
        messageText.text = letter.content

    }

    /**
     * This method is used during automated tests.
     *
     * DON'T REMOVE THIS METHOD
     */
    @VisibleForTesting
    fun getLetter(): Letter = letter
}
