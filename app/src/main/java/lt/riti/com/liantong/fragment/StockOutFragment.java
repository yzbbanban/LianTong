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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.adapter.StockIdAdapter;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.entity.PublicData;

/**
 * Created by brander on 2017/9/22.
 */

public class StockOutFragment extends BaseFragment implements IAsynchronousMessage {
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.stock_out_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData(this);
        return view;
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

}
