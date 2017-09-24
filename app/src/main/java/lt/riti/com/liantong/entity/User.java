package lt.riti.com.liantong.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	private String id;
	private String companyId;
	private String name;
	private String password;
	private String token;
	private String nick;
//	private String gender;
//	private String email;
//	private Date createTime;
//	private int userLevel;

	public User() {
		
	}

	public User(String id, String name, String password, String token, String nick) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.token = token;
		this.nick = nick;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//
//	public int getUserLevel() {
//		return userLevel;
//	}
//
//	public void setUserLevel(int userLevel) {
//		this.userLevel = userLevel;
//	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", companyId='" + companyId + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", token='" + token + '\'' +
				", nick='" + nick + '\'' +
//				", gender='" + gender + '\'' +
//				", email='" + email + '\'' +
//				", createTime=" + createTime +
//				", userLevel=" + userLevel +
				'}';
	}
}
