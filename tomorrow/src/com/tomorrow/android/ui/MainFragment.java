package com.tomorrow.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tomorrow.android.R;
import com.tomorrow.android.mgr.PredictionMgr;

import java.util.Calendar;

/**
 * Created by jasontujun on 14-7-5.
 */
public class MainFragment extends Fragment {

    private TextView mYearView, mMonthView, mDayView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mYearView = (TextView) rootView.findViewById(R.id.year_view);
        mMonthView = (TextView) rootView.findViewById(R.id.month_view);
        mDayView = (TextView) rootView.findViewById(R.id.day_view);
        View publishView = rootView.findViewById(R.id.publish_view);
        View myPredictionView = rootView.findViewById(R.id.my_prediction_view);
        View myCollectionView = rootView.findViewById(R.id.my_collection_view);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null)
                    return;
//                activity.switchScreen(MainActivity.FRAGMENT_OTHER);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;// 月从1开始
                int day = calendar.get(Calendar.DATE);
                PredictionMgr.getInstance().getOtherPredictionList(getActivity(),
                        year, month, day, 0, 20);
            }
        };
        mYearView.setOnClickListener(listener);
        mMonthView.setOnClickListener(listener);
        mDayView.setOnClickListener(listener);

        publishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null)
                    return;
                activity.showPublishFragment();
            }
        });
        myPredictionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null)
                    return;
//                activity.showMyPredictionFragment();
                PredictionMgr.getInstance().getMyPredictionList(getActivity(),
                        0, 20);
            }
        });
        myCollectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null)
                    return;
//                activity.showMyCollectionFragment();
//                PredictionMgr.getInstance().getPredictionDetail(getActivity(), "40280c81471f75f701471f77df3a0001");
                PredictionMgr.getInstance().getPredictionReply(getActivity(), "40280c81471f75f701471f77df3a0001");
            }
        });

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
