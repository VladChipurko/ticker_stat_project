package telran.java2022.ticker.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaxStatDto {
	
	LocalDate maxDateStart;
	LocalDate maxDateEnd;
	double maxPriceStart;
	double maxPriceEnd;
	double maxPercentApy;
	double maxRevenue;
	
}
