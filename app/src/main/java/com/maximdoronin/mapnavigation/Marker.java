// Copyright (c) 2017. All rights reserved.
// Author: Maxim Doronin <maximdoronin@yandex-team.ru>

package com.maximdoronin.mapnavigation;

import com.google.android.gms.maps.model.LatLng;


public class Marker {
    private LatLng mPosition;
    private String mTitle;

    public Marker(LatLng position, String title) {
        mPosition = position;
        mTitle = title;
    }

    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }
}
