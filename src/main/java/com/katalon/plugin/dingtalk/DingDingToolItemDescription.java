package com.katalon.plugin.dingtalk;

import com.katalon.platform.api.extension.ToolItemDescription;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.DialogActionService;

public class DingDingToolItemDescription implements ToolItemDescription {

    @Override
    public String name() {
        return "DingDing";
    }

    @Override
    public String toolItemId() {
        return DingDingConstants.PLUGIN_ID + ".dingtalkToolItem";
    }

    @Override
    public String iconUrl() {
        return "platform:/plugin/" + DingDingConstants.PLUGIN_ID + "/icons/dingtalk_32x24.png";
    }

    @Override
    public void handleEvent() {
        ApplicationManager.getInstance().getUIServiceManager().getService(DialogActionService.class).openPluginPreferencePage(
                DingDingConstants.PREF_PAGE_ID);
    }

    @Override
    public boolean isItemEnabled() {
        return true;
    }
}
