package lt.riti.com.liantong.entity;

/**
 * Created by brander on 2017/9/23.
 */

public class RfidOrder {
    private String idName;
    private Long idTime;
    private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "RfidOrder{" +
                "idName='" + idName + '\'' +
                ", idTime=" + idTime +
                ", isChecked=" + isChecked +
                '}';
    }


}
