package yanglas.socket.serializable.model;

import java.io.Serializable;

/**
 * 订阅返回POJO
 */
public class SubscribeResp implements Serializable {
    private static final long serialVersionUID = 1L;


    private int id;

    private int respCode;

    private String desc;

    @Override
    public String toString() {
        return "SubscribeResp{" +
                "id=" + id +
                ", respCode=" + respCode +
                ", desc='" + desc + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
