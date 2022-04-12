package ru.aminovniaz.testtask.service;

import org.springframework.stereotype.Service;
import ru.aminovniaz.testtask.model.Interval;
import ru.aminovniaz.testtask.model.Event;
import ru.aminovniaz.testtask.model.Human;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {

    private boolean collision(Event event1, Event event2) {
        boolean happensBefore = event1.getFinish().isBefore(event2.getStart());
        boolean happensAfter = event1.getStart().isAfter(event2.getFinish());
        return !(happensBefore || happensAfter);
    }

    @Override
    public boolean hasCollision(List<Event> events, Event newEvent) {
        for (Event event : events) {
            if (collision(event, newEvent)) {
                System.out.printf("Произошла коллизия между %s и %s \n", event.getName(), newEvent.getName());
                return true;
            }
        }
        return false;
    }

    private boolean containsEvent(List<Event> events, Event newEvent) {
        for (Event event : events) {
            if (event.getStart().equals(newEvent.getStart()) && event.getFinish().equals(newEvent.getFinish()))
                return true;
        }
        return false;
    }

    @Override
    public List<Interval> commonGaps(List<Human> humans) {
        List<Event> commonEvents = new ArrayList<>();
        for (Human human : humans) {
            for (Event humanEvent : human.getEvents()) {
                if (!containsEvent(commonEvents, humanEvent)) {
                    commonEvents.add(humanEvent);
                }
            }
        }
        commonEvents.sort(Comparator.comparing(Event::getStart));

        List<Interval> gaps = new ArrayList<>();
        LocalTime gapStart = LocalTime.MIDNIGHT;
        LocalTime gapEnd;
        for (Event event : commonEvents) {
            if (event.getStart().isBefore(gapStart) && event.getFinish().isAfter(gapStart)) {
                gapStart = event.getFinish();
            }
            else {
                gapEnd = event.getStart();
                gaps.add(new Interval(gapStart, gapEnd));
                gapStart = event.getFinish();
            }
        }
        gaps.add(new Interval(gapStart, LocalTime.MIDNIGHT));
        return gaps;
    }
}
