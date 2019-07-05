package com.katalon.plugin.dingtalk;

import com.katalon.platform.api.execution.TestSuiteCollectionExecutionContext;

public class TestSuiteCollectionStatusSummary {
    private int totalPasses;

    private int totalFailures;

    private int totalErrors;

    private int totalSkipped;

    private TestSuiteCollectionStatusSummary() {
        totalPasses = 0;
        totalFailures = 0;
        totalErrors = 0;
        totalSkipped = 0;
    }

    public int getTotalPasses() {
        return totalPasses;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    public int getTotalTestCases() {
        return totalPasses + totalFailures + totalErrors + totalSkipped;
    }

    public static TestSuiteCollectionStatusSummary of(TestSuiteCollectionExecutionContext testSuiteContext) {
        TestSuiteCollectionStatusSummary summary = new TestSuiteCollectionStatusSummary();

        testSuiteContext.getTestSuiteResults().forEach(tsContext -> {
            tsContext.getTestCaseContexts().forEach(tcContext -> {
                switch (tcContext.getTestCaseStatus()) {
                    case TestCaseStatusConstants.PASSED:
                        summary.totalPasses++;
                        break;
                    case TestCaseStatusConstants.FAILED:
                        summary.totalFailures++;
                        break;
                    case TestCaseStatusConstants.ERROR:
                        summary.totalErrors++;
                        break;
                    default:
                        summary.totalSkipped++;
                }
            });

        });
        return summary;
    }
}
