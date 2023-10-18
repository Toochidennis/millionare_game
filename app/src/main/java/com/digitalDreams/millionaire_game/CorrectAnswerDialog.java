package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CorrectAnswerDialog extends BottomSheetDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    //    public CorrectAnswerDialog(@NonNull Context context) {
//        super(context);
//
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.correct_answer_dialog);
//        close_dialog = findViewById(R.id.close_dialog);
//        setCancelable(false);
//
//
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.correct_answer_dialog,
                container, false);
        RelativeLayout   close_dialog = v.findViewById(R.id.close_dialog);
        ImageView close =  v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("render2","dddd");
                dismiss();

            }
        });
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("render2","dddd");
                dismiss();

            }
        });

        return  v;
    }
}
