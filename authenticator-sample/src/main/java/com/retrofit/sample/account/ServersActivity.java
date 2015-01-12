package com.retrofit.sample.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.retrofit.sample.R;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServersActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ServersListFragment listFragment = ServersListFragment.instance();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, listFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activit_servers_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addServer) {
            addServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addServer() {
        ServerFragment formFragment = ServerFragment.instance();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, formFragment)
                .addToBackStack(null)
                .commit();
    }
}
