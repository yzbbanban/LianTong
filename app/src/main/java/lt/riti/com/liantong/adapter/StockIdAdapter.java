package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.StockId;

/**
 * Created by Administrator on 2017/9/23.
 */

public class StockIdAdapter extends BaseRecyclerViewAdapter<StockId> {
    private static final String TAG = "StockIdAdapter";

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
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
        StockId storeId = mList.get(position);
        storeViewHolder.tv_item_stock_id.setText("" + (position + 1));
        storeViewHolder.tv_item_stock_name.setText(storeId.getIdName());
        storeViewHolder.tv_item_stock_time.setText("" + storeId.getIdTime());


    }


    class StoreViewHolder extends BaseViewHolder {
        CheckBox cb_item_stock;
        TextView tv_item_stock_id;
        TextView tv_item_stock_name;
        TextView tv_item_stock_time;

        public StoreViewHolder(View itemView) {
            super(itemView);
            cb_item_stock = (CheckBox) itemView.findViewById(R.id.cb_item_stock);
            tv_item_stock_id = (TextView) itemView.findViewById(R.id.tv_item_stock_id);
            tv_item_stock_name = (TextView) itemView.findViewById(R.id.tv_item_stock_name);
            tv_item_stock_time = (TextView) itemView.findViewById(R.id.tv_item_stock_time);
        }
    }
}
