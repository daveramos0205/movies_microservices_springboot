package com.davidango.useraccounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.davidango.useraccounts.externalresources.MoviesTheatersServiceResponseErrorHandler;

import ch.qos.logback.access.tomcat.LogbackValve;


@SpringBootApplication
@ComponentScan({"com.davidango.useraccounts","com.davidango.error"})
@EnableEurekaClient
public class UsersAccountsServiceApplication {

	@Value("${moviesTheatersService.connecttimeout}")
	private int connectTimeout;
	
	@Value("${moviesTheatersService.readtimeout}")
	private int readTimeout;
	
	public static void main(String[] args) {
		SpringApplication.run(UsersAccountsServiceApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate moviesTheatersRestTemplate(MoviesTheatersServiceResponseErrorHandler errorHandler){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(readTimeout);
		requestFactory.setConnectTimeout(connectTimeout);
		RestTemplate template = new RestTemplate(requestFactory);
		template.setErrorHandler(errorHandler);
		return template;
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
