package com.project.vaccinemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
public class VaccinemanagementsystemApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(VaccinemanagementsystemApplication.class, args);
		
	}
	@Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.epam.vaccinemanagement.restcontroller")).build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfo("Vaccine Management System ", "REST API'S for Vaccine Management System", "version : 1.1.2",
                "Free to use", new Contact("Vaccine Management MVC Application", "", ""), "License of API", "",
                java.util.Collections.emptyList());
    }

}
