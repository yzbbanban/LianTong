package lt.riti.com.liantong.entity;

import java.io.Serializable;

public class Manufacture implements Serializable {
	private long id;
	private String manufactor_name;
	private String manufactor_address;
	private String depot_code;
	private String create_time;
	private String admin_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getManufactor_name() {
		return manufactor_name;
	}

	public void setManufactor_name(String manufactor_name) {
		this.manufactor_name = manufactor_name;
	}

	public String getManufactor_address() {
		return manufactor_address;
	}

	public void setManufactor_address(String manufactor_address) {
		this.manufactor_address = manufactor_address;
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
		return "Manufacture [id=" + id + ", manufactor_name=" + manufactor_name
				+ ", manufactor_address=" + manufactor_address
				+ ", depot_code=" + depot_code + ", create_time=" + create_time
				+ ", admin_id=" + admin_id + "]";
	}

}
