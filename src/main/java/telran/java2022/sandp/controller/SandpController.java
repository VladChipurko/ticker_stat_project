package telran.java2022.sandp.controller;

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
import telran.java2022.sandp.dto.DateBetweenDto;
import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.dto.StatDto;
import telran.java2022.sandp.model.SandPDate;
import telran.java2022.sandp.service.SandpService;

@RestController
@RequiredArgsConstructor
public class SandpController {

	final SandpService service;
	
	@GetMapping("/{name}/{date}")
	public SandpDto findSandpByDate(@PathVariable String name,@PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.findSandpByDate(new SandPDate(name.toLowerCase(), localDate));
	}
	
	@PostMapping("/ticker")
	public void addSandp(@RequestBody SandpDto sandpDto) {
		service.addSandp(sandpDto);
	}
	
	@DeleteMapping("/{name}/{date}")
	public SandpDto deleteSandpByDate(@PathVariable String name,@PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.deleteSandpByDate(new SandPDate(name.toLowerCase(), localDate));
	}
	
	@PutMapping("/{name}/{date}")
	public SandpDto updateSandp (@PathVariable String name,@PathVariable String date, @RequestBody double priceClose) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.updateSandp(new SandPDate(name.toLowerCase(), localDate), priceClose);
	}
	
	@PostMapping("/sandp/min/period")
	public SandpDto findMinSandpByPeriod (@RequestBody DateBetweenDto dateBetweenDto) {
		return service.findMinSandpByPeriod(dateBetweenDto);
	}
	
	@PostMapping("/sandp/max/period")
	public SandpDto findMaxSandpByPeriod (@RequestBody DateBetweenDto dateBetweenDto) {
		return service.findMaxSandpByPeriod(dateBetweenDto);
	}
	
	@PostMapping("/sandp/{periodDays}/{sum}/{termDays}")
	public StatDto findStatistic(@PathVariable long periodDays, @PathVariable double sum, @PathVariable long termDays) {
		return service.findStatistic(periodDays, sum, termDays);
	}
}
