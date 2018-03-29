package lt.riti.com.liantong.model;

import android.util.Log;

import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.ILoginContract;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.service.LoginService;
import lt.riti.com.liantong.util.LogUtil;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by brander on 2017/9/24.
 */

public class ILoginModel implements ILoginContract.Model {
    private static final String TAG = "ILoginModel";

    @Override
    public void login(User user, final ICallBack callback) {
        LoginService request = RetrofitUtils.getRetrofit(Urls.COOL_USER_AL).create(LoginService.class);
        Call<ResultCode<User>> call = request.call(user.getUser_name(), user.getPassword());
        call.enqueue(new MyCallback<ResultCode<User>>() {
            @Override
            public void onSuc(Response<ResultCode<User>> response) {
                Log.i(TAG, "onSuc-->: " + response.code());
                if ("10000".equals(response.body().getCode())) {
                    LogUtil.info(TAG, response.body().getMessage());
                    LogUtil.info(TAG, response.body().getCode());
                    long userId = response.body().getResult().getId();
                    String userName = response.body().getResult().getUser_name();
                    long depot = response.body().getResult().getDepots();
                    int userType = response.body().getResult().getUser_type();
                    //登陆成功将userId保存到CoolApplication中，退出app自动消失；
                    StockApplication.USER_ID = userId;
                    StockApplication.DEPOT_ID = depot;
                    StockApplication.USER_NAME = userName;
                    StockApplication.userType = userType;
                    Log.i(TAG, "onSuc: "+userType);
                    callback.setSuccess("登录成功");
                } else {
                    callback.setSuccess(response.body().getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                Log.i(TAG, "onFail: " + message);
                callback.setFailure(message);
            }
        });
    }
}
