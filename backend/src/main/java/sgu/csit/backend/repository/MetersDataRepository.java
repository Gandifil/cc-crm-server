package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;

import java.util.List;

@Repository
public interface MetersDataRepository extends JpaRepository<MetersData, Long> {
    //void add(MetersData metersData);
    //Iterable<MetersData> getAll(PeriodType periodType);
}
