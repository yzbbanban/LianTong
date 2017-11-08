package lt.riti.com.liantong.model;

import android.util.Log;

import java.util.List;

import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IRfidProductContract;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.service.RfidProductListService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/11/7.
 */

public class IRfidProductModel implements IRfidProductContract.Model {
    private static final String TAG = "IRfidProductModel";
    @Override
    public void getRfidProduct(String id,final ICallBack callBack) {
        Retrofit retrofit= RetrofitUtils.getRetrofit(Urls.COOL_RFID_PRODUCT_AL);
        RfidProductListService request = retrofit.create(RfidProductListService.class);
        Call<ResultCode<List<Product>>> call = request.call(id);
        call.enqueue(new MyCallback<ResultCode<List<Product>>>() {
            @Override
            public void onSuc(Response<ResultCode<List<Product>>> response) {
                Log.i(TAG, "onSuc: "+response.body().getResult());
                callBack.setSuccess(response.body().getResult());
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
