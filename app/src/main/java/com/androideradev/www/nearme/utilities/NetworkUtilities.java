package com.androideradev.www.nearme.utilities;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.androideradev.www.nearme.BuildConfig;
import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.model.PlaceReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtilities {

    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();

    private static final String PLACE_TYPE_SEARCH_BASE_URL = "https://api.foursquare.com/v2/venues/search";
    private static final String CLIENT_ID_KEY_NAME = "client_id";
    private static final String CLIENT_ID_KEY_VALUE = BuildConfig.CLIENT_ID;
    private static final String CLIENT_SECRET_KEY_NAME = "client_secret";
    private static final String CLIENT_SECRET_KEY_VALUE = BuildConfig.CLIENT_SECRET;
    private static final String LOCATION_PARAMETER_NAME = "ll";
    private static final String RADIUS_PARAMETER_NAME = "radius";
    private static final String RADIUS_PARAMETER_VALUE = "1500";
    private static final String PLACE_TYPE_PARAMETER_NAME = "categoryId";
    private static final String PLACES_LIMIT_PARAMETER_NAME = "limit";
    private static final String PLACES_LIMIT_PARAMETER_VALUE = "15";

    private static final String PHOTO_USER_WIDTH_AND_HEIGHT_VALUE = "100x100";
    private static final String PHOTO_PLACE_WIDTH_AND_HEIGHT_VALUE = "500x500";

    private static final String PLACE_DETAILS_BASE_URL = "https://api.foursquare.com/v2/venues/";
    private static final String PLACE_LIKES_BASE_URL = "https://api.foursquare.com/v2/venues/49eeaf08f964a52078681fe3/likes";

    private static final String REQUEST_TIME_PARAMETER_NAME = "v";


//    public static Place extractPlaceBasedOnId(String jsonResponse){
//
//        return new Place()
//    }

    public static URL buildPlaceDetailsUrl(String placeId) {
        Uri baseUri = Uri.parse(PLACE_DETAILS_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendPath(placeId)
                .appendQueryParameter(CLIENT_ID_KEY_NAME, CLIENT_ID_KEY_VALUE)
                .appendQueryParameter(CLIENT_SECRET_KEY_NAME, CLIENT_SECRET_KEY_VALUE)
                .appendQueryParameter(REQUEST_TIME_PARAMETER_NAME, "20180805");

        Uri completedUri = uriBuilder.build();
        return buildUrl(completedUri.toString());
    }

    public static URL buildTypeContentUrl(String locationParameterValue, String placeType) {
        Uri baseUri = Uri.parse(PLACE_TYPE_SEARCH_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendQueryParameter(LOCATION_PARAMETER_NAME, locationParameterValue)
                .appendQueryParameter(PLACE_TYPE_PARAMETER_NAME, placeType)
                .appendQueryParameter(RADIUS_PARAMETER_NAME, RADIUS_PARAMETER_VALUE)
                .appendQueryParameter(PLACES_LIMIT_PARAMETER_NAME, PLACES_LIMIT_PARAMETER_VALUE)
                .appendQueryParameter(CLIENT_ID_KEY_NAME, CLIENT_ID_KEY_VALUE)
                .appendQueryParameter(CLIENT_SECRET_KEY_NAME, CLIENT_SECRET_KEY_VALUE)
                .appendQueryParameter(REQUEST_TIME_PARAMETER_NAME, "20180806");


        Uri completedUri = uriBuilder.build();
        return buildUrl(completedUri.toString());
    }

    public static String buildUserPhotoUrl(String prefix, String suffix) {
        return prefix + PHOTO_USER_WIDTH_AND_HEIGHT_VALUE + suffix;

    }

    public static String buildPlacePhotoUrl(String prefix, String suffix) {
        return prefix + PHOTO_PLACE_WIDTH_AND_HEIGHT_VALUE + suffix;

    }

    public static String buildLikesUrl(String placeID) {
        Uri baseUri = Uri.parse(PLACE_DETAILS_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendPath(placeID)
                .appendPath("likes")
                .appendQueryParameter(CLIENT_ID_KEY_NAME, CLIENT_ID_KEY_VALUE)
                .appendQueryParameter(CLIENT_SECRET_KEY_NAME, CLIENT_SECRET_KEY_VALUE)
                .appendQueryParameter(REQUEST_TIME_PARAMETER_NAME, "20180805");

        Uri completedUri = uriBuilder.build();
        return completedUri.toString();
    }


    public static URL buildUrl(String stringUrl) {
        URL requestUrl;
        try {
            requestUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return requestUrl;
    }

    public static String getJsonResponseFromHttpUrl(URL requestUrl) {
        if (requestUrl == null) {
            return null;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = httpURLConnection.getInputStream();

                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }
}
