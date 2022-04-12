package ru.aminovniaz.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aminovniaz.testtask.dto.EventDto;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.exception.EntityNotFoundException;
import ru.aminovniaz.testtask.model.Event;
import ru.aminovniaz.testtask.model.Human;
import ru.aminovniaz.testtask.repository.EventRepository;
import ru.aminovniaz.testtask.repository.HumanRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private TimetableService timetableService;

    @Override
    public List<EventDto> getAllEvents() {
        return convert(eventRepository.findAll());
    }

    private EventDto convert(Event event) {
        EventDto eventData = EventDto.builder()
                            .name(event.getName())
                            .start(event.getStart())
                            .finish(event.getFinish())
                            .build();

        if (event.getMembers() != null) {
            eventData.setMembers(event.getMembers()
                    .stream().map(Human::getName).collect(Collectors.toList()));
        }
        return eventData;
    }

    private List<EventDto> convert(List<Event> events) {
        return events.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public EventDto addEvent(EventDto eventData) {
        Event event = Event.builder()
                    .name(eventData.getName())
                    .start(eventData.getStart())
                    .finish(eventData.getFinish())
                    .build();

        eventRepository.save(event);
        return eventData;
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findEventById(eventId)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Human getHumanByName(String humanName) {
        return humanRepository.findHumanByName(humanName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public EventDto addEventToHuman(Long eventId, HumanDto humanDto) {
        Event event = getEventById(eventId);
        Human human = getHumanByName(humanDto.getName());

        if (!human.getEvents().contains(event) &&
                !timetableService.hasCollision(human.getEvents(), event)) {
            event.getMembers().add(human);
            eventRepository.save(event);
        }
        if (!event.getMembers().contains(human)) {
            human.getEvents().add(event);
            humanRepository.save(human);
        }

        return convert(event);
    }

    @Override
    public EventDto addEventToHumans(Long eventId, List<HumanDto> humansDto) {
        Event event = getEventById(eventId);
        for (HumanDto humanDto : humansDto) {
            Human human = getHumanByName(humanDto.getName());
            if (!human.getEvents().contains(event)) {
                human.getEvents().add(event);
                humanRepository.save(human);
            }
            if (!event.getMembers().contains(human))
                event.getMembers().add(human);
        }
        eventRepository.save(event);

        return convert(event);
    }
}
