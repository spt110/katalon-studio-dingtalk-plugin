package com.katalon.plugin.slack;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;

public class SlackPreferencePage extends PreferencePage {

    private Button chckEnableIntegration;

    private Group grpAuthentication;

    private Text txtToken;

    private Text txtChannel;

    @Override
    protected Control createContents(Composite composite) {
        Composite container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        chckEnableIntegration = new Button(container, SWT.CHECK);
        chckEnableIntegration.setText("Using Slack");

        grpAuthentication = new Group(container, SWT.NONE);
        grpAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout glAuthentication = new GridLayout(2, false);
        glAuthentication.horizontalSpacing = 15;
        glAuthentication.verticalSpacing = 10;
        grpAuthentication.setLayout(glAuthentication);
        grpAuthentication.setText("Authentication");

        Label lblToken = new Label(grpAuthentication, SWT.NONE);
        lblToken.setText("Authentication Token");
        GridData gdLabel = new GridData(SWT.LEFT, SWT.TOP, false, false);
        gdLabel.widthHint = 150;
        lblToken.setLayoutData(gdLabel);

        txtToken = new Text(grpAuthentication, SWT.BORDER);
        txtToken.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label lblChannel = new Label(grpAuthentication, SWT.NONE);
        lblChannel.setText("Chanel/Group");
        lblToken.setLayoutData(gdLabel);

        txtChannel = new Text(grpAuthentication, SWT.BORDER);
        txtChannel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        handleControlModifyEventListeners();
        initializeInput();

        return container;
    }

    private void handleControlModifyEventListeners() {
        chckEnableIntegration.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                grpAuthentication.setEnabled(chckEnableIntegration.getSelection());
            }
        });
    }

    @Override
    protected void performApply() {
        IPreferenceStore store = getPreferenceStore();

        store.setValue(SlackConstants.PREF_IS_SLACK_ENABLED, chckEnableIntegration.getSelection());
        store.setValue(SlackConstants.PREF_AUTH_TOKEN, txtToken.getText());
        store.setValue(SlackConstants.PREF_AUTH_CHANNEL, txtChannel.getText());

        if (store.needsSaving()) {
            IPersistentPreferenceStore persistentPreferenceStore = (IPersistentPreferenceStore) store;
            try {
                persistentPreferenceStore.save();
            } catch (IOException e) {
                MessageDialog.openWarning(getShell(), "Warning", "Unable to update Slack Integration Settings.");
            }
        }

        super.performApply();
    }

    private void initializeInput() {
        IPreferenceStore store = getPreferenceStore();

        chckEnableIntegration.setSelection(store.getBoolean(SlackConstants.PREF_IS_SLACK_ENABLED));
        chckEnableIntegration.notifyListeners(SWT.Selection, new Event());

        txtToken.setText(store.getString(SlackConstants.PREF_AUTH_TOKEN));
        txtChannel.setText(store.getString(SlackConstants.PREF_AUTH_CHANNEL));
    }
}