package com.quixom.musixom.util;

import android.net.Uri;

/**
 * Created by Admin on 29-Sep-15.
 */
public class Music {

    String trackTitle, trackArtist;
    Uri trackUri;

    public Uri getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(Uri trackUri) {
        this.trackUri = trackUri;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public void setTrackArtist(String trackArtist) {
        this.trackArtist = trackArtist;
    }
}
