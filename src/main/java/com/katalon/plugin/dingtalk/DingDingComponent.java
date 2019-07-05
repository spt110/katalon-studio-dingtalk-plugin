package com.katalon.plugin.dingtalk;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;

public interface DingDingComponent {
    default PluginPreference getPluginStore() throws ResourceException {
        PluginPreference pluginStore = ApplicationManager.getInstance().getPreferenceManager().getPluginPreference(
                ApplicationManager.getInstance().getProjectManager().getCurrentProject().getId(),
                DingDingConstants.PLUGIN_ID);
        return pluginStore;
    }
}
