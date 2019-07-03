package build.dream.catering.listeners;

import build.dream.catering.constants.Constants;
import build.dream.catering.services.JDDJService;
import build.dream.common.utils.JacksonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JDDJMessageListener implements MessageListener<String, String> {
    @Autowired
    private JDDJService jddjService;

    @KafkaListener(topics = "${jddj.message.topic}")
    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        String value = data.value();
        Map<String, Object> message = JacksonUtils.readValueAsMap(value, String.class, Object.class);
        int type = MapUtils.getIntValue(message, "type");
        switch (type) {
            case Constants.DJSW_TYPE_NEW_ORDER:
                jddjService.handleNewOrder(message);
                break;
        }
    }
}