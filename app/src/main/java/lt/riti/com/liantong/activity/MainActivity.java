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
    @BindView(R.id.ll_user)
    LinearLayout llUser;

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
        Intent intent = new Intent(this, StoreActivity.class);
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
     * 用户
     *
     * @param view
     */
    @OnClick(R.id.ll_user)
    public void startSettingUser(View view) {
        Intent intent = new Intent(this, SettingUserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
}
