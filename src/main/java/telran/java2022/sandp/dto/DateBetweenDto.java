package telran.java2022.sandp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateBetweenDto {

	private LocalDate dateFrom;
	private LocalDate dateTo;
}
