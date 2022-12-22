package telran.java2022.sandp.dao;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.sandp.model.Sandp;

public interface SandpRepository extends CrudRepository<Sandp, LocalDateTime>{

}
