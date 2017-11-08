package lt.riti.com.liantong.contract;

import java.util.List;

import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.presenter.BasePresenter;
import lt.riti.com.liantong.view.BaseView;

/**
 * Created by brander on 2017/9/24.
 */

public interface IRfidBucketContract {
    interface Model {
        void addBucket(UploadingBucket uploadingBucket, List<Bucket> buckets, ICallBack callBack);
    }

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void addBucketTask(UploadingBucket uploadingBucket,  List<Bucket> buckets);
    }
}
