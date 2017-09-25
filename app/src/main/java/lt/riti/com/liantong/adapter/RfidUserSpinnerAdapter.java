package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.RfidUser;

/**
 * Created by brander on 2017/9/25.
 */

public class RfidUserSpinnerAdapter extends BaseAdapter {

    public RfidUserSpinnerAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RfidUser rfidUser = (RfidUser) getItem(position);
        convertView = getInflater().inflate(R.layout.item_spinner, null);
        ViewHolder holder;
        if (convertView != null) {
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_spinner);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(rfidUser.getRfidUserName());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
