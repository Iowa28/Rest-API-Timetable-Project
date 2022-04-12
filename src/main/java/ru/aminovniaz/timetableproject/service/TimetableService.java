package ru.aminovniaz.testtask.service;

import ru.aminovniaz.testtask.model.Interval;
import ru.aminovniaz.testtask.model.Event;
import ru.aminovniaz.testtask.model.Human;

import java.util.List;

public interface TimetableService {
    boolean hasCollision(List<Event> events, Event newEvent);

    List<Interval> commonGaps(List<Human> humans);
}
