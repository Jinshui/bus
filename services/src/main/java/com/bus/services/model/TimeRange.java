package com.bus.services.model;

import java.util.Date;
import java.util.List;

public class TimeRange {
    private Date startTime;
    private Date endTime;
    private List<Vehicle> vehicles;
}

//        startTime: '2014.12.01',
//        endTime: '2014.12.05',
//        vehicles:[
//        {
//        name: '巴士A1',
//        id  : '京CA0653',
//        driverName: '陈师傅',
//        driverPhone: '18888888888',
//        setoutTime: ['07:20', '08:00', '08:40', 09:20]
//        },
//        {
//        name: '巴士A2',
//        id  : '京CA0654',
//        driverName: '李师傅',
//        driverPhone: '19999999999',
//        setoutTime: ['07:40', '08:20', '09:00']
//        }
//        ]