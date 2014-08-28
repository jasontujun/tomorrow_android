package com.tomorrow.android.ui.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tomorrow.android.R;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.ui.PredictionActivity;
import com.xengine.android.data.cache.XAdapterDataSource;
import com.xengine.android.data.cache.XDataChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasontujun on 14-7-20.
 */
public class PredictionAdapter extends BaseAdapter
        implements XDataChangeListener<Prediction>,
        AdapterView.OnItemClickListener {

    private Context mContext;
    private List<Prediction> mItems;
    private XAdapterDataSource<Prediction> mSource;
    private SwipeRefreshLayout mSwipeLayout;

    public PredictionAdapter(Context context, XAdapterDataSource<Prediction> source,
                             SwipeRefreshLayout swipeLayout) {
        mContext = context;
        mItems = new ArrayList<Prediction>();
        mSource = source;
        mSwipeLayout = swipeLayout;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        public TextView titleView;
        public TextView creditView;
        public Button replyBtn;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holderHead = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_prediction, null);
            holderHead = new ViewHolder();
            holderHead.titleView = (TextView) convertView.findViewById(R.id.title_view);
            holderHead.creditView = (TextView) convertView.findViewById(R.id.credit_view);
            holderHead.replyBtn = (Button) convertView.findViewById(R.id.reply_btn);
            convertView.setTag(holderHead);
        } else {
            holderHead = (ViewHolder) convertView.getTag();
        }
        final Prediction prediction = (Prediction) getItem(i);
        holderHead.titleView.setText(prediction.getWhat()
                + " " + prediction.getHow() + " " + prediction.getWhere()
                + " " + prediction.getWhenYear() + "年"
                + prediction.getWhenMonth() + "月"
                + prediction.getWhenDay() + "日");
        int creditPercent = (int) (prediction.getCredit() * 100);
        holderHead.creditView.setText(creditPercent + "%");
        holderHead.replyBtn.setText("" + prediction.getReplyCount());
        holderHead.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击了回复按钮！", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, PredictionActivity.class);
                intent.putExtra(PredictionActivity.PID, prediction.getPredictionId());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private Handler mHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    if (mSwipeLayout != null)
                        mSwipeLayout.setRefreshing(false);
                    break;
                case 1:
                    mItems = mSource.copyAll();
                    notifyDataSetChanged();
                    if (mSwipeLayout != null)
                        mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void onChange() {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onAdd(Prediction prediction) {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onAddAll(List<Prediction> predictions) {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onDelete(Prediction prediction) {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onDeleteAll(List<Prediction> predictions) {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(mContext, "点击了Item！", Toast.LENGTH_SHORT).show();
        Prediction prediction = (Prediction) getItem(i);
        if (prediction == null)
            return;

        Intent intent = new Intent(mContext, PredictionActivity.class);
        intent.putExtra(PredictionActivity.PID, prediction.getPredictionId());
        mContext.startActivity(intent);
    }
}
