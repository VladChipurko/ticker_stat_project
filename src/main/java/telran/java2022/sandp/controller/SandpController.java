package telran.java2022.sandp.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java2022.sandp.dto.SandpDto;
import telran.java2022.sandp.model.SandPDate;
import telran.java2022.sandp.service.SandpService;

@RestController
@RequiredArgsConstructor
public class SandpController {

	final SandpService service;
	
	@GetMapping("/sandp/{date}")
	public SandpDto findSandpByDate(@PathVariable String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return service.findSandpByDate(new SandPDate("S&P", localDate));
	}
}
