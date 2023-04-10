package telran.java2022.ticker.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.ticker.dao.TickerRepository;
import telran.java2022.ticker.dto.DateBetweenDto;
import telran.java2022.ticker.dto.FullStatDto;
import telran.java2022.ticker.dto.MaxStatDto;
import telran.java2022.ticker.dto.MinStatDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.dto.exceptions.AlreadyExistException;
import telran.java2022.ticker.dto.exceptions.NotFoundExeption;
import telran.java2022.ticker.model.Ticker;
import telran.java2022.ticker.model.TickerId;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

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
		Ticker s = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(name, dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.min((s1, s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose())).orElse(null);
		return modelMapper.map(s, TickerDto.class);
	}

	@Override
	public TickerDto findMaxTickerByPeriod(DateBetweenDto dateBetweenDto, String name) {
		Ticker s = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(name, dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.max((s1, s2) -> Double.compare(s1.getPriceClose(), s2.getPriceClose())).orElse(null);
		return modelMapper.map(s, TickerDto.class);
	}
	
	/**
	 * Statistic with LocalDate
	 */
	@Override
	public FullStatDto statistic(String name, DateBetweenDto dateBetweenDto, double sum, long depositPeriodDays) {
		List<Double> allStats = new ArrayList<>();
		List<Ticker> datesOfEnds = new ArrayList<>();
		List<Ticker> allPeriod = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(name, dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.collect(Collectors.toList());
		LocalDate lastStart = dateBetweenDto.getDateTo().minusDays(depositPeriodDays);
		int end = allPeriod.indexOf(new Ticker(new TickerId(name, lastStart), 0.0));
		while (end < 0) {
			end = allPeriod.indexOf(new Ticker(new TickerId(name, lastStart.minusDays(1)), 0.0));
		}
		LocalDate dateEndOfPeriod = null;
		TickerId tickerIdEnd = new TickerId(name, LocalDate.now());
		Ticker tickerEnd = new Ticker(tickerIdEnd, 0.0);
		for (int start = 0; start < end; start++) {
			dateEndOfPeriod = allPeriod.get(start).getDate().getDate().plusDays(depositPeriodDays);
			tickerIdEnd.setDate(dateEndOfPeriod);
			tickerEnd.setDate(tickerIdEnd);
			int indexEnd = allPeriod.indexOf(tickerEnd);
			while (indexEnd < 0) {
				dateEndOfPeriod = dateEndOfPeriod.minusDays(1);
				tickerIdEnd.setDate(dateEndOfPeriod);
				tickerEnd.setDate(tickerIdEnd);
				indexEnd = allPeriod.indexOf(tickerEnd);
			}
			Double apr = (allPeriod.get(indexEnd).getPriceClose() - allPeriod.get(start).getPriceClose())
					/ allPeriod.get(start).getPriceClose(); 
			Double apy  = 100 * (Math.pow(1+ apr, 365.0/depositPeriodDays) - 1); 
			allStats.add(apy);
			datesOfEnds.add(allPeriod.get(indexEnd));
		}
		double minPercent = allStats.stream().min((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double maxPercent = allStats.stream().max((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double minRevenue = sum * (minPercent / 100) + sum;
		double maxRevenue = sum * (maxPercent / 100) + sum;
		double avgPercent = allStats.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
		double avgRevenue = sum * (avgPercent / 100) + sum;
		int indexMin = allStats.indexOf(minPercent);
		int indexMax = allStats.indexOf(maxPercent);
		String[] names = {name};
		return new FullStatDto(names, depositPeriodDays, 
				new MinStatDto(
						allPeriod.get(indexMin).getDate().getDate(),
						datesOfEnds.get(indexMin).getDate().getDate(),
						allPeriod.get(indexMin).getPriceClose(),
						datesOfEnds.get(indexMin).getPriceClose(),
						minPercent, minRevenue),
				new MaxStatDto(
						allPeriod.get(indexMax).getDate().getDate(),
						datesOfEnds.get(indexMax).getDate().getDate(),
						allPeriod.get(indexMax).getPriceClose(),
						datesOfEnds.get(indexMax).getPriceClose(),
						maxPercent, maxRevenue),
				avgPercent, avgRevenue);
	}

	/**
	 * Statistic with days
	 */
	@Override
	public FullStatDto statistic(String name, long periodDays, double sum, long termDays) {
		DateBetweenDto dateBetweenDto = new DateBetweenDto(LocalDate.now().minusDays(periodDays), LocalDate.now());
		return statistic(name, dateBetweenDto, sum, termDays);
	}
	
	/**
	 * Correlation with LocalDate
	 */
	@Override
	public String correlation(String name1, String name2, DateBetweenDto dateBetweenDto) {
		double[] tickersFirst = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(name1, dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.map(t->t.getPriceClose())
				.mapToDouble(Double::doubleValue)
				.toArray();
		double[] tickersSecond = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(name2, dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.map(t->t.getPriceClose())
				.mapToDouble(Double::doubleValue)
				.toArray();
		double correlation = new PearsonsCorrelation().correlation(tickersFirst, tickersSecond);
		String result = resultCorrelation(correlation);
		return correlation + ": " + result;
	}
	
	/**
	 * Correlation with days
	 */
	@Override
	public String correlation(String name1, String name2, int termDays) {
		DateBetweenDto dateBetweenDto = new DateBetweenDto(LocalDate.now().minusDays(termDays), LocalDate.now());
		return correlation(name1, name2, dateBetweenDto);
	}
	
	private String resultCorrelation(double correlation) {		
		String res = "";
		if (correlation <= 1.00 && correlation > 0.90) {
			res = "very strong correlation";
		} else if (correlation <= 0.90 && correlation > 0.70) {
			res = "strong correlation";
		} else if (correlation <= 0.70 && correlation > 0.50) {
			res = "moderate correlation";
		} else if (correlation <= 0.50 && correlation > 0.30) {
			res = "weak correlation";
		} else if (correlation <= 0.30 && correlation > 0.00) {
			res = "negligible correlation";
		} else if (correlation >= -1.00 && correlation < -0.90) {
			res = "inverse very strong correlation";
		} else if (correlation >= -0.90 && correlation < -0.70) {
			res = "inverse strong correlation";
		} else if (correlation >= -0.70 && correlation < -0.50) {
			res = "inverse moderate correlation";
		} else if (correlation >= -0.50 && correlation < -0.30) {
			res = "inverse weak correlation";
		} else if (correlation >= -0.30 && correlation < 0.00) {
			res = "inverse negligible correlation";
		}
		return res; 
	}

	/**
	 * Downloading new data by name and date between
	 */
	@Override
	public int downloadDataByTickerName(String[] tickerName, DateBetweenDto dateBetweenDto) {
		List<HistoricalQuote> googleHistQuotes = requestData(tickerName[0], dateBetweenDto);
		List<Ticker> requesTickers = new ArrayList<>();
		googleHistQuotes.stream()
			.forEach(e -> {
				LocalDate date = e.getDate().toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDate();
				if(e.getClose() != null) {
					double price = e.getClose().doubleValue();
					Ticker ticker = new Ticker(new TickerId(tickerName[0], date), price);
					requesTickers.add(ticker);
				}
			});
		List<Ticker> baseTickers = repository
				.findQueryByDateNameAndDateDateBetweenOrderByDateDate(tickerName[0], dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.collect(Collectors.toList());
		List<Ticker> newData = requesTickers.stream()
				.filter(ticker -> !baseTickers.stream().anyMatch(ticker::equals))
				.collect(Collectors.toList());
		repository.saveAll(newData);
		return newData.size();
	}

	private List<HistoricalQuote> requestData(String tickerName, DateBetweenDto dateBetweenDto) {
		Calendar from = GregorianCalendar.from(ZonedDateTime.of(dateBetweenDto.getDateFrom().atTime(0, 0), ZoneId.systemDefault()));
		Calendar to = GregorianCalendar.from(ZonedDateTime.of(dateBetweenDto.getDateTo().atTime(0,0), ZoneId.systemDefault()));
		Stock tickerRequest = null;
		try {
			tickerRequest = YahooFinance.get(tickerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<HistoricalQuote> googleHistQuotes = new ArrayList<>();
		try {
			googleHistQuotes = tickerRequest.getHistory(from, to, Interval.DAILY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return googleHistQuotes;
	}

	/**
	 * Statistic of investment bag, we can add more tickers to analyze 
	 */
	@Override
	public FullStatDto investmentPortfolio(String[] names, DateBetweenDto dateBetweenDto, double sum, long depositPeriodDays) {
		List<Double> allStats = new ArrayList<>();
		List<Ticker> datesOfEnds = new ArrayList<>();
		List<Ticker> allPeriod = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(names[0], dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
				.collect(Collectors.toList());
		LocalDate lastStart = dateBetweenDto.getDateTo().minusDays(depositPeriodDays);
		int end = allPeriod.indexOf(new Ticker(new TickerId(names[0], lastStart), 0.0));
		while (end < 0) {
			end = allPeriod.indexOf(new Ticker(new TickerId(names[0], lastStart.minusDays(1)), 0.0));
		}
		LocalDate dateEndOfPeriod = null;
		TickerId tickerIdEnd = new TickerId(names[0], LocalDate.now());
		Ticker tickerEnd = new Ticker(tickerIdEnd, 0.0);
		allPeriod = makePortfolioPriceClose(allPeriod, names, dateBetweenDto);
		for (int start = 0; start < end; start++) {
			dateEndOfPeriod = allPeriod.get(start).getDate().getDate().plusDays(depositPeriodDays);
			tickerIdEnd.setDate(dateEndOfPeriod);
			tickerEnd.setDate(tickerIdEnd);
			int indexEnd = allPeriod.indexOf(tickerEnd);
			while (indexEnd < 0) {
				dateEndOfPeriod = dateEndOfPeriod.minusDays(1);
				tickerIdEnd.setDate(dateEndOfPeriod);
				tickerEnd.setDate(tickerIdEnd);
				indexEnd = allPeriod.indexOf(tickerEnd);
			}
			Double apr = (allPeriod.get(indexEnd).getPriceClose() - allPeriod.get(start).getPriceClose())
					/ allPeriod.get(start).getPriceClose(); 
			Double apy  = 100 * (Math.pow(1+ apr, 365.0/depositPeriodDays) - 1); 
			allStats.add(apy);
			datesOfEnds.add(allPeriod.get(indexEnd));
		}
		double minPercent = allStats.stream().min((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double maxPercent = allStats.stream().max((p1, p2) -> Double.compare(p1, p2)).orElse(null);
		double minRevenue = sum * (minPercent / 100) + sum;
		double maxRevenue = sum * (maxPercent / 100) + sum;
		double avgPercent = allStats.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
		double avgRevenue = sum * (avgPercent / 100) + sum;
		int indexMin = allStats.indexOf(minPercent);
		int indexMax = allStats.indexOf(maxPercent);
		return new FullStatDto(names, depositPeriodDays, 
				new MinStatDto(
						allPeriod.get(indexMin).getDate().getDate(),
						datesOfEnds.get(indexMin).getDate().getDate(),
						allPeriod.get(indexMin).getPriceClose(),
						datesOfEnds.get(indexMin).getPriceClose(),
						minPercent, minRevenue),
				new MaxStatDto(
						allPeriod.get(indexMax).getDate().getDate(),
						datesOfEnds.get(indexMax).getDate().getDate(),
						allPeriod.get(indexMax).getPriceClose(),
						datesOfEnds.get(indexMax).getPriceClose(),
						maxPercent, maxRevenue),
				avgPercent, avgRevenue);
	}

	private List<Ticker> makePortfolioPriceClose(List<Ticker> allPeriod, String[] names, DateBetweenDto dateBetweenDto) {
		List<Ticker> res = allPeriod;
		for (int i = 1; i < names.length; i++) {
			List<Ticker> query = repository.findQueryByDateNameAndDateDateBetweenOrderByDateDate(names[i], dateBetweenDto.getDateFrom(), dateBetweenDto.getDateTo())
					.collect(Collectors.toList());
			for (int j = 0; j < res.size(); j++) {
				double price = res.get(j).getPriceClose() + query.get(j).getPriceClose();
				res.get(j).setPriceClose(price);
			}
		}
		return res;
	}

	@Override
	public int deleteAllTickersByName(String name) {
		return repository.deleteAllTickersByDateName(name);
	}

	@Override
	public List<String> findAllTickerNames() {
		LocalDate date = LocalDate.of(2020, 1, 7);
		return repository.findByDateDate(date)
				.map(t->t.getDate().getName())
				.collect(Collectors.toList());
	}

}
