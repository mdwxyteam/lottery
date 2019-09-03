package com.md.luck.lottery.common.entity;

import lombok.Data;

@Data
public class Prize {
    private long id;
    private String prizeDescription;
    private String iconUrl;
    private int prizeCount;
}
