package ru.aminovniaz.testtask.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aminovniaz.testtask.model.Human;

import java.util.List;
import java.util.Optional;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {
    Optional<Human> findHumanByName(String name);

    Optional<Human> findHumanById(Long id);

    @Query(value = "SELECT * FROM human h \n" +
            "    INNER JOIN timetable t on h.id = t.human_id \n" +
            "    INNER JOIN event e on t.event_id = e.id \n" +
            "    WHERE h.id = ?1 \n" +
            "    ORDER BY e.start",
            nativeQuery = true)
    Human findHumanByIdSorted(Long id);

}
