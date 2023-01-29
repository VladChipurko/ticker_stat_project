package telran.java2022.sandp.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.sandp.model.SandPDate;
import telran.java2022.sandp.model.Sandp;

public interface SandpRepository extends CrudRepository<Sandp, SandPDate>{

	Stream<Sandp> findSandpByDateDateBetween(LocalDate dateFrom, LocalDate dateTo);
}
