package net.lucidman.excel.repository;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;

public class MyBatisConnectionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    /** XML에 명시된 접속 정보를 읽어들인다. */
    static {
        try {
            // 접속 정보를 명시하고 있는 XML의 경로 읽기
            InputStream is = MyBatisConnectionFactory.class.getClassLoader().getResourceAsStream("mybatis/mybatis-config.xml");

            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    /** 데이터베이스 접속 객체를 리턴한다. */
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

}
