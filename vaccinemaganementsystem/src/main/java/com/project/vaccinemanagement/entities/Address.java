package com.project.vaccinemanagement.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int addressId;
	
	String personAddress;

	@ManyToOne(targetEntity = Person.class)
	@JoinColumn(name = "aadharNumber")
	Person person;

	

	
	
}
