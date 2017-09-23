package lt.riti.com.liantong.entity;

import java.io.Serializable;

/**
 * Created by brander on 2017/9/21.
 */

public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                '}';
    }
}
