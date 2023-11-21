package com.digitalDreams.millionaire_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class UserDetails extends AppCompatActivity {
    EditText usernameEdt;
    RelativeLayout bg;
    RelativeLayout close_container;
    CardView avatarContainer1, avatarContainer2, avatarContainer3, avatarContainer4;
    GridLayout gridLayout;
    String username = "", avatar = "",
            country = "Afghanistan", flag = "";
    //AutoCompleteTextView spinner;
    CountryAdapter countryAdapter;
    ArrayList countries = new ArrayList();
    ArrayList flags = new ArrayList();
    EditText country_name;
    Dialog dialog;
    CardView card;

    //private static boolean


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        addCountries();


        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
//        getSupportActionBar().setTitle("");
        close_container = findViewById(R.id.close_container);
        country_name = findViewById(R.id.country_name);


        bg = findViewById(R.id.rootview);
        card = findViewById(R.id.card);


        usernameEdt = findViewById(R.id.username);


        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        country = sharedPreferences.getString("country", "");
        flag = sharedPreferences.getString("country_flag", "");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        if (username.equals(getResources().getString(R.string.anonymous_user))) {
            username = "";
        }
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);
        usernameEdt.requestFocus();
        usernameEdt.setText(username);
        Button continueBtn = findViewById(R.id.continueBtn);
        //spinner = findViewById(R.id.country);
        countryAdapter = new CountryAdapter(this, countries, flags);

        continueBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(continueBtn, getApplicationContext());

            validateInput();
        });


        gridLayout = findViewById(R.id.grid);
        for (int a = 0; a < gridLayout.getChildCount(); a++) {
            CardView card = (CardView) gridLayout.getChildAt(a);
            int b = a;
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avatar = String.valueOf(b + 1);
                    selectAvatar(card);
                }
            });
        }
        if (type != null && type.equals("edit")) {
            selectAvatar();
        }


        //////////////////////////
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countries);


        //   ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,countries);
        //spinner.setAdapter(adapter);
        // int spinnerPosition = countryAdapter.getPosition(country);
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
            naviget();

        });


        country_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserDetails.this, R.layout.single_tv, countries);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                listView.setOnItemClickListener((parent, view, position, id) -> {
                    // when item selected from list
                    // set selected item on textView
                    country_name.setText(adapter.getItem(position));

                    String selectedItem = adapter.getItem(position).toString();
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
            }
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
            Toast.makeText(UserDetails.this, "Player name can't be empty", Toast.LENGTH_SHORT).show();
        } else if (country.equals("") || country.equals("Select country") || country.equals("default")) {
            Toast.makeText(UserDetails.this, "Select your country", Toast.LENGTH_SHORT).show();


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

            naviget();

        }
    }


    @Override
    protected void onResume() {


        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public void addCountries() {
        // Log.i("llllll","flag");
        try {
            String json = readRawTextFile(R.raw.country_json);
            JSONArray jsonArray = new JSONArray(json);
            //Iterator<String> iterator = jsonObject.keys();

            //Log.i("llllll","flag");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject obj1 = jsonArray.getJSONObject(j);
                String name = obj1.getString("name");
                String flag = obj1.getString("image");
                countries.add(name);
                flags.add(flag);
            }

//            while (iterator.hasNext()){
//                String key = iterator.next();
//                JSONObject obj1 = jsonObject.getJSONObject(key);
//                String name = obj1.getString("name");
//                String flag = obj1.getString("image");
//                countries.add(name);
//                flags.add(flag);
//
//                Log.i("llllll",flag);
//
//               /// countryAdapter.notifyDataSetChanged();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String[] countries4ii = new String[]{"Select country", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas",
            "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
            "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam",
            "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China, People's republic of", "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
            "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
            "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
            "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
            "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
            "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
            "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
            "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
            "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
            "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kosovo", "Kuwait",
            "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar",
            "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
            "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat",
            "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
            "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands",
            "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Palestine", "Peru", "Philippines", "Pitcairn",
            "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda",
            "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
            "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore",
            "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
            "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon",
            "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
            "Taiwan", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Tibet", "Togo", "Tokelau", "Tonga",
            "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
            "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay",
            "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)",
            "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};


    private String readRawTextFile(int resId) throws IOException {
        InputStream is = getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[10024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

    @Override
    public void onBackPressed() {


        naviget();
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


    public void naviget() {

        Intent i;

        if (Utils.IS_DONE_INSERTING) {
            i = new Intent(UserDetails.this, Utils.destination_activity);

        } else {
            i = new Intent(UserDetails.this, WelcomeActivity.class);

        }

        startActivity(i);
        finish();

//        Intent intent;
//
//        intent = new Intent(UserDetails.this, Utils.destination_activity);
//
//        startActivity(intent);
//        finish();
//        Intent i =  new Intent(UserDetails.this,Dashboard.class);
//        startActivity(i);


    }


    //@SuppressLint("ResourceAsColor")
    // @SuppressLint("ResourceAsColor")

}