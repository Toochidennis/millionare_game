package com.digitalDreams.millionaire_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    Context context;
    private JSONArray histories;
    String currentDat = "";
    String highest_score;

    public HistoryAdapter(@NonNull Context context, JSONArray histories, String highest_score) {
        this.context = context;
        this.histories = histories;
        this.highest_score = highest_score;
        Log.i("checking", String.valueOf(histories.length()));

    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_history, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        try {
            JSONObject history = histories.getJSONObject(position);
            //String content = history.getString("content");
            String date_played = history.getString("date_played");
            // String correctAnswer = history.getString("correct");
            String high_score = history.getString("high_score");
            String correct_answers = history.getString("correct_answers");
            String wrong_answers = history.getString("wrong_answers");
            // JSONArray answers = new JSONArray(history.getString("answer"));
            holder.dateLayout.setVisibility(View.GONE);
            holder.mAdView.setVisibility(View.GONE);
            holder.high_score_layout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = 0;
            holder.rootview.setLayoutParams(params);
            if (position == 0) {
                currentDat = date_played;

                holder.dateLayout.setVisibility(View.VISIBLE);
                holder.high_score_layout.setVisibility(View.VISIBLE);


//            AdRequest adRequest = new AdRequest.Builder().build();
//            holder. mAdView.loadAd(adRequest);
                holder.high_score.setText(highest_score);
//            holder.mAdView.setVisibility(View.VISIBLE);
            } else {


                if (!currentDat.equals(date_played)) {
                    currentDat = date_played;
                    holder.dateLayout.setVisibility(View.VISIBLE);
                    params.topMargin = 40;
                    holder.rootview.setLayoutParams(params);

//                AdRequest adRequest = new AdRequest.Builder().build();
//                holder. mAdView.loadAd(adRequest);
//                holder. mAdView.setVisibility(View.VISIBLE);
                }
            }

            if (position % 3 == 0 && position != 0) {
                AdRequest adRequest = new AdRequest.Builder().build();
                holder.mAdView.loadAd(adRequest);
                holder.mAdView.setVisibility(View.VISIBLE);
            } else {
                holder.mAdView.setVisibility(View.GONE);

            }


            holder.session.setText("" + date_played);
            holder.session_high_score.setText(Utils.addCommaAndDollarSign(Double.parseDouble(high_score)));

            String correctAnswerTxt = correct_answers + " " + context.getResources().getString(R.string.correct_answers);
            holder.question.setText(correctAnswerTxt);
            String wrongAnswerText = wrong_answers + " " + context.getResources().getString(R.string.wrong_answers);
            holder.wrong.setText(wrongAnswerText);
            double true_value = Double.parseDouble(correct_answers);
            double false_value = Double.parseDouble(correct_answers);
            double overall_value = 15; // (Integer.parseInt(wrong_answers)+Integer.parseInt(correct_answers));
            double accuracy = (true_value / overall_value) * 100;// ((overall_value)/15);

            holder.accuracy.setText(String.format(Locale.getDefault(), "%.2f %s", accuracy, context.getResources().getString(R.string.accuracy)));

            holder.view_answers.setOnClickListener(view -> {
                Intent i = new Intent(context, HistoryDetals.class);
                i.putExtra("date_played", date_played);
                context.startActivity(i);
            });
            //HistoryOptionAdapter  historyOptionAdapter = new HistoryOptionAdapter(context,answers,correctAnswer);
            // holder.optionsList.setAdapter(historyOptionAdapter);
            // historyOptionAdapter.notifyDataSetChanged();

            //inflateOptions(answers,holder,correctAnswer);
            //setListViewHeightBasedOnChildren(holder.optionsList);


        } catch (Exception e) {
            Log.i("checking", String.valueOf(e));

        }

    }

    @Override
    public int getItemCount() {
        // Log.i("checking==", String.valueOf(histories.length()));

        return histories.length();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView session, question, accuracy, high_score, session_high_score, wrong;
        LinearLayout optionLayout;
        //ListView optionsList;
        CardView dateLayout;
        LinearLayout rootview;
        AdView mAdView;
        LinearLayout high_score_layout;
        RelativeLayout view_answers;


        public HistoryViewHolder(@NonNull View itemView) {

            super(itemView);
            session = itemView.findViewById(R.id.session);
            question = itemView.findViewById(R.id.question);
            accuracy = itemView.findViewById(R.id.accuracy);
            wrong = itemView.findViewById(R.id.wrong);
            //optionLayout = itemView.findViewById(R.id.optionLayout);
            //optionsList = itemView.findViewById(R.id.optionsList);
            dateLayout = itemView.findViewById(R.id.dateLayout);
            rootview = itemView.findViewById(R.id.rootview);
            mAdView = itemView.findViewById(R.id.adView);
            mAdView.setVisibility(View.GONE);
            high_score_layout = itemView.findViewById(R.id.high_score_layout);
            high_score = itemView.findViewById(R.id.high_score);
            session_high_score = itemView.findViewById(R.id.session_high_score);
            high_score_layout.setVisibility(View.GONE);
            view_answers = itemView.findViewById(R.id.view_answers);


        }
    }

    public void inflateOptions(JSONArray answers, HistoryViewHolder holder, String correctAnswer) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(context);


            for (int i = 0; i < answers.length(); i++) {
                String[] alpha_opts = {"A", "B", "C", "D", "E", "F"};
                JSONObject text = answers.getJSONObject(i);
                View view;
                view = layoutInflater.inflate(R.layout.options_layout, holder.optionLayout, false);
                RelativeLayout view_count = view.findViewById(R.id.view_cont);
                TextView alpha_opt = view.findViewById(R.id.alpha_opt);
                TextView opt = view.findViewById(R.id.opt);
                alpha_opt.setText(alpha_opts[i]);
                opt.setText(text.getString("text"));
                if (text.getString("text").equals(correctAnswer)) {
                    view_count.getLayoutParams().height = 100;
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                        view_count.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.hex_green));
                    } else {
                        view_count.setBackground(ContextCompat.getDrawable(context, R.drawable.hex_green));
                    }
//                ///view_count.setBackgroundColor(context.getResources().getColor(R.color.green));
                }

                holder.optionLayout.addView(view);


            }

        } catch (Exception e) {
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView myListView) {
        ListAdapter adapter = myListView.getAdapter();
        if (myListView != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View item = adapter.getView(i, null, myListView);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight + (myListView.getDividerHeight() * (adapter.getCount() - 1));
            myListView.setLayoutParams(params);
        }
    }


}
