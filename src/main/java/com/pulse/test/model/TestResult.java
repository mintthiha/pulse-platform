package com.pulse.test.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
@Getter
@Setter
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_name", nullable = false, length = 500)
    private String scenarioName;

    @Column(name = "ticket_id", nullable = false, length = 50)
    private String ticketId;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "branch", nullable = false, length = 255)
    private String branch;

    @Column(name = "run_timestamp", nullable = false)
    private LocalDateTime runTimestamp;

    @Column(name = "duration_ms")
    private Long durationMs;
}