package com.tomorrow.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.OtherPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.mgr.SystemMgr;
import com.tomorrow.android.ui.adpter.PredictionAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * 他人预测列表
 * Created by jasontujun on 14-7-5.
 */
public class OtherPredictionFragment extends Fragment {

    private PredictionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.content_list);

        OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.OTHER_PREDICTION);
        mAdapter = new PredictionAdapter(getActivity(), source);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.OTHER_PREDICTION);
        source.registerDataChangeListener(mAdapter);

        // 获取数据
        int[] date = SystemMgr.getInstance().getTomorrow();
//        PredictionMgr.getInstance().getOtherPredictionList(getActivity(),
//                date[0], date[1], date[2], 0, 20);
        PredictionMgr.getInstance().getOtherPredictionList(getActivity(),
                2014, 7, 13, 0, 20);
    }


    @Override
    public void onStop() {
        super.onStop();
        OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.OTHER_PREDICTION);
        source.unregisterDataChangeListener(mAdapter);
    }
}
