package com.tomorrow.android.mgr;

import android.content.Context;
import com.tomorrow.android.data.cache.*;
import com.tomorrow.android.test.FakeData;
import com.xengine.android.data.cache.DefaultDataRepo;
import com.xengine.android.data.cache.XDataRepository;
import com.xengine.android.session.http.XHttp;
import com.xengine.android.session.http.apache.XApacheHttpClient;

import java.util.Calendar;

/**
 * 系统公共模块管理器
 * Created by jasontujun on 14-7-12.
 */
public class SystemMgr {

    private static class SingletonHolder {
        final static SystemMgr INSTANCE = new SystemMgr();
    }

    public static SystemMgr getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final String USER_AGENT = "tomorrow_android";
    private XHttp mHttp;

    private SystemMgr() {}

    /**
     * 程序第一次进来时调用此初始化方法。
     * @param context
     */
    public synchronized void init(Context context) {
        // 已经初始化过，则不用再次初始化
        if (mHttp != null)
            return;

        // 初始化httpClient
        mHttp = new XApacheHttpClient(context, USER_AGENT);
        // 初始化数据源
        XDataRepository repo = DefaultDataRepo.getInstance();
        repo.registerDataSource(new GlobalDataSource(context));
        repo.registerDataSource(new MyPredictionSource());
        repo.registerDataSource(new HistoryPredictionSource());
        repo.registerDataSource(new FuturePredictionSource());

        // TODO 假数据
        FuturePredictionSource futureSource = (FuturePredictionSource) repo.
                getSource(SourceName.FUTURE_PREDICTION);
        int[] tomorrow = getTomorrow();
        futureSource.addAll(FakeData.createPredictions(tomorrow[0], tomorrow[1], tomorrow[2], 20));
        HistoryPredictionSource historySource = (HistoryPredictionSource) repo.
                getSource(SourceName.HISTORY_PREDICTION);
        int[] yesterday = getYesterday();
        historySource.addAll(FakeData.createPredictions(yesterday[0],yesterday[1],yesterday[2], 20));
    }

    public XHttp getHttpClient() {
        return mHttp;
    }


    /**
     * 获取昨天的年月日
     */
    public int[] getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int[] result = new int[3];
        result[0] = calendar.get(Calendar.YEAR);
        result[1] = calendar.get(Calendar.MONTH) + 1;
        result[2] = calendar.get(Calendar.DATE);
        return result;
    }

    /**
     * 获取明天的年月日
     */
    public int[] getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        int[] result = new int[3];
        result[0] = calendar.get(Calendar.YEAR);
        result[1] = calendar.get(Calendar.MONTH) + 1;
        result[2] = calendar.get(Calendar.DATE);
        return result;
    }
}
