package com.katalon.plugin.dingtalk;

import org.eclipse.jface.preference.PreferencePage;

import com.katalon.platform.api.extension.PluginPreferencePage;

public class DingDingPluginPreferencePage implements PluginPreferencePage {

    @Override
    public String getName() {
        return "DingDing";
    }

    @Override
    public String getPageId() {
        return "com.katalon.plugin.dingtalk.DingDingPluginPreferenPage";
    }

    @Override
    public Class<? extends PreferencePage> getPreferencePageClass() {
        return DingDingPreferencePage.class;
    }

}
