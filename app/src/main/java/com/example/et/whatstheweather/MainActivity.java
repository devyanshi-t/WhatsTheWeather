package com.example.et.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
   String name,des,main,n;
    TextView textView;
    EditText editText;
    Button b;




    public class Download extends AsyncTask<String,Void,String>
    {


        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();
                while (data != -1)
                {
                    char ch=(char)data;
                    result+=ch;
                    data=reader.read();
                }
                return result;

            } catch (Exception e)
            {e.printStackTrace();}
            return "FAILED";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                String main = null;
                String description = null;
                Double temp=null;
                int humidity=0;
                String name = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String weatherinfo = jsonObject.getString("weather");
                    String tempinfo = jsonObject.getString("main");
                    tempinfo="["+tempinfo+"]";
                    String visibility=jsonObject.getString("visibility");
                    name = jsonObject.getString("name");
                    String msg = null;
                    Log.i("hello", name);
                    Log.i("hello",tempinfo);

                    double tempc;
                    JSONArray arr = new JSONArray(weatherinfo);
                   JSONArray arrtemp = new JSONArray(tempinfo);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject part = arr.getJSONObject(i);
                        main = part.getString("main");
                        description = part.getString("description");
                        msg = main + "\r\n" + description;

                    }
                   for (int i = 0; i < arrtemp.length(); i++) {
                        JSONObject part = arrtemp.getJSONObject(i);
                        temp = part.getDouble("temp");
                       temp=((temp-273.15));
                       DecimalFormat format = new DecimalFormat("##.00");
                       format.format(temp);
                        humidity= part.getInt("humidity");

                        msg =msg+"\n"+ "humidity is : " +humidity+ "\r\n" +"temperature is : " + format.format(temp)+"C"+"\n"+"Visibility:"+visibility;

                    }
                    if (description!=null) {
                        textView.setText(msg);
                        textView.setVisibility(View.VISIBLE);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could not fetch weather info ", Toast.LENGTH_LONG).show();
                        textView.setVisibility(View.INVISIBLE);
                    }


                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Could not fetch weather info ", Toast.LENGTH_LONG).show();
                    textView.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }}
                else
                {Toast.makeText(getApplicationContext(),"Enter a valid city",Toast.LENGTH_LONG).show();
                    textView.setVisibility(View.INVISIBLE);

            }


        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView);
        b=(Button)findViewById(R.id.button);


    }
    public void onClick(View view) {

        String n1 = editText.getText().toString();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);


        try {
            n = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            Download obj = new Download();
            obj.execute("http://api.openweathermap.org/data/2.5/weather?q=" + n + "&APPID=ea574594b9d36ab688642d5fbeab847e");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Enter a valid country",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }



    }

    }


