package fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.hy.maps.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import object.News_Point;
import object.sstatic;

public class News_frag extends Fragment {
    private View view;
    private TextView title,content;
    JSONArray informations=new JSONArray();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);
        title=(TextView)view.findViewById(R.id.news_title);
        content=(TextView)view.findViewById(R.id.news_content);
        title.setText(sstatic.id);
        SendTask s=new SendTask();
        s.execute();
        return view;
    }
    public class SendTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            Log.d("USRL",""+1);
            try {
                URL ServerUrl = new URL("http://steins.xin:3000/news/one_news?id=" +sstatic.id);

                HttpURLConnection conn;
                conn = (HttpURLConnection) ServerUrl.openConnection();
                conn.setConnectTimeout(13000);
                conn.setRequestMethod("GET");
                //conn.setDoOutput(true);
                conn.setDoInput(true);
                if (conn.getResponseCode()==200) {
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while ((line = reader.readLine()) != null)   sb.append(line);
                    String result = new String(sb);
                    informations= com.alibaba.fastjson.JSONArray.parseArray(result);
                    Log.d("info1111",""+informations.size());
                    return 1;
                }
                else return 2;
            } catch (Exception e) {
                e.printStackTrace();
                return 3;
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            if ((int)o==1){
                for (Iterator tor = informations.iterator(); tor.hasNext(); ) {
                    com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) tor.next();
                    String tilex=json.getString("title");
                    String contentx=json.getString("content");
                    title.setText(tilex);
                    content.setText(contentx);
                }
            }
        }
    }
}
