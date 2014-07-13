package com.tomorrow.android.api;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.tomorrow.android.R;
import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.Reply;
import com.tomorrow.android.data.model.Result;
import com.tomorrow.android.data.model.User;
import com.tomorrow.android.mgr.SystemMgr;
import com.tomorrow.android.utils.GsonUtil;
import com.xengine.android.session.http.XHttp;
import com.xengine.android.session.http.XHttpRequest;
import com.xengine.android.session.http.XHttpResponse;
import com.xengine.android.utils.XLog;
import com.xengine.android.utils.XStringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jasontujun on 14-7-12.
 */
public class SessionApi {

    private static final String TAG = "API";

    /**
     * 登录
     * @param context
     * @param userId
     * @param password
     * @param result 返回的User对象
     * @return
     */
    public static int login(Context context, String userId,
                            String password, User result) {
        if (context == null
                || TextUtils.isEmpty(userId)
                || TextUtils.isEmpty(password)
                || result == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.login);
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.POST)
                .addStringParam("userId", userId)
                .addStringParam("password", password);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "登陆返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "登陆返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    User user = (User) GsonUtil.toObject(cnt, User.class);
                    if (user != null) {
                        XLog.d(TAG, "解析User, userId:" + user.getUserId()
                                + ",userName:" + user.getUserName()
                                + ",credit:" + user.getCredit()
                                + ",createdTime:" + user.getCreatedTime());
                        result.copy(user);
                    } else {
                        XLog.d(TAG, "解析User为null");
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 注册
     * @param context
     * @param userId
     * @param password
     * @param userName
     * @return
     */
    public static int register(Context context, String userId,
                               String password, String userName) {
        if (context == null
                || TextUtils.isEmpty(userId)
                || TextUtils.isEmpty(password))
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.register);
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.POST)
                .addStringParam("userId", userId)
                .addStringParam("password", password)
                .addStringParam("userName", userName);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        int resultCode = response.getStatusCode();
        XLog.d(TAG, "注册返回码：" + resultCode);
        response.consumeContent();
        return resultCode;
    }

    /**
     * 发布预测
     * @param context
     * @param prediction
     * @return
     */
    public static int publishPrediction(Context context, Prediction prediction) {
        if (context == null
                || prediction == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.publish_prediction);
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.POST)
                .addStringParam("what", prediction.getWhat())
                .addStringParam("how", prediction.getHow())
                .addStringParam("where", prediction.getWhere())
                .addStringParam("whenYear", "" + prediction.getWhenYear())
                .addStringParam("whenMonth", "" + prediction.getWhenMonth())
                .addStringParam("whenDay", "" + prediction.getWhenDay())
                .addStringParam("reason", prediction.getReason())
                .addStringParam("authorId", prediction.getAuthorId())
                .addStringParam("lat", "" + prediction.getPublishLatitude())
                .addStringParam("lon", "" + prediction.getPublishLongitude());
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "发布预测返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "发布预测返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    try {
                        JSONObject jsonObject =  new JSONObject(cnt);
                        String createdTime = jsonObject.getString("createdTime");
                        String predictionId = jsonObject.getString("predictionId");
                        XLog.d(TAG, "解析返回内容, createdTime：" + createdTime
                                + ",publishId：" + predictionId );
                        prediction.setPredictionId(predictionId);
                        prediction.setCreatedTime(Long.parseLong(createdTime));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 回复预测
     * @param context
     * @param reply
     * @return
     */
    public static int replyPrediction(Context context, Reply reply) {
        if (context == null
                || reply == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.reply_prediction);
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.POST)
                .addStringParam("predictionId", reply.getPredictionId())
                .addStringParam("senderId", reply.getSenderId())
                .addStringParam("receiverId", reply.getReceiverId())
                .addStringParam("possibility", "" + reply.getPossibility())
                .addStringParam("reason", reply.getReason());
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "回复预测返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "回复预测返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    try {
                        JSONObject jsonObject =  new JSONObject(cnt);
                        String createdTime = jsonObject.getString("createdTime");
                        String replyId = jsonObject.getString("replyId");
                        XLog.d(TAG, "解析返回内容, createdTime：" + createdTime
                                + ",replyId：" + replyId );
                        reply.setReplyId(replyId);
                        reply.setCreatedTime(Long.parseLong(createdTime));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 验证预测
     * @param context
     * @param result
     * @return
     */
    public static int checkPrediction(Context context, Result result) {
        if (context == null
                || result == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.check_prediction);
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.POST)
                .addStringParam("predictionId", result.getPredictionId())
                .addStringParam("senderId", result.getSenderId())
                .addStringParam("accuracy", "" + result.getAccuracy());
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "验证预测返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "验证预测返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    try {
                        JSONObject jsonObject =  new JSONObject(cnt);
                        String createdTime = jsonObject.getString("createdTime");
                        String  resultId = jsonObject.getString("resultId");
                        XLog.d(TAG, "解析返回内容, createdTime：" + createdTime
                                + ", resultId：" +  resultId );
                        result.setResultId(resultId);
                        result.setCreatedTime(Long.parseLong(createdTime));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 获取我的预测
     * @param context
     * @param userId
     * @param offset
     * @param size
     * @param results
     * @return
     */
    public static int getMyPrediction(Context context, String userId,
                                      int offset, int size,
                                      List<Prediction> results) {
        return getPredictionList(context, userId, offset, size, results,
                R.string.get_my_prediction);
    }

    /**
     * 获取我的回复过的预测列表
     * @param context
     * @param userId
     * @param offset
     * @param size
     * @param results
     * @return
     */
    public static int getCollectedPrediction(Context context, String userId,
                                             int offset, int size,
                                             List<Prediction> results) {
        return getPredictionList(context, userId, offset, size, results,
                R.string.get_collected_prediction);
    }

    /**
     * 获取别人预测的列表
     * @param context
     * @param userId 用户id(游客为null)
     * @param offset
     * @param size
     * @param results
     * @return
     */
    public static int getOtherPrediction(Context context, String userId,
                                         int offset, int size,
                                         int year, int month, int day,
                                         List<Prediction> results) {if (context == null
            || TextUtils.isEmpty(userId)
            || offset < 0 || size < 0
            || results == null)
        return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.get_other_prediction)
                + "?userId=" + userId
                + "&offset=" + offset
                + "&size=" + size
                + "&year=" + year
                + "&month=" + month
                + "&day=" + day;
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.GET);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "获取预测列表返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "获取预测列表返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    Type type = new TypeToken<List<Prediction>>(){}.getType();
                    List<Prediction> list = (List<Prediction>) GsonUtil.toObjectArray(cnt, type);
                    if (list != null) {
                        for (Prediction p : list)
                            XLog.d(TAG, "解析结果, predicitonId:" + p.getPredictionId()
                                    + ",what:" + p.getWhat()
                                    + ",where:" + p.getWhere()
                                    + ",how:" + p.getHow());
                        results.addAll(list);
                    } else {
                        XLog.d(TAG, "解析结果为null");
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 获取我的回复过的预测
     * @param context
     * @param userId
     * @param offset
     * @param size
     * @param results
     * @return
     */
    private static int getPredictionList(Context context, String userId,
                                         int offset, int size,
                                         List<Prediction> results,
                                         int apiUrl) {
        if (context == null
                || TextUtils.isEmpty(userId)
                || offset < 0 || size < 0
                || results == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(apiUrl)
                + "?userId=" + userId
                + "&offset=" + offset
                + "&size=" + size;
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.GET);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "获取预测列表返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "获取预测列表返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    Type type = new TypeToken<List<Prediction>>(){}.getType();
                    List<Prediction> list = (List<Prediction>) GsonUtil.toObjectArray(cnt, type);
                    if (list != null) {
                        for (Prediction p : list)
                            XLog.d(TAG, "解析结果, predicitonId:" + p.getPredictionId()
                                    + ",what:" + p.getWhat()
                                    + ",where:" + p.getWhere()
                                    + ",how:" + p.getHow());
                        results.addAll(list);
                    } else {
                        XLog.d(TAG, "解析结果为null");
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }


    /**
     * 获取预测的回复和结果
     * @param context
     * @param userId 用户id(游客为null)
     * @param predictionId
     * @param result
     * @return
     */
    public static int getPredictionReply(Context context, String userId,
                                         String predictionId, Prediction result) {
        if (context == null
                || TextUtils.isEmpty(predictionId)
                || TextUtils.isEmpty(userId)
                || result == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.get_prediction_reply)
                + "?userId=" + userId
                + "&predictionId=" + predictionId;
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.GET);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "获取预测回复和结果返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "获取预测回复和结果返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    try {
                        JSONObject jsonObject = new JSONObject(cnt);
                        String listReplyStr = jsonObject.getString("listReply");
                        Type replyType = new TypeToken<List<Reply>>(){}.getType();
                        List<Reply> replyList = (List<Reply>) GsonUtil.toObjectArray(listReplyStr, replyType);
                        XLog.d(TAG, "获取预测回复和结果,listReply内容：" + listReplyStr);
                        String  listResultStr = jsonObject.getString("listResult");
                        Type resultType = new TypeToken<List<Result>>(){}.getType();
                        List<Result> resultList = (List<Result>) GsonUtil.toObjectArray(listResultStr, resultType);
                        XLog.d(TAG, "获取预测回复和结果,listResult内容：" + listResultStr);
                        if (replyList != null)
                            result.setReplyList(replyList);
                        if (resultList != null)
                            result.setResultList(resultList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }

    /**
     * 获取预测详情
     * @param context
     * @param userId
     * @param predictionId
     * @param result
     * @return
     */
    public static int getPredictionDetail(Context context, String userId,
                                          String predictionId, Prediction result) {
        if (context == null
                || TextUtils.isEmpty(predictionId)
                || TextUtils.isEmpty(userId)
                || result == null)
            return StatusCode.HTTP_LOCAL_ERROR_ARGUMENT;

        XHttp httpClient = SystemMgr.getInstance().getHttpClient();
        if (httpClient == null)
            return StatusCode.HTTP_EXCEPTION;

        String url = context.getString(R.string.host)
                + context.getString(R.string.get_prediction_detail)
                + "?userId=" + userId
                + "&predictionId=" + predictionId;
        XLog.d(TAG, "请求url：" + url);
        XHttpRequest request = httpClient
                .newRequest(url)
                .setMethod(XHttpRequest.HttpMethod.GET);
        XHttpResponse response = httpClient.execute(request);

        if (response == null)
            return StatusCode.HTTP_EXCEPTION;

        try {
            int resultCode = response.getStatusCode();
            XLog.d(TAG, "获取预测详情返回码：" + resultCode);
            if (StatusCode.success(resultCode)) {
                InputStream is = response.getContent();
                if (is == null)
                    return resultCode;
                String cnt = XStringUtil.convertStreamToString(is).replaceAll("\\\\r", "");
                XLog.d(TAG, "获取预测详情返回内容：" + cnt);
                if (!TextUtils.isEmpty(cnt)) {
                    try {
                        JSONObject jsonObject = new JSONObject(cnt);
                        String predictionStr = jsonObject.getString("prediction");
                        Prediction prediction = (Prediction) GsonUtil.toObject(predictionStr, Prediction.class);
                        XLog.d(TAG, "获取预测详情,prediction内容：" + predictionStr);
                        String listReplyStr = jsonObject.getString("listReply");
                        Type replyType = new TypeToken<List<Reply>>(){}.getType();
                        List<Reply> replyList = (List<Reply>) GsonUtil.toObjectArray(listReplyStr, replyType);
                        XLog.d(TAG, "获取预测详情,listReply内容：" + listReplyStr);
                        String  listResultStr = jsonObject.getString("listResult");
                        Type resultType = new TypeToken<List<Result>>(){}.getType();
                        List<Result> resultList = (List<Result>) GsonUtil.toObjectArray(listResultStr, resultType);
                        XLog.d(TAG, "获取预测详情,listResult内容：" + listResultStr);
                        // 赋值给返回值
                        result.copy(prediction);
                        if (replyList != null)
                            result.setReplyList(replyList);
                        if (resultList != null)
                            result.setResultList(resultList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        } finally {
            response.consumeContent();
        }
    }
}
