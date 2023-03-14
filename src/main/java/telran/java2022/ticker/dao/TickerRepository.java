package telran.java2022.ticker.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java2022.ticker.model.Ticker;
import telran.java2022.ticker.model.TickerId;

public interface TickerRepository extends CrudRepository<Ticker, TickerId> {

	Stream<Ticker> findTickerByDateDateBetween(LocalDate dateFrom, LocalDate dateTo);

	Stream<Ticker> findTickerByDateDateBetweenOrderByDateDate(LocalDate dateFrom, LocalDate dateTo);

	@Query("{'date.name': ?0,  'date.date': { $gt: ?1, $lt: ?2 }}")
	Stream<Ticker> findQueryByNameAndByDateBetweenOrderByDate(String name, LocalDate dateFrom, LocalDate dateTo);
}
