package lt.riti.com.liantong.model;

import android.util.Log;

import java.util.List;

import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IUpdateContract;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.service.RfidUpdateService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/11/26.
 */

public class IUpdateModel implements IUpdateContract.Model {
    private static final String TAG = "IUpdateModel";

    @Override
    public void update(String version, final ICallBack callBack) {
        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_UPDATE);
        RfidUpdateService request = retrofit.create(RfidUpdateService.class);
        Call<ResultCode<String>> call = request.call(version);
        call.enqueue(new MyCallback<ResultCode<String>>() {
            @Override
            public void onSuc(Response<ResultCode<String>> response) {
                Log.i(TAG, "onSuc: " + response.body().getResult());
                callBack.setSuccess(response.body().getResult());
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
