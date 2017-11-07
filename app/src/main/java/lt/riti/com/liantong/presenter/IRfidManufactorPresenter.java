package lt.riti.com.liantong.presenter;

import java.util.List;

import lt.riti.com.liantong.contract.IRfidManufactorContract;
import lt.riti.com.liantong.entity.Manufacture;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IRfidManufactorModel;

/**
 * Created by brander on 2017/11/7.
 */

public class IRfidManufactorPresenter implements IRfidManufactorContract.Presenter {
    private IRfidManufactorContract.View view;
    private IRfidManufactorContract.Model model;
    public IRfidManufactorPresenter(IRfidManufactorContract.View view) {
        this.view = view;
        this.model=new IRfidManufactorModel();
    }

    @Override
    public void getRfidManufactorTask(String id) {
        model.getRfidManufactor(id, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showData((List<Manufacture>) message);
            }

            @Override
            public void setFailure(Object message) {

            }
        });
    }
}
