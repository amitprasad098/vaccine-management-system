package com.project.vaccinemanagement.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccineDto {
	@NotNull(message = "name cannot be null")
	@Size(min = 3,max=30,message="invalid name")
	String name;
	@NotNull(message = "date connot be null")
	String date;
	@Positive(message="initial must be greater than zero")
	long initial;

}
