CREATE TABLE test_results (
    id BIGSERIAL PRIMARY KEY,
    scenario_name VARCHAR(500) NOT NULL,
    ticket_id VARCHAR(50) NOT NULL,
    status VARCHAR(10) NOT NULL,
    branch VARCHAR(255) NOT NULL,
    run_timestamp TIMESTAMP NOT NULL,
    duration_ms BIGINT
);

CREATE INDEX idx_test_results_ticket_id ON test_results(ticket_id);
CREATE INDEX idx_test_results_run_timestamp ON test_results(run_timestamp);