package com.project.vaccinemanagement.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.vaccinemanagement.entities.Vaccine;

public interface VaccineRepository extends JpaRepository<Vaccine, LocalDate> {

	@Query("select v from Vaccine v")
	List<Vaccine> getLastInsertedVaccine(Pageable pageable);

}
