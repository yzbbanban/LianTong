package lt.riti.com.liantong.entity;

/**
 * Created by brander on 2017/9/23.
 */

public class StockId {
    private String idName;
    private Long idTime;

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

    @Override
    public String toString() {
        return "StockId{" +
                "idName='" + idName + '\'' +
                ", idTime=" + idTime +
                '}';
    }
}
