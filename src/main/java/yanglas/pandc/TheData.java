package yanglas.pandc;

/**
 * 要处理的数据
 * 不变模式，避免多线程问题，只赋值一次
 */
public final class TheData {
    private final int intData;

    public TheData(int intData) {
        //唯一赋值
        this.intData = intData;
    }

    public TheData(String intData){
        this.intData = Integer.parseInt(intData);
    }

    public int getIntData() {
        return intData;
    }

    @Override
    public String toString() {
        return "TheData{" +
                "intData=" + intData +
                '}';
    }
}
