package org.example.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatEntityRespository extends JpaRepository<StatEntity, Long> {
  @Query(
      nativeQuery = true,
      value = "select count(*) from stats where cast(:stateAutomate as state) = s_state;")
  int count(String stateAutomate);
}
