package lt.riti.com.liantong.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import lt.riti.com.liantong.R;

/**
 * Created by brander on 2017/9/21.
 */
public class SettingRFIDActivity extends BaseActivity {
    @BindView(R.id.et_id)
    EditText etId;//id
    @BindView(R.id.et_name)
    EditText etName;//name
    @BindView(R.id.et_rfid_power)
    EditText etRfidPower;//power
    @BindView(R.id.et_api)
    EditText etApi;//api
    @BindView(R.id.et_uid)
    EditText etUid;//uid
    @BindView(R.id.et_pw)
    EditText etPw;//pw
    @BindView(R.id.btn_save_param)
    Button btnSaveParam;//保存按钮
    @BindView(R.id.btn_cancel)
    Button btnCancel;//取消

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_rfid);
        ButterKnife.bind(this);


    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("RFID配置");
    }
}
