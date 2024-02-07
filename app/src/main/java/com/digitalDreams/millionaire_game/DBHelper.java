package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageText;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {
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
    // public static final String QUESTION_IMAGE = "QUESTION_IMAGE";
    //  public static final String TITLE = "TITLE";
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
        StringBuilder sqlText = new StringBuilder();
        for (int a = 0; a < columnList.size(); a++) {
            String column = columnList.get(a);
            sqlText.append(column).append(" TEXT, ");
        }
        sqlText = new StringBuilder(sqlText.substring(0, sqlText.length() - 2));
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
            Cursor cursor = null;
            SQLiteDatabase db = null;

            try {
                db = getWritableDatabase();

                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String languageCode = sharedPreferences.getString("language", "");
                String language = getLanguageText(context, languageCode);

                String selectQuery = "SELECT * FROM " + JSON_TABLE + " WHERE LEVEL = ? AND  LANGUAGE = ? ORDER BY RANDOM() LIMIT 1";
                Log.i("query", selectQuery);

                cursor = db.rawQuery(selectQuery, new String[]{level, language});

                if (cursor != null && cursor.getCount() < 1) {
                    String selectQuery2 = "SELECT * FROM " + JSON_TABLE + " WHERE LANGUAGE = ? ORDER BY RANDOM() LIMIT 1";
                    cursor.close();
                    cursor = db.rawQuery(selectQuery2, new String[]{language});
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }

            return cursor;
        }
    }

    public Cursor getLevels() {
        synchronized (lock) {
            Cursor res = null;
            try {
                SQLiteDatabase db = this.getWritableDatabase();

                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String languageCode = sharedPreferences.getString("language", "");
                String language = getLanguageText(context, languageCode);

                //String selectQuery = "SELECT * FROM " + JSON_TABLE + " where LEVEL = "+level+" ORDER BY RANDOM() LIMIT 1";
                String selectQuery = "SELECT * FROM " + JSON_TABLE + " WHERE  LANGUAGE = ? ORDER BY  STAGE ASC";

                res = db.rawQuery(selectQuery, new String[]{language});

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }
    }


    public Cursor getQuestionByLevel(String level) {
        synchronized (lock) { // Added this line for a synchronizing access to the database///more below
            Cursor res1 = null;

            try {
                SQLiteDatabase db = this.getWritableDatabase();

                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
               /* String game_level = sharedPreferences.getString("game_level", "1");
                String current_play_level = sharedPreferences.getString("current_play_level", "1");*/


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

    public int getQuestionSize() {
        synchronized (lock) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            int count = 0;

            try {
                db = this.getWritableDatabase();
                String selectQuery = "SELECT * FROM " + JSON_TABLE;
                cursor = db.rawQuery(selectQuery, null);

                if (cursor != null && cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

                if (db != null && db.isOpen()) {
                    db.close();
                }
            }

            return count;
        }
    }

    public String buildJson() {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject questionObject = new JSONObject();
            JSONArray jsonArray1 = new JSONArray();

            for (int a = 1; a < 16; a++) {
                Cursor cursor = getQuestionByLevel2(String.valueOf(a));

                if (cursor != null) {
                    cursor.moveToNext();

                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ID));
                    @SuppressLint("Range") String language = cursor.getString(cursor.getColumnIndex(LANGUAGE));
                    @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex(QUESTION));
                    @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex(ANSWER));
                    @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(TYPE));
                    @SuppressLint("Range") String correct = cursor.getString(cursor.getColumnIndex(CORRECT));
                    @SuppressLint("Range") String reason = cursor.getString(cursor.getColumnIndex(REASON));

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
                    jsonArray1.put(contentObj);

                    cursor.close();
                }
            }

            questionObject.put("0", jsonArray1);
            jsonObject.put("q", questionObject);
            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonArray.toString();
    }

    public void saveHistory(
            String questionId,
            String answer,
            String correctAnswer,
            String reason,
            String session,
            String date_played,
            String high_score,
            boolean is_correct
    ) {
        synchronized (lock) {
            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("QUESTION_ID", questionId);
                contentValues.put("ANSWER", answer);
                contentValues.put("CORRECT_ANSWER", correctAnswer);
                contentValues.put("SESSION", session);
                contentValues.put("DATE_PLAYED", date_played);
                contentValues.put("HIGH_SCORE", high_score);
                contentValues.put("IS_CORRECT", is_correct);
                contentValues.put("REASON", reason);

                if (!checkHistory(db, questionId)) {
                    db.insert(HISTORY_TABLE, null, contentValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
    }


    public boolean checkHistory(SQLiteDatabase db, String questionId) {
        synchronized (lock) {
            Cursor cursor = null;
            boolean isQuestionExist = false;

            try {
                String sql = "SELECT * FROM " + HISTORY_TABLE + " WHERE QUESTION_ID = ?";
                cursor = db.rawQuery(sql, new String[]{questionId});

                isQuestionExist = cursor != null && cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return isQuestionExist;
        }
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
