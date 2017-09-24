package lt.riti.com.liantong.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by brander on 2017/9/21.
 */

public class RfidUser implements Serializable {
    private String rfidUserId;
    private String userId;
    private String rfidUserName;
    private String rfidUserLocation;
    private String rfidUserCreatTime;

    public String getRfidUserCreatTime() {
        return rfidUserCreatTime;
    }

    public void setRfidUserCreatTime(String rfidUserCreatTime) {
        this.rfidUserCreatTime = rfidUserCreatTime;
    }

    public String getRfidUserId() {
        return rfidUserId;
    }

    public void setRfidUserId(String rfidUserId) {
        this.rfidUserId = rfidUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRfidUserName() {
        return rfidUserName;
    }

    public void setRfidUserName(String rfidUserName) {
        this.rfidUserName = rfidUserName;
    }

    public String getRfidUserLocation() {
        return rfidUserLocation;
    }

    public void setRfidUserLocation(String rfidUserLocation) {
        this.rfidUserLocation = rfidUserLocation;
    }

    @Override
    public String toString() {
        return "RfidUser{" +
                "rfidUserId='" + rfidUserId + '\'' +
                ", userId='" + userId + '\'' +
                ", rfidUserName='" + rfidUserName + '\'' +
                ", rfidUserLocation='" + rfidUserLocation + '\'' +
                ", rfidUserCreatTime=" + rfidUserCreatTime +
                '}';
    }
}
