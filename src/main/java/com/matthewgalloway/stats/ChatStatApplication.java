package com.matthewgalloway.stats;

import javax.jms.Queue;
import javax.sql.DataSource;

import org.apache.activemq.command.ActiveMQQueue;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@PropertySource("classpath:config.properties")
public class ChatStatApplication {

	@Value("classpath:schema.sql")
	private Resource H2_SCHEMA_SCRIPT;

	@Bean(name="beginViewersUpdate")
	public Queue viewersQueue() {
		return new ActiveMQQueue("chatstat.viewers");
	}
	
	@Bean(name="lookupViewer")
	public Queue viewerQueue() {
		return new ActiveMQQueue("chatstat.viewers.viewer");
	}
	
	@Bean(name="lookupViewerFromApi")
	public Queue viewerApiQueue() {
		return new ActiveMQQueue("chatstat.followers.viewer.call");
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());

		return initializer;
	}

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(H2_SCHEMA_SCRIPT);

		return populator;
	}
	
	@Bean
	public DataSource dataSource() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:./stat.v2;");
		ds.setUser("sa");
		ds.setPassword("");

		return ds;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(this.dataSource());
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatStatApplication.class, args);
	}
}
