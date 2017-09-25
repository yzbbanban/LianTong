package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;

/**
 * 封装view显示的相同方法
 * Created by brander on 2016/9/13.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private List<T> dates;
    private Context context;
    private LayoutInflater inflater;

    public BaseAdapter(List<T> datas, Context context) {
        setContext(context);
        setDates(datas);
        setInflater();
    }

    public List<T> getDates() {
        return dates;
    }

    private final void setDates(List<T> dates) {
        if (dates == null) {
            dates = new ArrayList<>();
        }
        this.dates = dates;
    }

    private final void setContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context 不为null");
        }
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    private void setInflater() {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dates.size() == 0 ? 0 : dates.size();
    }

    @Override
    public Object getItem(int position) {
        if (dates != null && position < 0) {
            return dates.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
