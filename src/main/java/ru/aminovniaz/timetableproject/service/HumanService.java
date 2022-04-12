package ru.aminovniaz.testtask.service;


import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.model.Interval;

import java.util.List;

public interface HumanService {
    List<HumanDto> getAllHuman();

    HumanDto addHuman(HumanDto humanData);

    HumanDto getHumanById(Long humanId);

    List<Interval> getCommonGaps(List<String> humanNames);
}
