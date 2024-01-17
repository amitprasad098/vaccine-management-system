package com.project.vaccinemanagement.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PersonDto {
	@NotNull(message = "aadharNumber cannot be null")
	@Size(min=12, max = 12 ,message = "invalid aadhar")
	private String aadharNumber;
	@NotNull(message = "name cannot be null")
	@Size(min=3,max=20,message="invalid name")
	private String name;
	@Positive(message="age should be positive")
	private int age;
	@NotNull(message="date cannot be null")
	private String shotOneDate;
	private String shotTwoDate;
	@NotNull(message = "address cannot be null")
	private String addressOne;
	@NotNull(message = "address cannot be null")
	private String addressTwo;
	
	
}
