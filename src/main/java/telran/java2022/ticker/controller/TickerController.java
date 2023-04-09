package telran.java2022.ticker.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java2022.ticker.dto.DateBetweenDto;
import telran.java2022.ticker.dto.FullStatDto;
import telran.java2022.ticker.dto.NamesAndDatesDto;
import telran.java2022.ticker.dto.NamesAndDatesStatDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.model.TickerId;
import telran.java2022.ticker.service.TickerService;

@RestController
@RequiredArgsConstructor
public class TickerController {

	final TickerService service;

	@GetMapping("/financials/ticker/{name}/{date}")
	public TickerDto findSandpByDate(@PathVariable String name, @PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.findTickerByDate(new TickerId(name, localDate));
	}

	@PostMapping("/financials/ticker")
	public void addSandp(@RequestBody TickerDto sandpDto) {
		service.addTicker(sandpDto);
	}

	@DeleteMapping("/financials/{name}/{date}")
	public TickerDto deleteSandpByDate(@PathVariable String name, @PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.deleteTickerByDate(new TickerId(name, localDate));
	}
	
	@DeleteMapping("/financials/{name}")
	public int deleteAllTickersByName(@PathVariable String name) {
		return service.deleteAllTickersByName(name);
	}

	@PutMapping("/financials/{name}/{date}")
	public TickerDto updateSandp(@PathVariable String name, @PathVariable String date, @RequestBody double priceClose) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.updateTicker(new TickerId(name, localDate), priceClose);
	}

	@PostMapping("/financials/min/{name}")
	public TickerDto findMinSandpByPeriod(@RequestBody DateBetweenDto dateBetweenDto, @PathVariable String name) {
		return service.findMinTickerByPeriod(dateBetweenDto, name);
	}

	@PostMapping("/financials/max/{name}")
	public TickerDto findMaxSandpByPeriod(@RequestBody DateBetweenDto dateBetweenDto, @PathVariable String name) {
		return service.findMaxTickerByPeriod(dateBetweenDto, name);
	}

	@PostMapping("/financials/statistic")
	public FullStatDto statistic(@RequestBody NamesAndDatesStatDto namesAndDatesStatDto) {
		return service.statistic(namesAndDatesStatDto.getNames()[0],
				namesAndDatesStatDto.getDateBetweenDto(),
				namesAndDatesStatDto.getSum(),
				namesAndDatesStatDto.getDepositPeriodDays());
	}

	@PostMapping("/financials/statistic/{name}/{periodDays}/{sum}/{termDays}")
	public FullStatDto statistic(@PathVariable String name, @PathVariable long periodDays, @PathVariable double sum,
			@PathVariable long termDays) {
		return service.statistic(name, periodDays, sum, termDays);
	}

	@GetMapping("/financials/correlation/{name1}/{name2}/{termDays}")
	public String correlation(@PathVariable String name1, @PathVariable String name2, @PathVariable int termDays) {
		return service.correlation(name1, name2, termDays);
	}

	@PostMapping("/financials/correlation")
	public String correlation(@RequestBody NamesAndDatesDto namesAndDatesDto) {
		return service.correlation(namesAndDatesDto.getNames()[0], namesAndDatesDto.getNames()[1], namesAndDatesDto.getDateBetweenDto());
	}
	
	@PostMapping("/financials/download")
	public int downloadDataByTickerName(@RequestBody NamesAndDatesDto namesAndDatesDto) {
		return service.downloadDataByTickerName(namesAndDatesDto.getNames(), namesAndDatesDto.getDateBetweenDto());
	}
	
	@PostMapping("/financials/statistic/investmentPortfolio")
	public FullStatDto investmentPortfolio(@RequestBody NamesAndDatesStatDto namesAndDatesStatDto) {
		return service.investmentPortfolio(namesAndDatesStatDto.getNames(), 
				namesAndDatesStatDto.getDateBetweenDto(), 
				namesAndDatesStatDto.getSum(), 
				namesAndDatesStatDto.getDepositPeriodDays());
	}
	
	@GetMapping("/financials/tickers")
	public List<String> findAllTickerNames(){
		return service.findAllTickerNames();
	}
}
