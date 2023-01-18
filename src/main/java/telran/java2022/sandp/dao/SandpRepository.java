package telran.java2022.sandp.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.sandp.model.SandPDate;
import telran.java2022.sandp.model.Sandp;

public interface SandpRepository extends CrudRepository<Sandp, SandPDate>{

}
