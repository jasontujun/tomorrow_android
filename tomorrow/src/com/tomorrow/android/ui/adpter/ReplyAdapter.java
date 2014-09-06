package com.tomorrow.android.ui.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.tomorrow.android.R;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.Reply;
import com.xengine.android.utils.XStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasontujun on 14-9-6.
 */
public class ReplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<Reply> mReplyList;

    public ReplyAdapter(Context context) {
        mContext = context;
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
        return mReplyList.size();
    }

    @Override
    public Object getItem(int i) {
        return mReplyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ReplyViewHolder {
        public TextView senderView;
        public TextView titleView;
        public TextView possibilityView;
        public ImageView replyBtn;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ReplyViewHolder holderHead = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_reply, null);
            holderHead = new ReplyViewHolder();
            holderHead.senderView = (TextView) convertView.findViewById(R.id.sender_view);
            holderHead.titleView = (TextView) convertView.findViewById(R.id.title_view);
            holderHead.possibilityView = (TextView) convertView.findViewById(R.id.possibility_view);
            holderHead.replyBtn = (ImageView) convertView.findViewById(R.id.reply_btn);
            convertView.setTag(holderHead);
        } else {
            holderHead = (ReplyViewHolder) convertView.getTag();
        }
        Reply reply = (Reply) getItem(i);
        holderHead.senderView.setText(reply.getSenderName());
        holderHead.titleView.setText(reply.getReason());
        holderHead.possibilityView.setText("" + reply.getPossibility());
        holderHead.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 回复
            }
        });
        return convertView;
    }
}
