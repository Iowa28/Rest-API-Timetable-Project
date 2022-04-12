package ru.aminovniaz.testtask.service;

import ru.aminovniaz.testtask.dto.EventDto;
import ru.aminovniaz.testtask.dto.HumanDto;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();

    EventDto addEvent(EventDto eventData);

    EventDto addEventToHuman(Long eventId, HumanDto humanDto);

    EventDto addEventToHumans(Long eventId, List<HumanDto> humansDto);
}
