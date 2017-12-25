package lt.riti.com.liantong.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;
import com.clouiotech.util.Helper.Helper_ThreadPool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.BaseRecyclerViewAdapter;
import lt.riti.com.liantong.adapter.StockIdAdapter;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IRfidBucketContract;
import lt.riti.com.liantong.contract.IRfidProductContract;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.presenter.IRfidBucketPresenter;
import lt.riti.com.liantong.presenter.IRfidUserPresenter;
import lt.riti.com.liantong.util.LogUtil;
import lt.riti.com.liantong.util.ToastUtil;
import lt.riti.com.liantong.view.IShowList;

/**
 * Created by brander on 2017/9/22.
 */

public class CustomerFragment extends BaseFragment implements IAsynchronousMessage,
        IRfidUserContract.View, IRfidProductContract.View, IRfidBucketContract.View,IShowList {
    private static final String TAG = "StockInFragment";
    @BindView(R.id.ll_stock_customer_stock)
    LinearLayout llStockCustomerStock;//客户
    @BindView(R.id.tv_stock_customer_good)
    TextView tvStockCustomerGood;
    @BindView(R.id.cb_stock_customer_all)
    CheckBox cbStockCustomerAll;
    @BindView(R.id.recycleView_stock_customer)
    RecyclerView recycleViewStockCustomer;
    @BindView(R.id.btn_stock_customer_submit)
    Button btnStockCustomerSubmit;//提交
    @BindView(R.id.btn_stock_customer_clear)
    Button btnStockCustomerClear;//清除
//    @BindView(R.id.rb_stock_customer_single)
//    RadioButton rbStockCustomerSingle;
//    @BindView(R.id.rb_stock_customer_mass)
//    RadioButton rbStockCustomerMass;
    @BindView(R.id.tv_stock_customer_stock)
    TextView tvStockCustomerStock;
    @BindView(R.id.ll_stock_customer_good)
    LinearLayout llStockCustomerGood;

    Unbinder unbinder;

    protected StockIdAdapter adapter;
    @BindView(R.id.btn_open)
    Button btnOpen;

    //    private IRfidProductContract.Presenter productPresenter = new IRfidProductPresenter(this);//产品
    private IRfidUserContract.Presenter presenter = new IRfidUserPresenter(this);//客户

    private IRfidBucketContract.Presenter orderPresent = new IRfidBucketPresenter(this);
    private List<Product> products;
    private List<RfidUser> rfidUsers;
    private String depot_code = "";//创建公司编号
    private String product_code = "";
    private int customer_id;
    private int codeStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    public void setCodeStatus(int codeStatus) {
        this.codeStatus = codeStatus;
        if (codeStatus == 1) {
            btnOpen.setVisibility(View.GONE);
        } else {
            btnOpen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_customer_rfid, null);
        unbinder = ButterKnife.bind(this, view);
        initData(this);
        initView();
        initListener();
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    protected void initView() {
        super.initView();
        adapter = new StockIdAdapter(getContext());
        presenter.getRfidUserTask(StockApplication.USER_ID);
//        productPresenter.getRfidProductTask(StockApplication.USER_ID);
    }

    /**
     * 翻页调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i(TAG, "Visible StockInFragment: " + isVisibleToUser);
        if (isVisibleToUser) {
//            initView();
        }
    }


    @Override
    protected void initListener() {
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String controlText = btnOpen.getText().toString();
                if (controlText.equals(getString(R.string.btn_read))) {
                    PingPong_Read();
                    btnOpen.setText(getString(R.string.btn_read_stop));
                } else {
                    Pingpong_Stop();
                    btnOpen.setText(getString(R.string.btn_read));
                }
            }
        });
        //全选/全不选
        cbStockCustomerAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockCustomerAll.isChecked()) {//不选
                    Log.i(TAG, "onClick: 1");
                    for (int i = 0; i < buckets.size(); i++) {
                        buckets.get(i).setChecked(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {//全选
                    Log.i(TAG, "onClick: 2");
                    for (int i = 0; i < buckets.size(); i++) {
                        buckets.get(i).setChecked(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
//        //产品
//        tvCustomerProductStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setProductPickView();
//            }
//        });
        //选择客户或仓库
        llStockCustomerStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPickView();
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
                buckets.get(position).setChecked(b);
            }
        });
        /**
         * 提交数据
         */
        btnStockCustomerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnStockInSubmit onClick: " + buckets);
//                if ("".equals(tvCustomerProductStock.getText().toString().trim())) {
//                    ToastUtil.showShortToast("请选择产品");
//                    return;
//                }
                Log.i(TAG, "btnStockInSubmit onClick: " + buckets);
                if ("".equals(tvStockCustomerStock.getText().toString().trim())) {
                    ToastUtil.showShortToast("请选择客户/仓库");
                    return;
                }
                showDialog();
                UploadingBucket uploadingBucket = new UploadingBucket();

                uploadingBucket.setBucket_address(2);//表示客户入库
                uploadingBucket.setCustomer_id(customer_id);//客户
                uploadingBucket.setProduct_code(product_code);//产品
                uploadingBucket.setDepot_code(depot_code);//创建公司编号
                uploadingBucket.setStatus(1);//正常桶
                uploadingBucket.setOutInStatus(1);//入库

                orderPresent.addBucketTask(uploadingBucket, buckets);

            }
        });
        /**
         * 清除数据
         */
        btnStockCustomerClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("警告").setMessage("确认清除订单？").setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Clear(view);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        //选择客户/仓库


        //输入托盘号
        llStockCustomerGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = getActivity().getLayoutInflater().inflate(R.layout.user_dialog, null);
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
                            //向列表添加数据
                            Bucket bu = new Bucket();
                            bu.setChecked(true);
                            bu.setBucket_code(name);//吨桶编号
                            bu.setBucket_address(2);//表示客户入库
                            bu.setCustomer_id(customer_id);//客户
                            bu.setProduct_code(product_code);//产品
                            bu.setOutInStatus(1);//入库
                            bu.setAdmin_id(StockApplication.USER_ID);
                            bu.setIdTime(1L);//读取次数
                            //没有数据则直接显示
                            if (buckets.size() == 0) {
                                buckets.add(bu);
                                showView(buckets);
                            } else {
                                buckets.add(bu);
                                adapter.notifyDataSetChanged();
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
                ibtnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
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
    public boolean onKeyDown(int keyCode, KeyEvent event, int inputType) {
//        Log.d(TAG, "onKeyDown keyCode = " + keyCode);
        if (inputType == 1) {
            busy = false;
//            if (isRcodeSingle) {
            DeCode(this);
//                showView(getRCodeData());
//            }
        } else {
//            Toast.makeText(getActivity(), "onKeyDown 33--->: ", Toast.LENGTH_SHORT).show();
            if (keyCode == 131 || keyCode == 135) { // 按下扳机
                openRfid();
            }
        }
        return true;
    }

    // 间歇性读
    private void PingPong_Read() {
        if (isStartPingPong)
            return;
        isStartPingPong = true;
        Helper_ThreadPool.ThreadPool_StartSingle(new Runnable() {
            @Override
            public void run() {
                while (isStartPingPong) {
                    try {
                        if (!isPowerLowShow) {
                            if (usingBackBattery && !canUsingBackBattery()) {
                                ToastUtil.showShortToast("电量低");
                            }
                            handler.sendEmptyMessage(0);
                            Thread.sleep(1000); // 一秒钟刷新一次
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    openRfid();
                    break;
            }

        }
    };

    // 停止间歇性读
    private void Pingpong_Stop() {
        isStartPingPong = false;
        CLReader.Stop();
        keyDownCount = 0;
        isKeyDown = false;
        isLongKeyDown = false;
    }

    private void openRfid() {
        LogUtil.info(TAG, "openRfid");
//        if (rbStockInSingle.isChecked()) {
//                    isSingle = true;
//                }
//                if (rbStockInMass.isChecked()) {
        isSingle = false;
//                }
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


    /**
     * 显示列表
     */
    protected void showList() {
        Log.i(TAG, "showList: " + getData());
        showView(getData());
    }

    /**
     * 展示界面
     *
     * @param buckets
     */
    @Override
    public void showView(List<Bucket> buckets) {
        adapter.setList(buckets);
        LinearLayoutManager lM = new LinearLayoutManager(getActivity());
//        recycleViewStockCustomer.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recycleViewStockCustomer.setLayoutManager(lM);
        recycleViewStockCustomer.setAdapter(adapter);
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
        hideDialog();
        ToastUtil.showShortToast(description);
        if ("提交成功".equals(description)) {
            Clear(null);
        }
    }

    /**
     * 显示产品
     *
     * @param products
     */
    @Override
    public void showProductData(List<Product> products) {
        //Log.i(TAG, "showProductData: "+user);
        productsName.clear();
        if (products != null && products.size() > 0) {
            //设置界面
            this.products = products;
            for (int i = 0; i < products.size(); i++) {
                String name = products.get(i).getProduct_name();
                productsName.add(name);
            }
        } else {
            ToastUtil.showShortToast("请绑定产品");
        }
    }

    //显示数据（客户/仓库）
    @Override
    public void showData(List<RfidUser> rfidUsers) {
//        Log.i(TAG, "showProductData: "+user);
        //设置界面
        rfidName.clear();
        if (rfidUsers != null && rfidUsers.size() > 0) {
            this.rfidUsers = rfidUsers;
            for (int i = 0; i < rfidUsers.size(); i++) {
                String name = rfidUsers.get(i).getCustomer_name();
                rfidName.add(name);
            }
        } else {
            ToastUtil.showShortToast("请添加客户");
        }
    }

    List<String> rfidName = new ArrayList<>();
    List<String> productsName = new ArrayList<>();

    //客户
    public void setPickView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(),
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //设置Text
                        tvStockCustomerStock.setText(rfidUsers.get(options1).getCustomer_name());
                        customer_id = rfidUsers.get(options1).getId();
                    }
                }).build();
        pvOptions.setPicker(rfidName, null, null);
        pvOptions.show();
    }

//    /**
//     * 产品选择器
//     */
//    public void setProductPickView() {
//        //条件选择器
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(),
//                new OptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        //设置Text
//                        tvCustomerProductStock.setText(products.get(options1).getProduct_name());
//                        product_code = products.get(options1).getProduct_code();
//                        depot_code = products.get(options1).getDepot_code();
//                    }
//                }).build();
//        pvOptions.setPicker(productsName, null, null);
//        pvOptions.show();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialog();
        ScanDispose();
    }
}
