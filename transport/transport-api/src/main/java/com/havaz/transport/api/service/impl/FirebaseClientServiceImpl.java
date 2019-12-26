package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.form.FirebaseDataForm;
import com.havaz.transport.api.form.FirebaseForm;
import com.havaz.transport.api.form.FirebaseNotificationForm;
import com.havaz.transport.api.model.FirebaseHavazNowDTO;
import com.havaz.transport.api.service.FirebaseClientService;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class FirebaseClientServiceImpl implements FirebaseClientService {
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseClientServiceImpl.class);

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", CacheData.CONFIGURATION_DATA.get(Constant.APP_MOBILE_FIREBASE_ID));
        return headers;
    }

    private String getFirebaseUrl(){
        return CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_URL);
    }

    @Override
    public void sendNotification(String to, String title, String content, Object data, String audio) {
        try {
            LOG.info("Begin send notification to firebase.");
            RestTemplate restTemplate = new RestTemplate();
            FirebaseForm firebaseForm = new FirebaseForm();
            firebaseForm.setNotification(new FirebaseNotificationForm(title, content, audio, audio));
            firebaseForm.setTo(to);
            firebaseForm.setData(data);
            HttpEntity<FirebaseForm> requestBody = new HttpEntity<>(firebaseForm, getHeader());

            restTemplate.postForObject(getFirebaseUrl(), requestBody, Object.class);
            LOG.info("Send done.");
        } catch (Exception e){
            LOG.error("Error send notification to firebase.", e);
        }

    }

    @Override
    public void sendDataToTopic(Object obj, String topicName) {
        try {
            LOG.info("Begin send data to firebase.");
            RestTemplate restTemplate = new RestTemplate();
            FirebaseDataForm firebaseDataForm = new FirebaseDataForm();
            firebaseDataForm.setTo(topicName);
            HttpEntity<FirebaseDataForm> requestBody = new HttpEntity<>(firebaseDataForm, getHeader());

            restTemplate.postForObject(getFirebaseUrl(), requestBody, Object.class);
            LOG.info("Send data done.");
        }catch (Exception e){
            LOG.error("Error send data to firebase.", e);
        }
    }

    @Override
    public void sendNotification(int toTaiXeId, String title, String content, Object data, String audio) {
        try {
            AdminLv2UserEntity adminLv2UserEntity = adminLv2UserRepository.findById(toTaiXeId).orElse(null);
            if (adminLv2UserEntity != null && !StringUtils.isEmpty(adminLv2UserEntity.getAdmAppToken())) {
                this.sendNotification(adminLv2UserEntity.getAdmAppToken(), title, content, data, audio);
            }
        } catch (Exception e) {
            LOG.error("Error send notification to firebase.", e);
        }
    }

    @Override
    public void sendDataToTopicFirebase(String topic, Object data) {
        try {
            LOG.info("Begin send CMD Havaz Now data to firebase.");
            FirebaseHavazNowDTO firebaseData = new FirebaseHavazNowDTO();
            firebaseData.setTo(topic);
            firebaseData.setData(data);
            HttpEntity<FirebaseHavazNowDTO> requestBody = new HttpEntity<>(firebaseData, getHeader());
            restTemplate.postForObject(getFirebaseUrl(), requestBody, Object.class);
            LOG.info("Send Cmd Havaz Now data done.");
        }catch (Exception e){
            LOG.error("Error send data to firebase.", e);
        }
    }


}
