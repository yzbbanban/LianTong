package lt.riti.com.liantong.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by brander on 2017/9/23.
 */

public class RfidOrder implements Serializable {
    private Long rfidOrderId;// rfidOrderId
    private String rfidUserId;// rfidid
    private String rfidOrderNum;// 单号
    private String idName;// 单号名称
    private Long idTime;// 读取次数
    private Date rfidOrderCreateTime;// 创建时间
    private boolean checked;
    private int stockType;// 0入库或1出库

    public Long getRfidOrderId() {
        return rfidOrderId;
    }

    public void setRfidOrderId(Long rfidOrderId) {
        this.rfidOrderId = rfidOrderId;
    }

    public String getRfidUserId() {
        return rfidUserId;
    }

    public void setRfidUserId(String rfidUserId) {
        this.rfidUserId = rfidUserId;
    }

    public String getRfidOrderNum() {
        return rfidOrderNum;
    }

    public void setRfidOrderNum(String rfidOrderNum) {
        this.rfidOrderNum = rfidOrderNum;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public Long getIdTime() {
        return idTime;
    }

    public void setIdTime(Long idTime) {
        this.idTime = idTime;
    }

    public Date getRfidOrderCreateTime() {
        return rfidOrderCreateTime;
    }

    public void setRfidOrderCreateTime(Date rfidOrderCreateTime) {
        this.rfidOrderCreateTime = rfidOrderCreateTime;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getStockType() {
        return stockType;
    }

    public void setStockType(int stockType) {
        this.stockType = stockType;
    }

    @Override
    public String toString() {
        return "RfidOrder [rfidOrderId=" + rfidOrderId + ", rfidUserId="
                + rfidUserId + ", rfidOrderNum=" + rfidOrderNum + ", idName="
                + idName + ", idTime=" + idTime + ", rfidOrderCreateTime="
                + rfidOrderCreateTime + ", checked=" + checked + ", stockType="
                + stockType + "]";
    }
}
