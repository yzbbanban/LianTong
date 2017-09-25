package lt.riti.com.liantong.contract;

import java.util.List;

import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.presenter.BasePresenter;
import lt.riti.com.liantong.view.BaseView;

/**
 * Created by brander on 2017/9/24.
 */

public interface IRfidOrderContract {
    interface Model {
        void addOrder(int orderIdType,String orderId,List<RfidOrder> rfidOrders, ICallBack callBack);
    }

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void addOrderTask(int orderIdType,String orderId,List<RfidOrder> rfidOrders);
    }
}
