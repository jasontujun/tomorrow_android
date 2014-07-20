package com.tomorrow.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.OtherPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.ui.adpter.PredictionDetailAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * Created by jasontujun on 14-7-20.
 */
public class PredictionActivity extends Activity {

    public static final String PID = "predictionId";

    private ListView mListView;
    private PredictionDetailAdapter mAdapter;

    private String mPredictionId;
    private Prediction mPrediction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_prediction);
        mListView = (ListView) findViewById(R.id.content_list);

        Intent intent = getIntent();
        if (intent != null) {
            mPredictionId = intent.getStringExtra(PID);
            if (TextUtils.isEmpty(mPredictionId))
                return;

            OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo
                    .getInstance().getSource(SourceName.OTHER_PREDICTION);
            mPrediction = source.getById(mPredictionId);
            if (mPrediction == null)
                return;

            mAdapter = new PredictionDetailAdapter(this, mPrediction);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (TextUtils.isEmpty(mPredictionId))
            return;

        if (mPrediction == null) {
            PredictionMgr.getInstance().getPredictionDetail(this, mPredictionId,
                    new PredictionMgr.Listener() {
                        @Override
                        public void onFinish(boolean success, Prediction prediction) {
                            mPrediction = prediction;
                            OtherPredictionSource source = (OtherPredictionSource) DefaultDataRepo
                                    .getInstance().getSource(SourceName.OTHER_PREDICTION);
                            source.add(mPrediction);
                            mAdapter = new PredictionDetailAdapter(PredictionActivity.this, mPrediction);
                            mListView.setAdapter(mAdapter);
                        }
                    });
        } else if (mPrediction.getReplyList() == null) {
            PredictionMgr.getInstance().getPredictionReply(this, mPredictionId,
                    new PredictionMgr.Listener() {
                        @Override
                        public void onFinish(boolean success, Prediction prediction) {
                            mPrediction.setReplyList(prediction.getReplyList());
                            mAdapter.setRelyList(mPrediction.getReplyList());
                        }
                    });
        }
    }
}