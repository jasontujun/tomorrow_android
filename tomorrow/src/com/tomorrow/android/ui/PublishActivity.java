package com.tomorrow.android.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
 * 发布预测界面
 * Created by jasontujun on 14-7-6.
 */
public class PublishActivity extends Activity {

    private EditText mWhatView, mHowView, mWhereView, mReasonView;
    private TextView mYearView, mMonthView, mDayView, mCreditView;
    private Button mPublishBtn;
    private int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.fragment_publish);

        mWhatView = (EditText) findViewById(R.id.what_input);
        mHowView = (EditText) findViewById(R.id.how_input);
        mWhereView = (EditText) findViewById(R.id.where_input);
        mReasonView = (EditText) findViewById(R.id.reason_input);
        mYearView = (TextView) findViewById(R.id.year_view);
        mMonthView = (TextView) findViewById(R.id.month_view);
        mDayView = (TextView) findViewById(R.id.day_view);
        mCreditView = (TextView) findViewById(R.id.credit_view);
        mPublishBtn = (Button) findViewById(R.id.publish_btn);

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
                    Toast.makeText(PublishActivity.this, R.string.login_request, Toast.LENGTH_SHORT).show();
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
                        if (PublishActivity.this.isFinishing())
                            return -1;
                        return SessionApi.publishPrediction(PublishActivity.this, prediction);
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if (PublishActivity.this.isFinishing())
                            return;

                        if (StatusCode.success(result)) {
                           Toast.makeText(PublishActivity.this, R.string.publish_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PublishActivity.this, StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
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
