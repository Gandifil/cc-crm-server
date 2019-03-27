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

    public Iterable<MetersData> getAllMetersData(PeriodType periodType) {
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

    public Iterable<MetersData> getAllMetersDataByUserId(PeriodType periodType, Long userId) {
        Iterable<MetersData> metersData;
        switch (periodType) {
            case CURRENT_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = metersDataRepository.findByStartDateAfterAndUserId(calendar.getTime(), userId);
                break;
            case CURRENT_YEAR:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = metersDataRepository.findByStartDateAfterAndUserId(calendar.getTime(), userId);
                break;
            case ALL:
                metersData = metersDataRepository.findByUserId(userId);
                break;
            default:
                return null;
        }
        return metersData;
    }

    public Iterable<MetersData> getAllMetersDataFromIrresponsibleUsers(PeriodType periodType) {
        Iterable<MetersData> metersData;
        switch (periodType) {
            case CURRENT_MONTH:
                // TODO: доделать получения показателей пользователей, кто не отправлял последний месяц;
                break;
            case CURRENT_YEAR:
                // TODO: доделать получения показателей пользователей, кто не отправлял последний год;
                break;
            case ALL:
                // TODO: доделать получения показателей пользователей, кто не отправлял вообще (а смысл?);
                break;
            default:
                return null;
        }
        return null;
    }
}
