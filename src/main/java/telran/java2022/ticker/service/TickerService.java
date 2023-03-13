package telran.java2022.ticker.service;

import telran.java2022.ticker.dto.DateBetweenDto;
import telran.java2022.ticker.dto.StatDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.model.TickerId;

public interface TickerService {
	
	void addTicker(TickerDto sandpDto);
	
	TickerDto findTickerByDate(TickerId date);
	
	TickerDto deleteTickerByDate(TickerId date);
	
	TickerDto updateTicker (TickerId date, double priceClose);
	
	TickerDto findMinTickerByPeriod (DateBetweenDto dateBetweenDto, String name);
	
	TickerDto findMaxTickerByPeriod (DateBetweenDto dateBetweenDto, String name);
	
	StatDto findStatistic(String name, long periodDays, double sum, long termDays);
	
	double correlation(String name1, String name2, int termDays);
	
	double correlation(String name1, String name2, DateBetweenDto dateBetweenDto);
}
