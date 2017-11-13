package lt.riti.com.liantong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.ILoginContract;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.presenter.ILoginPresenter;
import lt.riti.com.liantong.ui.CleanEditText;
import lt.riti.com.liantong.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements ILoginContract.View {

    @BindView(R.id.et_login_username)
    CleanEditText etLoginUsername;
    @BindView(R.id.tv_set_url)
    TextView tvSetUrl;
    @BindView(R.id.et_login_password)
    CleanEditText etLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private String name;
    private String password;
    private ILoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        User user = new User();
        name = etLoginUsername.getText().toString().trim();
        password = etLoginPassword.getText().toString().trim();
        if ("".equals(name) || "".equals(password)) {
            ToastUtil.showShortToast("请输入用户名或密码");
        } else {
            user.setUser_name(name);
            user.setPassword(password);
            presenter.loginTask(user);
        }
    }

    @OnClick(R.id.tv_set_url)
    public void setUrl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.user_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btnAdd = v.findViewById(R.id.btn_dialog_add);
        Button btnCancel = v.findViewById(R.id.btn_dialog_cancel);
        ImageButton ibtnClose = v.findViewById(R.id.ibtn_dialog_close);
        final EditText etDialogName = v.findViewById(R.id.et_dialog_name);
        //添加或更新
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showShortToast("Add");
                String name = etDialogName.getText().toString().trim();
                if ("".equals(name)) {
                    ToastUtil.showShortToast("请输入");
                } else {
                    StockApplication.url = "http://" + name + "/rfid/";
                    Log.i(TAG, "onClick: " + StockApplication.url);
                    alertDialog.dismiss();
                }

            }
        });
        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShortToast("Cancel");
                alertDialog.dismiss();
            }
        });
        ibtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void initData() {
        presenter = new ILoginPresenter(this);
        SharedPreferences preferences = getSharedPreferences("user", this.MODE_PRIVATE);
        String n = preferences.getString("name", "");
        String pwd = preferences.getString("password", "");
        Log.i(TAG, "initData: "+n+","+pwd);
        if (!"".equals(n) && !"".equals(pwd)) {
            etLoginUsername.setText(n);
            etLoginPassword.setText(pwd);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    private static final String TAG = "LoginActivity";

    @Override
    public void showDescription(String description) {
        Log.i(TAG, "showDescription: " + description);
        if (description == null) {
            ToastUtil.showShortToast("登录超时请重新登录！");
            return;
        }
        ToastUtil.showShortToast(description);
        if ("登录成功".equals(description)) {
            SharedPreferences.Editor editor = getSharedPreferences("user", this.MODE_PRIVATE).edit();
            Log.i(TAG, "showDescription: "+name+","+password);
            editor.putString("name", name);
            editor.putString("password", password);
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
