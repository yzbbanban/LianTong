package lt.riti.com.liantong.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.BaseRecyclerViewAdapter;
import lt.riti.com.liantong.adapter.RfidUserSpinnerAdapter;
import lt.riti.com.liantong.adapter.StockIdAdapter;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IRfidOrderContract;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.presenter.IRfidOrderPresenter;
import lt.riti.com.liantong.presenter.IRfidUserPresenter;
import lt.riti.com.liantong.util.ToastUtil;

/**
 * Created by brander on 2017/9/22.
 */

public class StockOutFragment extends BaseFragment implements IAsynchronousMessage,
        IRfidUserContract.View, IRfidOrderContract.View {
    private static final String TAG = "StockOutFragment";

    @BindView(R.id.sp_stock_out_stock)
    Spinner spStockOutStock;
    @BindView(R.id.et_stock_out_order)
    EditText etStockOutOrder;
    @BindView(R.id.cb_stock_out)
    CheckBox cbStockOut;
    @BindView(R.id.st_stock_out_good)
    EditText stStockOutGood;
    @BindView(R.id.cb_stock_out_all)
    CheckBox cbStockOutAll;
    @BindView(R.id.recycleView_stock_out)
    RecyclerView recycleViewStockOut;
    @BindView(R.id.btn_stock_out_submit)
    Button btnStockOutSubmit;
    @BindView(R.id.btn_stock_out_clear)
    Button btnStockOutClear;
    @BindView(R.id.rb_stock_in_single)
    RadioButton rbStockInSingle;
    @BindView(R.id.rb_stock_in_mass)
    RadioButton rbStockInMass;

    Unbinder unbinder;
    protected StockIdAdapter adapter;
    private RfidUserSpinnerAdapter spinnerAdapter;
    private IRfidUserContract.Presenter presenter = new IRfidUserPresenter(this);
    private IRfidOrderContract.Presenter orderPresent = new IRfidOrderPresenter(this);
    private List<RfidUser> rfidUsers;
    private String orderId;
    private int OrderIdType;//0仓库或1订单

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.stock_out_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData(this);
        initView();
        initListener();
        return view;
    }

    @Override
    protected void initView() {
        adapter = new StockIdAdapter(getContext());
        spinnerAdapter = new RfidUserSpinnerAdapter(getActivity());
        presenter.getRfidUserTask(StockApplication.USER_ID);
        Log.i(TAG, "initView: ");
        //初始化单号不可用
        if (!cbStockOut.isChecked()) {
            etStockOutOrder.setEnabled(false);
        }
    }
    

    /**
     * 翻页调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            initView();
        }
    }


    @Override
    protected void initListener() {
        //点击使用单号
        cbStockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockOut.isChecked()) {
                    etStockOutOrder.setEnabled(false);
                } else {
                    etStockOutOrder.setEnabled(true);
                }

            }
        });
        //全选/全不选
        cbStockOutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockOutAll.isChecked()) {//不选
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
        btnStockOutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnStockInSubmit onClick: " + storeIds);
                String stockInOrder = etStockOutOrder.getText().toString().trim();
                if (!"".equals(stockInOrder)) {
                    orderId = stockInOrder;
                    OrderIdType = 1;
                }
                orderPresent.addOrderTask(OrderIdType, orderId, storeIds);

            }
        });
        /**
         * 清除数据
         */
        btnStockOutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //选择客户/仓库
        spStockOutStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                orderId = rfidUsers.get(position).getRfidUserId();
                OrderIdType = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 显示列表
     */
    protected void showList() {
        Log.i(TAG, "showList: " + getData());
        adapter = new StockIdAdapter(getContext());
        adapter.setList(getData());
        LinearLayoutManager lM = new LinearLayoutManager(getActivity());
        recycleViewStockOut.setLayoutManager(lM);
        recycleViewStockOut.setAdapter(adapter);
    }


    protected void Clear(View v) {
        Log.i(TAG, "Clear: ");
        totalReadCount = 0;
        readTime = 0;
        hmList.clear();
        showList();
    }

    /**
     * 按钮按下
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown keyCode = " + keyCode);
        if (keyCode == 131 || keyCode == 135) { // 按下扳机
            if (rbStockInSingle.isChecked()) {
                isSingle = true;
            }
            if (rbStockInMass.isChecked()) {
                isSingle = false;
            }
            showList();
            if (!isKeyDown) {
                isKeyDown = true;
                StockApplication.setIsInStock(2);
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showDescription(String description) {
        ToastUtil.showShortToast(description);
    }

    @Override
    public void showData(List<RfidUser> rfidUsers) {
        Log.i(TAG, "showData: " + rfidUsers);
        //设置界面
        this.rfidUsers = rfidUsers;
        spinnerAdapter.setDates(rfidUsers);
        spStockOutStock.setAdapter(spinnerAdapter);

    }
}
