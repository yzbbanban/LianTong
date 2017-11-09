package lt.riti.com.liantong.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by brander on 2017/9/21.
 */
public class StockApplication extends Application {
    public static long USER_ID = 0L;
    public static String USER_NAME = "";
    private static Context context;
    private static int isInStock = 0;
    public static boolean isOnAppStore = false;
    public static int stockType = 0;//0入库或1出库
    public static String url="http://119.23.228.4/rfid/";
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
