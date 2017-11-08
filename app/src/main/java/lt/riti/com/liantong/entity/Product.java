package lt.riti.com.liantong.entity;

import java.io.Serializable;

/**
 * Created by brander on 2017/11/7.
 */

public class Product implements Serializable {
    private String id;
    private String product_name;//产品名称
    private String product_code;//产品编号【唯一，系统自动生成】
    private String number;//库存，默认为0
    private String depot_code;//公司编号
    private String create_time;//创建时间
    private String admin_id;//创建人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepot_code() {
        return depot_code;
    }

    public void setDepot_code(String depot_code) {
        this.depot_code = depot_code;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_code='" + product_code + '\'' +
                ", number='" + number + '\'' +
                ", depot_code='" + depot_code + '\'' +
                ", create_time='" + create_time + '\'' +
                ", admin_id='" + admin_id + '\'' +
                '}';
    }
}
