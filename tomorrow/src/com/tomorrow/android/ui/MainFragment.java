package com.tomorrow.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tomorrow.android.R;
import com.tomorrow.android.mgr.PredictionMgr;
import com.tomorrow.android.mgr.SystemMgr;

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
            }
        });
        myCollectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null)
                    return;
//                activity.showMyCollectionFragment();
            }
        });

        // 初始化日期
        int[] date = SystemMgr.getInstance().getTomorrow();
        mYearView.setText(("" + date[0]).substring(2));
        mMonthView.setText("" + date[1]);
        mDayView.setText("" + date[2]);

        return rootView;
    }
}
