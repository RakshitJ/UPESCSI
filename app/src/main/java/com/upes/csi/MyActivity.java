package com.upes.csi;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.fourmob.poppyview.PoppyViewHelper;
import com.upes.fragment.NavigationDrawerFragment;
import com.upes.fragment.SectionOneFragment;
import com.upes.fragment.SectionThreeFragment;
import com.upes.fragment.SectionTwoFragment;


public class MyActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        SectionOneFragment.OnFragmentInteractionListener, SectionTwoFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private PoppyViewHelper mPoppyViewHelper;
    private View poppyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        ActionBar ab = this.getActionBar();
        ab.hide();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Nav drawer item click action
        Fragment fragment = null;
        switch(position) {
            case 1:
                fragment = new SectionOneFragment();
                mTitle = getResources().getString(R.string.title_section1);
                break;
            case 2:
                fragment = new SectionTwoFragment();
                mTitle = getResources().getString(R.string.title_section2);
                break;
            case 3:
                fragment = new SectionThreeFragment();
                mTitle = getResources().getString(R.string.title_section3);
                break;
            default:
                fragment = new SectionOneFragment();
                mTitle = getResources().getString(R.string.title_section1);
                break;
        }

        if(fragment!=null) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            setTitle(mTitle);
        }

    }

    public void onFragmentInteraction(Uri uri) {
        //Interact between fragments and activity
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
