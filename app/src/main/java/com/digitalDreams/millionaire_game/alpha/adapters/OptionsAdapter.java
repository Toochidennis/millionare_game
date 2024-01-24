package com.digitalDreams.millionaire_game.alpha.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {


    private final List<OptionsModel> itemList;

    private final String[] labelList = {"A", "B", "C", "D", "E"};


    public OptionsAdapter(List<OptionsModel> itemList) {
        this.itemList = itemList;
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
            String label = labelList[getAdapterPosition()];
            labelTextView.setText(label);
            optionTextView.setText(optionsModel.getOptionText());
        }
    }

}
