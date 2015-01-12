package com.retrofit.sample;

import android.app.Fragment;

/**
 * @author Tom Koptel
 * @since 1.9
 */
class ServerFragment extends Fragment {
    public static ServerFragment instance() {
        ServerFragment serverFragment = new ServerFragment();
        return serverFragment;
    }
}
