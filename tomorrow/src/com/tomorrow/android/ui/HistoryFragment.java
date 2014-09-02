package com.tomorrow.android.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.HistoryPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.mgr.SystemMgr;
import com.tomorrow.android.ui.adpter.PredictionAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * 已发生预测的列表
 * Created by jasontujun on 14-7-5.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int REFRESH_COMPLETE = 1;

    private SwipeRefreshLayout mSwipeLayout;
    private PredictionAdapter mAdapter;
    private boolean mIsRefreshing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        ListView listView = (ListView) rootView.findViewById(R.id.content_list);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(R.color.blue, R.color.green, R.color.yellow, R.color.pink);
        HistoryPredictionSource source = (HistoryPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.HISTORY_PREDICTION);
        mAdapter = new PredictionAdapter(getActivity(), source, mSwipeLayout);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 注册监听
        HistoryPredictionSource source = (HistoryPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.HISTORY_PREDICTION);
        source.registerDataChangeListener(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        // 解除监听
        HistoryPredictionSource source = (HistoryPredictionSource) DefaultDataRepo.getInstance().
                getSource(SourceName.HISTORY_PREDICTION);
        source.unregisterDataChangeListener(mAdapter);
    }

    @Override
    public void onRefresh() {
        if (mIsRefreshing) {
            Log.d(MainActivity.TAG, "HistoryFragment onRefresh()!正在刷新，无视此次回调!");
            return;
        }
        Log.d(MainActivity.TAG, "HistoryFragment onRefresh()!响应回调!");
        mIsRefreshing = true;

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
                    HistoryPredictionSource source = (HistoryPredictionSource) DefaultDataRepo.getInstance().
                            getSource(SourceName.HISTORY_PREDICTION);
                    long time = System.currentTimeMillis();
                    List<Prediction> newPredictions = new ArrayList<Prediction>();
                    for (int i = 0; i< 10; i++) {
                        Prediction prediction = new Prediction();
                        prediction.setPredictionId("" + (time + i));
                        source.add(prediction);
                        newPredictions.add(prediction);
                    }
                    mIsRefreshing = false;
                    break;

            }
        }
    };
}
