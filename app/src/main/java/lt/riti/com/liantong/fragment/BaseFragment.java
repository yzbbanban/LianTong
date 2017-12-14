package lt.riti.com.liantong.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;
import com.clouiotech.pda.rfid.uhf.UHF;
import com.clouiotech.pda.rfid.uhf.UHFReader;
import com.clouiotech.pda.scanner.ScanReader;
import com.clouiotech.pda.scanner.Scanner;
import com.clouiotech.port.Adapt;
import com.clouiotech.util.Helper.Helper_ThreadPool;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.app.StockApplication;
import lt.riti.com.liantong.entity.Bucket;
import lt.riti.com.liantong.entity.PublicData;
import lt.riti.com.liantong.entity.RfidOrder;
import lt.riti.com.liantong.util.ToastUtil;
import lt.riti.com.liantong.view.IShowList;

/**
 * Created by brander on 2017/9/22.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    static Boolean _UHFSTATE = false; // 模块是否已经打开
    // static int _PingPong_ReadTime = 10000; // 默认是100:3
    // static int _PingPong_StopTime = 300;
    static int _NowAntennaNo = 1; // 读写器天线编号
    static int _UpDataTime = 0; // 重复标签上传时间，控制标签上传速度不要太快
    static int _Max_Power = 30; // 读写器最大发射功率
    static int _Min_Power = 0; // 读写器最小发射功率

    static int low_power_soc = 10;

    public static UHF CLReader = UHFReader.getUHFInstance();


    protected boolean isStartPingPong = false; //
    protected static boolean isKeyDown = false; // 板机是否按下
    protected boolean isLongKeyDown = false; // 板机是否是长按状态
    protected int keyDownCount = 0; // 板机按下次数

    protected int readTime = 0;
    protected int lastReadCount = 0;
    protected int totalReadCount = 0; // 总读取次数
    protected int speed = 0; // 读取速度
    protected int _ReadType = 0; // 0 为读EPC，1 为读TID
    protected String _NowReadParam = _NowAntennaNo + "|1"; // 读标签参数
    protected static HashMap<String, EPCModel> hmList = new HashMap<String, EPCModel>();
    protected Object hmList_Lock = new Object();
    protected boolean flag = true; //
    protected Boolean IsFlushList = true; // 是否刷列表
    protected Object beep_Lock = new Object();
    ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM,
            ToneGenerator.MAX_VOLUME);
    protected static boolean isPowerLowShow = false;

    protected static boolean isSingle = false;
    protected boolean usingBackBattery = false;
    protected static int stockFlag = 1;
    protected List<Bucket> buckets = new ArrayList<>();

    private IAsynchronousMessage am;
    //二维码扫描
    boolean busy = false;
    private Scanner scanReader = ScanReader.getScannerInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initListener() {
    }

    protected void initView() {

    }

    protected void initData(IAsynchronousMessage am) {
//        Log.i(TAG, "initData: ");
        usingBackBattery = canUsingBackBattery();
        if (!UHF_Init(usingBackBattery, am)) { // 打开模块电源失败
            try {
                ShowPowerLow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                UHF_GetReaderProperty(); // 获得读写器的能力
                _NowReadParam = _NowAntennaNo + "|1";
                Thread.sleep(20);
                CLReader.Stop(); // 停止指令
                Thread.sleep(20);
                UHF_SetTagUpdateParam(); // 设置标签重复上传时间为20ms
            } catch (Exception ee) {
            }
            IsFlushList = true;

            Helper_ThreadPool.ThreadPool_StartSingle(new Runnable() { // 蜂鸣器发声
                @Override
                public void run() {
                    while (IsFlushList) {
                        synchronized (beep_Lock) {
                            try {
                                beep_Lock.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                        if (IsFlushList) {
                            toneGenerator
                                    .startTone(ToneGenerator.TONE_PROP_BEEP);
                        }

                    }
                }
            });

        }
    }


    /**
     * 超高频模块初始化
     *
     * @param log 接口回调方法
     * @return 是否初始化成功
     */
    public Boolean UHF_Init(boolean usingBackupPower, IAsynchronousMessage log) {
        Boolean rt = false;
        try {
            if (_UHFSTATE == false) {
                rt = CLReader.OpenConnect(usingBackupPower, log);
                if (rt) {
                    _UHFSTATE = true;
                }
            } else {
                rt = true;
            }
        } catch (Exception ex) {
//            Log.d("debug", "UHF上电出现异常：" + ex.getMessage());
        }
        return rt;
    }

    /**
     * 超高频模块释放
     */
    public void UHF_Dispose() {
        if (_UHFSTATE == true) {
            CLReader.CloseConnect();
            _UHFSTATE = false;
        }
    }

    /**
     * 获得读写器的读写能力
     */
    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("serial")
    protected void UHF_GetReaderProperty() {
        String propertyStr = CLReader.GetReaderProperty();
//        Log.d("Debug", "获得读写器能力：" + propertyStr);
        String[] propertyArr = propertyStr.split("\\|");
        HashMap<Integer, Integer> hm_Power = new HashMap<Integer, Integer>() {
            {
                put(1, 1);
                put(2, 3);
                put(3, 7);
                put(4, 15);
            }
        };
        if (propertyArr.length > 3) {
            try {
                _Max_Power = Integer.parseInt(propertyArr[0]);
                _Min_Power = Integer.parseInt(propertyArr[1]);
                int powerIndex = Integer.parseInt(propertyArr[2]);
                _NowAntennaNo = hm_Power.get(powerIndex);
            } catch (Exception ex) {
//                Log.d("Debug", "获得读写器能力失败,转换失败！");
            }
        } else {
//            Log.d("Debug", "获得读写器能力失败！");
        }
    }

    /**
     * 设置标签上传参数
     */
    protected void UHF_SetTagUpdateParam() {
        // 先查询当前的设置是否一致，如果不一致才设置
        String searchRT = CLReader.GetTagUpdateParam();
        String[] arrRT = searchRT.split("\\|");
        if (arrRT.length >= 2) {
            int nowUpDataTime = Integer.parseInt(arrRT[0]);
//            Log.d("Debug", "查标签上传时间：" + nowUpDataTime);
            if (_UpDataTime != nowUpDataTime) {
                CLReader.SetTagUpdateParam("1," + _UpDataTime); // 设置标签重复上传时间为20ms
//                Log.d("Debug", "设置标签上传时间...");
            } else {

            }
        } else {
//            Log.d("Debug", "查询标签上传时间失败...");
        }
    }

    public void stopRfid() {
//        Log.i(TAG, TAG + "onKeyUp: ");
        CLReader.Stop();
        keyDownCount = 0;
        isKeyDown = false;
        isLongKeyDown = false;
    }

    /**
     * 按钮反弹
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == 131 || keyCode == 135) { // 放开扳机
//            Log.i(TAG, TAG + "onKeyUp: ");
            CLReader.Stop();
            keyDownCount = 0;
            isKeyDown = false;
            isLongKeyDown = false;
            ScanDispose();
        }
        isRcodeSingle = true;
        busy = false;

//        buckets.clear();
        i = 0;
        return true;
    }

    /**
     * 二维码扫描
     */

    protected void ScanDispose() {
        if (scanReader != null) {
            scanReader.close();
//            scanReader = null;
        }
    }

    String idString;
    HashMap<String, String> bs = new HashMap<>();
    int i = 0;
    private IShowList showList;

    protected void DeCode(final IShowList showList) {
        this.showList = showList;
        if (i == 0) {
            bs.clear();
            i++;
        }
//        ToastUtil.showLongToast("i:" + (i++));
//        bs.clear();
        scaninit();
        if (busy) {
//            ToastUtil.showShortToast(getString(R.string.str_busy));
            return;
        }

        busy = true;
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        singleThreadExecutor.execute(new Runnable() {

            @Override
            public void run() {
                try {
//
                    byte[] id = scanReader.decode();
                    if (id != null) {
                        busy = true;
                        idString = new String(id, Charset.forName("Utf8")) + "\n";
                        idString = idString.trim();
                        if (!"".equals(id)) {
                            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getActivity(), "id+++: " + idString, Toast.LENGTH_SHORT).show();
                                }
                            });

                            if (bs.containsKey(("Bucket_code" + idString))) {//有

                            } else {
                                bs.put("Bucket_code" + idString, idString);
                                handler.sendEmptyMessage(0);

                            }
                            isRcodeSingle = false;
                            ScanDispose();

                        }

                    } else {
//                        idString = null;
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(), getString(R.string.str_faild), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        busy = false;

                    }
//                    Log.i(TAG, "run: _________________");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showList.showView(getRCodeData());
                    break;

            }
        }
    };


    private void scaninit() {
        if (null == scanReader) {
//            return false;
            scanReader = ScanReader.getScannerInstance();
            scanReader.open(getActivity().getApplicationContext());
//            return true;
//            return true;
        } else {
            scanReader.open(getActivity().getApplicationContext());
//            return true;
        }
//        boolean ret = scanReader.open(getActivity().getApplicationContext());
//        Toast.makeText(getActivity(), "scaninit: " + ret, Toast.LENGTH_SHORT).show();
//        return ret;
    }

    //读取一个
    boolean isRcodeSingle = true;

    /**
     * 获取bucket
     *
     * @return
     */
    protected List<Bucket> getRCodeData() {
//        buckets.clear();
        Iterator iterator = bs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            Bucket bucket = new Bucket();
            String idName = val;
            if (!"".equals(idName)) {
                bucket.setBucket_code(idName);
                bucket.setIdTime(1);
                bucket.setChecked(true);
                buckets.add(bucket);
            }

        }
        //去除重复值
        if (buckets.size() > 0) {
            for (int j = 0; j < buckets.size() - 1; j++) {
                for (int k = buckets.size() - 1; k > j; k--) {
                    if (buckets.get(k).getBucket_code().equals(buckets.get(j).getBucket_code())) {
                        buckets.remove(k);
                    }
                }
            }
        }

        return buckets;
    }

    // 获得更新数据源
    @SuppressWarnings({"rawtypes", "unused"})
    protected List<Bucket> getData() {
        buckets.clear();
        synchronized (hmList_Lock) {
            // if(hmList.size() > 0){ //
            Iterator iter = hmList.entrySet().iterator();
//            Log.i(TAG, "getData: size:+++++++++++++++++>> " + hmList.size());
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                EPCModel val = (EPCModel) entry.getValue();
                Map<String, Object> map = new HashMap<String, Object>();
                Bucket bucket = new Bucket();
                String idName = val._EPC;
//                idName = idName.substring(idName.length() - 11);
                bucket.setBucket_code(idName);
                bucket.setIdTime(val._TotalCount);
                bucket.setChecked(true);
                buckets.add(bucket);
            }
            // }
        }
