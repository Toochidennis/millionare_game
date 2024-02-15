package com.digitalDreams.millionaire_game.alpha.testing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digitalDreams.millionaire_game.R
import com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG
import com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_SHORT
import com.digitalDreams.millionaire_game.alpha.Constants.getLabelFromList
import com.digitalDreams.millionaire_game.alpha.adapters.OnOptionsClickListener
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel
import java.util.Random

class OptionsAdapter2(
    private val itemList: MutableList<OptionsModel>,
    private val optionsClickListener: OnOptionsClickListener
) : RecyclerView.Adapter<OptionsAdapter2.OptionViewHolder>() {

    private val hiddenPositions = mutableListOf<String>()

    private var shouldSkipAnimation = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionsAdapter2.OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent, false)

        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionsAdapter2.OptionViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val labelTextView: TextView = itemView.findViewById(R.id.label_text)
        private val optionTextView: TextView = itemView.findViewById(R.id.option_text)

        fun bind(optionsModel: OptionsModel) {
            val label = getLabelFromList(itemView.context, adapterPosition)
            labelTextView.text = label
            optionTextView.text = optionsModel.optionText

            if (hiddenPositions.contains(optionsModel.optionText)) {
                itemView.visibility = View.INVISIBLE
            } else {
                itemView.visibility = View.VISIBLE
            }

            fadeInAnimation(itemView, adapterPosition)

            itemView.setOnClickListener {
                optionsClickListener.onOptionClick(adapterPosition, itemView)
            }

        }
    }

    fun hideRandomOptions(correctAnswerText: String) {
        hiddenPositions.clear()

        while (hiddenPositions.size < 2) {
            val hiddenPosition = getRandomOptionPosition()
            if (hiddenPosition != correctAnswerText && !hiddenPositions.contains(hiddenPosition)) {
                hiddenPositions.add(hiddenPosition)
            }
        }

        for (hiddenPosition in hiddenPositions) {
            val position = getPositionByOptionText(hiddenPosition)
            if (position != -1) {
                notifyItemChanged(position)
                shouldSkipAnimation = true
            }
        }
    }

    private fun getRandomOptionPosition(): String {
        val randomPosition = Random().nextInt(itemList.size)
        return itemList[randomPosition].optionText
    }

    fun fadeInAnimation(view: View, position: Int) {
        if (!shouldSkipAnimation) {
            val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in_right)
            val startOffSet = (position + 1) * DELAY_INTERVAL_SHORT + DELAY_INTERVAL_LONG
            animation.startOffset = startOffSet
            view.startAnimation(animation)
        }
    }

    private fun getPositionByOptionText(optionText: String): Int {
        for (i in itemList.indices) {
            if (itemList[i].optionText == optionText) {
                return i
            }
        }
        return -1 // Option not found
    }
}