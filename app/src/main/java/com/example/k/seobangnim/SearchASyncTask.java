package com.example.k.seobangnim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mutecsoft on 2016-09-05.
 */
public class SearchAsyncTask extends AsyncTask<String, Void, ArrayList> {
    private static final String TAG = SearchAsyncTask.class.getSimpleName();

    Context mContext;
    MapActivity.InvalidateListener mInvalidateListener;
    ProgressDialog mProgressDialog;

    public SearchAsyncTask(Context context, MapActivity.InvalidateListener invalidateListener) {
        mContext = context;
        mInvalidateListener = invalidateListener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("잠시만 기다려주세요...");
        mProgressDialog.show();
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        ArrayList<HashMap<String, String >> hashMapArrayList = new ArrayList<>();

        try {
            String query = URLEncoder.encode(params[0], "utf-8");
            String strUrl = "https://openapi.naver.com/v1/search/local.xml?query=" + query + "&display=10&start=1";
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", MapActivity.CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret", MapActivity.CLIENT_SECRET);
            connection.setRequestProperty("Content-Type", "application/xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            int status = connection.getResponseCode();
            InputStream is;
            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }

            parser.setInput(is, null);

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
                            } else if (tag.equals("mapx")) {
                                parser.next();
                                map.put("mapx", parser.getText());
                            } else if (tag.equals("mapy")) {
                                parser.next();
                                map.put("mapy", parser.getText());
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

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        mProgressDialog.dismiss();
        if (arrayList == null) {
            Toast.makeText(mContext, "Sorry Can't search now", Toast.LENGTH_SHORT).show();
            return;
        }
        mInvalidateListener.invalidate(arrayList);
    }
}
