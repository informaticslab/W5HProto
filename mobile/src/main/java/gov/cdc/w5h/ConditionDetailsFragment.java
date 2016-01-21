package gov.cdc.w5h;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ConditionDetailsFragment extends Fragment {
    private WebView webView;
    private String page = null;

    public static ConditionDetailsFragment newInstance(String page) {
        ConditionDetailsFragment cdf = new ConditionDetailsFragment();
        Bundle args = new Bundle();
        args.putString("page", page);
        cdf.setArguments(args);
        return cdf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getString("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_condition_details, container, false);
        webView = (WebView) rootView.findViewById(R.id.condition_web_view);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("file:///android_asset/content/" + page);
        return rootView;
    }
}
