package testbase;

import java.math.BigDecimal;

public class TestBigDecimal {
    public static void main(String[] args){

        BigDecimal b = new BigDecimal("123.22").subtract(new BigDecimal("0"));
        System.out.println(b.toString());
    }
}
