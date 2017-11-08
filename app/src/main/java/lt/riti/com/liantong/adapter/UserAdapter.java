package lt.riti.com.liantong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.RfidUser;

/**
 * Created by brander on 2017/9/21.
 */

public class UserAdapter extends BaseRecyclerViewAdapter<RfidUser> {
    private static final String TAG = "UserAdapter";

    public UserAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_user_position, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(v);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        RfidUser user = mList.get(position);
        userViewHolder.tv_item_id.setText("" + (position + 1));
        userViewHolder.tv_item_name.setText(user.getCustomer_name());
        if (position  % 2 == 0 && position != 0) {//条纹色
            userViewHolder.ll_bac.setBackgroundColor(Color.WHITE);
        }
        userViewHolder.tv_item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonUpdateInterface != null) {
                    //接口实例化后的而对象，调用重写后的方法
                    buttonUpdateInterface.onclick(view, position);
                }
            }
        });
        userViewHolder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonDeleteInterface != null) {
                    //接口实例化后的而对象，调用重写后的方法
                    buttonDeleteInterface.onclick(view, position);
                }
            }
        });
    }

    private class UserViewHolder extends BaseViewHolder {
        public LinearLayout ll_bac;
        public TextView tv_item_id;
        public TextView tv_item_name;
        public TextView tv_item_update;
        public TextView tv_item_delete;

        public UserViewHolder(View itemView) {
            super(itemView);
            tv_item_id = (TextView) itemView.findViewById(R.id.tv_item_id);
            ll_bac = (LinearLayout) itemView.findViewById(R.id.ll_bac);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            tv_item_update = (TextView) itemView.findViewById(R.id.tv_item_update);
            tv_item_delete = (TextView) itemView.findViewById(R.id.tv_item_delete);
        }
    }

    private ButtonUpdateInterface buttonUpdateInterface;

    private ButtonDeleteInterface buttonDeleteInterface;

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonUpdateInterface {
        void onclick(View view, int position);
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonDeleteInterface {
        void onclick(View view, int position);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonUpdateSetOnclick(ButtonUpdateInterface buttonUpdateInterface) {
        this.buttonUpdateInterface = buttonUpdateInterface;
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonDeleteSetOnclick(ButtonDeleteInterface buttonDeleteInterface) {
        this.buttonDeleteInterface = buttonDeleteInterface;
    }
}
