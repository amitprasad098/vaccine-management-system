package com.project.vaccinemanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.vaccinemanagement.dto.VaccineDto;
import com.project.vaccinemanagement.entities.Vaccine;
import com.project.vaccinemanagement.repositories.VaccineRepository;

@Service
public class VaccineStockManagementServiceImpl implements IVaccineStockManagementService {

	@Autowired
	VaccineRepository vaccineRepository;
	private ModelMapper mapper = new ModelMapper();
	private void setBalance(Vaccine vaccine) {
		if (vaccineRepository.count() > 0)
			vaccine.setBalance(vaccine.getInitial() + availaibleVaccineCount());
		else
			vaccine.setBalance(vaccine.getInitial());
	}

	@Override
	public void storeStock(VaccineDto vaccineDto) {
		LocalDate date = LocalDate.parse(vaccineDto.getDate());
		Optional<Vaccine> vaccineOptional = vaccineRepository.findById(date);
		if (vaccineOptional.isPresent()) {
			reStoreStock(vaccineOptional.get(), vaccineDto.getInitial());
		} else {
			Vaccine vaccine = mapper.map(vaccineDto, Vaccine.class);
			vaccine.setDate(LocalDate.parse(vaccineDto.getDate()));
			setBalance(vaccine);
			vaccineRepository.save(vaccine);
		}
	}

	private void reStoreStock(Vaccine vaccine, long count) {
		vaccine.setBalance(vaccine.getBalance() + count);
		vaccine.setInitial(vaccine.getInitial() + count);
		vaccineRepository.save(vaccine);
	}

	@Override
	public long availaibleVaccineCount() {
		long count = 0;
		List<Vaccine> vaccineList = vaccineRepository
				.getLastInsertedVaccine(PageRequest.of(0, 1, Sort.by("date").descending()));
		if (!vaccineList.isEmpty())
			count = vaccineList.get(0).getBalance();
		return count;
	}
}
