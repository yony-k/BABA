package org.example.baba.repository;

import org.example.baba.domain.Register;
import org.springframework.data.repository.CrudRepository;

public interface RegisterRepository extends CrudRepository<Register, String> {

  boolean existsByMemberName(String memberName);
}
