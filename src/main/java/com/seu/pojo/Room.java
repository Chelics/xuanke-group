package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Integer id;
    private String building;//教学楼名 例：教一
    private String roomName;
    private Short roomStorage;
}
