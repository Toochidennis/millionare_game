package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Loader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            obj.put("course_id", "4");
            obj.put("course_name", "Government");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(obj);
        getJson(jsonArray.toString());
    }

    private void getJson(String t){
        String url= "http://www.cbtportal.linkskool.com/api/question_json.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response","response "+response);
                Intent intent = new Intent(Loader.this,GameActivity2.class);
                intent.putExtra("Json",response);
                intent.putExtra("from","assessment");
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String,String> params = new HashMap<>();
                params.put("id","6");
                params.put("courses",t);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}