package telran.java2022.ticker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.ticker.dao.TickerRepository;
import telran.java2022.ticker.dto.DateBetweenDto;
import telran.java2022.ticker.dto.StatDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.dto.exceptions.AlreadyExistException;
import telran.java2022.ticker.dto.exceptions.NotFoundExeption;
import telran.java2022.ticker.model.Ticker;
import telran.java2022.ticker.model.TickerId;

@Service
@RequiredArgsConstructor
public class TickerServiceImpl implements TickerService {
	
	final TickerRepository repository;
	final ModelMapper modelMapper;

	@Override
	public TickerDto findTickerByDate(TickerId date) {
		Ticker ticker = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		return modelMapper.map(ticker, TickerDto.class);
	}

	@Override
	public void addTicker(TickerDto sandpDto) {
		if(repository.existsById(modelMapper.map(sandpDto.getDate(), TickerId.class))) {
			throw new AlreadyExistException();
		}
		repository.save(modelMapper.map(sandpDto, Ticker.class));
	}

	@Override
	public TickerDto deleteTickerByDate(TickerId date) {
		Ticker ticker = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		repository.deleteById(date);
		return modelMapper.map(ticker, TickerDto.class);
	}

	@Override
	public TickerDto updateTicker(TickerId date, double priceClose) {
		Ticker ticker = repository.findById(date).orElseThrow(() -> new NotFoundExeption());
		ticker.setPriceClose(priceClose);
		repository.save(ticker);
		return modelMapper.map(ticker, TickerDto.class);
	}

	@Override
	public TickerDto findMinTickerByPeriod(DateBetweenDto dateBetweenDto, String name) {
		Ticker s = repository.findTickerByDateDateBetween(dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.filter(t -> t.getDate().getName().equals(name))
				.min((s1,s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose()))
				.orElse(null);
		return modelMapper.map(s, TickerDto.class);
	}

	@Override
	public TickerDto findMaxTickerByPeriod(DateBetweenDto dateBetweenDto, String name) {
		Ticker s = repository.findTickerByDateDateBetween(dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.filter(t -> t.getDate().getName().equals(name))
				.max((s1,s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose()))
				.orElse(null);
		return modelMapper.map(s, TickerDto.class);
	}
	
	@Override
	public StatDto findStatistic(String name, long periodDays, double sum, long termDays) {
		LocalDate dateStart = LocalDate.now().minusDays(periodDays + termDays);
		List<Double> allStats = new ArrayList<>();
		List<Ticker> allPeriod = repository.findTickerByDateDateBetweenOrderByDateDate(dateStart, LocalDate.now())
				.filter(t -> t.getDate().getName().equals(name))
				.collect(Collectors.toList());
		LocalDate lastStart = LocalDate.now().minusDays(termDays);
		int end = allPeriod.indexOf(new Ticker(new TickerId(name, lastStart), 0.0));
		while(end < 0) {
			end = allPeriod.indexOf(new Ticker(new TickerId(name, lastStart.minusDays(1)), 0.0));
		}
		LocalDate dateEndOfPeriod = null;
		for (int start = 0; start < end; start++) {
			dateEndOfPeriod = allPeriod.get(start).getDate().getDate().plusDays(termDays);
			while (!repository.existsById(new TickerId(name, dateEndOfPeriod))) {
				dateEndOfPeriod = dateEndOfPeriod.minusDays(1);
			}
			Ticker tickerEnd = repository.findById(new TickerId(name, dateEndOfPeriod)).get();
			Double apy = (tickerEnd.getPriceClose() / allPeriod.get(start).getPriceClose() - 1) / (termDays / 365.0); 
			allStats.add(apy);
		}
		double minPercent = allStats.stream().min((p1,p2) -> Double.compare(p1, p2)).orElse(null);
		double maxPercent = allStats.stream().max((p1,p2) -> Double.compare(p1, p2)).orElse(null);
		double minRevenue = sum * (minPercent * ((termDays * 1.0 / 365) / 100.0) + 1);
		double maxRevenue = sum * (maxPercent * ((termDays * 1.0 / 365) / 100.0) + 1);
		return new StatDto(minPercent, maxPercent, minRevenue, maxRevenue);
	}

}
