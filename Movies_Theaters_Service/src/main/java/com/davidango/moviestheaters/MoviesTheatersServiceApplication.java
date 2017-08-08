package com.davidango.moviestheaters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import ch.qos.logback.access.tomcat.LogbackValve;

@SpringBootApplication
@ComponentScan({"com.davidango.moviestheaters","com.davidango.error"})
@EnableEurekaClient
public class MoviesTheatersServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MoviesTheatersServiceApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainerFactory(){
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		LogbackValve logbackValve = new LogbackValve();
		logbackValve.setFilename("logback-access.xml");
		tomcat.addContextValves(logbackValve);
		return tomcat;
	}
}
