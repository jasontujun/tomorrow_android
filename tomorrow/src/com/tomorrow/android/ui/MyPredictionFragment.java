package com.tomorrow.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.MyPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.ui.adpter.PredictionAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * Created by jasontujun on 14-7-5.
 */
public class MyPredictionFragment extends Fragment {

    private ListView mListView;
    private BaseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        mListView = (ListView) rootView.findViewById(R.id.content_list);


        MyPredictionSource source = (MyPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.MY_PREDICTION);
        mAdapter = new PredictionAdapter(getActivity(), source, null);
        mListView.setAdapter(mAdapter);
        return rootView;
    }
}
