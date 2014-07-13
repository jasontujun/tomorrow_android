package com.tomorrow.android.mgr;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.api.SessionApi;
import com.tomorrow.android.api.StatusCode;
import com.tomorrow.android.data.cache.GlobalDataSource;
import com.tomorrow.android.data.cache.MyPredictionSource;
import com.tomorrow.android.data.cache.OtherPredictionSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.User;
import com.xengine.android.data.cache.DefaultDataRepo;
import com.xengine.android.utils.XLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasontujun on 14-7-13.
 */
public class PredictionMgr {

    private static class SingletonHolder {
        final static PredictionMgr INSTANCE = new PredictionMgr();
    }

    public static PredictionMgr getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public boolean getOtherPredictionList(final Context context,
                                          final int year, final int month,
                                          final int day,
                                          final int offset, final int size) {
        if (context == null)
            return false;

        GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                .getInstance().getSource(SourceName.GLOBAL_DATA);
        User user = globalDataSource.getCurrentUser();
        final String userId = user == null ? null : user.getUserId();

        new AsyncTask<Void, Void, Integer>() {
            List<Prediction> predictions;

            @Override
            protected Integer doInBackground(Void... voids) {
                if (context == null)
                    return -1;
                predictions = new ArrayList<Prediction>();
                return SessionApi.getOtherPrediction(context, userId, offset, size,
                        year, month, day, predictions);
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (StatusCode.success(result)) {
                    Toast.makeText(context, R.string.get_prediction_list_success, Toast.LENGTH_SHORT).show();
                    OtherPredictionSource otherSource = (OtherPredictionSource) DefaultDataRepo
                            .getInstance().getSource(SourceName.OTHER_PREDICTION);
                    otherSource.addAll(predictions);
                    XLog.d("API", "获取别人的prediction数量为:" + predictions.size());
                } else {
                    Toast.makeText(context, StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        return true;
    }



    public boolean getMyPredictionList(final Context context,
                                          final int offset, final int size) {
        if (context == null)
            return false;

        GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                .getInstance().getSource(SourceName.GLOBAL_DATA);
        User user = globalDataSource.getCurrentUser();
        final String userId = user == null ? null : user.getUserId();

        new AsyncTask<Void, Void, Integer>() {
            List<Prediction> predictions;

            @Override
            protected Integer doInBackground(Void... voids) {
                predictions = new ArrayList<Prediction>();
                return SessionApi.getMyPrediction(context, userId,
                        offset, size, predictions);
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (StatusCode.success(result)) {
                    Toast.makeText(context, R.string.get_prediction_list_success, Toast.LENGTH_SHORT).show();
                    MyPredictionSource otherSource = (MyPredictionSource) DefaultDataRepo
                            .getInstance().getSource(SourceName.MY_PREDICTION);
                    otherSource.addAll(predictions);
                    XLog.d("API", "获取我的prediction数量为:" + predictions.size());
                } else {
                    Toast.makeText(context, StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        return true;
    }



    public boolean getPredictionReply(final Context context,
                                       final String predictionId) {
        if (context == null)
            return false;

        GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                .getInstance().getSource(SourceName.GLOBAL_DATA);
        User user = globalDataSource.getCurrentUser();
        if (user == null)
            return false;
        final String userId = user.getUserId();

        new AsyncTask<Void, Void, Integer>() {
            Prediction prediction;

            @Override
            protected Integer doInBackground(Void... voids) {
                prediction = new Prediction();
                return SessionApi.getPredictionReply(context, userId, predictionId, prediction);
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (StatusCode.success(result)) {
                    Toast.makeText(context, R.string.get_prediction_detail_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        return true;
    }


    public boolean getPredictionDetail(final Context context,
                                       final String predictionId) {
        if (context == null)
            return false;

        GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                .getInstance().getSource(SourceName.GLOBAL_DATA);
        User user = globalDataSource.getCurrentUser();
        if (user == null)
            return false;
        final String userId = user.getUserId();

        new AsyncTask<Void, Void, Integer>() {
            Prediction prediction;

            @Override
            protected Integer doInBackground(Void... voids) {
                prediction = new Prediction();
                return SessionApi.getPredictionDetail(context, userId, predictionId, prediction);
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (StatusCode.success(result)) {
                    Toast.makeText(context, R.string.get_prediction_detail_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        return true;
    }
}
