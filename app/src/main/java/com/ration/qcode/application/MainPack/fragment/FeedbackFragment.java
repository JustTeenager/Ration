package com.ration.qcode.application.MainPack.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ration.qcode.application.R;

/**
 * Created by deepdev on 05.04.17.
 */

public class FeedbackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("FEEDBACK_FRAGMENT", FeedbackFragment.class.toString());
        View view = inflater.inflate(R.layout.feedback_fragment, container, false);

        return view;
    }

}
