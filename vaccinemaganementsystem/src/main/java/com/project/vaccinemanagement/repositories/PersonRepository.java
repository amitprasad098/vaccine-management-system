package com.project.vaccinemanagement.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.vaccinemanagement.entities.Person;

public interface PersonRepository extends JpaRepository<Person, String> {

	List<Person> findByShotOneDateOrShotTwoDate(LocalDate shotOneDate, LocalDate shotTwoDate);

	Person findByAadharNumber(String aadharNumber);

}
