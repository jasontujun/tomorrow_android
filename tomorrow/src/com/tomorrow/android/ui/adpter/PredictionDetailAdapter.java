package com.tomorrow.android.ui.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.Reply;
import com.xengine.android.utils.XStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasontujun on 14-7-20.
 */
public class PredictionDetailAdapter extends BaseAdapter {

    private static final int TYPE_DETAIL = 0;
    private static final int TYPE_REPLY = 1;

    private Context mContext;
    private Prediction mPrediction;
    private List<Reply> mReplyList;

    public PredictionDetailAdapter(Context context, Prediction prediction) {
        mContext = context;
        mPrediction = prediction;
        mReplyList = prediction.getReplyList();
        if (mReplyList == null)
            mReplyList = new ArrayList<Reply>();
    }

    /**
     * 外部获取回复数据
     * @param replyList
     */
    public void setRelyList(List<Reply> replyList) {
        if (replyList == null)
            return;
        List<Reply> newList = new ArrayList<Reply>();
        newList.addAll(replyList);
        mReplyList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mReplyList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i == 0)
            return mPrediction;
        else
            return mReplyList.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_DETAIL;
        else
            return TYPE_REPLY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class DetailViewHolder {
        public TextView authorView;
        public TextView timeView;
        public TextView titleView;
        public TextView reasonView;
        public TextView creditView;
        public Button replyBtn;
    }

    private class ReplyViewHolder {
        public TextView senderView;
        public TextView timeView;
        public TextView titleView;
        public TextView possibilityView;
        public Button replyBtn;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(i);

        switch (type) {
            // 预测详情
            case TYPE_DETAIL: {
                DetailViewHolder holderHead = null;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_prediciton_detail, null);
                    holderHead = new DetailViewHolder();
                    holderHead.authorView = (TextView) convertView.findViewById(R.id.author_view);
                    holderHead.timeView = (TextView) convertView.findViewById(R.id.time_view);
                    holderHead.titleView = (TextView) convertView.findViewById(R.id.title_view);
                    holderHead.reasonView = (TextView) convertView.findViewById(R.id.reason_view);
                    holderHead.creditView = (TextView) convertView.findViewById(R.id.credit_view);
                    holderHead.replyBtn = (Button) convertView.findViewById(R.id.reply_btn);
                    convertView.setTag(holderHead);
                } else {
                    holderHead = (DetailViewHolder) convertView.getTag();
                }
                Prediction prediction = (Prediction) getItem(i);
                holderHead.authorView.setText(prediction.getAuthorName());
                holderHead.timeView.setText(XStringUtil.date2str(prediction.getCreatedTime()));
                holderHead.titleView.setText(prediction.getWhat()
                        + " " + prediction.getHow() + " " + prediction.getWhere()
                        + " " + prediction.getWhenYear() + "年"
                        + prediction.getWhenMonth() + "月"
                        + prediction.getWhenDay() + "日");
                holderHead.reasonView.setText(prediction.getReason());
                int creditPercent = (int) (prediction.getCredit() * 100);
                holderHead.creditView.setText(creditPercent + "%");
                holderHead.replyBtn.setText("" + prediction.getReplyCount());
                // 本地判断，一个人不能回复2次以上
                holderHead.replyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 回复
                    }
                });
                break;
            }
            // 回复
            case TYPE_REPLY: {
                ReplyViewHolder holderHead = null;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_reply, null);
                    holderHead = new ReplyViewHolder();
                    holderHead.senderView = (TextView) convertView.findViewById(R.id.sender_view);
                    holderHead.timeView = (TextView) convertView.findViewById(R.id.time_view);
                    holderHead.titleView = (TextView) convertView.findViewById(R.id.title_view);
                    holderHead.possibilityView = (TextView) convertView.findViewById(R.id.possibility_view);
                    holderHead.replyBtn = (Button) convertView.findViewById(R.id.reply_btn);
                    convertView.setTag(holderHead);
                } else {
                    holderHead = (ReplyViewHolder) convertView.getTag();
                }
                Reply reply = (Reply) getItem(i);
                holderHead.senderView.setText(reply.getSenderName());
                holderHead.timeView.setText(XStringUtil.date2str(reply.getCreatedTime()));
                holderHead.titleView.setText(reply.getReason());
                holderHead.possibilityView.setText("" + reply.getPossibility());
                holderHead.replyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 回复
                    }
                });
                break;
            }
        }
        return convertView;
    }
}
