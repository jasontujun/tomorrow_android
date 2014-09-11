package com.tomorrow.android.test;

import com.tomorrow.android.data.model.Prediction;
import com.tomorrow.android.data.model.Reply;
import com.tomorrow.android.data.model.Result;
import com.tomorrow.android.mgr.SystemMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * User: jasontujun
 * Date: 14-9-2
 * Time: 下午6:06
 * </pre>
 */
public class FakeData {

    private static final String[][] P_CONTENT = {{"有人", "放火", "在市政府", "举报有奖..."},
            {"...", "下大雨哦", "在上海", "大数据分析"},
            {"腾讯股票", "大涨", "...", "内幕消息哦"},
            {"XXX女神", "吃午饭", "中午到9食堂", "来源不便透露，嘿嘿"}};// what, how, where, reason
    private static final String[] R_CONTENT = {"呵呵...", "鬼才信哦", "不明觉厉！",
            "...", "路过...", "打酱油的...", "有点道理,嗯"};
    private static final String[][] AUTHORS = {{"AID001", "Jason"},
            {"AID002", "Ken"}, {"AID003", "Marry"}, {"AID004", "Peter"}};// id, name

    public static List<Prediction> createPredictions(int whenYear,
                                                     int whenMonth,
                                                     int whenDay,
                                                     int count) {
        List<Prediction> predictions = new ArrayList<Prediction>();
        for (int i = 0; i < count; i++)
            predictions.add(createPrediction(whenYear, whenMonth, whenDay));
        return predictions;
    }

    public static Prediction createPrediction(int whenYear,
                                              int whenMonth,
                                              int whenDay) {
        Prediction prediction = new Prediction();
        prediction.setWhenYear(whenYear);
        prediction.setWhenMonth(whenMonth);
        prediction.setWhenDay(whenDay);
        prediction.setPredictionId("PID" + System.currentTimeMillis());
        int cIndex = (int) (Math.random() * P_CONTENT.length);
        prediction.setWhat(P_CONTENT[cIndex][0]);
        prediction.setHow(P_CONTENT[cIndex][1]);
        prediction.setWhere(P_CONTENT[cIndex][2]);
        prediction.setReason(P_CONTENT[cIndex][3]);
        int aIndex = (int) (Math.random() * AUTHORS.length);
        prediction.setAuthorId(AUTHORS[aIndex][0]);
        prediction.setAuthorName(AUTHORS[aIndex][1]);
        prediction.setCredit(Math.random());
        prediction.setCreatedTime(System.currentTimeMillis());
        prediction.setScanCount((int) (Math.random() * 500));
        // 构造回复数据
        int replyCount = (int) (Math.random() * 50);
        List<Reply> replyList = new ArrayList<Reply>();
        for (int i = 0; i < replyCount; i++)
            replyList.add(createRely(prediction.getPredictionId(),
                    prediction.getAuthorId(),
                    prediction.getAuthorName()));
        prediction.setReplyCount(replyCount);
        prediction.setReplyList(replyList);
        // 构造Result
        if (Math.random() < 0.3) {
            int resultCount = (int) (Math.random() * 10);
            List<Result> resultList = new ArrayList<Result>();
            for (int i = 0; i < resultCount; i++)
                resultList.add(createResult(prediction.getPredictionId()));
            prediction.setResultCount(resultCount);
            prediction.setResultList(resultList);
        }
        return prediction;
    }

    private static Reply createRely(String predictionId,
                                    String receiverId,
                                    String receiverName) {
        Reply reply = new Reply();
        reply.setPredictionId(predictionId);
        reply.setReplyId("RpID" + System.currentTimeMillis());
        reply.setReceiverId(receiverId);
        reply.setReceiverName(receiverName);
        int aIndex = (int) (Math.random() * AUTHORS.length);
        reply.setSenderId(AUTHORS[aIndex][0]);
        reply.setSenderName(AUTHORS[aIndex][1]);
        reply.setPossibility((int) (Math.random() * 5) + 1);
        reply.setReason(R_CONTENT[(int) (Math.random() * R_CONTENT.length)]);
        reply.setCreatedTime(System.currentTimeMillis());
        return reply;
    }

    private static Result createResult(String predictionId) {
        Result result = new Result();
        result.setPredictionId(predictionId);
        result.setResultId("RtID" + System.currentTimeMillis());
        int aIndex = (int) (Math.random() * AUTHORS.length);
        result.setSenderId(AUTHORS[aIndex][0]);
        result.setSenderName(AUTHORS[aIndex][1]);
        result.setAccuracy(Math.random());
        result.setCreatedTime(System.currentTimeMillis());
        return result;
    }
}
