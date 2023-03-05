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
		if (repository.existsById(modelMapper.map(sandpDto.getDate(), TickerId.class))) {
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
				.min((s1, s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose())).orElse(null);
		return modelMapper.map(s, TickerDto.class);
	}

	@Override
	public TickerDto findMaxTickerByPeriod(DateBetweenDto dateBetweenDto, String name) {
		Ticker s = repository.findTickerByDateDateBetween(dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.filter(t -> t.getDate().getName().equals(name))
				.max((s1, s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose())).orElse(null);
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
		while (end < 0) {
			end = allPeriod.indexOf(new Ticker(new TickerId(name, lastStart.minusDays(1)), 0.0));
		}
		LocalDate dateEndOfPeriod = null;
		TickerId tickerIdEnd = new TickerId(name, LocalDate.now());
		Ticker tickerEnd = new Ticker(tickerIdEnd, 0.0);
		for (int start = 0; start < end; start++) {
			dateEndOfPeriod = allPeriod.get(start).getDate().getDate().plusDays(termDays);
			tickerIdEnd.setDate(dateEndOfPeriod);
			tickerEnd.setDate(tickerIdEnd);
			int indexEnd = allPeriod.indexOf(tickerEnd);
			while (indexEnd < 0) {
				dateEndOfPeriod = dateEndOfPeriod.minusDays(1);
				tickerIdEnd.setDate(dateEndOfPeriod);
				tickerEnd.setDate(tickerIdEnd);
				indexEnd = allPeriod.indexOf(tickerEnd);
			}
			Double apy = (allPeriod.get(indexEnd).getPriceClose() - allPeriod.get(start).getPriceClose())
					/ allPeriod.get(start).getPriceClose() * (365.0 / termDays) * 100;
			allStats.add(apy);
		}
		double minPercent = allStats.stream().min((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double maxPercent = allStats.stream().max((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double minRevenue = sum * (minPercent / 100) + sum;
		double maxRevenue = sum * (maxPercent / 100) + sum;
		return new StatDto(minPercent, maxPercent, minRevenue, maxRevenue);
	}

	@Override
	public double correlation(String name1, String name2, int termDays) {
		LocalDate dateStart = LocalDate.now().minusDays(termDays);
		List<Double> tickersFirst = repository.findTickerByDateDateBetweenOrderByDateDate(dateStart, LocalDate.now())
				.filter(t -> t.getDate().getName().equals(name1))
				.map(t->t.getPriceClose())
				.collect(Collectors.toList());
		List<Double> tickersSecond = repository.findTickerByDateDateBetweenOrderByDateDate(dateStart, LocalDate.now())
				.filter(t -> t.getDate().getName().equals(name2))
				.map(t->t.getPriceClose())
				.collect(Collectors.toList());
		double avrX = tickersFirst.stream()
				.reduce(0.0, (x,y) -> x + y);
		avrX = avrX / tickersFirst.size();
		double avrY = tickersSecond.stream()
				.reduce(0.0, (x,y) -> x + y);
		avrY = avrY / tickersSecond.size();
		double avrXY = tickersFirst.stream()
				.map(t-> t * tickersSecond.get(tickersFirst.indexOf(t)))
				.reduce(0.0, (x, y) -> x + y);
		avrXY = avrXY / tickersFirst.size();
		double tX = tickersFirst.stream()
				.map(t->t*t)
				.reduce(0.0, (x,y)->x+y);
		tX = tX / tickersFirst.size();
		tX = tX - avrX * avrX;
		tX = Math.sqrt(tX);
		double tY = tickersSecond.stream()
				.map(t->t*t)
				.reduce(0.0, (x,y)->x+y);
		tY = tY / tickersSecond.size();
		tY = tY - avrY * avrY;
		tY = Math.sqrt(tY);
		double res = (avrXY - (avrX * avrY)) / (tX * tY);
		return res;
	}

}
