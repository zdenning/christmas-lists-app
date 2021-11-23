package com.denning.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.denning.demo.api.ListApi;
import com.denning.demo.api.ListApiController;
import com.denning.demo.api.UserApi;
import com.denning.demo.api.UserApiController;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Application.class, ListApi.class, ListApiController.class, UserApi.class,
		UserApiController.class})
public class Application {

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

}
