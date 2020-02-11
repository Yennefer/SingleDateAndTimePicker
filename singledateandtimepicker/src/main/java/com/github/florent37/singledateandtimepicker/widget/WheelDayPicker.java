package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateWithLabel;
import com.github.florent37.singledateandtimepicker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.DAYS_PADDING;

public class WheelDayPicker extends WheelPicker<DateWithLabel> {

    private static final String DAY_FORMAT_PATTERN = "EEE d MMM";

    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat customDateFormat;

    private OnDaySelectedListener onDaySelectedListener;

    private DateWithLabel today;

    public WheelDayPicker(Context context) {
        super(context);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat(DAY_FORMAT_PATTERN, getCurrentLocale());
        today = new DateWithLabel(getTodayText(), new Date());
    }

    @Override
    public void setCustomLocale(Locale customLocale) {
        super.setCustomLocale(customLocale);
        simpleDateFormat = new SimpleDateFormat(DAY_FORMAT_PATTERN, getCurrentLocale());
    }

    @Override
    protected DateWithLabel initDefault() {
        return today;
    }

    @NonNull
    private String getTodayText() {
        return getLocalizedString(R.string.picker_today);
    }

    @Override
    protected void onItemSelected(int position, DateWithLabel item) {
        if (onDaySelectedListener != null) {
            onDaySelectedListener.onDaySelected(this, position, item.first, item.second);
        }
    }

    @Override
    protected List<DateWithLabel> generateAdapterValues() {
        final List<DateWithLabel> days = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1 * DAYS_PADDING - 1);
        for (int i = (-1) * DAYS_PADDING; i < 0; ++i) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            instance.set(Calendar.MILLISECOND, 0);
            instance.set(Calendar.SECOND, 0);
            Date date = instance.getTime();
            days.add(new DateWithLabel(getFormattedValue(date), date));
        }

        //today
        days.add(today);

        instance = Calendar.getInstance();

        for (int i = 0; i < DAYS_PADDING; ++i) {
            instance.add(Calendar.DATE, 1);
            instance.set(Calendar.MILLISECOND, 0);
            instance.set(Calendar.SECOND, 0);
            Date date = instance.getTime();
            days.add(new DateWithLabel(getFormattedValue(date), date));
        }

        return days;
    }

    protected String getFormattedValue(Object value) {
        return getDateFormat().format(value);
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat){
        this.customDateFormat = simpleDateFormat;
        updateAdapter();
        return this;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Date getCurrentDate() {
        return adapter.getItem(super.getCurrentItemPosition()).second;
    }

    private SimpleDateFormat getDateFormat() {
        if (customDateFormat != null) {
            return customDateFormat;
        }
        return simpleDateFormat;
    }

    public void setTodayText(String todayText) {
        int index = adapter.getData().indexOf(getTodayText());
        if (index != -1) {
            today = new DateWithLabel(todayText, new Date());
            adapter.getData().set(index, today);
            notifyDatasetChanged();
        }
    }

    public int getTodayItemPosition() {
        return adapter.getData().indexOf(today);
    }

    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }
}