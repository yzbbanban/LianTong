package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.util.ToastUtil;

/**
 * Created by brander on 2017/9/23.
 */

public class StockIdAdapter extends BaseRecyclerViewAdapter<Bucket> {
    private static final String TAG = "StockIdAdapter";
    private static HashMap<Integer, Boolean> isSelected;//checkbox的选中情况

    public StockIdAdapter(Context context) {
        super(context);
        Log.i(TAG, "StockIdAdapter: ");
    }

    /**
     * 引入界面
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_stock, parent, false);
        StoreViewHolder storeViewHolder = new StoreViewHolder(view);
        return storeViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
        Bucket storeId = mList.get(position);
        storeViewHolder.tv_item_stock_id.setText("" + (position + 1));
        storeViewHolder.tv_item_stock_name.setText(storeId.getBucket_code());
        storeViewHolder.tv_item_stock_time.setText("" + storeId.getIdTime());
        //设置被点击事件
        storeViewHolder.cb_item_stock.setChecked(storeId.getChecked());
        Log.i(TAG, "onBindViewHolder1: "+(position / 2 ));
        Log.i(TAG, "onBindViewHolder2: " + position + "," + position % 2);
//        if (position % 2 == 0 ) {
//            storeViewHolder.ll_stock.setBackgroundColor(Color.WHITE);
//        }else {
//
//        }
        storeViewHolder.cb_item_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //重写点击事件
                if (checkItemInterface != null) {
                    //接口实例化后的而对象，调用重写后的方法
                    checkItemInterface.onclick(compoundButton, b, position);
                }
            }
        });
    }


    class StoreViewHolder extends BaseViewHolder {
        LinearLayout ll_stock;
        CheckBox cb_item_stock;
        TextView tv_item_stock_id;
        TextView tv_item_stock_name;
        TextView tv_item_stock_time;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ll_stock = (LinearLayout) itemView.findViewById(R.id.ll_stock);
            cb_item_stock = (CheckBox) itemView.findViewById(R.id.cb_item_stock);
            tv_item_stock_id = (TextView) itemView.findViewById(R.id.tv_item_stock_id);
            tv_item_stock_name = (TextView) itemView.findViewById(R.id.tv_item_stock_name);
            tv_item_stock_time = (TextView) itemView.findViewById(R.id.tv_item_stock_time);
        }
    }

    private CheckItemInterface checkItemInterface;

    /**
     * checkbox事件
     */
    public interface CheckItemInterface {
        void onclick(CompoundButton compoundButton, boolean b, int position);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void checkboxSetOnclick(CheckItemInterface checkItemInterface) {
        this.checkItemInterface = checkItemInterface;
    }


}
