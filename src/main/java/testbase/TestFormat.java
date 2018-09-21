package testbase;

public class TestFormat {
    private static long _flowID = 1L;
    public static void main(String[] args){
        System.out.println(String.format("%0$01d", new Object[]{Long.valueOf(_flowID)}));
    }
}
