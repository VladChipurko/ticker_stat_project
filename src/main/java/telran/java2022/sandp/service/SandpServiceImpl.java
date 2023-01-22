package telran.java2022.sandp.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.sandp.dao.SandpRepository;
import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.dto.exceptions.AlreadyExistException;
import telran.java2022.sandp.dto.exceptions.NotFoundExeption;
import telran.java2022.sandp.model.SandPDate;
import telran.java2022.sandp.model.Sandp;

@Service
@RequiredArgsConstructor
public class SandpServiceImpl implements SandpService {
	
	final SandpRepository repository;
	final ModelMapper modelMapper;

	@Override
	public SandpDto findSandpByDate(SandPDate date) {
		Sandp sandp = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		return modelMapper.map(sandp, SandpDto.class);
	}

	@Override
	public void addSandp(SandpDto sandpDto) {
		if(repository.existsById(modelMapper.map(sandpDto.getDate(), SandPDate.class))) {
			throw new AlreadyExistException();
		}
		repository.save(modelMapper.map(sandpDto, Sandp.class));
	}

	@Override
	public SandpDto deleteSandpByDate(SandPDate date) {
		Sandp sandp = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		repository.deleteById(date);
		return modelMapper.map(sandp, SandpDto.class);
	}

}
