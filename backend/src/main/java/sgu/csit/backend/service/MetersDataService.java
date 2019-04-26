package sgu.csit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgu.csit.backend.dto.MetersDataDTO;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.model.User;
import sgu.csit.backend.repository.MetersDataRepository;
import sgu.csit.backend.response.MetersDataSendResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class MetersDataService {
    private final MetersDataRepository metersDataRepository;

    @Autowired
    public MetersDataService(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
    }

    // saving
    private MetersData getLast(List<MetersData> metersData) {
        MetersData lastMD = metersData.get(0);
        for (MetersData md : metersData)
            if (md.getDate().after(lastMD.getDate()))
                lastMD = md;
        return lastMD;
    }
    public MetersDataSendResponse addMetersData(MetersData newMetersData) {
        MetersDataSendResponse response = new MetersDataSendResponse("ok", "ok", "ok");

        System.out.println("New meters data: " + newMetersData);

        Integer apartment = newMetersData.getUser().getApartment();
        List<MetersData> apartMetersData = metersDataRepository.findByUser_Apartment(apartment);

        if (apartMetersData.isEmpty()) {
            System.out.println("Apartment meters data is empty!");
            System.out.println("Saving first meters data...");
            metersDataRepository.save(newMetersData);
            return response;
        } else {
            System.out.println("Apartment meters data is not empty!");
            System.out.println("Retrieving last meters data...");
            MetersData lastMetersData = getLast(apartMetersData);

            System.out.println("Last meters data: " + lastMetersData);

            System.out.println("Checking...");
            if (lastMetersData.getElectricity() > newMetersData.getElectricity())
                response.setElectro("error");
            if (lastMetersData.getColdWater() > newMetersData.getColdWater())
                response.setCold("error");
            if (lastMetersData.getHotWater() > newMetersData.getHotWater())
                response.setHot("error");

            if (response.getElectro().equals("ok")
                && response.getCold().equals("ok")
                && response.getHot().equals("ok")) {
                System.out.println("New meters data is valid");
                System.out.println("Saving new meters data...");
                metersDataRepository.save(newMetersData);
                return response;
            }

            System.out.println("New meters data is not valid!");
            return response;
        }
    }

    // retrieval
    public List<MetersDataDTO> getApartment(Integer apartment, PeriodType periodType) {
        List<MetersDataDTO> metersData;
        switch (periodType) {
            case CURRENT_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = MetersDataDTO.toDTO(metersDataRepository.
                                    findByDateAfterAndUser_Apartment(calendar.getTime(), apartment));
                break;
            case CURRENT_YEAR:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                metersData = MetersDataDTO.toDTO(metersDataRepository.
                                    findByDateAfterAndUser_Apartment(calendar.getTime(), apartment));
                break;
            case ALL:
                metersData = MetersDataDTO.toDTO(metersDataRepository.findByUser_Apartment(apartment));
                break;
            default:
                return null;
        }
        return metersData;
    }

    public List<MetersDataDTO> getApartmentByRange(Integer apartment, Date fromDate, Date toDate) {
        List<MetersDataDTO> metersData = MetersDataDTO.toDTO(metersDataRepository
                                                                .findByUser_ApartmentAndDateAfterAndDateBefore(apartment, fromDate, toDate));
        return metersData;
    }
}
