package lt.riti.com.liantong.util.retrofit;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import lt.riti.com.liantong.consts.ServiceResult;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brander on 2017/8/17.
 */

public abstract class MyCallback<T extends ResultCode> implements Callback<T> {
    private static final String TAG = "MyCallback";
    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.raw().code() == 200) {//200是服务器有合理响应
            LogUtil.info(TAG, "code: "+response.body().getCode());
            if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_SUCCESS.getIndex())) {//正常
                LogUtil.info(TAG, "正常" );
                onSuc(response);
            } else if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_FALSE.getIndex())) {//上传参数问题
                LogUtil.info(TAG, "上传参数问题" );
                onSuc(response);
            } else if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_SERVICE_ERROR.getIndex())) {//服务器问题
                LogUtil.info(TAG, "服务器问题" );
                onSuc(response);
            } else if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_TIMEOUT.getIndex())) {//网络访问超时，请重新上传
                LogUtil.info(TAG, "网络访问超时，请重新上传" );
                onSuc(response);
            } else if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_NO_DATA.getIndex())) {//没有数据
                LogUtil.info(TAG, "没有数据" );
                onSuc(response);
            } else if (response.body().getCode().equals(ServiceResult.GET_MESSAGE_DEV_UPDATE_SUCCESS.getIndex())) {//设备信息更新成功
                LogUtil.info(TAG, "设备信息更新成功" );
                onSuc(response);
            } else {
                onFail(response.body().getMessage());
            }

        } else {//失败响应
            LogUtil.info(TAG, "失败响应" );
            onFailure(call, new RuntimeException("response error,detail = " + response.raw().toString()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {//网络问题会走该回调
        LogUtil.info(TAG, "code failure: "+t.getMessage());
        if (t instanceof SocketTimeoutException) {
            //
        } else if (t instanceof ConnectException) {
            //
        } else if (t instanceof RuntimeException) {
            //
        }
        onFail(t.getMessage());
    }

    public abstract void onSuc(Response<T> response);

    public abstract void onFail(String message);

}
