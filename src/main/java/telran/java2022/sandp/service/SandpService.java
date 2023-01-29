package telran.java2022.sandp.service;

import telran.java2022.sandp.dto.DateBetweenDto;
import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.dto.StatDto;
import telran.java2022.sandp.model.SandPDate;

public interface SandpService {
	
	void addSandp(SandpDto sandpDto);
	
	SandpDto findSandpByDate(SandPDate date);
	
	SandpDto deleteSandpByDate(SandPDate date);
	
	SandpDto updateSandp (SandPDate date, double priceClose);
	
	SandpDto findMinSandpByPeriod (DateBetweenDto dateBetweenDto);
	
	SandpDto findMaxSandpByPeriod (DateBetweenDto dateBetweenDto);
	
	StatDto findStatistic(long periodDays, double sum, long termDays);
}
