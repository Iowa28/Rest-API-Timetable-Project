package ru.aminovniaz.testtask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.model.Interval;
import ru.aminovniaz.testtask.service.HumanService;

import java.util.List;

@RestController()
public class HumanController {

    @Autowired
    private HumanService humanService;

    @GetMapping("/humans")
    public ResponseEntity<List<HumanDto>> getHumans() {
        return ResponseEntity.ok(humanService.getAllHuman());
    }

    @PostMapping("/humans")
    public ResponseEntity<HumanDto> addHuman(@RequestBody HumanDto humanData) {
        return ResponseEntity.ok(humanService.addHuman(humanData));
    }

    @GetMapping("/humans/{human-id}")
    public ResponseEntity<HumanDto> getHumanById(@PathVariable("human-id") Long humanId) {
        return ResponseEntity.ok(humanService.getHumanById(humanId));
    }

    @PostMapping("/humans/common-gaps")
    public ResponseEntity<List<Interval>> getCommonGaps(@RequestBody List<String> humanNames) {
        return ResponseEntity.ok(humanService.getCommonGaps(humanNames));
    }
}
