package com.project.vaccinemanagement.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Entity
@Setter
@Getter
public class Vaccine {
	String name;
	@Id
	LocalDate date;
	long initial;
	long balance;

	


}