//        Log.i(TAG, "getData: " + buckets);
        return buckets;
    }

    /**
     * 标签读取监听器
     *
     * @param model
     */
    public void OutPutEPC(EPCModel model) {
        if (!isKeyDown) {
            return;
        }
        try {
            synchronized (hmList_Lock) {
                //判断是否同一个作业情况:入库，出库
                if (StockApplication.getIsInStock() == stockFlag) {//同一情况，没有变换页面

                } else {//变换页面
                    stockFlag = StockApplication.getIsInStock();
                    //清空hmList数据
                    hmList.clear();
                }
                //存在相同数据
                if (hmList.containsKey(model._EPC + model._TID)) {
                    EPCModel tModel = hmList.get(model._EPC + model._TID);
                    tModel._TotalCount++;
                } else {//没有相同数据添加
                    //TODO 读取单个数据
                    if (isSingle) {
//                        Log.i(TAG, "OutPutEPC:--------------> ");
//                        Log.i(TAG, "OutPutEPC size: " + hmList.size());
                        if (hmList.size() >= 0 && hmList.size() < 1) {//只允许插入一个数
//                            Log.i(TAG, "j size: " + hmList.size());
                            hmList.put(model._EPC + model._TID, model);
                        } else {
                            return;
                        }
                    } else {
//                        Log.i(TAG, TAG + " OutPutEPC: " + model._EPC + model._TID);
                        //TODO 批量读取
                        hmList.put(model._EPC + model._TID, model);
                    }

                }
            }
            synchronized (beep_Lock) {
                beep_Lock.notify();
            }
            totalReadCount++;
        } catch (Exception ex) {
//            Log.d("Debug", "标签输出异常：" + ex.getMessage());
        }

    }

    /**
     * 显示电量低
     */
    protected void ShowPowerLow() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confim")
                // 设置对话框标题
                .setMessage(getString(R.string.uhf_low_power_consumption))
                // 设置显示的内容
                .setPositiveButton(getString(R.string.str_ok),
                        new DialogInterface.OnClickListener() {// 添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {// 确定按钮的响应事件
                                UHF_Init(false, am);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                isPowerLowShow = false;
                            }
                        })
                .setNegativeButton(getString(R.string.str_cancel),
                        new DialogInterface.OnClickListener() {// 添加返回按钮
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {// 响应事件
                                Dispose();
                                isPowerLowShow = false;
                                getActivity().finish();
                            }
                        }).show();// 在按键响应事件中显示此对话框

    }

    /**
     * 释放资源
     */
    public void Dispose() {
        isStartPingPong = false;
        IsFlushList = false;
        synchronized (beep_Lock) {
            beep_Lock.notifyAll();
        }
        UHF_Dispose();
    }

    // 判断副电电量
    protected Boolean canUsingBackBattery() {
        if (Adapt.getPowermanagerInstance().getBackupPowerSOC() < low_power_soc) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ScanDispose();
    }


}
