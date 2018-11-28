package io.jachoteam.kaska;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.jachoteam.kaska.dummy.DummyContent;

public class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
