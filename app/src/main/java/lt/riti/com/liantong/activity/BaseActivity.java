package lt.riti.com.liantong.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.util.ToolbarHelper;

/**
 * Created by brander on 2017/9/20.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ToolbarHelper mToolBarHelper;
    public Toolbar toolbar;
    public TextView tv_center;
    public TextView tv_right;

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolBarHelper = new ToolbarHelper(this, layoutResID);
        toolbar = mToolBarHelper.getToolBar();
        toolbar.setTitle("");
        tv_center = mToolBarHelper.getTvCenter();
        tv_right = mToolBarHelper.getTvRight();
        tv_right.setOnClickListener(this);
        //返回帧布局视图
        setContentView(mToolBarHelper.getContentView());
        setSupportActionBar(toolbar);//把toolbar设置到activity中
        onCreateCustomToolBar(toolbar);
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        //插入toolbar视图的内容的起始点与结束点
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void logInfo(Class cls, String message) {
        Log.i(cls.getName(), message);
    }

    @Override
    public void onClick(View view) {

    }


    private Toast _MyToast = null;

    protected static final int MSG_SHOW_WAIT = 1;
    protected static final int MSG_HIDE_WAIT = 2;
    protected static final int MSG_SHOW_TIP = 3;

    protected static final int MSG_USER_BEG = 100;

    ProgressDialog waitDialog = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            msgProcess(msg);
        }
    };

    protected void showWaitDialog(String title, String info) {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
        waitDialog = ProgressDialog.show(this, title, info);
    }

    protected void hideWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    protected void msgProcess(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_WAIT:
                showWaitDialog(null, (String) msg.obj);
                break;
            case MSG_HIDE_WAIT:
                hideWaitDialog();
                break;
            case MSG_SHOW_TIP:
                doShowTip((String) msg.obj);
                break;
            default:
                break;
        }
    }

    protected void sendMessage(int what, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, obj));
    }

    /**
     * 气泡提示
     *
     * @param msg
     */
    protected void ShowTip(String msg) {
        sendMessage(MSG_SHOW_TIP, msg);
    }

    private void doShowTip(String msg) {
        if (_MyToast == null) {
            _MyToast = Toast.makeText(BaseActivity.this, msg,
                    Toast.LENGTH_SHORT);
        } else {
            _MyToast.setText(msg);
        }
        _MyToast.show();
    }

    /**
     * 显示提示消息
     *
     * @param msg
     * @param listener
     */
    protected void ShowMsg(String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info).setTitle("Tooltip")
                .setMessage(msg)
                .setPositiveButton(getString(R.string.str_ok), listener)
                .create().show();
    }

    protected void ShowConfim(String msg, DialogInterface.OnClickListener okListener,
                              DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle(getString(R.string.str_confirm))
                .setMessage(msg)
                .setPositiveButton(getString(R.string.str_ok), okListener)
                .setNegativeButton(getString(R.string.str_cancel),
                        cancelListener).show();
    }

    public boolean CheckHexInput(String strInput) {
        boolean rt = false;
        Pattern p = Pattern.compile("^[a-f,A-F,0-9]*$");
        Matcher m = p.matcher(strInput);
        rt = m.matches();
        return rt;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            return null;
        }
    }

    private long lastClickTime;

    protected synchronized boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
