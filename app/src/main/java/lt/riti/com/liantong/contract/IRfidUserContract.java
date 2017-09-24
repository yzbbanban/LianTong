package lt.riti.com.liantong.contract;

import java.util.List;

import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.presenter.BasePresenter;
import lt.riti.com.liantong.view.BaseView;

/**
 * Created by brander on 2017/9/24.
 */

public interface IRfidUserContract {
    //业务
    interface Model {
        void addRfidUser(RfidUser rfidUser, ICallBack callBack);

        void updateRfidUser(RfidUser rfidUser, ICallBack callBack);

        void deleteRfidUser(String id, ICallBack callBack);

        void getRfidUser(String id, ICallBack callBack);
    }

    //视图
    interface View extends BaseView {
        void showData(List<RfidUser> user);
    }

    //连接
    interface Presenter extends BasePresenter {
        void addRfidUserTask(RfidUser rfidUser);

        void updateRfidUserTask(RfidUser rfidUser);

        void deleteRfidUserTask(String id);

        void getRfidUserTask(String id);
    }
}
