package com.programmer.gate.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.programmer.gate.model.US;
@Repository
public interface USRepository extends CrudRepository<US, Long>{

}
