package lt.riti.com.liantong.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lt.riti.com.liantong.R;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.fragment.StockInFragment;
import lt.riti.com.liantong.fragment.StockOutFragment;
import lt.riti.com.liantong.util.ToastUtil;

/**
 * Created by brander on 2017/9/20.
 * 仓库作业界面
 */
public class StockActivity extends BaseActivity {
    @BindView(R.id.stockInLayout)
    TextView stockInLayout;
    @BindView(R.id.stockOutLayout)
    TextView stockOutLayout;
    // 滚动条图片
    @BindView(R.id.scrollbar)
    ImageView scrollbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    private View sIL;
    private View sOL;
    Fragment fg = new Fragment();
    private static final String TAG = "StockActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    /**
     * 设置toolbar
     *
     * @param toolbar
     */
    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("出入库作业");
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        //界面数据
        fragments = new ArrayList<>();
        fragments.add(new StockInFragment());
        fragments.add(new StockOutFragment());
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
        stockInLayout.setTextColor(Color.parseColor("#0ebbfa"));
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //滚动条宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar).getWidth();
        //获取屏幕宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //当前窗口信息放入容器
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //屏幕宽度
        int screenW = displayMetrics.widthPixels;
        //计算滚动条初始偏移量
        offset = (screenW / 2 - bmpW) / 2;
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);

    }

    /**
     * 设置监听器
     */
    protected void initListener() {
        stockInLayout.setOnClickListener(this);
        stockOutLayout.setOnClickListener(this);

    }


    /**
     * 初始化界面
     */
    protected void initView() {
        sIL = LayoutInflater.from(this).inflate(R.layout.stock_in_layout, null);
        ButterKnife.bind(sIL);
        sOL = LayoutInflater.from(this).inflate(R.layout.stock_out_layout, null);
        ButterKnife.bind(sOL);
    }

    /**
     * 监听器
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stockInLayout:
                fg = fragments.get(0);
                viewPager.setCurrentItem(0);
                StockApplication.stockType = 0;//入库
                stockInLayout.setTextColor(Color.parseColor("#0ebbfa"));
                stockOutLayout.setTextColor(Color.BLACK);
                break;
            case R.id.stockOutLayout:
                fg = fragments.get(1);
                viewPager.setCurrentItem(1);
                StockApplication.stockType = 1;//出库
                stockInLayout.setTextColor(Color.BLACK);
                stockOutLayout.setTextColor(Color.parseColor("#0ebbfa"));
                break;

        }
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    fg = fragments.get(0);
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    stockInLayout.setTextColor(Color.parseColor("#0ebbfa"));
                    stockOutLayout.setTextColor(Color.BLACK);
                    StockApplication.stockType = 0;//入库
                    break;
                case 1:
                    fg = fragments.get(1);
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    stockInLayout.setTextColor(Color.BLACK);
                    stockOutLayout.setTextColor(Color.parseColor("#0ebbfa"));
                    StockApplication.stockType = 1;//出库
                    break;
            }
            //position为切换到的页的编码
            currIndex = position;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
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
        if (fg instanceof StockInFragment) {
            Log.i(TAG, "instanceof: " + keyCode);
            ((StockInFragment) fragments.get(0)).onKeyDown(keyCode, event);
        } else {
            ((StockOutFragment) fragments.get(1)).onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
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
        if (fg instanceof StockInFragment) {
            Log.i(TAG, "instanceof: " + keyCode);
            ((StockInFragment) fragments.get(0)).onKeyUp(keyCode, event);
        } else {
            ((StockOutFragment) fragments.get(1)).onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }
}
