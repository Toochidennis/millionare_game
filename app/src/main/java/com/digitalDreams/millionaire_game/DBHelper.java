package com.digitalDreams.millionaire_game;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

class DBHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "trivia.db";
    //SQLiteDatabase db;
    public static String JSON_TABLE = "json_table";
    public static String HISTORY_TABLE = "history_table";
    public static String ID = "ID";
    public static String QUESTION = "QUESTION";
    public static final String ANSWER = "ANSWER";
    public static final String TYPE = "TYPE";
    public static final String CORRECT = "CORRECT";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String QUESTION_IMAGE = "QUESTION_IMAGE";
    public static final String TITLE = "TITLE";
    public static final String LEVEL = "LEVEL";
    public static final String STAGE_NAME = "STAGE_NAME";
    public static final String STAGE = "STAGE";
    public static final String REASON = "REASON";
    Context context;

    private static final Object lock = new Object();

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 7);
        //db = getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + JSON_TABLE + " (ID TEXT PRIMARY KEY, QUESTION TEXT,ANSWER TEXT, TYPE TEXT,CORRECT TEXT,QUESTION_IMAGE TEXT, TITLE TEXT, LEVEL TEXT,LANGUAGE TEXT,STAGE_NAME TEXT,STAGE TEXT,REASON TEXT)");
        sqLiteDatabase.execSQL("create table " + HISTORY_TABLE + " (ID TEXT PRIMARY KEY, QUESTION_ID TEXT,ANSWER TEXT,CORRECT_ANSWER TEXT,HIGH_SCORE TEXT, SESSION TEXT,DATE_PLAYED TEXT, IS_CORRECT BOOLEN,REASON TEXT)");

        //createTable(MainActivity.columnList,sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JSON_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        sqLiteDatabase.execSQL("create table " + JSON_TABLE + " (ID TEXT PRIMARY KEY, QUESTION TEXT,ANSWER TEXT, TYPE TEXT,CORRECT TEXT,QUESTION_IMAGE TEXT, TITLE TEXT, LEVEL TEXT,LANGUAGE TEXT,STAGE_NAME TEXT,STAGE TEXT,REASON TEXT)");
        sqLiteDatabase.execSQL("create table " + HISTORY_TABLE + " (ID TEXT PRIMARY KEY, QUESTION_ID TEXT,ANSWER TEXT,CORRECT_ANSWER TEXT,HIGH_SCORE TEXT ,SESSION TEXT,DATE_PLAYED TEXT, IS_CORRECT BOOLEN, REASON TEXT)");

    }


    public void createTable(List<String> columnList, SQLiteDatabase db) {
        String sqlText = "";
        for (int a = 0; a < columnList.size(); a++) {
            String column = columnList.get(a);
            sqlText += column + " TEXT, ";
        }
        sqlText = sqlText.substring(0, sqlText.length() - 2);
        String CREATE_TABLE_SQL = "CREATE TABLE " + JSON_TABLE + " ( sqlText )";
        db.execSQL(CREATE_TABLE_SQL);
    }

    public void insertDetails(List<String> column, List<String> data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int a = 0; a < data.size(); a++) {
            for (int b = 0; b < data.size(); b++) {
                contentValues.put(column.get(b), data.get(a));
            }
        }
        long result = db.insert(JSON_TABLE, null, contentValues);

    }

    public void insertDetails(String language, String level, String id,
                              String content, String type,
                              String answer, String correct,
                              String stage_name, String stage, String reason) {
        synchronized (lock) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(LANGUAGE, language);
                contentValues.put(LEVEL, level);
                contentValues.put(ID, id);
                contentValues.put(QUESTION, content);
                contentValues.put(TYPE, type);
                contentValues.put(ANSWER, answer);
                contentValues.put(CORRECT, correct);
                contentValues.put(STAGE_NAME, stage_name);
                contentValues.put(STAGE, stage);
                contentValues.put(REASON, reason);
                long result = db.insert(JSON_TABLE, null, contentValues);
                Log.i("contentValues", String.valueOf(contentValues));
                Log.i("response", "result " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Cursor getQuestionByLevel2(String level) {
        synchronized (lock) {
            Cursor res = null;
            try {
                SQLiteDatabase db = getWritableDatabase();
                // this line was changed
              /*  if (db.isOpen()) {
                    //db.close();
                    db = getWritableDatabase();

                }*/
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String game_level = sharedPreferences.getString("game_level", "1");
                String current_play_level = sharedPreferences.getString("current_play_level", "1");
                String languageCode = sharedPreferences.getString("language", "");
                String language;

                switch (languageCode) {
                    case "fr":
                        language = context.getString(R.string.french);
                        break;
                    case "es":
                        language = context.getString(R.string.spanish);
                        break;

                    default:
                        language = context.getString(R.string.english);
                        break;
                }

                Log.i("current_play_level", current_play_level + " Language " + language);


                String selectQuery = "SELECT * FROM " + JSON_TABLE + " WHERE LEVEL = '" + level + "' AND " + " LANGUAGE = '" + language + "' ORDER BY RANDOM() LIMIT 1";
                //String selectQuery = "SELECT * FROM " + JSON_TABLE + " where LEVEL = "+level+" and STAGE = " +current_play_level+ " ORDER BY RANDOM() LIMIT 1";

                Log.i("99999999", selectQuery);
                res = db.rawQuery(selectQuery, null);
//        Cursor res1 = res;
//      while (res1.moveToNext()){
//          String id = res1.getString(res.getColumnIndex("ID"));
//          Log.i("99999999",id);
//
//      }
                if (res.getCount() < 1) {
                    String selectQuery2 = "SELECT * FROM " + JSON_TABLE + " WHERE LANGUAGE = '" + language + "' ORDER BY RANDOM() LIMIT 1";
                    //where  STAGE = " +current_play_level+

                    Cursor res2 = db.rawQuery(selectQuery2, null);
                    /// db.close(); ///new removal
                    return res2;

                }

                db.close(); ///new addition

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }
    }

    public Cursor getLevels() {
        synchronized (lock) {
            Cursor res = null;
            try {
                SQLiteDatabase db = this.getWritableDatabase();

                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String languageCode = sharedPreferences.getString("language", "");
                String language;

                switch (languageCode) {
                    case "fr":
                        language = context.getString(R.string.french);
                        break;
                    case "es":
                        language = context.getString(R.string.spanish);
                        break;

                    default:
                        language = context.getString(R.string.english);
                        break;
                }

                //String selectQuery = "SELECT * FROM " + JSON_TABLE + " where LEVEL = "+level+" ORDER BY RANDOM() LIMIT 1";
                String selectQuery = "SELECT * FROM " + JSON_TABLE + " WHERE  LANGUAGE = '" + language + "' ORDER BY  STAGE ASC";

                res = db.rawQuery(selectQuery, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }
    }


    public Cursor getQuestionByLevel(String level) {
        synchronized (lock) { // Added this line for a synchronizing access to the database///more below
            // Log.i("uuuuuuu",level);

            Cursor res1 = null;
            try {
                SQLiteDatabase db = this.getWritableDatabase();

                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String game_level = sharedPreferences.getString("game_level", "1");
                String current_play_level = sharedPreferences.getString("current_play_level", "1");


                String selectQuery = "SELECT * FROM " + JSON_TABLE + " where LEVEL = " + level;
                Cursor res = db.rawQuery(selectQuery, null);


                int count = res.getCount();
                int randomNumber;
                if (count <= 0) {
                    randomNumber = new Random().nextInt(30);
                } else {
                    randomNumber = new Random().nextInt(count);
                }

                String selectQuery1 = "SELECT * FROM " + JSON_TABLE + " where LEVEL = " + level + " ORDER BY ID LIMIT " + randomNumber + ",1";

                //String selectQuery1 = "SELECT * FROM " + JSON_TABLE + " where LEVEL = "+level+" ORDER BY ID LIMIT "+randomNumber+",1";
                res1 = db.rawQuery(selectQuery1, null);


                Log.d("query", selectQuery1);

                db.close(); // new addition

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res1;
        }
    }

    public Cursor allQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + JSON_TABLE;
        Cursor res = db.rawQuery(selectQuery, null);
        db.close();
        return res;
    }


    public int getQuestionSize() {
        synchronized (lock) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                String selectQuery = "SELECT * FROM " + JSON_TABLE;
                Cursor res = db.rawQuery(selectQuery, null);

                return res.getCount();

            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public String buildJson() {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject qObj = new JSONObject();
            JSONArray arr = new JSONArray();

            for (int a = 1; a < 16; a++) {
                Cursor res = getQuestionByLevel2(String.valueOf(a));

                if (res != null) {
                    res.moveToNext();

                    @SuppressLint("Range") String id = res.getString(res.getColumnIndex("ID"));
                    @SuppressLint("Range") String language = res.getString(res.getColumnIndex("LANGUAGE"));
                    @SuppressLint("Range") String question = res.getString(res.getColumnIndex("QUESTION"));
                    @SuppressLint("Range") String answer = res.getString(res.getColumnIndex("ANSWER"));
                    @SuppressLint("Range") String type = res.getString(res.getColumnIndex("TYPE"));
                    @SuppressLint("Range") String correct = res.getString(res.getColumnIndex("CORRECT"));
                    @SuppressLint("Range") String reason = res.getString(res.getColumnIndex("REASON"));

                    Log.d("reason", reason + " " + answer + " " + language);

                    JSONObject contentObj = new JSONObject();
                    contentObj.put("id", id);
                    contentObj.put("parent", "0");
                    contentObj.put("content", question);
                    contentObj.put("title", "");
                    contentObj.put("type", type);
                    contentObj.put("answer", answer);
                    contentObj.put("correct", correct);
                    contentObj.put("question_image", "");
                    contentObj.put("reason", reason);
                    arr.put(contentObj);

                    res.close();
                }

            }
            qObj.put("0", arr);
            jsonObject.put("q", qObj);
            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public void saveHistory(String questionId, String answer,
                            String correctAnswer,
                            String session,
                            String date_played, String high_score, boolean is_correct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUESTION_ID", questionId);
        contentValues.put("ANSWER", answer);
        contentValues.put("CORRECT_ANSWER", correctAnswer);
        contentValues.put("SESSION", session);
        contentValues.put("DATE_PLAYED", date_played);
        contentValues.put("HIGH_SCORE", high_score);
        contentValues.put("IS_CORRECT", is_correct);
        contentValues.put("REASON", getReason(questionId));


        boolean questionExists = checkHistory(questionId);

        if (!questionExists) {
            db.insert(HISTORY_TABLE, null, contentValues);

        }

    }

    @SuppressLint("Range")
    public String getReason(String questionId) {
        String reason = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + JSON_TABLE + " WHERE ID = " + questionId;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            reason = cursor.getString(cursor.getColumnIndex("REASON"));

        }

        return reason;
    }

    public boolean checkHistory(String questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + HISTORY_TABLE + " WHERE QUESTION_ID = '" + questionId + "'";

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount() > 0;

    }

    public Cursor getHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + HISTORY_TABLE + " ORDER BY ID DESC";
        return db.rawQuery(sql, null);

    }

    public Cursor getHistoryByDate(String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT  * FROM " + HISTORY_TABLE + " WHERE DATE_PLAYED = '" + dateTime + "' GROUP BY QUESTION_ID  ORDER BY ID DESC";
        return db.rawQuery(sql, null);


    }

    public JSONArray buildHistoriesByDateTime(String dateTime) {
        JSONArray jsonArray = new JSONArray();


        try {

            Cursor histories = getHistoryByDate(dateTime);
            while (histories.moveToNext()) {


                @SuppressLint("Range") String questionId = histories.getString(histories.getColumnIndex("QUESTION_ID"));
                @SuppressLint("Range") String answered = histories.getString(histories.getColumnIndex("ANSWER"));
                @SuppressLint("Range") String DATE_PLAYED = histories.getString(histories.getColumnIndex("DATE_PLAYED"));
                @SuppressLint("Range") String HIGH_SCORE = histories.getString(histories.getColumnIndex("HIGH_SCORE"));
                @SuppressLint("Range") String REASON = histories.getString(histories.getColumnIndex("REASON"));


                JSONObject object = getQuestionById(questionId);
                object.put("answered", answered);
                object.put("date_played", DATE_PLAYED);
                object.put("high_score", HIGH_SCORE);
                object.put("reason", REASON);
                Log.i("checkinglol1", String.valueOf(questionId));
                Log.i("checkinglol", String.valueOf(object));
                jsonArray.put(object);


            }

        } catch (Exception e) {
            Log.i("checkingjj", String.valueOf(e));
            e.printStackTrace();

        }
        Log.i("checkingjj", String.valueOf(jsonArray));

        return jsonArray;


    }

    public JSONArray buildHistories() {
        JSONArray jsonArray = new JSONArray();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String date = "";


            Cursor histories = getHistory();
            while (histories.moveToNext()) {


                @SuppressLint("Range") String questionId = histories.getString(histories.getColumnIndex("QUESTION_ID"));
                @SuppressLint("Range") String answered = histories.getString(histories.getColumnIndex("ANSWER"));
                @SuppressLint("Range") String date_played = histories.getString(histories.getColumnIndex("DATE_PLAYED"));
                @SuppressLint("Range") String HIGH_SCORE = histories.getString(histories.getColumnIndex("HIGH_SCORE"));
                @SuppressLint("Range") String IS_CORRECT = histories.getString(histories.getColumnIndex("IS_CORRECT"));

//               Log.i("serious",questionId);
//               Log.i("serious",IS_CORRECT);
//               Log.i("serious",date_played);


                if (!date.equals(date_played)) {
                    // Log.i("serious",IS_CORRECT);
                    String sql1 = "SELECT * FROM " + HISTORY_TABLE + " WHERE DATE_PLAYED = '" + date_played + "' AND IS_CORRECT = '" + 1 + "'";
                    String sql2 = "SELECT * FROM " + HISTORY_TABLE + " WHERE DATE_PLAYED = '" + date_played + "' AND IS_CORRECT = '" + 0 + "'";
                    Cursor correctAnswers = db.rawQuery(sql1, null);
                    Cursor incorrectAnswers = db.rawQuery(sql2, null);

                    JSONObject object = new JSONObject();
                    object.put("answered", answered);
                    object.put("date_played", date_played);
                    object.put("high_score", HIGH_SCORE);
                    object.put("correct_answers", correctAnswers.getCount());
                    object.put("wrong_answers", incorrectAnswers.getCount());
                    object.put("is_correct", IS_CORRECT);

//               Log.i("checking", String.valueOf(questionId));
//              Log.i("checking", String.valueOf(object));
                    jsonArray.put(object);
                    date = date_played;


                }


                Log.i("checkingjj", String.valueOf(jsonArray));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }

    public JSONObject getQuestionById(String questionId) {
        JSONObject contentObj = new JSONObject();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "SELECT * FROM " + JSON_TABLE + " WHERE ID = " + questionId;
            Cursor res = db.rawQuery(sql, null);

            if (res.moveToNext()) {
                @SuppressLint("Range") String id = res.getString(res.getColumnIndex("ID"));
                @SuppressLint("Range") String language = res.getString(res.getColumnIndex("LANGUAGE"));
                @SuppressLint("Range") String question = res.getString(res.getColumnIndex("QUESTION"));
                @SuppressLint("Range") String answer = res.getString(res.getColumnIndex("ANSWER"));
                @SuppressLint("Range") String type = res.getString(res.getColumnIndex("TYPE"));
                @SuppressLint("Range") String correct = res.getString(res.getColumnIndex("CORRECT"));


                contentObj.put("id", id);
                contentObj.put("parent", "0");
                contentObj.put("content", question);
                contentObj.put("title", "");
                contentObj.put("type", type);
                contentObj.put("answer", answer);
                contentObj.put("correct", correct);
                contentObj.put("question_image", "");
            }
        } catch (Exception e) {
            Log.i("checking", "pppp2");
            e.printStackTrace();
        }

        return contentObj;

    }


    public ArrayList buildHistories2() {
        JSONObject jsonObject = new JSONObject();

        ArrayList keys = new ArrayList();


        try {

            Cursor histories = getHistory();
            while (histories.moveToNext()) {


                @SuppressLint("Range") String questionId = histories.getString(histories.getColumnIndex("QUESTION_ID"));
                @SuppressLint("Range") String answered = histories.getString(histories.getColumnIndex("ANSWER"));
                @SuppressLint("Range") String DATE_PLAYED = histories.getString(histories.getColumnIndex("DATE_PLAYED"));


                JSONObject object = getQuestionById(questionId);
                object.put("answered", answered);
                object.put("date_played", DATE_PLAYED);
//               Log.i("checking", String.valueOf(questionId));
//              Log.i("checking", String.valueOf(object));
                if (jsonObject.has(DATE_PLAYED)) {
                    jsonObject.getJSONArray(DATE_PLAYED).put(object);

                } else {
                    jsonObject.put(DATE_PLAYED, new JSONArray());
                    jsonObject.getJSONArray(DATE_PLAYED).put(object);


                }
                if (!keys.contains(DATE_PLAYED)) {
                    keys.add(DATE_PLAYED);
                }
                // jsonArray.put(object);


            }

        } catch (Exception e) {
            //Log.i("checking", "pppp1");
            e.printStackTrace();

        }


        ArrayList arrayList = new ArrayList();
        arrayList.add(keys);
        arrayList.add(jsonObject);

        Log.i("checking555", String.valueOf(arrayList));


        return arrayList;


    }

}
