package lt.riti.com.liantong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IUpdateContract;
import lt.riti.com.liantong.presenter.IUpdatePresenter;
import lt.riti.com.liantong.util.ToastUtil;
import lt.riti.com.liantong.util.VersionUtils;

/**
 * Created by brander on 2017/9/21.
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements IUpdateContract.View {
    private static final String TAG = "MainActivity";
    @BindView(R.id.ll_store)
    LinearLayout llStore;
    @BindView(R.id.ll_setting_rfid)
    LinearLayout llSettingRfid;
    @BindView(R.id.ll_binding_all)
    LinearLayout llBindingAll;
    //    @BindView(R.id.ll_user)
//    LinearLayout llUser;
    @BindView(R.id.ll_new_rfid)
    LinearLayout ll_new_rfid;
    @BindView(R.id.ll_customer_rfid)
    LinearLayout ll_customer_rfid;
    @BindView(R.id.ll_recycle_rfid)
    LinearLayout ll_recycle_rfid;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.iv_update_apk)
    ImageView ivUpdateApk;//更新

    @BindView(R.id.tv_stock_in_out)
    TextView tvStockINOut;
    private String versionName;
    private IUpdateContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initDate();
    }

    private void initDate() {
        presenter = new IUpdatePresenter(this);
        presenter.updateTask(versionName);
    }

    private void initView() {
        versionName = VersionUtils.getLocalVersionName(this);
        tvVersion.setText("V " + versionName);
        String type = StockApplication.userType;
        ll_new_rfid.setVisibility(View.GONE);
        llStore.setVisibility(View.GONE);
        llBindingAll.setVisibility(View.GONE);
        ll_recycle_rfid.setVisibility(View.GONE);
        if (type.contains("-8")) {//新桶
            ll_new_rfid.setVisibility(View.VISIBLE);
        }
        if (type.contains("0")) {//包装桶
            llBindingAll.setVisibility(View.VISIBLE);
        }
        if (type.contains("1") || type.contains("2")) {//入出库
            llStore.setVisibility(View.VISIBLE);
            if (type.contains("1") && type.contains("2")) {
                tvStockINOut.setText("产品出入库");
            } else if (type.contains("1")) {
                tvStockINOut.setText("产品入库");
            } else if ((type.contains("2"))) {
                tvStockINOut.setText("产品出库");
            }
        }
        if (type.contains("3")) {//回收
            ll_recycle_rfid.setVisibility(View.VISIBLE);
        }
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
     * 设置rfid
     *
     * @param view
     */
    @OnClick(R.id.ll_binding_all)
    public void startBingAll(View view) {
        Intent intent = new Intent(this, BindingActivity.class);
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
     * 新桶入库
     *
     * @param view
     */
    @OnClick(R.id.ll_customer_rfid)
    public void startCustomerRfid(View view) {
        Intent intent = new Intent(this, CustomerActivity.class);
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

    /**
     * 更新
     *
     * @param view
     */
    @OnClick(R.id.iv_update_apk)
    public void update(View view) {
        updateApk();
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


    private void updateApk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择:")
                .setMessage("有新版本是否更新")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //下载
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse("http://116.62.19.124:8080/rfid/CX.apk");
                        Uri content_url = Uri.parse(StockApplication.url + "CX.apk");
                        intent.setData(content_url);
                        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showDescription(String description) {
        Log.i(TAG, "des: " + description);
        Log.i(TAG, "tvVersion: " + versionName);


        if (!versionName.equals(description)) {
            Log.i(TAG, "showDescription: ");
            ivUpdateApk.setVisibility(View.VISIBLE);
        }
    }
}
