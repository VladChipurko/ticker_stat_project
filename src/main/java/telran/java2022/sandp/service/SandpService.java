package telran.java2022.sandp.service;

import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.model.SandPDate;

public interface SandpService {
	
	void addSandp(SandpDto sandpDto);
	
	SandpDto findSandpByDate(SandPDate date);
	
	SandpDto deleteSandpByDate(SandPDate date);
	
	SandpDto updateSandp (SandPDate date, double priceClose);
}
