package com.katalon.plugin.dingtalk;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.katalon.plugin.dingtalk.client.DingtalkChatbotClient;
import com.katalon.plugin.dingtalk.message.Message;
import com.katalon.plugin.dingtalk.message.TextMessage;

public class DingtalkService {
    DingtalkChatbotClient dingTalkClient = new DingtalkChatbotClient();
    static DingtalkService instance;
    static Object lockObject = new Object();
    public static synchronized DingtalkService getInstance() {
        if (instance == null) {
            instance = new DingtalkService();
        }
        return instance;
    }
    public void sendMessage(String webHook, Message message) throws IOException {
        dingTalkClient.send(webHook, message);
    }

    public void at(String webHook, List<String> list) throws IOException {
        String text = list.stream().map(f -> "@" + f).collect(Collectors.joining(""));
        TextMessage message = new TextMessage(text);
        message.setAtMobiles(list);
        sendMessage(webHook,message);
    }
    public void sendMessage(String webHook, String message) throws IOException {
        TextMessage textMessage=new TextMessage(message);
        sendMessage(webHook,textMessage);
    }
    public void sendMessage(String webHook,String mobiles, String message) throws IOException {
        TextMessage textMessage=new TextMessage(message);
        if(mobiles!=null&&mobiles!=""){
            textMessage.setAtMobiles(Arrays.asList(mobiles.split(",")));
        }

        sendMessage(webHook,textMessage);
    }

}