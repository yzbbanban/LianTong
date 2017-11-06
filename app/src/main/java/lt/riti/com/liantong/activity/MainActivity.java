package lt.riti.com.liantong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lt.riti.com.liantong.R;

/**
 * Created by brander on 2017/9/21.
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ll_store)
    LinearLayout llStore;
    @BindView(R.id.ll_setting_rfid)
    LinearLayout llSettingRfid;
//    @BindView(R.id.ll_user)
//    LinearLayout llUser;
    @BindView(R.id.ll_new_rfid)
    LinearLayout ll_new_rfid;
    @BindView(R.id.ll_recycle_rfid)
    LinearLayout ll_recycle_rfid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * 仓库作业
     *
     * @param view
     */
    @OnClick(R.id.ll_store)
    public void startStore(View view) {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }

    /**
     * 设置rfid
     *
     * @param view
     */
    @OnClick(R.id.ll_setting_rfid)
    public void startSettingRFID(View view) {
        Intent intent = new Intent(this, SettingRFIDActivity.class);
        startActivity(intent);
    }

    /**
     * 新桶入库
     *
     * @param view
     */
    @OnClick(R.id.ll_new_rfid)
    public void startNewRfid(View view) {
        Intent intent = new Intent(this, NewRfidActivity.class);
        startActivity(intent);
    }

    /**
     * 空桶回收
     *
     * @param view
     */
    @OnClick(R.id.ll_recycle_rfid)
    public void startRecycle(View view) {
        Intent intent = new Intent(this, RecycleActivity.class);
        startActivity(intent);
    }

//    /**
//     * 用户
//     *
//     * @param view
//     */
//    @OnClick(R.id.ll_user)
//    public void startSettingUser(View view) {
//        Intent intent = new Intent(this, SettingUserActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
}
