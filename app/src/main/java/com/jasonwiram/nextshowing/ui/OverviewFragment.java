package com.jasonwiram.nextshowing.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonwiram.nextshowing.R;

public class OverviewFragment extends DialogFragment {

    public OverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, viewGroup, false);

//        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView overviewTextView = (TextView) view.findViewById(R.id.overviewDetailsTextView);

        Bundle bundle = getArguments();
//        String title = bundle.getString("title");
        String overview = bundle.getString("overview");

//        titleTextView.setText(title);
        overviewTextView.setText(overview);

        return view;
    }
}
