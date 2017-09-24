package lt.riti.com.liantong.service;

import java.util.Date;

import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by brander on 2017/8/3.
 */

public interface RfidUserAddService {
    @POST("addRfidUser.do")
    @FormUrlEncoded
    Call<ResultCode<RfidUser>> call(@Field("rfidUserId") String rfidUserId,
                                    @Field("userId") String userId,
                                    @Field("rfidUserName") String rfidUserName,
                                    @Field("rfidUserLocation") String rfidUserLocation
                                    );
}