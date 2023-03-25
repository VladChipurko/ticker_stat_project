package telran.java2022.ticker.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import telran.java2022.ticker.dto.FullStatPortfolioDto;
import telran.java2022.ticker.dto.NamesAndDatesDto;
import telran.java2022.ticker.dto.TickerDto;
import telran.java2022.ticker.model.TickerId;
import telran.java2022.ticker.service.TickerService;

@RestController
@RequiredArgsConstructor
public class TickerController {

	final TickerService service;

	@GetMapping("/{name}/{date}")
	public TickerDto findSandpByDate(@PathVariable String name, @PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.findTickerByDate(new TickerId(name.toLowerCase(), localDate));
	}

	@PostMapping("/ticker")
	public void addSandp(@RequestBody TickerDto sandpDto) {
		service.addTicker(sandpDto);
	}

	@DeleteMapping("/{name}/{date}")
	public TickerDto deleteSandpByDate(@PathVariable String name, @PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.deleteTickerByDate(new TickerId(name.toLowerCase(), localDate));
	}

	@PutMapping("/{name}/{date}")
	public TickerDto updateSandp(@PathVariable String name, @PathVariable String date, @RequestBody double priceClose) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.updateTicker(new TickerId(name.toLowerCase(), localDate), priceClose);
	}

	@PostMapping("/{name}/min/period")
	public TickerDto findMinSandpByPeriod(@RequestBody DateBetweenDto dateBetweenDto, @PathVariable String name) {
		return service.findMinTickerByPeriod(dateBetweenDto, name.toLowerCase());
	}

	@PostMapping("/{name}/max/period")
	public TickerDto findMaxSandpByPeriod(@RequestBody DateBetweenDto dateBetweenDto, @PathVariable String name) {
		return service.findMaxTickerByPeriod(dateBetweenDto, name.toLowerCase());
	}

	@PostMapping("/stat/{name}/{sum}/{termDays}")
	public FullStatDto statistic(@PathVariable String name, @RequestBody DateBetweenDto dateBetweenDto,
			@PathVariable double sum, @PathVariable long termDays) {
		return service.statistic(name, dateBetweenDto, sum, termDays);
	}

	@PostMapping("/stat/{name}/{periodDays}/{sum}/{termDays}")
	public FullStatDto statistic(@PathVariable String name, @PathVariable long periodDays, @PathVariable double sum,
			@PathVariable long termDays) {
		return service.statistic(name.toLowerCase(), periodDays, sum, termDays);
	}

	@GetMapping("/correlation/{name1}/{name2}/{termDays}")
	public String correlation(@PathVariable String name1, @PathVariable String name2, @PathVariable int termDays) {
		return service.correlation(name1, name2, termDays);
	}

	@PostMapping("/correlation/{name1}/{name2}")
	public String correlation(@PathVariable String name1, @PathVariable String name2,
			@RequestBody DateBetweenDto dateBetweenDto) {
		return service.correlation(name1, name2, dateBetweenDto);
	}
	
	@GetMapping("/update/{name}")
	public int updateDataByTickerName(@PathVariable String name) {
		return service.updateDataByTickerName(name);
	}
	
	@PostMapping("/investmentPortfolio/{sum}/{termDays}")
	public FullStatPortfolioDto investmentPortfolio(@PathVariable double sum, @PathVariable long termDays, @RequestBody NamesAndDatesDto namesAndDatesDto) {
		return service.investmentPortfolio(namesAndDatesDto.getNames(), namesAndDatesDto.getDateBetweenDto(), sum, termDays);
	}
}
