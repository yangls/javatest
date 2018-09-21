package testbase;

public class TestFinal {


    final String my;

//    public TestFinal() {
//    }

    public TestFinal(String my) {
        this.my = my;
    }

    public static void main(String[] args){
        TestFinal t = new TestFinal("what?");

        System.out.println(t.toString());
    }

    @Override
    public String toString() {
        return "TestFinal{" +
                "my='" + my + '\'' +
                '}';
    }
}
