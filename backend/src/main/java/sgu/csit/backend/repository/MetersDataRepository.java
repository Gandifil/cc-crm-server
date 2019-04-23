package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.MetersData;

import java.util.Date;
import java.util.Set;

@Repository
public interface MetersDataRepository extends JpaRepository<MetersData, Long> {
    Set<MetersData> findByUser_Apartment(Integer userApart);
    Set<MetersData> findByDateAfterAndUser_Apartment(Date date, Integer userApart);
}
