package net.mikehardy.TradeSignals;

import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mike on 2/14/16.
 */
public class FetchFred {

    private static final String TAG = FetchFred.class.getName();

    private TextView view = null;

    protected void setView(TextView view) { this.view = view; }
    protected TextView getView() { return view; }

    public void fetchFredData() {
        String[] seriesLabels = {"UNRATE"};
        for (String series : seriesLabels)
        {

            getView().setText("Fetching FRED economic data...");

            AsyncHttpClient urlClient = new AsyncHttpClient();
            urlClient.get("https://research.stlouisfed.org/fred2/data/" + series + ".txt",
                    new AsyncHttpResponseHandler()
                    {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                            try
                            {
                                FredTextParser parser = new FredTextParser(new String(response));

                                getView().setText(
                                        "last observation of " + parser.getTitle()
                                                + " on " + parser.getLastObservationDate()
                                                + " updated on " + parser.getLastUpdated()
                                                + " was " + parser.getLastObservation() + ".");

                                if (parser.getContraction())
                                {
                                    getView().append("\r\nIt appears to be contracting.");
                                }
                                else
                                {
                                    getView().append("\r\nIt appears to be expanding.");
                                }
                                if (parser.isDowntrend() < 0)
                                {
                                    getView().append("\r\nIt appears to be in a downtrend. Remain invested.");
                                }
                                else
                                {
                                    getView().append("\r\nIt does not appear to be in a downtrend. Check Equity Signal.");
                                    getView().setBackgroundColor(0xFFFFFF00);
                                }
                            }
                            catch (Exception e)
                            {
                                // do nothing
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                        }
                    });
        }
    }
}
