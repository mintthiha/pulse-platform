package com.pulse.test.service;

import com.pulse.test.model.TestResult;
import com.pulse.test.repository.TestResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestResultServiceTest {

    @Mock
    private TestResultRepository testResultRepository;

    @InjectMocks
    private TestResultService testResultService;

    // This should save all results when a valid non-empty list is provided
    @Test
    void saveAll_savesResultsSuccessfully() {
        TestResult result = new TestResult();
        result.setTicketId("SCRUM-5");
        result.setScenarioName("SCRUM-5 - Test Equity Order cancellation");
        result.setStatus("PASS");

        testResultService.saveAll(List.of(result));

        verify(testResultRepository).saveAll(List.of(result));
    }

    // This should throw an error when a null list is passed
    @Test
    void saveAll_throwsWhenListIsNull() {
        assertThatThrownBy(() -> testResultService.saveAll(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The results list must not be null or empty");
    }

    // This should throw an error when an empty list is passed
    @Test
    void saveAll_throwsWhenListIsEmpty() {
        assertThatThrownBy(() -> testResultService.saveAll(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The results list must not be null or empty");
    }

    // This should return the results matching the given ticket ID
    @Test
    void getByTicketId_returnsMatchingResults() {
        TestResult result = new TestResult();
        result.setTicketId("SCRUM-5");

        when(testResultRepository.findByTicketId("SCRUM-5")).thenReturn(List.of(result));

        List<TestResult> results = testResultService.getByTicketId("SCRUM-5");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTicketId()).isEqualTo("SCRUM-5");
    }

    // This should return an empty list when no results match the ticket ID
    @Test
    void getByTicketId_returnsEmptyListWhenNoResults() {
        when(testResultRepository.findByTicketId("SCRUM-99")).thenReturn(Collections.emptyList());

        List<TestResult> results = testResultService.getByTicketId("SCRUM-99");

        assertThat(results).isEmpty();
    }
}