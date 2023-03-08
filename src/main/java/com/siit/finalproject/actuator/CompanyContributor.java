package com.siit.finalproject.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Component
public class CompanyContributor implements InfoContributor {
    Integer overallProfit=258;
    LocalDate currentDate = LocalDate.of(2021, 12, 15);


    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> data = new HashMap<>();
        data.put("current-date", currentDate);
        data.put("overall-profit", overallProfit);

        builder.withDetail("Date and profit information", data);
    }

    public void incrementCurrentDate(){
        LocalDate newDate = currentDate.plusDays(1);
    };

}
