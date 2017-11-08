package lt.riti.com.liantong.presenter;

import java.util.List;

import lt.riti.com.liantong.contract.IRfidProductContract;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IRfidProductModel;

/**
 * Created by brander on 2017/11/7.
 */

public class IRfidProductPresenter implements IRfidProductContract.Presenter {
    private IRfidProductContract.View view;
    private IRfidProductContract.Model model;

    public IRfidProductPresenter(IRfidProductContract.View view) {
        this.view = view;
        this.model=new IRfidProductModel();
    }

    @Override
    public void getRfidProductTask(long id) {
        model.getRfidProduct(id, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showProductData((List<Product>) message);
            }

            @Override
            public void setFailure(Object message) {

            }
        });
    }
}
