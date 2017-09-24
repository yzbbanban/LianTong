package lt.riti.com.liantong.presenter;

import lt.riti.com.liantong.contract.ILoginContract;
import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.ILoginModel;

/**
 * Created by Administrator on 2017/9/24.
 */

public class ILoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View view;
    private ILoginContract.Model model;

    public ILoginPresenter(ILoginContract.View view) {
        this.view = view;
        model = new ILoginModel();
    }

    @Override
    public void loginTask(User user) {
        model.login(user, new ICallBack() {
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
