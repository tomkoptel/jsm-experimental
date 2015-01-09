/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import com.squareup.otto.Bus;

/**
 * Created by samsung on 1/5/15.
 */
public enum EventBus {
    INSTANCE;
    private final Bus mBus;

    EventBus() {
        mBus = new Bus();
    }

    public void register(Object context) {
        mBus.register(context);
    }

    public Bus bus() {
        return mBus;
    }
}
