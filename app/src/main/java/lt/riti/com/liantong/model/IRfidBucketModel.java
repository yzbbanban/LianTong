package lt.riti.com.liantong.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IRfidBucketContract;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.service.RfidBucketAddService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/9/24.
 */

public class IRfidBucketModel implements IRfidBucketContract.Model {
    //用于上传
    private List<Bucket> orderList = new ArrayList<>();
    private static final String TAG = "IRfidBucketModel";
    private int status;

    @Override
    public void addBucket(UploadingBucket uploadingBucket, List<Bucket> buckets, final ICallBack callBack) {
        Log.i(TAG, "addBucket: " + buckets);
        orderList.clear();
        //最好在线程执行
        if (uploadingBucket.getManufactor_id() != 0 && uploadingBucket.getBucket_address() == 0) {//新桶
            orderList = setBucket(uploadingBucket, buckets);
            status = 0;
        } else if (!"".equals(uploadingBucket.getProduct_code()) && uploadingBucket.getBucket_address() == 1) {//产品
            orderList = setBucket(uploadingBucket, buckets);
            status = 1;
        } else if (uploadingBucket.getCustomer_id() != 0L && uploadingBucket.getBucket_address() == 2) {//客户
            orderList = setBucket(uploadingBucket, buckets);
            status = 2;
        } else if (uploadingBucket.getBucket_address() == 3) {//回收
            orderList = setBucket(uploadingBucket, buckets);
            status = 3;
        }
        Log.i(TAG, "orderList addBucket: " + orderList);
        if (orderList != null && orderList.size() > 0) {
            Gson gson = new Gson();
            String orderJs = gson.toJson(orderList);
            Log.i(TAG, "addBucket: " + orderJs);
            Retrofit retrofit = null;
            switch (status) {
                case 0:
                    retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_ORDER_AL);
                    break;
                case 1:
                    retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_ORDER_AL);
                    break;
                case 2:
                    retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_ORDER_AL);
                    break;
                case 3:
                    retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_ORDER_AL);
                    break;
            }

            RfidBucketAddService request = retrofit.create(RfidBucketAddService.class);
            final Call<ResultCode<String>> call = request.call(orderJs);
            call.enqueue(new MyCallback<ResultCode<String>>() {
                @Override
                public void onSuc(Response<ResultCode<String>> response) {
                    Log.i(TAG, "onSuc: " + response.body().getMessage());
                    callBack.setSuccess(response.body().getMessage());
                }

                @Override
                public void onFail(String message) {
                    Log.i(TAG, "onFail: " + message);
                    callBack.setSuccess(message);
                }
            });
        } else {
            callBack.setFailure("没有选中数据");
        }
    }

    /**
     * 设置吨桶值
     *
     * @param uploadingBucket
     * @param buckets
     * @return
     */
    private List<Bucket> setBucket(UploadingBucket uploadingBucket, List<Bucket> buckets) {
        Log.i(TAG, "setBucket: "+buckets);
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).getChecked()) {
                Log.i(TAG, "isChecked: ");
                Bucket bucket = buckets.get(i);
                bucket.setAdmin_id(StockApplication.USER_ID);
                bucket.setDepot_code(uploadingBucket.getDepot_code());
                bucket.setCustomer_id(uploadingBucket.getCustomer_id());
                bucket.setManufactor_id(uploadingBucket.getManufactor_id());
                bucket.setStatus(uploadingBucket.getStatus());
                bucket.setOutInStatus(uploadingBucket.getOutInStatus());
                bucket.setBucket_address(uploadingBucket.getBucket_address());
                orderList.add(buckets.get(i));
            }
        }
        return orderList;
    }
}