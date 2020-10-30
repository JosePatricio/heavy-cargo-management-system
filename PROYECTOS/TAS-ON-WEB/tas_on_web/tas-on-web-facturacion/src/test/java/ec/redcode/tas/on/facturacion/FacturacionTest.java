package ec.redcode.tas.on.facturacion;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class FacturacionTest {

    @Test
    public void testDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(diaSemana);
        calendar.add(Calendar.DATE, -7);
        Date todate = calendar.getTime();
        System.out.println(todate);
    }

}
