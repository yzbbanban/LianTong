package lt.riti.com.liantong.contract;

import java.util.List;

import lt.riti.com.liantong.entity.Manufacture;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.model.ICallBack;

/**
 * Created by Administrator on 2017/11/7.
 */

public interface IRfidManufactorContract {
    interface Model {
       void getRfidManufactor(String id, ICallBack callBack);
    }

    interface View {
        void showData(List<Manufacture> manufactures);
    }

    interface Presenter {
        void getRfidManufactorTask(String id);
    }
}
