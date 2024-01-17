package com.project.vaccinemanagement.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Person {


	@Id
	private String aadharNumber;

	private String name;
	private int age;
	private LocalDate shotOneDate;
	private LocalDate shotTwoDate;

	@OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Address> addresses;

	public void setAddresses(List<Address> addresses) {
		addresses.forEach(address -> address.setPerson(this));
		this.addresses = addresses;
	}

}
