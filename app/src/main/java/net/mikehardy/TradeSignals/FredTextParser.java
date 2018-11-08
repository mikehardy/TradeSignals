package net.mikehardy.TradeSignals;

import android.util.Log;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mike on 2/14/16.
 */
public class FredTextParser
{
    private static final String TAG = FredTextParser.class.getName();

    private String fredSeriesText = null;
    private void setFredSeriesText(String fredSeriesText) { this.fredSeriesText = fredSeriesText; }
    private String getFredSeriesText() { return fredSeriesText; }

    private String title = null;
    private void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    private Date lastUpdated = null;
    private void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    public Date getLastUpdated() { return lastUpdated; }

    public Date getLastObservationDate() { return observations.lastEntry().getKey(); }
    public BigDecimal getLastObservation() { return observations.lastEntry().getValue(); }

    private Boolean contraction = false;
    //public void setContraction(Boolean bool) { contraction = bool; }
    public Boolean getContraction() { return contraction; }

    private final TreeMap<Date, BigDecimal> observations = new TreeMap<>();

    public FredTextParser(String fredSeriesText)
    {
        parseFredSeries(fredSeriesText);
    }

    private void parseFredSeries(String fredSeriesText) {
        setFredSeriesText(fredSeriesText);
        String seriesLines[] = getFredSeriesText().split("\\r?\\n");

        Boolean parsingObservations = false;
        BigDecimal mostRecentObservation = null;
        for (String seriesLine : seriesLines) {
            if (seriesLine.contains("Title:")) {
                String titleParts[] = seriesLine.split(":\\s+");
                setTitle(titleParts[1]);
            }
            if (seriesLine.contains("Last Updated:")) {
                String lastUpdatedParts[] = seriesLine.split(":\\s+");
                SimpleDateFormat updatedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a zzz", Locale.US);
                try {
                    setLastUpdated(updatedFormat.parse(lastUpdatedParts[1]));
                } catch (Exception e) {
                    // lazy lazy lazy
                }
                Log.d(TAG, "found last updated " + getLastUpdated());

            }

            if (seriesLine.matches("DATE\\s+VALUE")) {
                parsingObservations = true;
                continue;
            }

            if (parsingObservations) {
                String observationParts[] = seriesLine.split("\\s+");
                Log.d(TAG, "date = " + observationParts[0] + " value = " + observationParts[1]);
                if (mostRecentObservation == null) {
                    mostRecentObservation = new BigDecimal(observationParts[1]);
                }
                BigDecimal newObservation = new BigDecimal(observationParts[1]);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date observationDate = null;
                try {
                    observationDate = dateFormatter.parse(observationParts[0]);
                } catch (Exception e) {
                    // I'm the kind of lazy sack that ignores exceptions while prototyping
                    Log.e(TAG, "bad date?", e);
                }

                // Do a simple comparison to see if we're in contraction
                if (newObservation.compareTo(mostRecentObservation) < 0) {
                    contraction = true;
                    Log.d(TAG, "in contraction on " + observationDate
                        + "(new " + newObservation + " vs old " + mostRecentObservation);
                } else {
                    contraction = false;
                    Log.d(TAG, "stable or in expansion on " + observationDate
                            + "(new " + newObservation + " vs old " + mostRecentObservation);
                }
                mostRecentObservation = newObservation;

                // Stuff all these things into our sortable map
                observations.put(observationDate, newObservation);
            }
        }
    }

    // Calculate the SMA over the given number of entries
    @SuppressWarnings("SameParameterValue")
    private BigDecimal getSMA(int period) {
        Log.d(TAG, "calculating SMA for " + period + " observations"
            + " (observation count is " + observations.size() + ")");
        if (observations.size() < period) { period = observations.size(); }

        BigDecimal periodSum = new BigDecimal(0);
        Map.Entry<Date, BigDecimal> entry = observations.lastEntry();
        for (int i = 0; i < period; i++) {
            periodSum = periodSum.add(entry.getValue());
            Log.d(TAG, "worked on entry " + entry.getKey() + " / " + entry.getValue());
            entry = observations.lowerEntry(entry.getKey());
            Log.d(TAG, "next entry " + entry.getKey() + " / " + entry.getValue());
            Log.d(TAG, "current periodSum is " + periodSum);
        }
        Log.d(TAG, "periodSum for " + period + " month SMA is " + periodSum);
        BigDecimal sma = periodSum.divide(new BigDecimal(period), BigDecimal.ROUND_HALF_UP);
        Log.d(TAG, "SMA appears to be " + sma);
        return sma;
    }

    // Determine whether we are in a downtrend vs 12-mo SMA or not
    public int isDowntrend() {
        BigDecimal twelveMonthSMA = getSMA(12);
        BigDecimal lastObservation = observations.lastEntry().getValue();
        Log.d(TAG, "sma is " + twelveMonthSMA + " and last is " + lastObservation);
        return lastObservation.compareTo(twelveMonthSMA);
    }
}
