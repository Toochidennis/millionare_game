package com.digitalDreams.millionaire_game.alpha.adapters;

import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG;
import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_SHORT;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLabelFromList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {

    private final List<OptionsModel> itemList;

    private OnOptionsClickListener optionsClickListener;

    private List<String> hiddenPositions = new ArrayList<>();

    private boolean shouldSkipAnimation = false;


    public OptionsAdapter(List<OptionsModel> itemList, OnOptionsClickListener optionsClickListener) {
        this.itemList = itemList;
        this.optionsClickListener = optionsClickListener;
    }


    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_options, parent, false);

        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class OptionsViewHolder extends RecyclerView.ViewHolder {

        private final TextView labelTextView;
        private final TextView optionTextView;


        public OptionsViewHolder(@NonNull View itemView) {
            super(itemView);

            labelTextView = itemView.findViewById(R.id.label_text);
            optionTextView = itemView.findViewById(R.id.option_text);
        }

        void bind(OptionsModel optionsModel) {
            String label = getLabelFromList(itemView.getContext(), getAdapterPosition());
            labelTextView.setText(label);
            optionTextView.setText(optionsModel.getOptionText());

            if (hiddenPositions.contains(optionsModel.getOptionText())) {
                itemView.setVisibility(View.INVISIBLE);
            } else {
                itemView.setVisibility(View.VISIBLE);
            }

            fadeInAnimation(itemView, getAdapterPosition());

            itemView.setOnClickListener(optionClicked -> optionsClickListener.onOptionClick(getAdapterPosition(), itemView));
        }

    }

    public void hideRandomOptions(String correctAnswerText) {
        // this.correctAnswerText = correctAnswerText;

        hiddenPositions.clear();

        while (hiddenPositions.size() < 2) {
            String hiddenPosition = getRandomOptionPosition();

            if (!hiddenPosition.equals(correctAnswerText) && !hiddenPositions.contains(hiddenPosition)) {
                hiddenPositions.add(hiddenPosition);
            }
        }

        for (String hiddenPosition : hiddenPositions) {
            int position = getPositionByOptionText(hiddenPosition);

            if (position != -1) {
                notifyItemChanged(position);
                shouldSkipAnimation = true;
            }
        }
    }

    private String getRandomOptionPosition() {
        int randomPosition = new Random().nextInt(itemList.size());
        return itemList.get(randomPosition).getOptionText();
    }

    public void fadeInAnimation(View view, int position) {
        if (!shouldSkipAnimation) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_right);
            long startOffSet = ((position + 1) * DELAY_INTERVAL_SHORT) + DELAY_INTERVAL_LONG;
            animation.setStartOffset(startOffSet);
            view.startAnimation(animation);
        }
    }

    private int getPositionByOptionText(String optionText) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getOptionText().equals(optionText)) {
                return i;
            }
        }
        return -1; // Option not found
    }

}