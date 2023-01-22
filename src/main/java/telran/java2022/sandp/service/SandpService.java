package telran.java2022.sandp.service;

import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.model.SandPDate;

public interface SandpService {
	
	SandpDto findSandpByDate(SandPDate date);

	void addSandp(SandpDto sandpDto);
	
	SandpDto deleteSandpByDate(SandPDate date);
}
