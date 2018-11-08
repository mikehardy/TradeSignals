package net.mikehardy.TradeSignals;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

/**
 * Heavily uses https://github.com/sstrickx/yahoofinance-api
 *
 * Created by mike on 2/3/16.
 */
public class FetchQuote extends AsyncTask<String, Void, Stock> {

    private static final String TAG = FetchQuote.class.getName();

    @SuppressLint("StaticFieldLeak")
    private TextView view = null;

    protected void setView(TextView view) {
        this.view = view;
    }

    protected Stock doInBackground(String... tickers) {
        try {
            String ticker = tickers[0];
            if ((ticker == null) || ticker.equals("")) {
                return null;
            }
            Log.d(TAG, "doInBackground: ticker is " + ticker);
            return YahooFinance.get(ticker);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: exception? Dang ", e);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    protected void onPostExecute(Stock stock) {

        StockQuote quote = stock.getQuote();

        view.setText(stock.getName() + " priced at "
                + quote.getPrice() + " vs SMA200 of "
                + quote.getPriceAvg200());

        if (quote.getPrice().compareTo(quote.getPriceAvg200()) < 0)
        {
            view.setBackgroundColor(0xFFFFFF00); // RGBA of yellow?
            view.append("\r\nStock trending down. Check recession indicator");
        }
        else
        {
            view.setBackgroundColor(0xFF00FF00); // RGBA of green?
        }

    }
}
