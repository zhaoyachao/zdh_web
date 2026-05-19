package com.zyc.zdh.job;

import com.zyc.zdh.entity.StrategyGroupInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class JobDigitalMarketTest {

    @Test
    public void chooseJobBean() {

        StrategyGroupInfo strategyGroupInfo=new StrategyGroupInfo();

        JobDigitalMarket.chooseJobBean(strategyGroupInfo, 0, null, null);
    }
}