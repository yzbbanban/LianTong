package lt.riti.com.liantong.presenter;

import android.util.Log;

import java.util.List;

import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IRfidUserModel;

/**
 * Created by brander on 2017/9/24.
 */
public class IRfidUserPresenter implements IRfidUserContract.Presenter {
    private IRfidUserContract.View view;
    private IRfidUserContract.Model model;
    private MyCallBack callback;
    private static final String TAG = "IRfidUserPresenter";

    public IRfidUserPresenter(IRfidUserContract.View view) {
        this.view = view;
        model = new IRfidUserModel();
        callback = new MyCallBack();
    }

//    @Override
//    public void addRfidUserTask(RfidUser rfidUser) {
//        model.addRfidUser(rfidUser, callback);
//    }

//    @Override
//    public void updateRfidUserTask(RfidUser rfidUser) {
//        model.updateRfidUser(rfidUser, callback);
//    }
//
//    @Override
//    public void deleteRfidUserTask(String id) {
//        model.deleteRfidUser(id, callback);
//    }

    @Override
    public void getRfidUserTask(long id) {
        model.getRfidUser(id, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showData((List<RfidUser>) message);
            }

            @Override
            public void setFailure(Object message) {
                view.showDescription((String) message);
            }
        });
    }

    class MyCallBack implements ICallBack {

        @Override
        public void setSuccess(Object message) {
            try {
                view.showDescription((String) message);
            } catch (Exception e) {
                view.showDescription(e.getMessage());
            }
        }

        @Override
        public void setFailure(Object message) {
            try {
                view.showDescription((String) message);
            } catch (Exception e) {

            }
        }
    }
}
