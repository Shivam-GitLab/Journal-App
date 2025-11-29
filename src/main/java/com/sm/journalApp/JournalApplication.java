package com.sm.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableTransactionManagement
public class JournalApplication {
	public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
	}
/*	@Bean
	public PlatformTransactionManager add(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}*/
}
