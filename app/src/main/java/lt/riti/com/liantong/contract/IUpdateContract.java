package lt.riti.com.liantong.contract;

import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.presenter.BasePresenter;
import lt.riti.com.liantong.view.BaseView;

/**
 * Created by brander on 2017/9/24.
 */
public interface IUpdateContract {
    interface Model {
        void update(String version, ICallBack callBack);
    }

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void updateTask(String version);
    }
}
