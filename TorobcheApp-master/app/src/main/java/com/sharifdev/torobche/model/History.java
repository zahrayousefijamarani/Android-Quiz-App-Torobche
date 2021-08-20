package com.sharifdev.torobche.model;

import java.util.Date;

public class History {
    private Date date;
    private int point;
    private String topic;

    public History(Date date, int point, String topic) {
        this.date = date;
        this.point = point;
        this.topic = topic;
    }
}
