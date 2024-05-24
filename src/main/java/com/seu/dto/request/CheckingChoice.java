package com.seu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 该类作为实体类用于教务批量通过或驳回请求
 */
public class CheckingChoice {
    private Integer status;
    private List<Integer> ids;
}
