package com.androideradev.www.nearme.utilities;

import android.text.TextUtils;

import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.model.Photo;
import com.androideradev.www.nearme.model.PlaceReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtilities {

    private static final String RESPONSE_OBJECT_NAME = "response";

    private static final String VENUES_ARRAY_NAME = "venues";
    private static final String PLACE_ID_VALUE_NAME = "id";
    private static final String PLACE_NAME_VALUE_NAME = "name";

    private static final String CONTACT_OBJECT_NAME = "contact";
    private static final String PLACE_PHONE_VALUE_NAME = "phone";

    private static final String LOCATION_OBJECT_NAME = "location";
    private static final String PLACE_ADDRESS_VALUE_NAME = "address";
    private static final String PLACE_CROSS_STREET_VALUE_NAME = "crossStreet";
    private static final String LAT_VALUE_NAME = "lat";
    private static final String LNG_VALUE_NAME = "lng";

    private static final String CATEGORIES_ARRAY_NAME = "categories";
    private static final int CATEGORIES_INDEX_OBJECT = 0;
    private static final String CATEGORY_TYPE_NAME_VALUE_NAME = "name";

    private static final String VENUES_OBJECT_NAME = "venue";
    private static final String RATTING_VALUE_NAME = "rating";
    private static final String PHOTOS_OBJECT_NAME = "photos";
    private static final String TIPS_OBJECT_NAME = "tips";


    public static List<Place> extractPlacesFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Place> places = new ArrayList<>();

        String placeId = null;
        String name = null;
        String phone = null;
        String address = null;
        String crossStreet = null;
        double lat = 0;
        double lng = 0;
        String placeType = null;

        try {
            JSONObject mainJsonObject = new JSONObject(jsonResponse);
            if (mainJsonObject.has(RESPONSE_OBJECT_NAME)) {
                JSONObject responseJsonObject = mainJsonObject.optJSONObject(RESPONSE_OBJECT_NAME);

                if (responseJsonObject.has(VENUES_ARRAY_NAME)) {
                    JSONArray venuesJsonArray = responseJsonObject.optJSONArray(VENUES_ARRAY_NAME);

                    for (int i = 0; i < venuesJsonArray.length(); i++) {
                        JSONObject venueJsonObject = venuesJsonArray.optJSONObject(i);

                        if (venueJsonObject.has(PLACE_ID_VALUE_NAME)) {
                            placeId = venueJsonObject.optString(PLACE_ID_VALUE_NAME);
                        }
                        if (venueJsonObject.has(PLACE_NAME_VALUE_NAME)) {
                            name = venueJsonObject.optString(PLACE_NAME_VALUE_NAME);
                        }
                        if (venueJsonObject.has(CONTACT_OBJECT_NAME)) {
                            JSONObject contactJsonObject = venueJsonObject.optJSONObject(CONTACT_OBJECT_NAME);
                            if (contactJsonObject.has(PLACE_PHONE_VALUE_NAME)) {
                                phone = contactJsonObject.optString(PLACE_PHONE_VALUE_NAME);
                            }
                        }
                        if (venueJsonObject.has(LOCATION_OBJECT_NAME)) {
                            JSONObject locationJsonObject = venueJsonObject.optJSONObject(LOCATION_OBJECT_NAME);
                            if (locationJsonObject.has(PLACE_ADDRESS_VALUE_NAME)) {
                                address = locationJsonObject.optString(PLACE_ADDRESS_VALUE_NAME);
                            }
                            if (locationJsonObject.has(PLACE_CROSS_STREET_VALUE_NAME)) {
                                crossStreet = locationJsonObject.optString(PLACE_CROSS_STREET_VALUE_NAME);
                            }
                            if (locationJsonObject.has(LAT_VALUE_NAME)) {
                                lat = locationJsonObject.optDouble(LAT_VALUE_NAME);
                            }
                            if (locationJsonObject.has(LNG_VALUE_NAME)) {
                                lng = locationJsonObject.optDouble(LNG_VALUE_NAME);
                            }
                        }
                        if (venueJsonObject.has(CATEGORIES_ARRAY_NAME)) {
                            JSONArray categoriesJsonArray = venueJsonObject.optJSONArray(CATEGORIES_ARRAY_NAME);

                            JSONObject categoryJsonObject = categoriesJsonArray.getJSONObject(CATEGORIES_INDEX_OBJECT);
                            if (categoryJsonObject.has(CATEGORY_TYPE_NAME_VALUE_NAME)) {
                                placeType = categoryJsonObject.optString(CATEGORY_TYPE_NAME_VALUE_NAME);
                            }
                        }
                        Place place = new Place(placeId, name, phone, address, crossStreet, lat, lng, placeType);
                        places.add(place);
                    }
                }
            }

            return places;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }
    }

    public static Place extractPlaceFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        String name = null;
        String phone = null;
        String address = null;
        String crossStreet = null;
        String placeType = null;
        String ratting = null;
        List<Photo> placePhotos = new ArrayList<>();
        List<PlaceReview> placeReviews = new ArrayList<>();
        String placeStatus = null;
        boolean isOpen = false;

        try {
            JSONObject mainJsonObject = new JSONObject(jsonResponse);
            if (mainJsonObject.has(RESPONSE_OBJECT_NAME)) {
                JSONObject responseJsonObject = mainJsonObject.optJSONObject(RESPONSE_OBJECT_NAME);
                if (responseJsonObject.has(VENUES_OBJECT_NAME)) {
                    JSONObject venueJsonObject = responseJsonObject.optJSONObject(VENUES_OBJECT_NAME);

                    if (venueJsonObject.has(PLACE_NAME_VALUE_NAME)) {
                        name = venueJsonObject.optString(PLACE_NAME_VALUE_NAME);
                    }
                    if (venueJsonObject.has(CONTACT_OBJECT_NAME)) {
                        JSONObject contactJsonObject = venueJsonObject.optJSONObject(CONTACT_OBJECT_NAME);
                        if (contactJsonObject.has(PLACE_PHONE_VALUE_NAME)) {
                            phone = contactJsonObject.optString(PLACE_PHONE_VALUE_NAME);
                        }
                    }
                    if (venueJsonObject.has(LOCATION_OBJECT_NAME)) {
                        JSONObject locationJsonObject = venueJsonObject.optJSONObject(LOCATION_OBJECT_NAME);
                        if (locationJsonObject.has(PLACE_ADDRESS_VALUE_NAME)) {
                            address = locationJsonObject.optString(PLACE_ADDRESS_VALUE_NAME);
                        }
                        if (locationJsonObject.has(PLACE_CROSS_STREET_VALUE_NAME)) {
                            crossStreet = locationJsonObject.optString(PLACE_CROSS_STREET_VALUE_NAME);
                        }
                    }
                    if (venueJsonObject.has(CATEGORIES_ARRAY_NAME)) {
                        JSONArray categoriesJsonArray = venueJsonObject.optJSONArray(CATEGORIES_ARRAY_NAME);

                        JSONObject categoryJsonObject = categoriesJsonArray.getJSONObject(CATEGORIES_INDEX_OBJECT);
                        if (categoryJsonObject.has(CATEGORY_TYPE_NAME_VALUE_NAME)) {
                            placeType = categoryJsonObject.optString(CATEGORY_TYPE_NAME_VALUE_NAME);
                        }
                    }
                    if (venueJsonObject.has(RATTING_VALUE_NAME)) {
                        ratting = venueJsonObject.optString(RATTING_VALUE_NAME);
                    }
                    if (venueJsonObject.has(PHOTOS_OBJECT_NAME)) {
                        JSONObject photoJsonObject = venueJsonObject.optJSONObject(PHOTOS_OBJECT_NAME);
                        final String groupArrayName = "groups";
                        if (photoJsonObject.has(groupArrayName)) {
                            JSONArray groupJsonArray = photoJsonObject.getJSONArray(groupArrayName);
                            for (int i = 0; i < groupJsonArray.length(); i++){
                                JSONObject placePhotoObject = groupJsonArray.optJSONObject(i);
                                final String itemsArrayName = "items";
                                if (placePhotoObject.has(itemsArrayName)) {
                                    JSONArray itemsJsonArray = placePhotoObject.optJSONArray(itemsArrayName);
                                    String prefix = null;
                                    String suffix = null;
                                    for (int j = 0; j < itemsJsonArray.length(); j++) {

                                        JSONObject photoObject = itemsJsonArray.optJSONObject(j);
                                        if (photoObject.has("prefix")) {
                                            prefix = photoObject.optString("prefix");
                                        }
                                        if (photoObject.has("suffix")) {
                                            suffix = photoObject.optString("suffix");
                                        }
                                        Photo placePhoto = new Photo(prefix, suffix);

                                        placePhotos.add(placePhoto);
                                    }
                                }
                            }
                        }
                    }
                    if (venueJsonObject.has(TIPS_OBJECT_NAME)) {
                        JSONObject tipsJsonObject = venueJsonObject.optJSONObject(TIPS_OBJECT_NAME);
                        final String groupArrayName = "groups";
                        if (tipsJsonObject.has(groupArrayName)) {
                            JSONArray groupJsonArray = tipsJsonObject.getJSONArray(groupArrayName);

                            for (int i = 0; i < groupJsonArray.length(); i++){

                                JSONObject groupJsonObject = groupJsonArray.optJSONObject(i);
                                final String itemsArrayName = "items";
                                if (groupJsonObject.has(itemsArrayName)) {
                                    JSONArray itemsJsonArray = groupJsonObject.optJSONArray(itemsArrayName);
                                    String tipTime = null;
                                    String tipText = null;
                                    String tipRatting = null;
                                    String userName = null;
                                    Photo userPhoto = null;
                                    for (int j = 0; j < itemsJsonArray.length(); j++) {
                                        JSONObject itemJsonObject = itemsJsonArray.optJSONObject(j);
                                        tipTime = itemJsonObject.optString("createdAt");
                                        tipText = itemJsonObject.optString("text");
                                        JSONObject likesJsonObject = itemJsonObject.optJSONObject("likes");
                                        tipRatting = likesJsonObject.optString("count");
                                        JSONObject userJsonObject = itemJsonObject.optJSONObject("user");
                                        userName = userJsonObject.optString("firstName");
                                        JSONObject userPhotoJsonObject = userJsonObject.optJSONObject("photo");
                                        userPhoto = new Photo(userPhotoJsonObject.optString("prefix"), userPhotoJsonObject.optString("suffix"));

                                        PlaceReview placeReview = new PlaceReview(tipTime, tipText, tipRatting, userName, userPhoto);
                                        placeReviews.add(placeReview);
                                    }
                                }
                            }


                        }
                    }
                    if (venueJsonObject.has("hours")) {
                        JSONObject hoursJsonObject = venueJsonObject.optJSONObject("hours");
                        placeStatus = hoursJsonObject.optString("status");
                        isOpen = hoursJsonObject.optBoolean("isOpen");
                    }
                }

            }

            return new Place(name, phone, address, crossStreet, placeType, ratting, placePhotos, placeReviews, placeStatus, isOpen);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
