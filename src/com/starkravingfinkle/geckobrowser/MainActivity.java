package com.starkravingfinkle.geckobrowser;

import org.mozilla.gecko.GeckoView;
import org.mozilla.gecko.GeckoViewChrome;
import org.mozilla.gecko.GeckoViewContent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final String LOGTAG = "GeckoBrowser";

	GeckoView mGeckoView;
	TextView mPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeckoView = (GeckoView) findViewById(R.id.gecko_view);

        Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText text = (EditText) findViewById(R.id.url_bar);
                GeckoView.Browser browser = mGeckoView.getSelected();
                if (browser == null) {
                	browser = mGeckoView.add(text.getText().toString());
                } else {
                	browser.loadUrl(text.getText().toString());
                }
            }
        });
        
        mGeckoView.setGeckoViewChrome(new MyGeckoViewChrome());
        mGeckoView.setGeckoViewContent(new MyGeckoViewContent());

        mPageTitle = (TextView) findViewById(R.id.page_title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class MyGeckoViewChrome extends GeckoViewChrome {
        @Override
        public void onReady(GeckoView view) {
        	Log.i(LOGTAG, "Gecko is ready");

        	// The Gecko libraries have finished loading and we can use the rendering engine.
        	// Let's add a browser (required) and load a page into it.
        	mGeckoView.add("http://starkravingfinkle.org");
        }
    }

    private class MyGeckoViewContent extends GeckoViewContent {
        @Override
        public void onReceivedTitle(GeckoView view, GeckoView.Browser browser, String title) {
        	Log.i(LOGTAG, "Received a title");

        	// Use the title returned from Gecko to update the UI
        	// TODO: Only if the browser is the selected browser
        	mPageTitle.setText(title);
        }
    }
}
