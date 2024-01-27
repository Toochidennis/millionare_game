package com.digitalDreams.millionaire_game.alpha.adapters;

import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG;
import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_SHORT;
import static com.digitalDreams.millionaire_game.alpha.Constants.GREEN;
import static com.digitalDreams.millionaire_game.alpha.Constants.ORANGE;
import static com.digitalDreams.millionaire_game.alpha.Constants.getBackgroundDrawable;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.ProgressActivity;
import com.digitalDreams.millionaire_game.R;

import java.util.List;
import java.util.Locale;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {


    private final List<Integer> itemList;
    private final List<Integer> amountWonList;

    public ProgressAdapter(List<Integer> itemList, List<Integer> amountWonList) {
        this.itemList = itemList;
        this.amountWonList = amountWonList;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_progress, parent, false);

        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        private final TextView positionTextView;
        private final TextView amountTextView;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.position_text);
            amountTextView = itemView.findViewById(R.id.amount_text);
        }

        void bind(int item) {
            String position = String.valueOf(getAdapterPosition() + 1);
            String amount = String.format(Locale.getDefault(), "$%d", item);

            positionTextView.setText(position);
            amountTextView.setText(amount);
            setBackgroundColor(item);

            fadeInAnimation(getAdapterPosition());
        }

        private void setBackgroundColor(int item) {
            int currentAmount = 0;

            for (int amount : amountWonList) {
                currentAmount = amount;
                if (currentAmount == item) {
                    getBackgroundDrawable(GREEN, itemView);
                }
            }

            if (currentAmount != 0) {
                if (getAdapterPosition() == amountWonList.indexOf(currentAmount) + 1) {
                    getBackgroundDrawable(ORANGE, itemView);
                }
            }

        }


        private void fadeInAnimation(int position) {
            final AnimationSet set = new AnimationSet(false);
            Animation inFromLeft = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f);

            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_in_right);

            set.addAnimation(inFromLeft);
            animation.setDuration(300);

            animation.setStartOffset(50L * (position + 1));
            itemView.startAnimation(animation);
        }
    }
}
