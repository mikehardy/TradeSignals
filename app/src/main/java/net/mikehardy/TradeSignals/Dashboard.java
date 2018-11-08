package net.mikehardy.TradeSignals;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Dashboard extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        EditText editText = findViewById(R.id.edit_message);
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
    @SuppressWarnings("WeakerAccess")
    @SuppressLint("SetTextI18n")
    public void lookupQuote(@SuppressWarnings("unused") View view) {

        EditText editText = findViewById(R.id.edit_message);
        String editMessage = editText.getText().toString();
        FetchQuote quoteFetcher = new FetchQuote();
        TextView tickerDisplay = findViewById(R.id.ticker_display);
        quoteFetcher.setView(tickerDisplay);
        tickerDisplay.setText("Fetching quote for " + editMessage);
        quoteFetcher.execute(editMessage);
    }

    /** Get Recessionary signal */
    @SuppressWarnings({"WeakerAccess", "unused"})
    @SuppressLint("SetTextI18n")
    public void getFredData(View view) {

        TextView fredTextView = findViewById(R.id.fred_info);
        fredTextView.setText("Fetching data from FRED...");
        FetchFred fredFetcher = new FetchFred();
        fredFetcher.setView(fredTextView);
        fredFetcher.fetchFredData();


    }
}
