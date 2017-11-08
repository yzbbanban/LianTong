package lt.riti.com.liantong.service;

import java.util.List;

import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by brander on 2017/8/3.
 * 获取客户及位置列表
 */

public interface RfidProductListService {
    @POST("getRfidProductList.do")
    @FormUrlEncoded
    Call<ResultCode<List<Product>>> call(@Field("id") long id);
}