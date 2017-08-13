
package ha.thanh.myandroidtv;

import android.app.Activity;

import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
 * Class for video playback with media control
 */
public class PlaybackOverlayFragment extends android.support.v17.leanback.app.RowsFragment {
    private static final String TAG = "PlaybackControlsFragment";
    private ArrayObjectAdapter mRowsAdapter;
    private ArrayList<Movie> mItems = new ArrayList<>();
    public MovieChangeListener movieChangeLisener;
    public int currentPlaying = 0, currentSelected = 0;
    public TextView tvBarTitle;
    public TextView tvNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init data
        List<Movie> movies = MovieList.setupMovies();

        for (int j = 0; j < movies.size(); j++) {
            mItems.add(movies.get(j));
        }

        // init view


        ClassPresenterSelector ps = new ClassPresenterSelector();
        ListRowPresenter listRowPresenter = new ListRowPresenter();
        listRowPresenter.setShadowEnabled(false);
        ps.addClassPresenter(ListRow.class, listRowPresenter);
        mRowsAdapter = new ArrayObjectAdapter(ps);

        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new CustomPresenter());
        for (Movie movie : mItems) {
            arrayObjectAdapter.add(movie);
        }
        ListRow listRow = new ListRow(null, arrayObjectAdapter);
        mRowsAdapter.add(listRow);
        setAdapter(mRowsAdapter);
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

    }

    private void initChildView() {
        tvBarTitle = getActivity().findViewById(R.id.tv_bar_title);
        tvNum = getActivity().findViewById(R.id.tv_bar_num);
    }

    public void setMovieChangeListener(MovieChangeListener listener) {
        this.movieChangeLisener = listener;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            currentPlaying = ((ListRowPresenter.ViewHolder) rowViewHolder).getGridView().getSelectedPosition();
            Movie selected = (Movie) item;
            movieChangeLisener.onMovieChanged(selected);

        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            initChildView();
            if (item instanceof Movie) {
                Movie selected = (Movie) item;
                currentSelected = ((ListRowPresenter.ViewHolder) rowViewHolder).getGridView().getSelectedPosition() + 1;
                tvNum.setText("" + currentSelected + "/" + mItems.size());
                tvBarTitle.setText(selected.getTitle());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

    }

    public void endVideo() {
        nextVideo();
    }

    public void nextVideo() {
        currentPlaying++;
        if (currentPlaying == mItems.size())
            currentPlaying = 0;
        Movie selected = mItems.get(currentPlaying);
        movieChangeLisener.onMovieChanged(selected);
    }

    public void preVideo() {
        currentPlaying--;
        if (currentPlaying < 0)
            currentPlaying = mItems.size() - 1;
        Movie selected = mItems.get(currentPlaying);
        movieChangeLisener.onMovieChanged(selected);
    }
}
