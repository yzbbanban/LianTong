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
    private List<RfidUser> dates;
    private Context context;

    public RfidUserSpinnerAdapter(List<RfidUser> dates, Context context) {
        super(dates, context);
        this.context = context;
        this.dates = dates;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RfidUser rfidUser = dates.get(position);
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
