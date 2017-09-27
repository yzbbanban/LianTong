package lt.riti.com.liantong.service;

import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by brander on 2017/8/3.
 * 删除rfid 客户及位置
 */

public interface RfidUserDeleteService {
    @POST("deleteRfidUser.do")
    @FormUrlEncoded
    Call<ResultCode<RfidUser>> call(@Field("id") String id);
}