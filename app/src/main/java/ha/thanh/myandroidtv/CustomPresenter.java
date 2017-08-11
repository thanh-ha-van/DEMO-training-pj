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

import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/*
 * A CustomPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CustomPresenter extends Presenter {

    private static final String TAG = "CustomPresenter";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_custom_card_movie, parent, false);

        itemView.setFocusable(true);
        itemView.setFocusableInTouchMode(true);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        LinearLayout layout = (LinearLayout) viewHolder.view;
        ImageView imageView = layout.findViewById(R.id.image_movie);
        TextView textView = layout.findViewById(R.id.tv_name);
        Log.d(TAG, "onBindViewHolder");
        if (movie!= null) {

            Glide.with(viewHolder.view.getContext())
                    .load(movie.getCardImageURI().toString())
                    .placeholder(R.drawable.grid_bg) // any placeholder to load at start
                    .centerCrop()
                    .error(R.drawable.grid_bg)
                    .into(imageView);

            textView.setText(movie.getTitle());

        }

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void setOnClickListener(ViewHolder holder, View.OnClickListener listener) {
        super.setOnClickListener(holder, listener);
    }

}
