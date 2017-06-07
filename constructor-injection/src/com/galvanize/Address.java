package com.galvanize;

public class Address {

    private final GoogleGeocoder geocoder;

    public Address() {
        this.geocoder = new GoogleGeocoder();
    }

    public LatLng getLatLng() {
        return this.geocoder.geocode();
    }

}
