package com.tomorrow.android.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.data.cache.FuturePredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.ui.adpter.ReplyAdapter;
import com.xengine.android.data.cache.DefaultDataRepo;
import com.xengine.android.utils.XStringUtil;

/**
 * 预测的详情页。
 * Created by jasontujun on 14-7-20.
 */
public class PredictionActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String PID = "predictionId";

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ReplyAdapter mAdapter;
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
        mSwipeLayout.setColorScheme(R.color.blue, R.color.green, R.color.yellow, R.color.pink);

        FuturePredictionSource source = (FuturePredictionSource) DefaultDataRepo
                .getInstance().getSource(SourceName.FUTURE_PREDICTION);
        mPrediction = source.getById(mPredictionId);
        if (mPrediction == null)
            return;

        initPredictionContent(this, mPrediction, mListView);
        mAdapter = new ReplyAdapter(this);
        mListView.setAdapter(mAdapter);
        mAdapter.setRelyList(mPrediction.getReplyList());
    }

    private static void initPredictionContent(final Context context,
                                              Prediction prediction, ListView listView) {
        View convertView = View.inflate(context, R.layout.item_prediciton_detail, null);
        TextView authorView = (TextView) convertView.findViewById(R.id.author_view);
        TextView timeView = (TextView) convertView.findViewById(R.id.time_view);
        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        TextView reasonView = (TextView) convertView.findViewById(R.id.reason_view);
        TextView creditView = (TextView) convertView.findViewById(R.id.credit_view);
        TextView replyBtn = (TextView) convertView.findViewById(R.id.reply_btn);
        authorView.setText(prediction.getAuthorName());
        timeView.setText(XStringUtil.date2str(prediction.getCreatedTime()));
        titleView.setText(prediction.getWhat()
                + " " + prediction.getHow() + " " + prediction.getWhere()
                + " " + prediction.getWhenYear() + "年"
                + prediction.getWhenMonth() + "月"
                + prediction.getWhenDay() + "日");
        reasonView.setText("理由: " + prediction.getReason());
        int creditPercent = (int) (prediction.getCredit() * 100);
        creditView.setText(creditPercent + "%");
        replyBtn.setText("" + prediction.getReplyCount());
        // 本地判断，一个人不能回复2次以上
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 回复
                Toast.makeText(context, "回复预测者", Toast.LENGTH_SHORT).show();
            }
        });
        listView.addHeaderView(convertView);
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
                                mAdapter = new ReplyAdapter(PredictionActivity.this);
                                mListView.setAdapter(mAdapter);
                                Toast.makeText(PredictionActivity.this,
                                        R.string.http_refresh_success, Toast.LENGTH_SHORT).show();
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
        } else {
            boolean result = PredictionMgr.getInstance().getPredictionReply(this, mPredictionId,
                    new PredictionMgr.PredictionListener() {
                        @Override
                        public void onFinish(boolean success, Prediction prediction) {
                            if (success) {
                                mPrediction.setReplyList(prediction.getReplyList());
                                mAdapter.setRelyList(mPrediction.getReplyList());
                                Toast.makeText(PredictionActivity.this,
                                        R.string.http_refresh_success, Toast.LENGTH_SHORT).show();
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