package com.katalon.plugin.dingtalk;

import com.katalon.platform.api.execution.TestSuiteExecutionContext;

public class TestSuiteMessageSummary {
   String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static TestSuiteMessageSummary of(TestSuiteExecutionContext testSuiteContext) {
        TestSuiteMessageSummary summary = new TestSuiteMessageSummary();
StringBuilder sb=new StringBuilder();
        testSuiteContext.getTestCaseContexts().forEach(tcContext -> {
          sb.append(tcContext.getTestCaseStatus()).append(tcContext.toString());
        });
        summary.setMessage(sb.toString());
        return summary;
    }
}
