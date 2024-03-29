package telran.java2022.ticker.service;

import java.util.List;

import telran.java2022.ticker.dto.DateBetweenDto;
import telran.java2022.ticker.dto.FullStatDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.model.TickerId;

public interface TickerService {
	
	void addTicker(TickerDto sandpDto);
	
	TickerDto findTickerByDate(TickerId date);
	
	TickerDto deleteTickerByDate(TickerId date);
	
	int deleteAllTickersByName(String name);
	
	TickerDto updateTicker (TickerId date, double priceClose);
	
	TickerDto findMinTickerByPeriod (DateBetweenDto dateBetweenDto, String name);
	
	TickerDto findMaxTickerByPeriod (DateBetweenDto dateBetweenDto, String name);
	
	FullStatDto statistic(String name, long periodDays, double sum, long termDays);
	
	FullStatDto statistic(String name, DateBetweenDto dateBetweenDto, double sum, long termDays);
	
	String correlation(String name1, String name2, int termDays);
	
	String correlation(String name1, String name2, DateBetweenDto dateBetweenDto);
	
	int downloadDataByTickerName (String[] name, DateBetweenDto dateBetweenDto);
	
	FullStatDto investmentPortfolio(String[] names, DateBetweenDto dateBetweenDto, double sum, long termDays);
	
	List<String> findAllTickerNames();
	
	int updateAllTickers();
}
