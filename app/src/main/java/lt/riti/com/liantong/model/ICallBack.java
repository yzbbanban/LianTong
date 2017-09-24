package lt.riti.com.liantong.model;

/**
 * Created by brander on 2017/9/24.
 */

public interface ICallBack {
    /**
     * 成功回调
     */
    void setSuccess(Object message);

    /**
     * 失败回调
     */
    void setFailure(Object message);
}
