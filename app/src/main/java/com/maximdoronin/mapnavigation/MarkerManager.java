// Copyright (c) 2017. All rights reserved.
// Author: Maxim Doronin <maximdoronin@yandex-team.ru>


package com.maximdoronin.mapnavigation;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MarkerManager {

    private GoogleMap mMap;
    private MarkerSaver mMarkerSaver;

    public MarkerManager(Context context, GoogleMap map) {
        mMap = map;
        MarkerSaverSql.init(context);
        mMarkerSaver = MarkerSaverSql.get();
    }

    public void addMarker(Marker marker) {
        mMap.addMarker(new MarkerOptions()
                .position(marker.getPosition())
                .title(marker.getTitle()));
        mMarkerSaver.addElement(marker);
    }

    public void clearScreen() {
        mMap.clear();
    }

    public void restoreLocations() {
        List<Marker> markers = mMarkerSaver.getElements();
        for (Marker marker : markers) {
            addMarker(marker);
        }
    }

    public void clearStore() {
        mMap.clear();
        mMarkerSaver.deleteAll();
    }
}
