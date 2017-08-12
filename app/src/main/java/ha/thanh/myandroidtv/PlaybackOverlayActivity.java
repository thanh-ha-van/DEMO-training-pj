/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ha.thanh.myandroidtv;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

/**
 * PlaybackOverlayActivity for video playback that loads PlaybackOverlayFragment
 */
public class PlaybackOverlayActivity extends Activity implements MovieChangeListener, Player.EventListener {


    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private LinearLayout queueBarLayout;
    private Movie movie;
    private FragmentManager f;
    private PlaybackOverlayFragment fragment;
    private TextView tvTitle;
    private SimpleExoPlayerView simpleExoPlayerView;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_controls);


        queueBarLayout = findViewById(R.id.layout_custom_row);
        queueBarLayout.setVisibility(LinearLayout.VISIBLE);
        queueBarLayout.requestFocus();

        tvTitle = findViewById(R.id.video_title_controller_bar);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);

        f = getFragmentManager();
        fragment = (PlaybackOverlayFragment) f.findFragmentById(R.id.playback_controls_fragment);
        fragment.setMovieChangeListener(this);
    }

    private void initializePlayer() {

        simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.hideController();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView.setPlayer(player);

        if(movie !=null) {
            if (movie.getVideoUrl().contains(".m3u8")) {
                MediaSource mediaSource = new HlsMediaSource(Uri.parse(movie.getVideoUrl()),
                        mediaDataSourceFactory, null, null);
                player.prepare(mediaSource);
            } else {
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(movie.getVideoUrl()),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                player.prepare(mediaSource);
            }
        }
        player.setPlayWhenReady(shouldAutoPlay);
        player.addListener(this);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch(playbackState) {

            case ExoPlayer.STATE_ENDED:
                fragment.endVideo();
                break;

            default:
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onMovieChanged(Movie movie) {

        this.movie = movie;
        releasePlayer();
        initializePlayer();
        queueBarLayout.setVisibility(View.GONE);
        tvTitle.setText(movie.getTitle());
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
        queueBarLayout.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_UP: // key up
                doKeyUpthing();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:  // key dow
                doKeyDownThing();
                return true;
            case 42:  // later will be the next video button
                fragment.nextVideo();
                return true;

            case 30:  // later will be the previous video button
                fragment.preVideo();
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                if (player.getPlayWhenReady())
                player.setPlayWhenReady(false);
                player.setPlayWhenReady(true);

        }
        return super.onKeyUp(keyCode, event);
    }

    private void showActionIcon (int statuscode) {
        switch (statuscode) {
            case 1: // playing
            case 2: // pausing
            case 3: // fast forward
            case 4: // rewind
            case 5: // next
            case 6: // previous
        }
    }
    private void doKeyUpthing() {
        if (queueBarLayout.getVisibility() == LinearLayout.GONE) {
            queueBarLayout.setVisibility(LinearLayout.VISIBLE);
            queueBarLayout.requestFocus();
        }
        simpleExoPlayerView.hideController();
    }
    private void doKeyDownThing () {
        if (queueBarLayout.getVisibility() == LinearLayout.VISIBLE) {
            queueBarLayout.setVisibility(LinearLayout.GONE);
        }
        simpleExoPlayerView.showController();
    }
}
