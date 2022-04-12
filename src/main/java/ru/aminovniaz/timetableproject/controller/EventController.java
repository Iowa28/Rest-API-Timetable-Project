package ru.aminovniaz.testtask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aminovniaz.testtask.dto.EventDto;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.service.EventService;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping("/events")
    public ResponseEntity<EventDto> addEvent(@RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.addEvent(eventDto));
    }

    @PostMapping("/events/human/{event-id}")
    public ResponseEntity<EventDto> addEventToHuman(@PathVariable("event-id") Long eventId,
                                                    @RequestBody HumanDto humanDto) {
        return ResponseEntity.ok(eventService.addEventToHuman(eventId, humanDto));
    }


    @PostMapping("/events/humans/{event-id}")
    public ResponseEntity<EventDto> addEventToHumans(@PathVariable("event-id") Long eventId,
                                                    @RequestBody List<HumanDto> humansDto) {
        return ResponseEntity.ok(eventService.addEventToHumans(eventId, humansDto));
    }
}
