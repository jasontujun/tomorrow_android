package com.tomorrow.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.api.SessionApi;
import com.tomorrow.android.api.StatusCode;
import com.tomorrow.android.data.cache.GlobalDataSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.User;
import com.xengine.android.data.cache.DefaultDataRepo;

import java.util.Calendar;

/**
 * Created by jasontujun on 14-7-6.
 */
public class PublishFragment extends Fragment {

    private EditText mWhatView, mHowView, mWhereView, mReasonView;
    private TextView mYearView, mMonthView, mDayView, mCreditView;
    private Button mPublishBtn;
    private int year, month, day;

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

        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String what = mWhatView.getText() == null ?
                        null : mWhatView.getText().toString();
                final String how = mHowView.getText() == null ?
                        null : mHowView.getText().toString();
                final String where = mWhereView.getText() == null ?
                        null : mWhereView.getText().toString();
                final String reason = mReasonView.getText() == null ?
                        null : mReasonView.getText().toString();

                GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                        .getInstance().getSource(SourceName.GLOBAL_DATA);
                User user = globalDataSource.getCurrentUser();
                if (user == null) {
                    Toast.makeText(getActivity(), R.string.login_request, Toast.LENGTH_SHORT).show();
                    return;
                }
                final Prediction prediction = new Prediction();
                prediction.setWhat(what);
                prediction.setHow(how);
                prediction.setWhere(where);
                prediction.setWhenYear(year);
                prediction.setWhenMonth(month);
                prediction.setWhenDay(day);
                prediction.setReason(reason);
                prediction.setAuthorId(user.getUserId());

                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... voids) {
                        if (getActivity() == null)
                            return -1;
                        return SessionApi.publishPrediction(getActivity(), prediction);
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if (getActivity() == null)
                            return;

                        if (StatusCode.success(result)) {
                           Toast.makeText(getActivity(), R.string.publish_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });

        return rootView;
    }

    /**
     * 初始化日期(明天)
     */
    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;// 月从1开始
        day = calendar.get(Calendar.DATE);
        mYearView.setText(("" + year).substring(2));
        mMonthView.setText("" + month);
        mDayView.setText("" + day);
    }
}
