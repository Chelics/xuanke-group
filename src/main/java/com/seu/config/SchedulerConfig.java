package com.seu.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@Component
@PropertySource("classpath:scheduler-config.yml")
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerConfig {
    private short maxTimeNumber;
    private short timeModulo;
    private short courseNumsOneDay;
    private short nullTime;
    private short[][] weekRange;
    private short[][] timeList;
    private Map<String, Integer[]> timeClassificationMap;

    public Map<Short, Integer[]> getTIME_CLASSIFICATION_MAP_SHORT() {
        Map<Short, Integer[]> timeClassificationMapShort = new HashMap<>();
        for (Map.Entry<String, Integer[]> entry : timeClassificationMap.entrySet()) {
            try {
                Short key = Short.parseShort(entry.getKey());
                timeClassificationMapShort.put(key, entry.getValue());
            } catch (NumberFormatException e) {
                log.error("scheduler-config类中键值类型错误: " + entry.getKey(), e);
            }
        }
        return timeClassificationMapShort;
    }
}
