package com.katalon.plugin.dingtalk;
import com.katalon.platform.api.execution.TestSuiteCollectionExecutionContext;
import com.katalon.platform.api.execution.TestSuiteExecutionContext;
import org.osgi.service.event.Event;
import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.event.ExecutionEvent;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.extension.EventListenerInitializer;
import com.katalon.platform.api.preference.PluginPreference;

import java.io.IOException;

public class DingDingEventListenerInitializer implements EventListenerInitializer, DingDingComponent {

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
            try {
                PluginPreference preferences = getPluginStore();
                boolean isIntegrationEnabled = preferences.getBoolean(DingDingConstants.PREF_IS_DINGTALK_ENABLED, false);
                if (!isIntegrationEnabled) {
                    return;
                }

                boolean enableSuiteCollectionMessage = preferences.getBoolean(DingDingConstants.ENABLE_SUITE_COLLECTION_MESSAGE, true);
                boolean enableSuiteMessage = preferences.getBoolean(DingDingConstants.ENABLE_SUITE_MESSAGE, true);
                if (enableSuiteCollectionMessage) {
                    sendSuiteCollectionMessage(listener, event);
                }
                if (enableSuiteMessage) {
                    sendSuiteMessage(listener, event);
                }

            } catch (ResourceException e) {
                e.printStackTrace(System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

   void sendSuiteCollectionMessage(EventListener listener,Event event) throws ResourceException, IOException {
        if (ExecutionEvent.TEST_SUITE_COLLECTION_FINISHED_EVENT.equals(event.getTopic())) {
            PluginPreference preferences = getPluginStore();
            String webHook = preferences.getString(DingDingConstants.WEB_HOOK, "");
            String mobiles = preferences.getString(DingDingConstants.MOBILES, "");
            ExecutionEvent eventObject = (ExecutionEvent) event.getProperty("org.eclipse.e4.data");

            TestSuiteCollectionExecutionContext testSuiteContext = (TestSuiteCollectionExecutionContext) eventObject
                    .getExecutionContext();
            TestSuiteCollectionStatusSummary testSuiteCollectionSummary = TestSuiteCollectionStatusSummary.of(testSuiteContext);
            System.out.println("DingDing: Start sending summary message to web hook: " + webHook);
            String message = "Summary execution result of test suite: " + testSuiteContext.getSourceId()
                    + "\nTotal test cases: " + Integer.toString(testSuiteCollectionSummary.getTotalTestCases())
                    + "\nTotal passes: " + Integer.toString(testSuiteCollectionSummary.getTotalPasses())
                    + "\nTotal failures: " + Integer.toString(testSuiteCollectionSummary.getTotalFailures())
                    + "\nTotal errors: " + Integer.toString(testSuiteCollectionSummary.getTotalErrors())
                    + "\nTotal skipped: " + Integer.toString(testSuiteCollectionSummary.getTotalSkipped());

            DingtalkService.getInstance().sendMessage(webHook,mobiles, message);

            System.out.println("DingDing: Summary message has been successfully sent");
        }
    }
    void sendSuiteMessage(EventListener listener,Event event) throws ResourceException, IOException {
        if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())) {
            PluginPreference preferences = getPluginStore();
            String webHook = preferences.getString(DingDingConstants.WEB_HOOK, "");
            String mobiles = preferences.getString(DingDingConstants.MOBILES, "");
            ExecutionEvent eventObject = (ExecutionEvent) event.getProperty("org.eclipse.e4.data");

            TestSuiteExecutionContext testSuiteContext = (TestSuiteExecutionContext) eventObject
                    .getExecutionContext();
            TestSuiteStatusSummary testSuiteSummary = TestSuiteStatusSummary.of(testSuiteContext);
            System.out.println("DingDing: Start sending summary message to web hook: " + webHook);
            String message = "Summary execution result of test suite1: " + testSuiteContext.getSourceId()
                    + "\nTotal test cases: " + Integer.toString(testSuiteSummary.getTotalTestCases())
                    + "\nTotal passes: " + Integer.toString(testSuiteSummary.getTotalPasses())
                    + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures())
                    + "\nTotal errors: " + Integer.toString(testSuiteSummary.getTotalErrors())
                    + "\nTotal skipped: " + Integer.toString(testSuiteSummary.getTotalSkipped());

            DingtalkService.getInstance().sendMessage(webHook,mobiles, message);
            DingtalkService.getInstance().sendMessage(webHook,mobiles,TestSuiteMessageSummary.of(testSuiteContext).getMessage());
            System.out.println("DingDing: Summary message has been successfully sent");
        }
    }
}
