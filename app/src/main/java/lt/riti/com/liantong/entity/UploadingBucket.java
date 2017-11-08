package lt.riti.com.liantong.entity;

import java.io.Serializable;

/**
 * Created by brander on 2017/11/8.
 */

public class UploadingBucket implements Serializable{
    private long manufactor_id;//厂家id
    private String product_code;//产品编号
    private int customer_id;//客户id
    private String depot_code;//创建公司编号
    private int bucket_address;//桶所在位置
    private int status;//0表示报废 1.表示正常

    public int getBucket_address() {
        return bucket_address;
    }

    public void setBucket_address(int bucket_address) {
        this.bucket_address = bucket_address;
    }

    public long getManufactor_id() {
        return manufactor_id;
    }

    public void setManufactor_id(long manufactor_id) {
        this.manufactor_id = manufactor_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDepot_code() {
        return depot_code;
    }

    public void setDepot_code(String depot_code) {
        this.depot_code = depot_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UploadingBucket{" +
                "manufactor_id=" + manufactor_id +
                ", product_code='" + product_code + '\'' +
                ", customer_id=" + customer_id +
                ", depot_code='" + depot_code + '\'' +
                ", bucket_address=" + bucket_address +
                ", status=" + status +
                '}';
    }
}
