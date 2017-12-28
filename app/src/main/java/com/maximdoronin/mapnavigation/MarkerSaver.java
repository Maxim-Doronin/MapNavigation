// Copyright (c) 2017. All rights reserved.
// Author: Maxim Doronin <maximdoronin@yandex-team.ru>

package com.maximdoronin.mapnavigation;

import java.util.List;


public interface MarkerSaver {
    void addElement(Marker marker);

    List<Marker> getElements();

    void deleteAll();
}
