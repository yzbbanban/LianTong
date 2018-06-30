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
import lt.riti.com.liantong.fragment.BindingFragment;

public class BindingActivity extends BaseActivity {
    private static final String TAG = "BindingActivity";
    @BindView(R.id.vp_binding_fg)
    ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    Fragment fg = new Fragment();
    private View newL;
    private int inputType = 0;//默认pda扫描
    private boolean isRCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("包装桶入库");
        tv_right.setText("RFID扫瞄");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRCode) {//是扫码
                    tv_right.setText("二维码扫描");
                    inputType = 1;
                    isRCode = false;
                } else {//PDA扫描
                    tv_right.setText("RFID扫描");
                    inputType = 0;
                    isRCode = true;
                }
                ((BindingFragment) fg).setCodeStatus(inputType);

            }
        });
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
        Log.i(TAG, "onKeyDown: " + keyCode);
//        Toast.makeText(this, "onKeyDown22: " + keyCode, Toast.LENGTH_SHORT).show();
        ((BindingFragment) fg).onKeyDown(keyCode, event, inputType);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化界面
     */
    protected void initView() {
        newL = LayoutInflater.from(this).inflate(R.layout.fragment_binding, null);
        ButterKnife.bind(newL);
    }


    /**
     * 初始化数据
     */
    protected void initData() {
        //界面数据
        fragments = new ArrayList<>();
        fragments.add(new BindingFragment());
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
        viewPager.addOnPageChangeListener(new BindingActivity.MyOnPageChangeListener());
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

    /**
     * 按钮反弹
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (fg instanceof BindingFragment) {
            Log.i(TAG, "instanceof: " + keyCode);
            ((BindingFragment) fragments.get(0)).onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }
}
