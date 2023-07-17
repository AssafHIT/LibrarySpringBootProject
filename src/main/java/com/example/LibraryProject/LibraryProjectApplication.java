package com.example.LibraryProject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
@EnableAsync
@SpringBootApplication
public class LibraryProjectApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(LibraryProjectApplication.class, args);
	}


	@Bean
	public Executor taskExecutor(){
//		ThreadPoolExecutor javaExecutor =
//		new ThreadPoolExecutor(3,5,3000,
//				TimeUnit.MILLISECONDS,
//				new LinkedBlockingQueue<>(100));
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setKeepAliveSeconds(3);
		taskExecutor.setQueueCapacity(100);
		taskExecutor.initialize();
		// define a Prefix for ThreadPoolTaskExecutor threads
		taskExecutor.setThreadNamePrefix("Spring ThreadPoolTaskExecutor");
		return taskExecutor;
	}
}
