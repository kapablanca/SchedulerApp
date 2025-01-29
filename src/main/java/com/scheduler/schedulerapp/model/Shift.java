package com.scheduler.schedulerapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Shift name is required")
    @Size(min = 3, max = 50, message = "Shift name must be between 3 and 50 characters")
    @Column(nullable = false)
    private String name; // Example: Morning, Evening, Night

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalTime startTime;

   @Column(nullable = false)
    private LocalTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
