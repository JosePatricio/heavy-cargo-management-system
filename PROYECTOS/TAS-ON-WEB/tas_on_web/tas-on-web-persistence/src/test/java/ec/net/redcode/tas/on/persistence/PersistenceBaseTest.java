package ec.net.redcode.tas.on.persistence;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PersistenceBaseTest {

    Logger logger = LoggerFactory.getLogger(PersistenceBaseTest.class);

    private ArrayList<String> APP_CONTEXT_DEV = new ArrayList<>();

    private static boolean configured = false;

    protected TransactionStatus status = null;

    private ApplicationContext appContext;

    protected List<String> springAppContexts(){
        return null;
    }

    private IDatabaseConnection getConnection() throws Exception {
        DataSource ds = (DataSource) getBean("dataSource");
        IDatabaseConnection connection = new DatabaseConnection(ds.getConnection());
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
        return connection;
    }

    protected IDataSet getDataSet() throws Exception {
        return (new FlatXmlDataSetBuilder()).build(
                this.getClass().getResourceAsStream("/dataset.xml"));
    }

    protected void dump() throws Exception {
        IDataSet fullDataSet = getConnection().createDataSet();
        FlatXmlDataSet.write(fullDataSet, System.out);
    }

    private void handleSetUpOperation() throws Exception {
        final IDatabaseConnection conn = getConnection();
        final IDataSet data = getDataSet();
        try {
            logger.info("** Resetting DB to known state **");
            DatabaseOperation.CLEAN_INSERT.execute(conn, data);

            //Commit the table inserts so other connections can retrieve it
            conn.getConnection().commit();
        } finally {
            conn.close();
        }
    }

    public ApplicationContext getAppContext() {
        return appContext;
    }

    public Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }

    public void createTables() throws Exception {
        logger.info("Creating tables");
        Connection conn = null;
        Statement stmt = null;
        try {
            DataSource ds = (DataSource) getBean("dataSource");
            conn = ds.getConnection();

            InputStream is = this.getClass().getResourceAsStream("/db/base_tason.sql");
            SqlRunner.runScript(conn, is);

        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    @Before
    public void runBefore() throws Exception {

        APP_CONTEXT_DEV.clear();
        APP_CONTEXT_DEV.add("conexion-test.xml");
        List<String> addlContexts = springAppContexts();
        if (addlContexts != null) { APP_CONTEXT_DEV.addAll(addlContexts.stream().collect(Collectors.toList())); }

        appContext = new ClassPathXmlApplicationContext(
                APP_CONTEXT_DEV.stream().toArray(size -> new String[size]),true);

        //Create the tables only once
        if (!configured) {
            createTables();
            configured = true;
        }
        // Force a transaction to prevent a LazyInitializationException
        beginTransaction();
        handleSetUpOperation();
    }

    @After
    public void runAfter() throws Exception {
        //Reset the database to the way it originally was
        clearTransaction();
    }

    protected void beginTransaction() {
        JpaTransactionManager jtm = (JpaTransactionManager) appContext.getBean("transactionManager");
        EntityManagerFactoryUtils.getTransactionalEntityManager(jtm.getEntityManagerFactory());
        jtm.getEntityManagerFactory().createEntityManager().getTransaction().begin();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        status = jtm.getTransaction(def);
    }

    protected void commitTransaction() {
        try {
            if (status != null) {
                JpaTransactionManager jtm = (JpaTransactionManager) appContext.getBean("transactionManager");
                jtm.commit(status);
            }
        } finally {
            status = null;
        }
    }

    protected void clearTransaction() {
        try {
            if (status != null) {
                JpaTransactionManager jtm = (JpaTransactionManager) appContext.getBean("transactionManager");
                jtm.rollback(status);
            }
        } finally {
            status = null;
        }
    }

    protected final File getBaseDir() {
        File dir;

        String tmp = System.getProperty("basedir");
        if (tmp != null) {
            dir = new File(tmp);
        } else {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

            dir = new File(path).getParentFile().getParentFile();

            System.setProperty("basedir", dir.getPath());
        }

        return dir;
    }
}
