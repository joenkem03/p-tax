package org.bizzdeskgroup.factory;

//import com.kumuluz.ee.logs.cdi.Log;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.log4j.ConsoleAppender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

//@Log
public class MyBatisConnectionFactory {
    private static SqlSessionFactory sqlSessionFactory;
//    private static LogFactory LOGGER =  LogFactory.useLog4JLogging();
//    private static Logger LOGGER = LoggerFactory.getLogger(User.class.getName());

    static {
        try {
//            String resource = "config/database-config.xml";
            String resource = "config/mybatis_db-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            if (sqlSessionFactory == null) {
                System.out.println("Got here...qqqq");
//                LOGGER.trace("final", reader);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.out.println("myBatisDbConfig");
            System.out.println(fileNotFoundException);
            fileNotFoundException.printStackTrace();
        }
        catch (IOException iOException) {
            System.out.println("myBatisDbConfig1");
            iOException.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println("myBatisDbConfig2");
            ex.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
