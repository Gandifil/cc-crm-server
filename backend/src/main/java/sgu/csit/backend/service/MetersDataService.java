package sgu.csit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.repository.MetersDataRepository;

import java.util.Calendar;

@Service
public class MetersDataService {

    private final MetersDataRepository metersDataRepository;

    @Autowired
    public MetersDataService(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
    }

    public void addMetersData(MetersData metersData) {
        metersDataRepository.save(metersData);
    }

    public Iterable<MetersData> getMetersData(PeriodType periodType) {
        Iterable<MetersData> metersData;
        switch (periodType) {
            case CURRENT_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = metersDataRepository.findByStartDateAfter(calendar.getTime());
                break;
            case CURRENT_YEAR:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = metersDataRepository.findByStartDateAfter(calendar.getTime());
                break;
            case ALL:
                metersData = metersDataRepository.findAll();
                break;
            default:
                return null;
        }
        return metersData;
    }
}
