package lt.riti.com.liantong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.fragment.NewRfidFragment;

public class NewRfidActivity extends BaseActivity {
    private static final String TAG = "NewRfidActivity";
    @BindView(R.id.vp_new_fg)
    ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    Fragment fg = new Fragment();
    private View newL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rfid);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("新桶入库");
    }

    /**
     * 按下按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: "+keyCode);
        ((NewRfidFragment)fg).onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化界面
     */
    protected void initView() {
        newL = LayoutInflater.from(this).inflate(R.layout.fragment_new_rfid, null);
        ButterKnife.bind(newL);
    }


    /**
     * 初始化数据
     */
    protected void initData() {
        //界面数据
        fragments = new ArrayList<>();
        fragments.add(new NewRfidFragment());
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager.setAdapter(adapter);
        //第一个界面
        viewPager.setCurrentItem(0);
        StockApplication.stockType = 0;//默认入库
        fg = fragments.get(0);
        viewPager.addOnPageChangeListener(new NewRfidActivity.MyOnPageChangeListener());
    }

    /**
     * 监听器
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fg = fragments.get(0);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
