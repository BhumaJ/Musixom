package com.quixom.musixom;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quixom.musixom.adapter.MusicListAdapter;
import com.quixom.musixom.util.Music;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


public class MostPlayed extends AppCompatActivity {

    public static String TAG = "musixom";
    static ArrayList<Music> displayList;

    ActionBar actionBar;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private SlidingUpPanelLayout mLayout;
    private String[] mNavigationTitles;

    private static void getAllSongs(Context context) {

        Uri allSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_ADDED, MediaStore.Images.Media._ID,
                MediaStore.Audio.Albums._ID};

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor mCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, MediaStore.Audio.Media.DATE_ADDED);

        try {
            if (mCursor.getCount() != 0) {
                if (mCursor.moveToFirst()) {
                    do {
                        String trackDuration = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        String trackTitle = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                        String addedTime = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
                        Long albumId = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                        String trackArtist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        String data = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        java.util.Date dateTime = new java.util.Date(Long.valueOf(addedTime) * 1000);

                        Music music = new Music();
                        music.setTrackTitle(trackTitle);
                        music.setTrackArtist(trackArtist);
                        music.setTrackUri(Uri.parse(data));

                        displayList.add(music);

                    } while (mCursor.moveToNext());
                }
                mCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCallLog(context);
    }

    private static void getCallLog(Context context) {
        int mNewMissedCallCount, mUnreadMissedCallCount;

        String newWhere = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + "=1";
        String[] newProjection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE};
        Cursor newCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, newProjection, newWhere, null, null);
        mNewMissedCallCount = newCursor.getCount();
        newCursor.close();
        String selection = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + " =1 " + " AND " + CallLog.Calls.IS_READ + " =0 ";
        String[] unreadProjection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE};
        Cursor unreadCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, unreadProjection, selection, null, null);
        mUnreadMissedCallCount = unreadCursor.getCount();
        unreadCursor.close();
        Log.e(TAG, " selection : " + selection);
        Log.e(TAG, "mNew       " + mNewMissedCallCount);
        Log.e(TAG, "mUnreadMissed:      " + mUnreadMissedCallCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_played);

        actionBar = getSupportActionBar();

        mTitle = mDrawerTitle = getTitle();
        mNavigationTitles = getResources().getStringArray(R.array.navigation_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavigationTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                actionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }

        displayList = new ArrayList<Music>();

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.bottom_panel);

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
            }

            @Override
            public void onPanelExpanded(View view) {
            }

            @Override
            public void onPanelAnchored(View view) {
            }

            @Override
            public void onPanelHidden(View view) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.most_played, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, actionBar.getTitle());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavigationTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            RecyclerView mRecyclerView;
            RecyclerView.LayoutManager mLayoutManager;
            RecyclerView.Adapter mAdapter;

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            getAllSongs(getActivity());

            mAdapter = new MusicListAdapter(displayList, getActivity());
            mRecyclerView.setAdapter(mAdapter);

            return rootView;

        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
