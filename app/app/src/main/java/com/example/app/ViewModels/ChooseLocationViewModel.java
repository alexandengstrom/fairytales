package com.example.app.ViewModels;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * ViewModel for the choose location activity.
 */
public class ChooseLocationViewModel extends ViewModel {

    /**
     * Translates coordinates to a string containing the location.
     */
    public String getLocation(LatLng point, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressString = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String country = address.getCountryName();

                if (city != null && country != null) {
                    addressString = city + ", " + country;
                } else if (city != null) {
                    addressString = city;
                } else if (country != null) {
                    addressString = country;
                } else {
                    addressString = "In the middle of the ocean";
                }
            }
        } catch (IOException e) {
            addressString = "A place far away";
        }

        return addressString;
    }
}
