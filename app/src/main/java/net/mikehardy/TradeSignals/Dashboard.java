package net.mikehardy.TradeSignals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Dashboard extends AppCompatActivity {

    public static final String TAG = Dashboard.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        EditText editText = (EditText) findViewById(R.id.edit_message);
        editText.setText("SPY");

        // do a lookup right at the start
        lookupQuote(null);
        getFredData(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when user clicks quote lookup */
    public void lookupQuote(View view) {

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String editMessage = editText.getText().toString();
        FetchQuote quoteFetcher = new FetchQuote();
        TextView tickerDisplay = (TextView) findViewById(R.id.ticker_display);
        quoteFetcher.setView(tickerDisplay);
        tickerDisplay.setText("Fetching quote for " + editMessage);
        quoteFetcher.execute(editMessage);
    }

    /** Get Recessionary signal */
    public void getFredData(View view) {

        TextView fredTextView = (TextView) findViewById(R.id.fred_info);
        fredTextView.setText("Fetching data from FRED...");
        FetchFred fredFetcher = new FetchFred();
        fredFetcher.setView(fredTextView);
        fredFetcher.fetchFredData();


    }
}
