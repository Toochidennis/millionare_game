package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class HistoryOptionAdapter extends ArrayAdapter {
    Context context;
    JSONArray options;
    String correctAnswer;
    public HistoryOptionAdapter(@NonNull Context context, JSONArray options, String correctAnswer) {
        super(context, 0);
        this.context = context;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int getCount() {

        return options.length();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.options_layout, parent, false);
        }



      try{
          String[] alpha_opts = {"A", "B", "C", "D", "E", "F"};
          //Log.i("checking", String.valueOf(options));
          JSONObject text = options.getJSONObject(position);

          RelativeLayout view_count = convertView.findViewById(R.id.view_cont);
          TextView alpha_opt = convertView.findViewById(R.id.alpha_opt);
          TextView opt = convertView.findViewById(R.id.opt);
          alpha_opt.setText(alpha_opts[position]);
          opt.setText(Utils.capitalizeFirstWord(text.getString("text")));
          if (text.getString("text").trim().equals(correctAnswer.trim())) {
//              Log.i("checking",text.getString("text"));
              view_count.getLayoutParams().height = 100;
              final int sdk = android.os.Build.VERSION.SDK_INT;
              if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                  view_count.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.hex_green2));
              } else {
                  view_count.setBackground(ContextCompat.getDrawable(context, R.drawable.hex_green2));
              }
//                ///view_count.setBackgroundColor(context.getResources().getColor(R.color.green));
          }


      }catch (Exception e){}




        return  convertView;

    }
}
