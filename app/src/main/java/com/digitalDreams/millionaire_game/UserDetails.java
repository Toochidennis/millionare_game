package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.getCountryResource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digitalDreams.millionaire_game.alpha.AudioManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserDetails extends AppCompatActivity {
    EditText usernameEdt;
    RelativeLayout bg;
    RelativeLayout close_container;
    CardView avatarContainer1, avatarContainer2, avatarContainer3, avatarContainer4;
    GridLayout gridLayout;
    String username = "", avatar = "",
            country = "Afghanistan", flag = "", languageCode = "";
    //AutoCompleteTextView spinner;
    CountryAdapter countryAdapter;
    ArrayList<String> countries = new ArrayList<>();
    ArrayList<String> flags = new ArrayList<>();
    EditText country_name;
    Dialog dialog;
    CardView card;

    //private static boolean


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        close_container = findViewById(R.id.close_container);
        country_name = findViewById(R.id.country_name);


        bg = findViewById(R.id.rootview);
        card = findViewById(R.id.card);


        usernameEdt = findViewById(R.id.username);


        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        country = sharedPreferences.getString("country", "");
        flag = sharedPreferences.getString("country_flag", "");
        languageCode = sharedPreferences.getString("language", "");
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);

        if (username.equals(getResources().getString(R.string.anonymous_user))) {
            username = "";
        }

        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        bg.setBackground(gd);
        usernameEdt.requestFocus();
        usernameEdt.setText(username);
        Button continueBtn = findViewById(R.id.continueBtn);
        //spinner = findViewById(R.id.country);
        countryAdapter = new CountryAdapter(this, countries, flags);

        continueBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this,continueBtn);

            validateInput();
        });


        addCountries();

        gridLayout = findViewById(R.id.grid);

        for (int a = 0; a < gridLayout.getChildCount(); a++) {
            CardView card = (CardView) gridLayout.getChildAt(a);
            int b = a;
            card.setOnClickListener(view -> {
                avatar = String.valueOf(b + 1);
                selectAvatar(card);
            });
        }

        if (type != null && type.equals("edit")) {
            selectAvatar();
        }


        //////////////////////////
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countries);


        if (!country.equals("default")) {
            country_name.setText(country);
        }
        Log.i("Flag", country);
        Log.i("Flag", flag);


//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//            }
//        });


        //////////////////////////
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedItem = adapterView.getItemAtPosition(i).toString();
//                country = selectedItem;
//                flag = flags.get(i).toString();
//                //spinner.setText(country);
//                Log.i("Flag",flags.get(i).toString());
//                Log.i("Flag",country);
//
//
//
//            }
//
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                //adapterView.getItemAtPosition(5).toString();
//
//            }
//        });

        close_container.setOnClickListener(view -> {

            Utils.saveAnonymouseUser(UserDetails.this);
            navigate();

        });


        country_name.setOnClickListener(v -> {

            // Initialize dialog
            dialog = new Dialog(UserDetails.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // set custom height and width
            dialog.getWindow().setLayout(650, 800);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.hex_blue));
            //dialog.getWindow().se

            // show dialog


            // Initialize and assign variable

            dialog.show();


            EditText editText = dialog.findViewById(R.id.edit_text);
            ListView listView = dialog.findViewById(R.id.list_view);
            // editText.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
            dialog.setOnDismissListener(dialogInterface -> {


                editText.clearFocus();
                // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            });

            dialog.setOnShowListener(dialogInterface -> {
                editText.clearFocus();
                // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            });


            // Initialize array adapter
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UserDetails.this, R.layout.single_tv, countries);

            // set adapter
            listView.setAdapter(adapter1);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter1.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            listView.setOnItemClickListener((parent, view, position, id) -> {
                // when item selected from list
                // set selected item on textView
                country_name.setText(adapter1.getItem(position));

                String selectedItem = adapter1.getItem(position).toString();
                country = selectedItem;
                flag = flags.get(countries.indexOf(country)).toString();
                //spinner.setText(country);

                // Log.i("Flag",flags.get( countries.indexOf(country)).toString());
                // Log.i("Flag",selectedItem);
                //Log.i("Flag",countries.get(i).toString());


                editText.clearFocus();
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                // Dismiss dialog
                dialog.dismiss();


            });
        });

    }

    private void selectAvatar(CardView cardView) {
        for (int a = 0; a < gridLayout.getChildCount(); a++) {
            CardView card = (CardView) gridLayout.getChildAt(a);
            card.setCardBackgroundColor(getResources().getColor(R.color.color5));
        }
        cardView.setCardBackgroundColor(getResources().getColor(R.color.green));

    }

    private void selectAvatar() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String avatar = sharedPreferences.getString("avatar", "1");

        for (int a = 0; a < gridLayout.getChildCount(); a++) {
            if (a + 1 == Integer.parseInt(avatar)) {
                CardView card = (CardView) gridLayout.getChildAt(a);
                card.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
        }
    }

    private void validateInput() {
        username = usernameEdt.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(UserDetails.this, getResources().getString(R.string.player_name_can_t_be_empty), Toast.LENGTH_SHORT).show();
        } else if (country.equals("") || country.equals(getResources().getString(R.string.select_country)) || country.equals("default")) {
            Toast.makeText(UserDetails.this, getResources().getString(R.string.select_your_country), Toast.LENGTH_SHORT).show();

        } else {
            //Random = new Random()
            final int min = 1;
            final int max = 3;
            int random = new Random().nextInt((max - min) + 1) + min;
            avatar = String.valueOf(random);

            SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            ///editor.putString("avatar","1");
            editor.putString("country", country);
            editor.putString("country_flag", flag);
            editor.putString("avatar", avatar);
            // editor.putString("current_play_level","1");
            editor.putBoolean("isFirstTime", true);

            editor.apply();


            checkScore();

            navigate();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void addCountries() {

        try {
            String json = readRawTextFile(getCountryResource(languageCode));

            JSONArray jsonArray = new JSONArray(json);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject obj1 = jsonArray.getJSONObject(j);
                String name = obj1.getString("name");
                String flag = obj1.getString("image");
                countries.add(name);
                flags.add(flag);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String readRawTextFile(int resId) throws IOException {
        InputStream is = getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[10024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return writer.toString();
    }

    @Override
    public void onBackPressed() {

        navigate();
        super.onBackPressed();
    }

    public void checkScore() {
        String modeValue = "";
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String highscore = sharedPreferences.getString("high_score", "0");
        String username = sharedPreferences.getString("username", "");
        String country = sharedPreferences.getString("country", "");
        String country_flag = sharedPreferences.getString("country_flag", "");
        String oldAmountWon = sharedPreferences.getString("amountWon", "");
        String mode = sharedPreferences.getString("game_mode", "0");
        //TextView modeTxt = findViewById(R.id.mode);
        if (mode.equals("0")) {
            //  modeTxt.setText("Mode: Normal");
            modeValue = "normal";
        } else {
            // modeTxt.setText("Mode: Hard");
            modeValue = "hard";
        }

        Map userDetails = new HashMap();
        userDetails.put("username", username);
        userDetails.put("country", country);
        userDetails.put("country_flag", country_flag);

        Utils.sendScoreToSever(UserDetails.this, highscore, userDetails, modeValue);
    }


    public void navigate() {

        Intent i;

        if (Utils.IS_DONE_INSERTING) {
            i = new Intent(UserDetails.this, Utils.destination_activity);

        } else {
            i = new Intent(UserDetails.this, WelcomeActivity.class);

        }

        startActivity(i);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManager.releaseMusicResources();
    }
}