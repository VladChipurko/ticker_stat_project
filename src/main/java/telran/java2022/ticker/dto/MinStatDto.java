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
public class MinStatDto {
	
	LocalDate minDateStart;
	LocalDate minDateEnd;
	double minPriceStart;
	double minPriceEnd;
	double minPercentApy;
	double minRevenue;
	
}
