    package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.Product;
import lt.riti.com.liantong.entity.RfidUser;

/**
 * Created by brander on 2017/9/25.
 */

public class RfidProductSpinnerAdapter extends BaseAdapter {

    public RfidProductSpinnerAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = (Product) getItem(position);
        convertView = getInflater().inflate(R.layout.item_spinner, null);
        ViewHolder holder;
        if (convertView != null) {
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_spinner);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(product.getProduct_name());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
