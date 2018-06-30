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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
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
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.presenter.IRfidBucketPresenter;
import lt.riti.com.liantong.presenter.IRfidProductPresenter;
import lt.riti.com.liantong.util.LogUtil;
import lt.riti.com.liantong.util.ToastUtil;
import lt.riti.com.liantong.view.IShowList;

/**
 * Created by brander on 2017/9/22.
 */

public class StockInFragment extends BaseFragment implements IAsynchronousMessage,
        IRfidProductContract.View, IRfidBucketContract.View ,IShowList{
    private static final String TAG = "StockInFragment";
    @BindView(R.id.tv_stock_in_stock)
    TextView tvStockInStock;
    //    @BindView(R.id.et_stock_in_order)
//    EditText etStockInOrder;
//    @BindView(R.id.cb_stock_in)
//    CheckBox cbStockIn;
    @BindView(R.id.tv_stock_in_good)
    TextView tvStockInGood;
    @BindView(R.id.cb_stock_in_all)
    CheckBox cbStockInAll;
    @BindView(R.id.recycleView_stock_in)
    RecyclerView recycleViewStockIn;
    @BindView(R.id.btn_stock_in_submit)
    Button btnStockInSubmit;//提交
    @BindView(R.id.btn_stock_in_clear)
    Button btnStockInClear;//清除
//    @BindView(R.id.rb_stock_in_single)
//    RadioButton rbStockInSingle;
//    @BindView(R.id.rb_stock_in_mass)
//    RadioButton rbStockInMass;
    @BindView(R.id.ll_stock_in_stock)
    LinearLayout llStockInStock;
    @BindView(R.id.ll_stock_in_good)
    LinearLayout llStockInGood;

    Unbinder unbinder;
    @BindView(R.id.btn_open)
    Button btnOpen;

    private String product_code = "";

    protected StockIdAdapter adapter;
    private IRfidProductContract.Presenter presenter = new IRfidProductPresenter(this);
    private IRfidBucketContract.Presenter orderPresent = new IRfidBucketPresenter(this);
    private List<Product> products;
    private String depot_code = "";
    private int inputType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    public void setCodeStatus(int inputType) {
        this.inputType = inputType;
        if (inputType == 1) {
            btnOpen.setVisibility(View.GONE);
        } else {
            btnOpen.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.stock_in_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData(this);
        initView();
        initListener();
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(this);
    }

    @Override
    protected void initView() {
        super.initView();
        adapter = new StockIdAdapter(getContext());
        presenter.getRfidProductTask(StockApplication.DEPOT_ID);
        //初始化单号不可用
//        if (!cbStockIn.isChecked()) {
//            etStockInOrder.setEnabled(false);
//        }
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
        //点击使用单号
//        cbStockIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!cbStockIn.isChecked()) {
//                    etStockInOrder.setEnabled(false);
//                } else {
//                    etStockInOrder.setEnabled(true);
//                }
//
//            }
//        });
        //全选/全不选
        cbStockInAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockInAll.isChecked()) {//不选
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
        /**
         * 选择产品
         */
        llStockInStock.setOnClickListener(new View.OnClickListener() {
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
        btnStockInSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnStockInSubmit onClick: " + buckets);
                if ("".equals(tvStockInStock.getText().toString().trim())) {
                    ToastUtil.showShortToast("请选择产品");
                    return;
                }
//                String stockInOrder = etStockInOrder.getText().toString().trim();

                showDialog();
                UploadingBucket uploadingBucket = new UploadingBucket();
                uploadingBucket.setBucket_address(1);//表示在产品区
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
        btnStockInClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("警告").setMessage("确认清除吨桶？").setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        clearChecked();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        //输入托盘号
        llStockInGood.setOnClickListener(new View.OnClickListener() {
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
                            bu.setBucket_address(1);//产品绑定
                            bu.setProduct_code(product_code);
                            bu.setDepot_code(depot_code);//创建公司编号
                            bu.setOutInStatus(1);//入库
                            bu.setStatus(1);//正常
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
//        recycleViewStockIn.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
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

    private void clearChecked(){
        if (buckets!=null&&buckets.size()>0){
            Iterator<Bucket> it = buckets.iterator();
            while(it.hasNext()){
                Bucket b = it.next();
                if (b.getChecked()==true){
                    it.remove();
                }
            }
//                    Log.i(TAG, "showDescription: "+buckets);
            showView(buckets);
        }else {
            Clear(null);
        }
    }

    //显示描述
    @Override
    public void showDescription(String description) {
        hideDialog();

        Gson gson=new Gson();
        try {

            List<Bucket> rowBucketList=gson.fromJson(description,new TypeToken<List<Bucket>>() {
            }.getType());
            for (Bucket bucket:buckets) {
                for (Bucket b:rowBucketList){
                    if (bucket.getBucket_code().equals(b.getBucket_code())){
                        bucket.setDataIsRight(1);
                    }
                }
            }
            showView(buckets);
            ToastUtil.showLongToast("标红产品不对应，请重新勾选");
        }catch (Exception e){
//            Log.i(TAG, "showDescription: "+buckets);
            ToastUtil.showShortToast(description);
            if ("提交成功".equals(description)) {
                clearChecked();
            }
        }


    }

    //显示数据（产品）
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

    List<String> productsName = new ArrayList<>();

    public void setPickView() {
        //条件选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(),
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //设置Text
                        tvStockInStock.setText(products.get(options1).getProduct_name());
                        product_code = products.get(options1).getProduct_code();
                        depot_code = products.get(options1).getDepot_code();
                    }
                }).build();
        pvOptions.setPicker(productsName, null, null);
        pvOptions.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialog();
        ScanDispose();
    }
}
