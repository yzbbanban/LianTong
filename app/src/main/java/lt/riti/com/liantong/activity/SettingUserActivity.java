package lt.riti.com.liantong.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.UserAdapter;
import lt.riti.com.liantong.entity.User;
import lt.riti.com.liantong.util.ToastUtil;


/**
 * Created by brander on 2017/9/21.
 */
public class SettingUserActivity extends BaseActivity {
    private static final String TAG = "SettingUserActivity";
    @BindView(R.id.btn_add)
    Button btnAdd;//添加
    @BindView(R.id.recycleView)
    RecyclerView recycleView;//列表视图

    private List<User> users;
    private UserAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    @Override
    protected void initListener() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User();
            u.setName("b" + i);
            users.add(u);
        }
        Log.i(TAG, "initData: " + users);
        //TODO 从网络获取
        if (adapter == null) {
            adapter = new UserAdapter(this);
        }
        adapter.setList(users);
        linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(adapter);
        //TODO 都从后台获取
        //修改数据
        adapter.buttonUpdateSetOnclick(new UserAdapter.ButtonUpdateInterface() {
            @Override
            public void onclick(View view, int position) {
                ToastUtil.showShortToast("update" + position);
            }
        });
        //删除数据
        adapter.buttonDeleteSetOnclick(new UserAdapter.ButtonDeleteInterface() {
            @Override
            public void onclick(View view, int position) {
                ToastUtil.showShortToast("delete" + position);
            }
        });
    }

    //添加数据
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                setDialog();
                break;

        }
    }

    List<User> uList = new ArrayList<>();

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.user_dialog, null);
        builder.setView(v);
        builder.setTitle("设定");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btnAdd = v.findViewById(R.id.btn_dialog_add);
        Button btnCancel = v.findViewById(R.id.btn_dialog_cancel);
        final EditText etDialogName = v.findViewById(R.id.et_dialog_name);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShortToast("Add");
                String name = etDialogName.getText().toString().trim();
                uList.clear();
                uList.addAll(users);
                users.clear();

                User user = new User();
                user.setName(name);
                uList.add(user);
                users.addAll(uList);
                adapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShortToast("Cancel");
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("客户及位置");
    }
}
