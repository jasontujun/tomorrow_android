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
public class MyPredictionFragment extends Fragment {

    private ListView mListView;
    private BaseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        mListView = (ListView) rootView.findViewById(R.id.content_list);

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_prediction,
                R.id.text_view, new String[]{"我的预测1 .....", "我的预测2 .....", "我的预测3 .....",
                "我的预测4 .....", "我的预测5 .....", "我的预测6 .....", "我的预测7 .....",
                "我的预测4 .....", "我的预测5 .....", "我的预测6 .....", "我的预测7 .....",
                "我的预测4 .....", "我的预测5 .....", "我的预测6 .....", "我的预测7 .....",
                "我的预测4 .....", "我的预测5 .....", "我的预测6 .....", "我的预测7 .....",
                "我的预测4 .....", "我的预测5 .....", "我的预测6 .....", "我的预测7 ....."});
        mListView.setAdapter(mAdapter);
        return rootView;
    }
}
