package lt.riti.com.liantong.model;

import android.util.Log;

import java.util.List;

import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.service.RfidUserDeleteService;
import lt.riti.com.liantong.service.RfidUserListService;
import lt.riti.com.liantong.service.RfidUserAddService;
import lt.riti.com.liantong.service.RfidUserUpdateService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/9/24.
 */

public class IRfidUserModel implements IRfidUserContract.Model {
    private static final String TAG = "IRfidUserModel";

//    @Override
//    public void addRfidUser(RfidUser rfidUser, final ICallBack callBack) {
//        Log.i(TAG, "addRfidUser: ");
//        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_USER_AL);
//        final RfidUserAddService request = retrofit.create(RfidUserAddService.class);
//        final Call<ResultCode<RfidUser>> call = request.call("",
//                rfidUser.getUserId(),
//                rfidUser.getRfidUserName(),
//                rfidUser.getRfidUserLocation()
//        );
//        call.enqueue(new MyCallback<ResultCode<RfidUser>>() {
//            @Override
//            public void onSuc(Response<ResultCode<RfidUser>> response) {
//                Log.i(TAG, "addRfidUser onSuc: " + response.body().getCode());
//                callBack.setSuccess(response.body().getMessage());
//            }
//
//            @Override
//            public void onFail(String message) {
//                Log.i(TAG, "addRfidUser onFail: " + message);
//                callBack.setFailure(message);
//            }
//        });
//    }

//    @Override
//    public void updateRfidUser(RfidUser rfidUser, final ICallBack callBack) {
//        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_USER_AL);
//        RfidUserUpdateService request = retrofit.create(RfidUserUpdateService.class);
//        final Call<ResultCode<RfidUser>> call = request.call(rfidUser.getRfidUserId(),
//                rfidUser.getUserId(),
//                rfidUser.getRfidUserName(),
//                rfidUser.getRfidUserLocation()
//        );
//        call.enqueue(new MyCallback<ResultCode<RfidUser>>() {
//            @Override
//            public void onSuc(Response<ResultCode<RfidUser>> response) {
//                Log.i(TAG, "addRfidUser onSuc: " + response.body().getCode());
//                callBack.setSuccess(response.body().getMessage());
//            }
//
//            @Override
//            public void onFail(String message) {
//                Log.i(TAG, "addRfidUser onFail: " + message);
//                callBack.setFailure(message);
//            }
//        });
//    }

//    @Override
//    public void deleteRfidUser(String id, final ICallBack callBack) {
//        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_USER_AL);
//        RfidUserDeleteService request = retrofit.create(RfidUserDeleteService.class);
//        Call<ResultCode<RfidUser>> call = request.call(id);
//        call.enqueue(new MyCallback<ResultCode<RfidUser>>() {
//            @Override
//            public void onSuc(Response<ResultCode<RfidUser>> response) {
//                Log.i(TAG, "onSuc: " + response.body().getMessage());
//                callBack.setSuccess(response.body().getMessage());
//            }
//
//            @Override
//            public void onFail(String message) {
//                callBack.setFailure(message);
//            }
//        });
//    }

    @Override
    public void getRfidUser(long id, final ICallBack callBack) {
        Log.i(TAG, "getRfidUser depot: " + id);
        Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_USER_AL);
        RfidUserListService request = retrofit.create(RfidUserListService.class);
        Call<ResultCode<List<RfidUser>>> call = request.call(id);
        call.enqueue(new MyCallback<ResultCode<List<RfidUser>>>() {
            @Override
            public void onSuc(Response<ResultCode<List<RfidUser>>> response) {
                Log.i(TAG, "user onSuc: "+response.body().getResult());
                callBack.setSuccess(response.body().getResult());
            }

            @Override
            public void onFail(String message) {
                callBack.setFailure(message);
            }
        });
    }
}
