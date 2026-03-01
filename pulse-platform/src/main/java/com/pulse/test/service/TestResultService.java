package com.pulse.test.service;

import com.pulse.test.model.TestResult;
import com.pulse.test.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestResultService {

    private final TestResultRepository testResultRepository;

    public void saveAll(List<TestResult> results) {
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("The results list must not be null or empty");
        }
        testResultRepository.saveAll(results);
    }

    public List<TestResult> getByTicketId(String ticketId) {
        return testResultRepository.findByTicketId(ticketId);
    }
}