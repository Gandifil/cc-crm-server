package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.MetersData;

import java.util.Date;
import java.util.List;

@Repository
public interface MetersDataRepository extends JpaRepository<MetersData, Long> {
    Iterable<MetersData> findByUser_Apartment(Integer userApart);
    Iterable<MetersData> findByDateAfter(Date date);
    Iterable<MetersData> findByDateAfterAndUser_Apartment(Date date, Integer userApart);
    Iterable<MetersData> findByDateBefore(Date date);
}
