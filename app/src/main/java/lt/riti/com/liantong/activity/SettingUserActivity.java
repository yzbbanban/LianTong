package lt.riti.com.liantong.activity;

import android.content.DialogInterface;
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
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.presenter.IRfidUserPresenter;
import lt.riti.com.liantong.util.ToastUtil;


/**
 * Created by brander on 2017/9/21.
 */
public class SettingUserActivity extends BaseActivity implements IRfidUserContract.View {
    private static final String TAG = "SettingUserActivity";
    @BindView(R.id.btn_add)
    Button btnAdd;//添加
    @BindView(R.id.recycleView)
    RecyclerView recycleView;//列表视图

    private List<RfidUser> users = new ArrayList<>();
    private UserAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private IRfidUserContract.Presenter presenter = new IRfidUserPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initListener() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.getRfidUserTask(StockApplication.USER_ID);
        Log.i(TAG, "initData: " + StockApplication.USER_ID);
    }

    @Override
    protected void initView() {
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
            public void onclick(View view, final int position) {
//                ToastUtil.showShortToast("update" + position);
                setDialog(2, position);

            }
        });
        //删除数据
        adapter.buttonDeleteSetOnclick(new UserAdapter.ButtonDeleteInterface() {
            @Override
            public void onclick(View view, final int position) {
//                ToastUtil.showShortToast("delete" + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserActivity.this);
                builder.setTitle("警告")
                        .setMessage("确定删除吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        presenter.deleteRfidUserTask(users.get(position).getRfidUserId());
                    }
                });
                AlertDialog d = builder.create();
                d.show();

            }
        });

    }

    //添加数据
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                setDialog(1, -1);
                break;

        }
    }

    List<RfidUser> uList = new ArrayList<>();

    private void setDialog(final int type, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.user_dialog, null);
        builder.setView(v);
        builder.setTitle("设定");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btnAdd = v.findViewById(R.id.btn_dialog_add);
        Button btnCancel = v.findViewById(R.id.btn_dialog_cancel);
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
                    RfidUser rfidUser = new RfidUser();
                    rfidUser.setRfidUserName(name);
                    rfidUser.setRfidUserId("");
                    rfidUser.setUserId(StockApplication.USER_ID);
                    rfidUser.setRfidUserLocation("");
                    Log.i(TAG, "onClick: " + rfidUser);
                    //添加数据到服务器
                    //判断是更新还是添加
                    if (type == 1) {
                        presenter.addRfidUserTask(rfidUser);
                    } else {
                        Log.i(TAG, "222: " + users.get(position));
                        rfidUser.setRfidUserId(users.get(position).getRfidUserId());
                        rfidUser.setRfidUserLocation("");
                        Log.i(TAG, "333: " + rfidUser);
                        presenter.updateRfidUserTask(rfidUser);
                    }
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
    }


    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("客户及位置");
    }

    //显示进度
    @Override
    public void showLoading() {

    }

    //隐藏进度
    @Override
    public void hideLoading() {

    }

    //返回描述
    @Override
    public void showDescription(String description) {
        Log.i(TAG, "showDescription: ");
        ToastUtil.showShortToast(description);
        if (!"无数据请添加".equals(description)) {
            initData();
        }
    }

    //获取数据
    @Override
    public void showData(List<RfidUser> users) {
        Log.i(TAG, "showData: ");
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }
}
