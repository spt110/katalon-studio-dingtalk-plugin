# Katalon Studio plugin for DingDing integration

This Katalon Studio plugin provides DingDing integration functions.

The main purpose is to collect and send a summary report of an execution to a channel after the execution has completed.

## Build

Requirements:
- JDK 1.8
- Maven 3.3+

`mvn clean package`

## Usage
#### Install
- Install the Katalon Studio v6.0.3 or later: [win32](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Windows_32.zip), [win64](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Windows_64.zip), [macOS](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon+Studio.dmg), and [linux64](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Linux_64.tar.gz).
- Choose *Plugin* > *Install Plugin* in Katalon Studio main menu and select the generated jar file.
- Click on *DingDing* toolbar button to configure DingDing Integration settings that is under  is under *Plugins* category of Project Settings.
![project-settings](/docs/images/project-settings.png)
![dingtalk_item](/docs/images/dingtalk_item.png)

#### DingDing Settings
- Uncheck *Using DingDing*, and enter your DingDing [Web Hook](https://open-doc.dingtalk.com/microapp/serverapi2/qf2nxq)
- Click the *Test Connection* to check your credentials. If everything is OK, a message should be sent to dingtalk.

![test_message](/docs/images/test_message.png)
- Click the *Apply* button then *OK* to close the Project Settings dialog.

#### Run test execution
- Execute a test suite and wait for the execution finished, a summary message should be sent to your DingDing channel.

![summary_message](/docs/images/summary_message.png)
