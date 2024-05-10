package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage {
    private Integer id;//ID
    private String stageName;//阶段名
    private LocalDateTime startTime;//开始时间
    private LocalDateTime endTime;//结束时间
}
