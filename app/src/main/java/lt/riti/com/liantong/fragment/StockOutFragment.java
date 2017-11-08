package lt.riti.com.liantong.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.BaseRecyclerViewAdapter;
import lt.riti.com.liantong.adapter.RfidUserSpinnerAdapter;
import lt.riti.com.liantong.adapter.StockIdAdapter;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.contract.IRfidBucketContract;
import lt.riti.com.liantong.contract.IRfidProductContract;
import lt.riti.com.liantong.contract.IRfidUserContract;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.entity.RfidUser;
import lt.riti.com.liantong.entity.UploadingBucket;
import lt.riti.com.liantong.presenter.IRfidBucketPresenter;
import lt.riti.com.liantong.presenter.IRfidProductPresenter;
import lt.riti.com.liantong.presenter.IRfidUserPresenter;
import lt.riti.com.liantong.util.ToastUtil;

/**
 * Created by brander on 2017/9/22.
 */

public class StockOutFragment extends BaseFragment implements IAsynchronousMessage,
        IRfidUserContract.View, IRfidProductContract.View, IRfidBucketContract.View {
    private static final String TAG = "StockOutFragment";

    @BindView(R.id.tv_stock_out_stock)
    TextView tvStockOutStock;
    @BindView(R.id.tv_stock_out_product)
    TextView tvStockOutProduct;
    //    @BindView(R.id.et_stock_out_order)
//    EditText etStockOutOrder;
//    @BindView(R.id.cb_stock_out)
//    CheckBox cbStockOut;
    @BindView(R.id.tv_stock_out_good)
    TextView tvStockOutGood;
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
    private IRfidProductContract.Presenter productPresenter = new IRfidProductPresenter(this);
    private IRfidBucketContract.Presenter orderPresent = new IRfidBucketPresenter(this);
    private List<RfidUser> rfidUsers;
    private int customer_id;
    private String depot_code;
    private List<Product> products;
    private String product_code;

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
        productPresenter.getRfidProductTask(StockApplication.USER_ID);
        Log.i(TAG, "initView: ");
        //初始化单号不可用
//        if (!cbStockOut.isChecked()) {
//            etStockOutOrder.setEnabled(false);
//        }
    }


    /**
     * 翻页调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            initView();
            rfidName.clear();
        }
    }


    @Override
    protected void initListener() {
        //点击使用单号
//        cbStockOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!cbStockOut.isChecked()) {
//                    etStockOutOrder.setEnabled(false);
//                } else {
//                    etStockOutOrder.setEnabled(true);
//                }
//
//            }
//        });
        //全选/全不选
        cbStockOutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStockOutAll.isChecked()) {//不选
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
        btnStockOutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(tvStockOutStock.getText().toString().trim())) {
                    ToastUtil.showShortToast("请选择客户/仓库");
                    return;
                }
                if ("".equals(tvStockOutProduct.getText().toString().trim())) {
                    ToastUtil.showShortToast("请选择产品");
                    return;
                }
                Log.i(TAG, "btnStockInSubmit onClick: " + buckets);
//                String stockInOrder = etStockOutOrder.getText().toString().trim();
//                if (!"".equals(stockInOrder) && cbStockOut.isChecked()) {//值不为空，且checkbox选中状态
//                    orderId = stockInOrder;
//                    OrderIdType = 1;
//                }else{
//                    stockInOrder="";
//                }

                UploadingBucket uploadingBucket = new UploadingBucket();
                uploadingBucket.setBucket_address(2);//表示在空桶区
                uploadingBucket.setCustomer_id(customer_id);//客户
                uploadingBucket.setProduct_code(product_code);//产品
                uploadingBucket.setDepot_code(depot_code);//创建公司编号
                uploadingBucket.setStatus(1);//正常桶

                orderPresent.addBucketTask(uploadingBucket, buckets);

            }
        });
        /**
         * 清除数据
         */
        btnStockOutClear.setOnClickListener(new View.OnClickListener() {
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
        /**
         * 选择客户或仓库
         */
        tvStockOutStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPickView();
            }
        });
        /**
         * 选择产品
         */
        tvStockOutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProductPickView();
            }
        });
        tvStockOutGood.setOnClickListener(new View.OnClickListener() {
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

                            bu.setBucket_code(name);//吨桶编号
                            bu.setBucket_address(2);//客户绑定
                            bu.setCustomer_id(customer_id);//客户
                            bu.setProduct_code(product_code);//产品
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
    private void showView(List<Bucket> buckets) {
        adapter.setList(buckets);
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

    /**
     * 客户选择器
     */
    public void setPickView() {
        //条件选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(),
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //设置Text
                        tvStockOutStock.setText(rfidUsers.get(options1).getCustomer_name());
                        customer_id = rfidUsers.get(options1).getId();
                        depot_code = rfidUsers.get(options1).getDepot_code();
                    }
                }).build();
        pvOptions.setPicker(rfidName, null, null);
        pvOptions.show();
    }

    /**
     * 产品选择器
     */
    public void setProductPickView() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(),
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //设置Text
                        tvStockOutProduct.setText(products.get(options1).getProduct_name());
                        product_code = products.get(options1).getProduct_code();
                        depot_code = products.get(options1).getDepot_code();
                    }
                }).build();
        pvOptions.setPicker(productsName, null, null);
        pvOptions.show();
    }

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
}
