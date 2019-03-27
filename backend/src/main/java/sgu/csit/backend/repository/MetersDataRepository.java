package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.MetersData;

import java.util.Date;
import java.util.List;

@Repository
public interface MetersDataRepository extends JpaRepository<MetersData, Long> {
    List<MetersData> findByUserId(Long userId);
    List<MetersData> findByStartDateAfter(Date date);
    List<MetersData> findByStartDateAfterAndUserId(Date date, Long userId);
    List<MetersData> findByStartDateBefore(Date date);
}
