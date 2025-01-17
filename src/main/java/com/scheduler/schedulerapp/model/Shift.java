package com.scheduler.schedulerapp.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Example: Morning, Evening, Night

    @Column(nullable = false)
    private LocalTime startTime;

   @Column(nullable = false)
    private LocalTime endTime;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        // Automatically calculate end time
        this.endTime = this.startTime.plusHours(8);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}
