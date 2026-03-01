ALTER TABLE test_results
ADD COLUMN report_tags TEXT[] NOT NULL DEFAULT '{}',
ADD COLUMN action_tags TEXT[] NOT NULL DEFAULT '{}';