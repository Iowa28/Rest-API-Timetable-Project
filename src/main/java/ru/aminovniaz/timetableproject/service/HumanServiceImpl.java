package ru.aminovniaz.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.exception.EntityNotFoundException;
import ru.aminovniaz.testtask.model.Interval;
import ru.aminovniaz.testtask.model.Event;
import ru.aminovniaz.testtask.model.Human;
import ru.aminovniaz.testtask.repository.HumanRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HumanServiceImpl implements HumanService {

    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private TimetableService timetableService;

    @Override
    public List<HumanDto> getAllHuman() {
        return convert(humanRepository.findAll());
    }

    private HumanDto convert(Human human) {
        HumanDto humanDto = HumanDto.builder()
                            .name(human.getName())
                            .login(human.getLogin())
                            .build();

        if (human.getEvents() != null) {
            humanDto.setEvents(human.getEvents().stream()
                    .sorted(Comparator.comparing(Event::getStart))
                    .map(event -> event.getStart() + " - " + event.getFinish() + " " + event.getName())
                    .collect(Collectors.toList()));
        }
        return humanDto;
    }

    private List<HumanDto> convert(List<Human> humans) {
        return humans.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public HumanDto addHuman(HumanDto humanData) {
        Human human = Human.builder()
                    .name(humanData.getName())
                    .login(humanData.getLogin())
                    .build();

        humanRepository.save(human);
        return humanData;
    }

    @Override
    public HumanDto getHumanById(Long humanId) {
        Human human = humanRepository.findHumanById(humanId)
                .orElseThrow(EntityNotFoundException::new);
        return convert(human);
    }

    @Override
    public List<Interval> getCommonGaps(List<String> humanNames) {
        return timetableService.commonGaps(getHumansByName(humanNames));
    }

    private List<Human> getHumansByName(List<String> humanNames) {
        return humanNames.stream()
                .map((name) -> humanRepository.findHumanByName(name)
                        .orElseThrow(EntityNotFoundException::new)
                )
                .collect(Collectors.toList());
    }
}
