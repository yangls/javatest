package yanglas.base;

public class Father {
    private int id;
    private Father() {
    }
    public Father(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
