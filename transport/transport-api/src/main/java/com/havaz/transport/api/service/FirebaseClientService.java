package com.havaz.transport.api.service;

public interface FirebaseClientService {
    void sendNotification(String to, String title, String content, Object data, String audio);

    void sendNotification(int toTaiXeId, String title, String content, Object data, String audio);

    void sendDataToTopic(Object obj, String topicName);

    void sendDataToTopicFirebase(String topic, Object data);
}
