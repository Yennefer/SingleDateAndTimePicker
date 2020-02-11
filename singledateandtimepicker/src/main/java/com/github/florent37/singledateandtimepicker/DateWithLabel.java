package com.github.florent37.singledateandtimepicker;

import android.util.Pair;

import java.util.Date;

public class DateWithLabel extends Pair<String, Date> {

    public DateWithLabel(String first, Date second) {
        super(first, second);
        if (second == null) {
            throw new IllegalArgumentException("null value provided. " +
                    "first=[" + first + "], second=[" + second + "]");
        }
    }

    @Override
    public String toString() {
        return first;
    }
}