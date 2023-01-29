package telran.java2022.sandp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.sandp.dao.SandpRepository;
import telran.java2022.sandp.dto.DateBetweenDto;
import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.dto.StatDto;
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

	@Override
	public SandpDto updateSandp(SandPDate date, double priceClose) {
		Sandp sandp = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		sandp.setPriceClose(priceClose);
		repository.save(sandp);
		return modelMapper.map(sandp, SandpDto.class);
	}

	@Override
	public SandpDto findMinSandpByPeriod(DateBetweenDto dateBetweenDto) {
		Sandp s = repository.findSandpByDateDateBetween(dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.min((s1,s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose()))
				.orElse(null);
		return modelMapper.map(s, SandpDto.class);
	}

	@Override
	public SandpDto findMaxSandpByPeriod(DateBetweenDto dateBetweenDto) {
		Sandp s = repository.findSandpByDateDateBetween(dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.max((s1,s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose()))
				.orElse(null);
		return modelMapper.map(s, SandpDto.class);
	}
	
	@Override
	public StatDto findStatistic(long periodDays, double sum, long termDays) {
		LocalDate dateStart = LocalDate.now().minusDays(periodDays + termDays);
		List<Double> allStats = new ArrayList<>();
		List<Sandp> allPeriod = repository.findSandpByDateDateBetween(dateStart, LocalDate.now() )
				.collect(Collectors.toList());
		for (int i = allPeriod.size() - 1; i >= termDays; i--) {
			Double apy = ((sum/allPeriod.get(i).getPriceClose() * allPeriod.get(i - (int)termDays).getPriceClose())-sum)/sum*100;
			allStats.add(apy);
		}
		double minPercent = allStats.stream().min((p1,p2) -> Double.compare(p1, p2)).orElse(null);
		double maxPercent = allStats.stream().max((p1,p2) -> Double.compare(p1, p2)).orElse(null);
		double minRevenue = sum + (sum * minPercent / 100);
		double maxRevenue = sum + (sum * maxPercent / 100);
		return new StatDto(minPercent, maxPercent, minRevenue, maxRevenue);
	}

//	@Override
//	public StatDto findStatistic(long periodDays, double sum, long termDays) {
//		LocalDate dateStart = LocalDate.now().minusDays(periodDays + termDays);
//		LocalDate dateFinish = LocalDate.now().minusDays(periodDays);
//		List<Double> allStats = new ArrayList<>();
//		while(dateStart.isBefore(LocalDate.now().minusDays(termDays))) {
//			Sandp sandpStart = repository.findById(new SandPDate("S&P", dateStart)).orElse(null);
//			if(sandpStart != null) {
//				Sandp sandpFinish = repository.findById(new SandPDate("S&P", dateFinish)).orElse(null);
//				while(sandpFinish == null) {
//					dateFinish = dateFinish.plusDays(1);
//					sandpFinish = repository.findById(new SandPDate("S&P", dateFinish)).orElse(null);
//				}
//				//возможно нужно будет еще делить на term/365
//				//пока что общий процент доходности
//				Double apy = ((sum/sandpStart.getPriceClose() * sandpFinish.getPriceClose())-sum)/sum*100;
//				allStats.add(apy);
//			}
//			dateStart.plusDays(1);
//			dateFinish.plusDays(1);
//		}
//		double minPercent = allStats.stream().min((p1,p2) -> Double.compare(p1, p2)).orElse(null);
//		double maxPercent = allStats.stream().max((p1,p2) -> Double.compare(p1, p2)).orElse(null);
//		double minRevenue = sum + (sum * minPercent / 100);
//		double maxRevenue = sum + (sum * maxPercent / 100);
//		return new StatDto(minPercent, maxPercent, minRevenue, maxRevenue);
//	}
}
