package yanglas.flowline;

public class Msg {
    private double i;
    private double j;
    private String desc;

    public double getJ() {
        return j;
    }

    public void setJ(double j) {
        this.j = j;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "i=" + i +
                ", j=" + j +
                ", desc='" + desc + '\'' +
                '}';
    }
}
