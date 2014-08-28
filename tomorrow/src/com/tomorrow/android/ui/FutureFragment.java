package com.tomorrow.android.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.FuturePredictionSource;
import com.tomorrow.android.data.cache.HistoryPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.ui.adpter.PredictionAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * 未发生预测的列表
 * Created by jasontujun on 14-7-5.
 */
public class FutureFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int REFRESH_COMPLETE = 1;

    private PredictionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        ListView listView = (ListView) rootView.findViewById(R.id.content_list);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        FuturePredictionSource source = (FuturePredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.FUTURE_PREDICTION);
        mAdapter = new PredictionAdapter(getActivity(), source, swipeLayout);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 注册监听
        FuturePredictionSource source = (FuturePredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.FUTURE_PREDICTION);
        source.registerDataChangeListener(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        // 解除监听
        FuturePredictionSource source = (FuturePredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.FUTURE_PREDICTION);
        source.unregisterDataChangeListener(mAdapter);
    }

    @Override
    public void onRefresh() {
//        // 从服务器更新数据
//        int[] date = SystemMgr.getInstance().getTomorrow();
//        PredictionMgr.getInstance().getOtherPredictionList(getActivity(),
//                date[0], date[1], date[2], 0, 20);
//                    PredictionMgr.getInstance().getOtherPredictionList(getActivity(),
//                            2014, 7, 13, 0, 20);
        // 本地测试数据
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    // TODO 添加测试数据
                    FuturePredictionSource source = (FuturePredictionSource) DefaultDataRepo.getInstance().
                            getSource(SourceName.FUTURE_PREDICTION);
                    Prediction prediction = new Prediction();
                    prediction.setPredictionId("" + System.currentTimeMillis());
                    source.add(prediction);
                    break;

            }
        }
    };
}
