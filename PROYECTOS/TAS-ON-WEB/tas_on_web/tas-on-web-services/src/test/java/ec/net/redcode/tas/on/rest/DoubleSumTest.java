package ec.net.redcode.tas.on.rest;

import org.junit.Test;

import java.text.DecimalFormat;


public class DoubleSumTest {

    @Test
    public void testSumDouble(){
        double uno = 2.21;
        double dos = 1210.31;
        double tres = 2707.49;

        double sum = uno + dos + tres;

        DecimalFormat format = new DecimalFormat("##.00");
        System.out.println(Double.parseDouble(format.format(sum)));

        Double total = 3920.01;
        System.out.println(Double.parseDouble(format.format(total)));
    }

}
