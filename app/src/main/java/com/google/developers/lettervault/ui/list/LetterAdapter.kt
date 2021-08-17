package com.google.developers.lettervault.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter

/**
 * Implementation of an Paging adapter that shows list of Letters.
 */
class LetterAdapter(
    private val clickListener: (Letter) -> Unit
) : PagedListAdapter<Letter, LetterViewHolder>(DIFF_CALLBACK) {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Letter>() {
            override fun areItemsTheSame(oldItem: Letter, newItem: Letter): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Letter, newItem: Letter): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.letter_list_item, parent, false)
        return LetterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = getItem(position) as Letter
        holder.bindData(letter, clickListener)
    }
}
