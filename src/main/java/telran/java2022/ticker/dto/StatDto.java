package telran.java2022.ticker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {

	double minPercent;
	double maxPercent;
	double minRevenue;
	double maxRevenue;
}
