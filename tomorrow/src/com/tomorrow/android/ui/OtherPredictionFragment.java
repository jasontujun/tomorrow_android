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

/**
 * Created by jasontujun on 14-7-5.
 */
public class OtherPredictionFragment extends Fragment {

    private ListView mListView;
    private BaseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        mListView = (ListView) rootView.findViewById(R.id.content_list);

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_prediction,
                R.id.text_view, new String[]{"预测1 .....", "预测2 .....", "预测3 .....",
                "预测4 .....", "预测5 .....", "预测6 .....", "预测7 .....",
                "预测4 .....", "预测5 .....", "预测6 .....", "预测7 .....",
                "预测4 .....", "预测5 .....", "预测6 .....", "预测7 .....",
                "预测4 .....", "预测5 .....", "预测6 .....", "预测7 .....",
                "预测4 .....", "预测5 .....", "预测6 .....", "预测7 ....."});
        mListView.setAdapter(mAdapter);

        return rootView;
    }
}
