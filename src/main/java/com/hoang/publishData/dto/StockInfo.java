package com.hoang.publishData.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StockInfo {
    private String symbol;
    private int floorPrice;
    private int matchPrice;
    private int ceilPrice;
}
