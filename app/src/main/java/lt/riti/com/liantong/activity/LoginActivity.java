package lt.riti.com.liantong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.contract.ILoginContract;
import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.presenter.ILoginPresenter;
import lt.riti.com.liantong.ui.CleanEditText;
import lt.riti.com.liantong.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements ILoginContract.View {

    @BindView(R.id.et_login_username)
    CleanEditText etLoginUsername;
    @BindView(R.id.et_login_password)
    CleanEditText etLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private ILoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        User user = new User();
        String name = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();
        if ("".equals(name) || "".equals(password)) {
            ToastUtil.showShortToast("请输入用户名或密码");
        } else {
            user.setUser_name(name);
            user.setPassword(password);
            presenter.loginTask(user);
        }
    }

    private void initData() {
        presenter = new ILoginPresenter(this);
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
        Log.i(TAG, "showDescription: "+description);
        ToastUtil.showShortToast(description);
        if ("登录成功".equals(description)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
