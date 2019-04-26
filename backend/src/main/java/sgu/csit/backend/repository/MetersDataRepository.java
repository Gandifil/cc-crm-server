package sgu.csit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sgu.csit.backend.model.MetersData;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface MetersDataRepository extends JpaRepository<MetersData, Long> {
    List<MetersData> findByUser_Apartment(Integer userApart);
    List<MetersData> findByDateAfterAndUser_Apartment(Date date, Integer userApart);
    List<MetersData> findByUser_ApartmentAndDateAfterAndDateBefore(Integer apart, Date fromDate, Date toDate);
}
