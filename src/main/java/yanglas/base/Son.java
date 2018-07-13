package yanglas.base;

public class Son extends Father {

    //继承了父类的子类，父类如果没有非私有的构建方法，子类将无法构建
    public Son(int id) {
        super(id);
    }
}
