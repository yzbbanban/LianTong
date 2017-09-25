package lt.riti.com.liantong.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.BaseRecyclerViewAdapter;
import lt.riti.com.liantong.adapter.RfidUserSpinnerAdapter;
import lt.riti.com.liantong.adapter.StockIdAdapter;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.presenter.IRfidUserPresenter;
import lt.riti.com.liantong.util.ToastUtil;

/**
 * Created by brander on 2017/9/22.
 */

public class StockInFragment extends BaseFragment implements IAsynchronousMessage, IRfidUserContract.View {
    private static final String TAG = "StockInFragment";
    @BindView(R.id.sp_stock_in_stock)
    Spinner spStockInStock;
    @BindView(R.id.et_stock_in_order)
    EditText etStockInOrder;
    @BindView(R.id.cb_stock_in)
    CheckBox cbStockIn;
    @BindView(R.id.st_stock_in_good)
    EditText stStockInGood;
    @BindView(R.id.cb_stock_in_all)
    CheckBox cbStockInAll;
    @BindView(R.id.recycleView_stock_in)
    RecyclerView recycleViewStockIn;
    @BindView(R.id.btn_stock_in_submit)
    Button btnStockInSubmit;//提交
    @BindView(R.id.btn_stock_in_clear)
    Button btnStockInClear;//清除
    @BindView(R.id.rb_stock_in_single)
    RadioButton rbStockInSingle;
    @BindView(R.id.rb_stock_in_mass)
    RadioButton rbStockInMass;

    Unbinder unbinder;

    protected StockIdAdapter adapter;
    private RfidUserSpinnerAdapter spinnerAdapter;
    private IRfidUserContract.Presenter presenter = new IRfidUserPresenter(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.stock_in_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData(this);
        initView();
        initListener();
        return view;
    }

    @Override
    protected void initView() {
        adapter = new StockIdAdapter(getContext());
        presenter.getRfidUserTask(StockApplication.USER_ID);
        //初始化单号不可用
        if (!cbStockIn.isChecked()) {
            etStockInOrder.setEnabled(false);
        }
    }

    @Override
    protected void initListener() {
        //点击使用单号
        cbStockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockIn.isChecked()) {
                    etStockInOrder.setEnabled(false);
                } else {
                    etStockInOrder.setEnabled(true);
                }

            }
        });
        //全选/全不选
        cbStockInAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockInAll.isChecked()) {//不选
                    Log.i(TAG, "onClick: 1");
                    for (int i = 0; i < storeIds.size(); i++) {
                        storeIds.get(i).setChecked(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {//全选
                    Log.i(TAG, "onClick: 2");
                    for (int i = 0; i < storeIds.size(); i++) {
                        storeIds.get(i).setChecked(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        /**
         * 选择列表(暂无功能)
         */
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.showShortToast("item: " + position);
            }
        });

        /**
         * 选择checkbox
         */
        adapter.checkboxSetOnclick(new StockIdAdapter.CheckItemInterface() {

            @Override
            public void onclick(CompoundButton compoundButton, boolean b, int position) {
//                ToastUtil.showShortToast("item: " + position + "check：" + b);
                //设置值是否为check
                storeIds.get(position).setChecked(b);
            }
        });
        /**
         * 提交数据
         */
        btnStockInSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + storeIds);
            }
        });
        /**
         * 清除数据
         */
        btnStockInClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 按钮按下
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown keyCode = " + keyCode);
        if (keyCode == 131 || keyCode == 135) { // 按下扳机
            if (rbStockInSingle.isChecked()) {
                isSingle = true;
            }
            if (rbStockInMass.isChecked()) {
                isSingle = false;
            }
            showList();
            if (!isKeyDown) {
                isKeyDown = true; //
                StockApplication.setIsInStock(1);
                Clear(null);
                CLReader.Read_EPC(_NowReadParam);
                if (PublicData._IsCommand6Cor6B.equals("6C")) {// 读6C标签
                    CLReader.Read_EPC(_NowReadParam);
                } else {// 读6B标签
                    CLReader.Get6B(_NowAntennaNo + "|1" + "|1" + "|"
                            + "1,000F");
                }
            } else {
                if (keyDownCount < 10000)
                    keyDownCount++;
            }
            if (keyDownCount > 100) {
                isLongKeyDown = true;
            }
        }
        return true;
    }

    /**
     * 显示列表
     */
    protected void showList() {
        Log.i(TAG, "showList: " + getData());
        adapter.setList(getData());
        LinearLayoutManager lM = new LinearLayoutManager(getActivity());
        recycleViewStockIn.setLayoutManager(lM);
        recycleViewStockIn.setAdapter(adapter);

    }

    /**
     * 清除数据
     *
     * @param v
     */
    protected void Clear(View v) {
        Log.i(TAG, "Clear: ");
        totalReadCount = 0;
        readTime = 0;
        hmList.clear();
        //重新显示
        showList();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 接收rfid信号
     *
     * @param model
     */
    @Override
    public void OutPutEPC(EPCModel model) {
        super.OutPutEPC(model);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放资源
        Dispose();
    }

    //显示进度条
    @Override
    public void showLoading() {

    }

    //隐藏进度条
    @Override
    public void hideLoading() {

    }

    //显示描述
    @Override
    public void showDescription(String description) {

    }

    //显示数据（客户/仓库）
    @Override
    public void showData(List<RfidUser> user) {
//        Log.i(TAG, "showData: "+user);
        spinnerAdapter = new RfidUserSpinnerAdapter(user, getActivity());
        spStockInStock.setAdapter(spinnerAdapter);

    }
}
