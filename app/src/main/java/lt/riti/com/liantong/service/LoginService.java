package lt.riti.com.liantong.service;

import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by brander on 2017/8/3.
 */

public interface LoginService {
    @POST("login.do")
    @FormUrlEncoded
    Call<ResultCode<User>> call(@Field("name") String name, @Field("password") String password);
}