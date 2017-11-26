package lt.riti.com.liantong.presenter;

import lt.riti.com.liantong.contract.IUpdateContract;
import lt.riti.com.liantong.model.ICallBack;
import lt.riti.com.liantong.model.IUpdateModel;

/**
 * Created by brander on 2017/11/26.
 */

public class IUpdatePresenter implements IUpdateContract.Presenter {
    private IUpdateContract.View view;
    private IUpdateContract.Model model;

    public IUpdatePresenter(IUpdateContract.View view) {
        this.view = view;
        this.model=new IUpdateModel();
    }

    @Override
    public void updateTask(final String version) {
        model.update(version, new ICallBack() {
            @Override
            public void setSuccess(Object message) {
                view.showDescription((String) message);
            }

            @Override
            public void setFailure(Object message) {

            }
        });

    }
}
