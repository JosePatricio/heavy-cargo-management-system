package ec.net.redcode.tas.on.persistence;

import java.util.Arrays;
import java.util.List;

public class CommonTest extends PersistenceBaseTest {

    @Override
    protected List<String> springAppContexts() {
        return Arrays.asList("persistence-test.xml");
    }

}
