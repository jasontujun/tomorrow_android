package com.tomorrow.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.tomorrow.android.R;

import java.util.Calendar;

/**
 * Created by jasontujun on 14-7-6.
 */
public class PublishFragment extends Fragment {

    private EditText mWhatView, mHowView, mWhereView, mReasonView;
    private TextView mYearView, mMonthView, mDayView, mCreditView;
    private Button mPublishBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_publish, container, false);
        mWhatView = (EditText) rootView.findViewById(R.id.what_input);
        mHowView = (EditText) rootView.findViewById(R.id.how_input);
        mWhereView = (EditText) rootView.findViewById(R.id.where_input);
        mReasonView = (EditText) rootView.findViewById(R.id.reason_input);
        mYearView = (TextView) rootView.findViewById(R.id.year_view);
        mMonthView = (TextView) rootView.findViewById(R.id.month_view);
        mDayView = (TextView) rootView.findViewById(R.id.day_view);
        mCreditView = (TextView) rootView.findViewById(R.id.credit_view);
        mPublishBtn = (Button) rootView.findViewById(R.id.publish_btn);

        // 初始化日期
        initDate();

        return rootView;
    }

    /**
     * 初始化日期(明天)
     */
    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String year = "" + calendar.get(Calendar.YEAR);
        String month = "" + (calendar.get(Calendar.MONTH) + 1);// 月从0开始计算
        String day = "" + calendar.get(Calendar.DATE);
        mYearView.setText(year.substring(2));
        mMonthView.setText(month);
        mDayView.setText(day);
    }
}
