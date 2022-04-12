package ru.aminovniaz.testtask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String login;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "timetable",
            joinColumns = {@JoinColumn(name = "human_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            indexes = {
                @Index(name = "idx_timetable_human_id", columnList = "human_id"),
                @Index(name = "idx_timetable_event_id", columnList = "event_id")
            })
    private List<Event> events;
}
