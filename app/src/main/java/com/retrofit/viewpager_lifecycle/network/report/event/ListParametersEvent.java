/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.network.report.event;

/**
 * Created by samsung on 1/5/15.
 */
public class ListParametersEvent {
    private final String mResourceUri;

    private ListParametersEvent(String resourceUri) {
        mResourceUri = resourceUri;
    }

    public String uri() {
        return mResourceUri;
    }

    public static ListParametersEvent forThe(String resourceUri) {
        return new ListParametersEvent(resourceUri);
    }
}
