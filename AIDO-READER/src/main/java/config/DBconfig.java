package config;

import java.io.FileInputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class DBconfig {
	@Bean
	public DataSource dataSource() {
		ComboPooledDataSource ds = new ComboPooledDataSource();	//Connection Pool 객체
		Properties prop = new Properties();
		try {
			FileInputStream fis = null;
			if(System.getProperty("os.name").split(" ")[0].equals("Windows")){
				fis = new FileInputStream("C:/properties/mssql.properties");
			}else{
				fis = new FileInputStream("/home/ec2-user/properties/mssql.properties");
			}
			prop.load(fis);
			ds.setDriverClass(prop.getProperty("driverclass"));
			ds.setJdbcUrl(prop.getProperty("url"));
			ds.setUser(prop.getProperty("user"));
			ds.setPassword(prop.getProperty("password"));
			ds.setMaxPoolSize(100);		//최대 연결 객체의 개수
			ds.setMinPoolSize(5);		//최소 연결 객체의 개수
			ds.setInitialPoolSize(40);	//초기의 연결 객체의 개수
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {	//mybatis 설정
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource());
		bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		return bean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
}