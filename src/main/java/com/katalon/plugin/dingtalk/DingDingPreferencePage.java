package com.katalon.plugin.dingtalk;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.UISynchronizeService;

public class DingDingPreferencePage extends PreferencePage implements DingDingComponent {

    private Button chckEnableIntegration;
    private Button chckEnableSuiteCollection;
    private Button chckEnableSuite;
    private Group grpAuthentication;
    private Group grpTestResult;
    private Text txtWebHook;

    private Text txtMobiles;

    private Composite container;

    private Button btnTestConnection;

    private Label lblConnectionStatus;

    private Thread thread;

    @Override
    protected Control createContents(Composite composite) {
        container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        chckEnableIntegration = new Button(container, SWT.CHECK);
        chckEnableIntegration.setText("Using DingDing");

        grpAuthentication = new Group(container, SWT.NONE);
        grpAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout glAuthentication = new GridLayout(2, false);
        glAuthentication.horizontalSpacing = 15;
        glAuthentication.verticalSpacing = 10;
        grpAuthentication.setLayout(glAuthentication);
        grpAuthentication.setText("Authentication");

        Label lblToken = new Label(grpAuthentication, SWT.NONE);
        lblToken.setText("Webhook");
        GridData gdLabel = new GridData(SWT.LEFT, SWT.TOP, false, false);
        lblToken.setLayoutData(gdLabel);

        txtWebHook = new Text(grpAuthentication, SWT.BORDER);
        GridData gdTxtToken = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdTxtToken.widthHint = 200;
        txtWebHook.setLayoutData(gdTxtToken);

        Label lblMobiles = new Label(grpAuthentication, SWT.NONE);
        lblMobiles.setText("Mobiles");
        lblToken.setLayoutData(gdLabel);

        txtMobiles = new Text(grpAuthentication, SWT.BORDER);
        txtMobiles.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        btnTestConnection = new Button(grpAuthentication, SWT.PUSH);
        btnTestConnection.setText("Test Connection");
        btnTestConnection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                testDingDingConnection(txtWebHook.getText(),txtMobiles.getText());
            }
        });

        lblConnectionStatus = new Label(grpAuthentication, SWT.NONE);
        lblConnectionStatus.setText("");
        lblConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
        createTestResultContents(composite);
        handleControlModifyEventListeners();
        initializeInput();

        return container;
    }
    protected Control createTestResultContents(Composite composite) {
        container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        grpTestResult = new Group(container, SWT.NONE);
        grpTestResult.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout glTestResult = new GridLayout(2, false);
        glTestResult.horizontalSpacing = 15;
        glTestResult.verticalSpacing = 10;
        grpTestResult.setLayout(glTestResult);
        grpTestResult.setText("Test Result");


        chckEnableSuiteCollection = new Button(grpTestResult, SWT.CHECK);
        chckEnableSuiteCollection.setText("Using Test Suite Collection Message");


        chckEnableSuite = new Button(grpTestResult, SWT.CHECK);
        chckEnableSuite.setText("Using Test Suite Message");

        return container;
    }
    private void testDingDingConnection(String webHook,String mobiles) {
        btnTestConnection.setEnabled(false);
        lblConnectionStatus.setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        lblConnectionStatus.setText("Connecting...");
        thread = new Thread(() -> {
            try {
                DingtalkService.getInstance().sendMessage(webHook, mobiles,"This is a test message from Katalon Studio using DingDing Plugin");
                syncExec(() -> {
                    lblConnectionStatus
                            .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
                    lblConnectionStatus.setText("Connection success");
                });
            } catch (Exception e) {
                e.printStackTrace(System.err);
                syncExec(() -> {
                    lblConnectionStatus
                            .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
                    lblConnectionStatus
                            .setText("Connection failed. Reason: " + StringUtils.defaultString(e.getMessage()));
                });
            } finally {
                syncExec(() -> btnTestConnection.setEnabled(true));
            }
        });
        thread.start();
    }

    void syncExec(Runnable runnable) {
        if (lblConnectionStatus != null && !lblConnectionStatus.isDisposed()) {
            ApplicationManager.getInstance()
                    .getUIServiceManager()
                    .getService(UISynchronizeService.class)
                    .syncExec(runnable);
        }
    }

    private void handleControlModifyEventListeners() {
        chckEnableIntegration.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                recursiveSetEnabled(grpAuthentication, chckEnableIntegration.getSelection());
            }
        });
    }

    public static void recursiveSetEnabled(Control ctrl, boolean enabled) {
        if (ctrl instanceof Composite) {
            Composite comp = (Composite) ctrl;
            for (Control c : comp.getChildren()) {
                recursiveSetEnabled(c, enabled);
                c.setEnabled(enabled);
            }
        } else {
            ctrl.setEnabled(enabled);
        }
    }

    @Override
    public boolean performOk() {
        if (!isControlCreated()) {
            return true;
        }
        try {
            PluginPreference pluginStore = getPluginStore();

            pluginStore.setBoolean(DingDingConstants.PREF_IS_DINGTALK_ENABLED, chckEnableIntegration.getSelection());
            pluginStore.setString(DingDingConstants.WEB_HOOK, txtWebHook.getText());
            pluginStore.setString(DingDingConstants.MOBILES,txtMobiles.getText());
            pluginStore.setBoolean(DingDingConstants.ENABLE_SUITE_COLLECTION_MESSAGE, chckEnableSuiteCollection.getSelection());
            pluginStore.setBoolean(DingDingConstants.ENABLE_SUITE_MESSAGE, chckEnableSuite.getSelection());

            pluginStore.save();
            return true;
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update DingDing Integration Settings.");
            return false;
        }
    }

    private void initializeInput() {
        try {
            PluginPreference pluginStore = getPluginStore();

            chckEnableIntegration.setSelection(pluginStore.getBoolean(DingDingConstants.PREF_IS_DINGTALK_ENABLED, false));
            chckEnableIntegration.notifyListeners(SWT.Selection, new Event());

            txtWebHook.setText(pluginStore.getString(DingDingConstants.WEB_HOOK, ""));
            txtMobiles.setText(pluginStore.getString(DingDingConstants.MOBILES,""));

            chckEnableSuiteCollection.setSelection(pluginStore.getBoolean(DingDingConstants.ENABLE_SUITE_COLLECTION_MESSAGE, true));

            chckEnableSuite.setSelection(pluginStore.getBoolean(DingDingConstants.ENABLE_SUITE_MESSAGE, true));

            container.layout(true, true);
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update DingDing Integration Settings.");
        }
    }
}
