package org.example.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateEntity2Repo extends JpaRepository<StateEntity2,Long> {
    @Query(
            nativeQuery = true,
            value = "select distinct story,season,episode,count(*) over(partition by story,season,episode) from st"
    )
    List<Object> getList();
}
