package sgu.csit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgu.csit.backend.dto.MetersDataDTO;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.repository.MetersDataRepository;

import java.util.Calendar;
import java.util.Set;

@Service
public class MetersDataService {
    private final MetersDataRepository metersDataRepository;

    @Autowired
    public MetersDataService(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
    }

    // saving
    public void addMetersData(MetersData metersData) {
        metersDataRepository.save(metersData);
    }

    // retrieval
    public Set<MetersDataDTO> getAllMetersDataByUserApart(PeriodType periodType, Integer userApart) {
        Set<MetersDataDTO> metersData;
        switch (periodType) {
            case CURRENT_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = MetersDataDTO.toDTO(metersDataRepository.
                                    findByDateAfterAndUser_Apartment(calendar.getTime(), userApart));
                break;
            case CURRENT_YEAR:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = MetersDataDTO.toDTO(metersDataRepository.
                                    findByDateAfterAndUser_Apartment(calendar.getTime(), userApart));
                break;
            case ALL:
                metersData = MetersDataDTO.toDTO(metersDataRepository.findByUser_Apartment(userApart));
                break;
            default:
                return null;
        }
        return metersData;
    }
}
