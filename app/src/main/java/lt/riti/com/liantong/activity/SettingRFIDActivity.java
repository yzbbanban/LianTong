package lt.riti.com.liantong.activity;

import java.util.*;


import com.clouiotech.pda.rfid.EPCModel;
import com.clouiotech.pda.rfid.IAsynchronousMessage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import lt.riti.com.liantong.R;
import lt.riti.com.liantong.entity.PublicData;


/**
 * @author RFID_lx 配置
 */
public class SettingRFIDActivity extends UHFBaseActivity implements
        IAsynchronousMessage {

    private Spinner sp_Configration_Power = null;
    private Spinner sp_PingPongReadTime = null;
    private Spinner sp_PingPongStopTime = null;
    private Spinner sp_Frequency = null;
    private Spinner sp_BaseSpeedType = null;
    private Spinner sp_BaseSpeedQValue = null;
    private Spinner sp_Carrier = null;
    private Spinner sp_TagType = null;

    // private boolean busy = false;

    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("serial")
    private HashMap<Integer, Integer> mm_PingPong = new HashMap<Integer, Integer>() {
        {
            put(0, 0);
            put(100, 1);
            put(200, 2);
            put(300, 3);
            put(500, 4);
            put(800, 5);
            put(1000, 6);
            put(5000, 7);
            put(10000, 8);
        }
    };

    protected void Init() {
        // do not using power switch in configure
        if (!UHF_Init(false, this)) { // 打开模块电源失败
            ShowMsg(getString(R.string.uhf_low_power_info),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SettingRFIDActivity.this.finish();
                        }
                    });
        } else {
            sp_Configration_Power = (Spinner) this
                    .findViewById(R.id.sp_Configration_Power);
            sp_PingPongReadTime = (Spinner) this
                    .findViewById(R.id.sp_PingPongReadTime);
            sp_PingPongStopTime = (Spinner) this
                    .findViewById(R.id.sp_PingPongStopTime);
            sp_Frequency = (Spinner) this.findViewById(R.id.sp_Frequency);
            sp_BaseSpeedType = (Spinner) this
                    .findViewById(R.id.sp_BaseSpeedType);
            sp_BaseSpeedQValue = (Spinner) this
                    .findViewById(R.id.sp_BaseSpeedQValue);
            sp_Carrier = (Spinner) this.findViewById(R.id.sp_Carrier);
            sp_TagType = (Spinner) this.findViewById(R.id.sp_TagType1);

            GetBaseBand();// 查询基带速率
            GetPower(); // 获取功率
            Button_GetFrequency(null); // 查询频段
            sp_PingPongReadTime.setSelection(mm_PingPong
                    .get(PublicData._PingPong_ReadTime));
            sp_PingPongStopTime.setSelection(mm_PingPong
                    .get(PublicData._PingPong_StopTime));
            //
            if (PublicData._IsCommand6Cor6B.equals("6C")) {
                sp_TagType.setSelection(0);
            } else {
                sp_TagType.setSelection(1);
            }

        }
        return;
    }

    // 查询当前功率
    protected boolean GetPower() {
        boolean rt = false;
        String sPower = CLReader.GetPower();
        Log.d("GetPower:", sPower);
        String[] arrParam = sPower.split("\\&");
        if (arrParam.length > 0) {
            for (String paramItem : arrParam) {
                String[] arrItem = paramItem.split(",");
                if (arrItem.length == 2) {
                    if (arrItem[0].equals("1")) {
                        if (sp_Configration_Power != null) {
                            sp_Configration_Power.setSelection(Integer
                                    .parseInt(arrItem[1]));
                            rt = true;
                        }
                    }
                }
            }
        }
        return rt;
    }

    // 释放资源
    protected void Dispose() {
        UHF_Dispose();
    }

    // 返回主页
    public void Back(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 创建
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.uhf_configration);
        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception ex) {
            Log.d("Debug", "初始化发现异常：" + ex.getMessage());
        }
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        tv_center.setText("RFID设置");
    }

    @Override
    protected void onDestroy() {
        // 释放
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // 待机锁屏
        super.onPause();
        Dispose();
    }

    @Override
    protected void onResume() {
        // 待机恢复
        super.onResume();
        try {
            Init();
            // super.UHF_GetReaderProperty();
        } catch (Exception ex) {
        }
    }

    @Override
    public void OutPutEPC(EPCModel model) {

    }

    // ------------------------- 按钮事件 -------------------------------
    // 获取功率
    public void Button_GetPower(View v) {
        if (isFastClick()) {
            return;
        }
        if (GetPower()) {
            ShowMsg(getString(R.string.str_success), null);
        } else {
            ShowMsg(getString(R.string.str_faild), null);
        }

    }

    // 设置功率
    public void Button_SetPower(View v) {
        if (isFastClick()) {
            return;
        }
        String setParam = "30";
        if (sp_Configration_Power != null) {
            setParam = sp_Configration_Power.getSelectedItem().toString();
        }
        String param = "1," + setParam; // + "&2," + setParam;
        if (_NowAntennaNo == 3) { // K3 两个天线
            param += "&2," + setParam;
        }
        String rt = CLReader.SetPower(param);
        if (rt.startsWith("0")) {
            ShowMsg(getString(R.string.str_success), null);
        } else {
            ShowMsg(getString(R.string.str_faild), null);
        }

    }

    // 读时间设置
    public void Button_SetPingPongReadTime(View v) {
        PublicData._PingPong_ReadTime = Integer.parseInt(sp_PingPongReadTime
                .getSelectedItem().toString());
        ShowMsg(getString(R.string.str_success), null);

    }

    // 间隔时间设置
    public void Button_SetPingPongStopTime(View v) {
        if (isFastClick()) {
            return;
        }
        PublicData._PingPong_StopTime = Integer.parseInt(sp_PingPongStopTime
                .getSelectedItem().toString());
        ShowMsg(getString(R.string.str_success), null);

    }

    // 查询工作频段
    public void Button_GetFrequency(View v) {
        try {
            Integer frequencyNo = Integer.parseInt(CLReader.GetFrequency());
            sp_Frequency.setSelection(frequencyNo);

            if (v != null)
                ShowMsg(getString(R.string.str_success), null);
        } catch (Exception ex) {
            if (v != null)
                ShowMsg(getString(R.string.str_faild), null);
        }

    }

    // 设置工作频段
    public void Button_SetFrequency(View v) {
        if (isFastClick()) {
            return;
        }
        String param = sp_Frequency.getSelectedItemPosition() + "";
        if (CLReader.SetFrequency(param).startsWith("0")) {
            ShowMsg(getString(R.string.str_success), null);
        } else {
            ShowMsg(getString(R.string.str_faild), null);
        }
    }

    // 恢复出厂设置
    public void Button_Configration_RF(View v) {
        if (isFastClick()) {
            return;
        }

        if (CLReader.SetRF("5AA5A55A").startsWith("0")) {
            ShowMsg(getString(R.string.str_success), null);
        } else {
            ShowMsg(getString(R.string.str_faild), null);
        }
    }

    // 设置读6C或6B标签
    public void Button_TagType_Set(View v) {
        if (isFastClick()) {
            return;
        }
        //
        PublicData._IsCommand6Cor6B = sp_TagType.getSelectedItem().toString();
        ShowMsg(getString(R.string.str_success), null);
    }

    // 升级基带
    public void Button_Configration_UpdateBase(View v) {
        super.ShowConfim("Confim UpdateBase？", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                CLReader.UpdateSoft("");
            }
        }, null);
    }

    // 查询基带参数
    public void Button_GetBaseBand(View v) {
        if (isFastClick()) {
            return;
        }
        GetBaseBand();
    }

    @SuppressWarnings("serial")
    private void GetBaseBand() {
        String rt = CLReader.GetBaseBand();
        String[] strParam = rt.split("\\|");
        if (strParam.length > 0) {
            for (int i = 0; i < strParam.length; i++) {
                if (i == 0) {
                    int iType = Integer.parseInt(strParam[i]);
                    if (iType == 255)
                        iType = 4;
                    sp_BaseSpeedType.setSelection(iType);
                } else if (i == 1) {

                    HashMap<String, Integer> mm = new HashMap<String, Integer>() {
                        {
                            put("4", 1);
                            put("0", 0);
                        }
                    };
                    if (mm.containsKey(strParam[i])) {
                        sp_BaseSpeedQValue.setSelection(mm.get(strParam[i]));
                    }
                }
            }
        }
    }

    // 设置基带参数
    public void Button_SetBaseBand(View v) {
        if (isFastClick()) {
            return;
        }
        String param = "";
        int typePosition = sp_BaseSpeedType.getSelectedItemPosition();
        if (typePosition == 4)
            typePosition = 255;
        int qValue = Integer.parseInt(sp_BaseSpeedQValue.getSelectedItem()
                .toString());
        param = "1," + typePosition + "&2," + qValue;
        if (CLReader.SetBaseBand(param).startsWith("0")) {
            ShowMsg(getString(R.string.str_success), null);
        } else {
            ShowMsg(getString(R.string.str_faild), null);
        }
    }

    public void Button_Test(View v) {
        if (isFastClick()) {
            return;
        }
        int antNo = Integer.parseInt(sp_Carrier.getSelectedItem().toString());
        CLReader.Set_0101_00((byte) antNo, (byte) 0x00);
        ShowMsg(CLReader.Get_0101_05(), null);
    }
}
