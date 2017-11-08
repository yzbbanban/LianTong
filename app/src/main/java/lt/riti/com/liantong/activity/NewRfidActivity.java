package lt.riti.com.liantong.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import lt.riti.com.liantong.R;

public class NewRfidActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rfid);
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("新桶入库");
    }
}
