package lt.riti.com.liantong.presenter;

import java.util.List;

import lt.riti.com.liantong.contract.IRfidBucketContract;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IRfidBucketModel;

/**
 * Created by brander on 2017/9/24.
 */

public class IRfidBucketPresenter implements IRfidBucketContract.Presenter {
    private IRfidBucketContract.View view;
    private IRfidBucketContract.Model model;

    public IRfidBucketPresenter(IRfidBucketContract.View view) {
        this.view = view;
        model = new IRfidBucketModel();
    }

    @Override
    public void addBucketTask(UploadingBucket uploadingBucket, List<Bucket> buckets) {
        model.addBucket(uploadingBucket,buckets, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showDescription((String) message);
            }

            @Override
            public void setFailure(Object message) {
                view.showDescription((String) message);

            }
        });
    }
}
