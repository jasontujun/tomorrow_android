package com.tomorrow.android.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.OtherPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.ui.adpter.PredictionDetailAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * 预测的详情页。
 * Created by jasontujun on 14-7-20.
 */
public class PredictionActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String PID = "predictionId";

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private PredictionDetailAdapter mAdapter;
    private boolean mIsRefreshing;

    private String mPredictionId;
    private Prediction mPrediction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null )
            return;
        mPredictionId = intent.getStringExtra(PID);
        if (TextUtils.isEmpty(mPredictionId))
            return;

        setContentView(R.layout.fragment_prediction);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mListView = (ListView) findViewById(R.id.content_list);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo
                .getInstance().getSource(SourceName.OTHER_PREDICTION);
        mPrediction = source.getById(mPredictionId);
        if (mPrediction == null)
            return;
        mAdapter = new PredictionDetailAdapter(this, mPrediction);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 在action bar点击app icon; 回到 home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (mIsRefreshing) {
            Log.d(MainActivity.TAG, "HistoryFragment onRefresh()!正在刷新，无视此次回调!");
            return;
        }
        Log.d(MainActivity.TAG, "HistoryFragment onRefresh()!响应回调!");
        mIsRefreshing = true;

        if (TextUtils.isEmpty(mPredictionId)) {
            mIsRefreshing = false;
            mSwipeLayout.setRefreshing(false);
            return;
        }

        if (mPrediction == null) {
            boolean result = PredictionMgr.getInstance().getPredictionDetail(this, mPredictionId,
                    new PredictionMgr.PredictionListener() {
                        @Override
                        public void onFinish(boolean success, Prediction prediction) {
                            if (success) {
                                mPrediction = prediction;
                                OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo
                                        .getInstance().getSource(SourceName.OTHER_PREDICTION);
                                source.add(mPrediction);
                                mAdapter = new PredictionDetailAdapter(PredictionActivity.this, mPrediction);
                                mListView.setAdapter(mAdapter);
                            }
                            mSwipeLayout.setRefreshing(false);
                            mIsRefreshing = false;
                        }
                        @Override
                        public void onCancelled() {
                            mSwipeLayout.setRefreshing(false);
                            mIsRefreshing = false;
                            Toast.makeText(PredictionActivity.this, R.string.toast_api_cancel,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, false);
            if (!result) {
                mSwipeLayout.setRefreshing(false);
                mIsRefreshing = false;
                Toast.makeText(this, R.string.toast_api_request_error, Toast.LENGTH_SHORT).show();
            }
        } else if (mPrediction.getReplyList() == null) {
            boolean result = PredictionMgr.getInstance().getPredictionReply(this, mPredictionId,
                    new PredictionMgr.PredictionListener() {
                        @Override
                        public void onFinish(boolean success, Prediction prediction) {
                            if (success) {
                                mPrediction.setReplyList(prediction.getReplyList());
                                mAdapter.setRelyList(mPrediction.getReplyList());
                            }
                            mSwipeLayout.setRefreshing(false);
                            mIsRefreshing = false;
                        }
                        @Override
                        public void onCancelled() {
                            mSwipeLayout.setRefreshing(false);
                            mIsRefreshing = false;
                            Toast.makeText(PredictionActivity.this, R.string.toast_api_cancel,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, false);
            if (!result) {
                mSwipeLayout.setRefreshing(false);
                mIsRefreshing = false;
                Toast.makeText(this, R.string.toast_api_request_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}