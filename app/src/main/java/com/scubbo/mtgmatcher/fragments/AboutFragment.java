package com.scubbo.mtgmatcher.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scubbo.mtgmatcher.R;

public class AboutFragment extends FragmentWithTitle {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aboutlayout, container, false);
    }

    @Override
    public String getTitle() {
        return "About";
    }
}
