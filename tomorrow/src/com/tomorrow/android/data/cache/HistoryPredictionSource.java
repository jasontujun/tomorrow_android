package com.tomorrow.android.data.cache;

import com.tomorrow.android.data.model.Prediction;
import com.xengine.android.data.cache.XBaseAdapterIdDataSource;

/**
 * <pre>
 * User: jasontujun
 * Date: 14-8-28
 * Time: 下午4:26
 * </pre>
 */
public class HistoryPredictionSource extends XBaseAdapterIdDataSource<Prediction> {

    @Override
    public String getSourceName() {
        return SourceName.HISTORY_PREDICTION;
    }

    @Override
    public String getId(Prediction prediction) {
        return prediction.getPredictionId();
    }
}
