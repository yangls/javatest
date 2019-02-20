package testbase;

public class TestFormat {
    private static long _flowID = 100000L;
    public static void main(String[] args){
        System.out.println(String.format("%0$01d", new Object[]{Long.valueOf(_flowID)}));
        System.out.println(_flowID);
    }
}
