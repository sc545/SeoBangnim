package com.example.k.seobangnim;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by K on 2016-09-05.
 */
public class SearchAsyncTask extends AsyncTask<String, Void, ArrayList> {
    private static final String TAG = SearchAsyncTask.class.getSimpleName();
    private static final int TYPE_XML = 0;
    private static final int TYPE_JSON = 1;
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_POINT = "point";

    MapActivity mActivity;
    MapActivity.InvalidateListener mInvalidateListener;
    ProgressDialog mProgressDialog;

    public SearchAsyncTask(MapActivity actvity, MapActivity.InvalidateListener invalidateListener) {
        mActivity = actvity;
        mInvalidateListener = invalidateListener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("잠시만 기다려주세요...");
        mProgressDialog.show();
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        // list for search data
        ArrayList<HashMap<String, String >> hashMapArrayList = new ArrayList<>();

        // for search in current location
        String currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            log("doInBackground : currentLocation is null");
            currentLocation = "";
        }
        try {
            String search = currentLocation+" "+params[0];
            log("doInBackground : search = "+search);
            String query = URLEncoder.encode(search, "utf-8");
            String strUrl = "https://openapi.naver.com/v1/search/local.xml?query=" + query + "&display=10&start=10";

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(getInputStream(strUrl, TYPE_XML), null);
//            String query = URLEncoder.encode(params[0], "UTF-8");
//            String strUrl = "http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f&query=" + query + "&target=local&start=1&display=10";
//
//            URL url = new URL(queryUrl);
//            InputStream is = url.openStream();
//
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = factory.newPullParser();
//            parser.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            parser.next();

            int eventType = parser.getEventType();
            boolean startFlag = false;


            HashMap<String, String> map = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag= parser.getName();    //테그 이름 얻어오기

                        if(tag.equals("item")) {
                            startFlag = true;
                            map = new HashMap<>();
                        }
                        if (startFlag) {
                            if (tag.equals("title")) {
                                parser.next();
                                map.put("title", parser.getText());
                            } else if (tag.equals("category")) {
                                parser.next();
                                map.put("category", parser.getText());
                            } else if (tag.equals("description")) {
                                parser.next();
                                map.put("description", parser.getText());
                            } else if (tag.equals("telephone")) {
                                parser.next();
                                map.put("telephone", parser.getText());
                            } else if (tag.equals("address")) {
                                parser.next();
                                map.put("address", parser.getText());
                                log("doInBackground : address = "+parser.getText());

                                query = URLEncoder.encode(parser.getText(), "utf-8");
                                strUrl = "https://openapi.naver.com/v1/map/geocode?encoding=utf-8&coord=latlng&output=json&query="+query;

                                JSONObject json = getJson(strUrl, KEY_POINT);
                                if (json != null) {
                                    String mapx = json.getString("x");
                                    String mapy = json.getString("y");
                                    log("doInBackground : mapx = "+mapx+", mapy = "+mapy);
                                    map.put("mapx", mapx);
                                    map.put("mapy", mapy);
                                } else {
                                    log("doInBackground : json is null");
                                }

                            } else if (tag.equals("mapx")) {
//                                parser.next();
//                                map.put("mapx", parser.getText());
                            } else if (tag.equals("mapy")) {
//                                parser.next();
//                                map.put("mapy", parser.getText());
                            }
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName();    //테그 이름 얻어오기

                        if(tag.equals("item")) {
                            startFlag = false;
                            hashMapArrayList.add(map);
                            map = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                eventType= parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return hashMapArrayList;
    }

    private JSONObject getJson(String strUrl, String key) {
        try {
            InputStream is = getInputStream(strUrl, TYPE_JSON);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            int bufferSize = 1024 * 1024;
            char[] readBuf = new char[bufferSize];
            int resultSize = 0;
            while ((resultSize = br.read(readBuf)) != -1) {
                if (resultSize == bufferSize) {
                    sb.append(readBuf);
                } else {
                    for (int i = 0; i < resultSize; i++) {
                        sb.append(readBuf[i]);
                    }
                }
            }
            String str = sb.toString();
//            log("str : " + str);
            JSONObject json = new JSONObject(str);
            JSONArray jarr = json.getJSONObject("result").getJSONArray("items");
            if (key.equals(KEY_ADDRESS))
                json = jarr.getJSONObject(0);
            else if (key.equals(KEY_POINT))
                json = jarr.getJSONObject(0).getJSONObject(KEY_POINT);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getInputStream(String strUrl, int type) {
        InputStream is;
        try {
            log("getInputStream : requestUrl = "+strUrl);
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", MapActivity.CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret", MapActivity.CLIENT_SECRET);
            if (type == TYPE_XML)
                connection.setRequestProperty("Content-Type", "application/xml");
            else if (type == TYPE_JSON)
                connection.setRequestProperty("Content-Type", "application/json");

            int status = connection.getResponseCode();
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return is;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        mProgressDialog.dismiss();
        if (arrayList == null) {
            Toast.makeText(mActivity, "Sorry Can't search now", Toast.LENGTH_SHORT).show();
            return;
        }
        mInvalidateListener.invalidate(arrayList);
    }

    private static void log(String str) {
        Log.d(TAG, str);
    }

    private String getCurrentLocation() {
        try {
            String query = mActivity.getmCurrentLatitude() + "," + mActivity.getmCurrentLongitude();
            String strUrl = "https://openapi.naver.com/v1/map/reversegeocode?encoding=utf-8&coord=latlng&output=json&query=" + query;

            JSONObject json = getJson(strUrl, KEY_ADDRESS);
            String currentLocation = "";
            if (json != null)
                currentLocation = json.getString(KEY_ADDRESS);
            else
                log("getCurrentLocation : json is null");
            return currentLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
