package lt.riti.com.liantong.contract;

import java.util.List;

import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.model.ICallBack;

/**
 * Created by brander on 2017/11/7.
 */

public interface IRfidProductContract {
    interface Model {
        void getRfidProduct(String id, ICallBack callBack);

    }

    interface View {
        void showData(List<Product> productList);
    }

    interface Presenter {
        void getRfidProductTask(String id);
    }
}
