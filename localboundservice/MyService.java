package com.example.asare.localboundservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Binder;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


import android.net.http.AndroidHttpClient;


import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyService extends Service {

    private final IBinder MyBinder = new MyLocalBinder();

    public static List<weatherList> weatherList;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        new HttpGetTask().execute();
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return MyBinder;
    }


    public static List<weatherList> getWeather()
    {
        return weatherList;
    }


    public class MyLocalBinder extends Binder{

        MyService getService(){
            return MyService.this;
        }
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<weatherList>> {

        private static final String URL = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<weatherList> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<weatherList> result) {
            if (null != mClient)
                mClient.close();


            weatherList = result;

            //setListAdapter(
              /*
                    mArrayAdapter = new ArrayAdapter<String>(
                    MyService.this,
                    R.layout.list_item, result);
                    */
        }

    }

    private class JSONResponseHandler implements ResponseHandler<List<weatherList>> {

        private static final String WEATHER_TAG = "weather";
        private static final String ID_TAG = "id";
        private static final String DESCRIPTION_TAG = "description";

        @Override
        public List<weatherList> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<weatherList> result = new ArrayList<>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                JSONArray weathers = responseObject.getJSONArray(WEATHER_TAG);


                for (int i = 0; i < weathers.length(); i++) {

                    JSONObject weather = (JSONObject) weathers.get(i);

                    String weatherID = Integer.toString(weather.getInt(ID_TAG));
                    String desc = weather.getString(DESCRIPTION_TAG);

                    weatherList obj = new weatherList(weatherID, desc);

                    result.add(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;

        }
    }
}
