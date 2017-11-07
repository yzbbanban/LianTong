package lt.riti.com.liantong.model;

import android.util.Log;

import java.util.List;

import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IRfidManufactorContract;
import lt.riti.com.liantong.entity.Manufacture;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.service.RfidManufactorListService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/11/7.
 */

public class IRfidManufactorModel implements IRfidManufactorContract.Model {
    private static final String TAG = "IRfidManufactorModel";
    @Override
    public void getRfidManufactor(String id, final ICallBack callBack) {
        Retrofit retrofit= RetrofitUtils.getRetrofit(Urls.COOL_RFID_MANUFACTOR_AL);
        final RfidManufactorListService request = retrofit.create(RfidManufactorListService.class);
        final Call<ResultCode<List<Manufacture>>> call = request.call(id);
        call.enqueue(new MyCallback<ResultCode<List<Manufacture>>>() {
            @Override
            public void onSuc(Response<ResultCode<List<Manufacture>>> response) {
                Log.i(TAG, "onSuc: "+response.body().getResult());
                callBack.setSuccess(response.body().getResult());
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
