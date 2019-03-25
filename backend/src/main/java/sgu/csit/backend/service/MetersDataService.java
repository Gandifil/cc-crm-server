package sgu.csit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.repository.MetersDataRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MetersDataService {

    private final MetersDataRepository metersDataRepository;

    @Autowired
    public MetersDataService(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
    }

    public void addMeterdsData(MetersData metersData) {
        metersDataRepository.save(metersData);
    }

    public Iterable<MetersData> getAll(PeriodType periodType) {
        Iterable<MetersData> metersData = metersDataRepository.findAll();
//        switch (periodType) {
//            case CURRENT_MONTH:
//                metersData = StreamSupport
//                        .stream(metersData.spliterator(), false)
//                        .filter(x -> x.getDate().compareTo(LocalDateTime.now().minusMonths(1)) > 0)
//                        .collect(Collectors.toList());
//                break;
//            case CURRENT_YEAR:
//                metersData = StreamSupport
//                        .stream(metersData.spliterator(), false)
//                        .filter(x -> x.getDate().compareTo(LocalDateTime.now().minusYears(1)) > 0)
//                        .collect(Collectors.toList());
//                break;
//            case ALL:
//                break;
//        }
        return metersData;
    }
}
