package lt.riti.com.liantong.presenter;

import java.util.List;

import lt.riti.com.liantong.contract.IRfidOrderContract;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IRfidOrderModel;

/**
 * Created by brander on 2017/9/24.
 */

public class IRfidOrderPresenter implements IRfidOrderContract.Presenter {
    private IRfidOrderContract.View view;
    private IRfidOrderContract.Model model;

    public IRfidOrderPresenter(IRfidOrderContract.View view) {
        this.view = view;
        model = new IRfidOrderModel();
    }

    @Override
    public void addOrderTask(int orderIdType,String orderId,String stockInOrder, List<RfidOrder> rfidOrders) {
        model.addOrder(orderIdType,orderId, stockInOrder,rfidOrders, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showDescription((String) message);
            }

            @Override
            public void setFailure(Object message) {
                view.showDescription((String) message);

            }
        });
    }
}
