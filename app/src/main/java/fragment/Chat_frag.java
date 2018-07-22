package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.hy.maps.R;

public class Chat_frag extends Fragment {
    private View view;
    private WebView wb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat, container, false);
        wb=(WebView)view.findViewById(R.id.chat_webView) ;
        String url = "file:///android_asset/index.html";
        loadLocalHtml(url);
        return view;
    }
    public void loadLocalHtml(String url){
        WebSettings ws = wb.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        wb.loadUrl(url);
    }
}
