package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;

public interface ActivService {

    ResponMsg add(Activ activ);

    ResponMsg conditionPage(int pageNum, int pageSize, int conditionType, String sponsorName);
}
