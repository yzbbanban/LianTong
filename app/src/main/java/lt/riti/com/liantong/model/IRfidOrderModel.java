package lt.riti.com.liantong.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.consts.Urls;
import lt.riti.com.liantong.contract.IRfidOrderContract;
import lt.riti.com.liantong.entity.ResultCode;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.service.RfidOrderAddService;
import lt.riti.com.liantong.util.retrofit.MyCallback;
import lt.riti.com.liantong.util.retrofit.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by brander on 2017/9/24.
 */

public class IRfidOrderModel implements IRfidOrderContract.Model {
    //用于上传
    private List<RfidOrder> orderList = new ArrayList<>();
    private static final String TAG = "IRfidOrderModel";

    @Override
    public void addOrder(int orderIdType, String orderId, List<RfidOrder> rfidOrders, final ICallBack callBack) {
//        Log.i(TAG, "addOrder: " + rfidOrders);
        orderList.clear();
        //最好在线程执行
        for (int i = 0; i < rfidOrders.size(); i++) {
            if (rfidOrders.get(i).getChecked()) {
//                Log.i(TAG, "isChecked: ");
                RfidOrder rfidOrder = rfidOrders.get(i);
                if (orderIdType == 0) {//仓库
                    rfidOrder.setRfidUserId(orderId);
                    rfidOrder.setRfidOrderNum("");//没有则为空
                } else {//单号
                    rfidOrder.setRfidUserId("");//没有则为空
                    rfidOrder.setRfidOrderNum(orderId);
                }
                rfidOrder.setStockType(StockApplication.stockType);//设置为出库或入库
                orderList.add(rfidOrders.get(i));
            }
        }
//        Log.i(TAG, "orderList addOrder: " + orderList);
        if (orderList != null && orderList.size() > 0) {
            Gson gson = new Gson();
            String orderJs = gson.toJson(orderList);
//            Log.i(TAG, "addOrder: " + orderJs);
            Retrofit retrofit = RetrofitUtils.getRetrofit(Urls.COOL_RFID_ORDER_AL);
            RfidOrderAddService request = retrofit.create(RfidOrderAddService.class);
            final Call<ResultCode<String>> call = request.call(orderJs);
            call.enqueue(new MyCallback<ResultCode<String>>() {
                @Override
                public void onSuc(Response<ResultCode<String>> response) {
                    Log.i(TAG, "onSuc: " + response.body().getMessage());
                    callBack.setSuccess(response.body().getMessage());
                }

                @Override
                public void onFail(String message) {
                    Log.i(TAG, "onFail: " + message);
                    callBack.setSuccess(message);
                }
            });
        } else {
            callBack.setFailure("没有选中数据");
        }
    }
}
