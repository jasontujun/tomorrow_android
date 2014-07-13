package com.tomorrow.android.data.cache;

import com.tomorrow.android.data.model.Prediction;
import com.xengine.android.data.cache.XBaseAdapterIdDataSource;

/**
 * 别人预测的数据源
 * Created by jasontujun on 14-7-5.
 */
public class OtherPredictionSource extends XBaseAdapterIdDataSource<Prediction> {
    @Override
    public String getSourceName() {
        return SourceName.OTHER_PREDICTION;
    }

    @Override
    public String getId(Prediction prediction) {
        return prediction.getPredictionId();
    }
}
