package lt.riti.com.liantong.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by brander on 2017/9/21.
 */
public class StockApplication extends Application {
    private static Context context;
    private static int isInStock = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static int getIsInStock() {
        return isInStock;
    }

    public static void setIsInStock(int isInStock) {
        StockApplication.isInStock = isInStock;
    }

    /**
     * 获取全局上下文对象
     *
     * @return
     */
    public static Context getAppContext() {
        return context;
    }
}
