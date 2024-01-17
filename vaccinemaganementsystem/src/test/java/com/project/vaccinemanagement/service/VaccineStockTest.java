package com.project.vaccinemanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.project.vaccinemanagement.dto.VaccineDto;
import com.project.vaccinemanagement.entities.Vaccine;
import com.project.vaccinemanagement.repositories.VaccineRepository;
import com.project.vaccinemanagement.service.VaccineStockManagementServiceImpl;

@ExtendWith(MockitoExtension.class)
class VaccineStockTest {
	
	
	@InjectMocks
	VaccineStockManagementServiceImpl stockManagement;
	
	@Mock
	VaccineRepository vaccineRepository;
	
	
	@Test
	void storeStockTest() {
		
		LocalDate date = LocalDate.parse("2021-06-07");
		VaccineDto vaccineDto = new VaccineDto();
		vaccineDto.setName("covaxin");
		vaccineDto.setInitial(2);
		vaccineDto.setDate("2021-06-07");
		when(vaccineRepository.findById(date)).thenReturn(Optional.empty());
		stockManagement.storeStock(vaccineDto);
		Vaccine vaccine = new Vaccine();
		vaccine.setBalance(2);
		vaccine.setInitial(2);
		vaccine.setName("covaxin");
		vaccine.setDate(date);
		when(vaccineRepository.count()).thenReturn((long)0);
		stockManagement.storeStock(vaccineDto);
		when(vaccineRepository.count()).thenReturn((long) 1);
		stockManagement.storeStock(vaccineDto);
		when(vaccineRepository.findById(date)).thenReturn(Optional.ofNullable(vaccine));
		stockManagement.storeStock(vaccineDto);
		verify(vaccineRepository,atLeastOnce()).save(vaccine);
	}
	
	@Test
	void avaliableCountTest() {
		LocalDate date = LocalDate.parse("2021-06-07");
		Vaccine vaccine = new Vaccine();
		vaccine.setBalance(12);
		List<Vaccine> vaccineList = new ArrayList<>();
		when(vaccineRepository.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending()))).thenReturn(vaccineList);
		long count = stockManagement.availaibleVaccineCount();
		assertEquals(0,count);
		vaccineList.add(vaccine);
		when(vaccineRepository.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending()))).thenReturn(vaccineList);
		count = stockManagement.availaibleVaccineCount();
		assertEquals(12,count);
	}
}
