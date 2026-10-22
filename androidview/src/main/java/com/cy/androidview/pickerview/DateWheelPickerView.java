package com.cy.androidview.pickerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

import java.util.Calendar;
import java.util.Locale;

public class DateWheelPickerView extends LinearLayout {

    public interface Callback {
        void onDateSelected(int year, int month, int day, long timeMillis);
    }

    private final Calendar minDate = Calendar.getInstance();
    private final Calendar maxDate = Calendar.getInstance();
    private final Calendar selectedDate = Calendar.getInstance();

    private WheelColumn yearWheel;
    private WheelColumn monthWheel;
    private WheelColumn dayWheel;

    private Callback callback;
    private boolean updating;

    private int dividerColor = Color.parseColor("#EEEEEE");
    private int selectedTextColor = Color.parseColor("#2B2B2B");
    private int normalTextColor = Color.parseColor("#999999");
    private int selectedBackgroundColor = Color.TRANSPARENT;

    private int dividerHeight;
    private int textSize;
    private int selectedTextSize;
    private int rowHeight;

    private int dp_3;

    public DateWheelPickerView(Context context) {
        this(context, null);
    }

    public DateWheelPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateWheelPickerView(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        dp_3 = ScreenUtils.dpAdapt(getContext(), 3);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.DateWheelPickerView);

            dividerColor = a.getColor(
                    R.styleable.DateWheelPickerView_cy_dividerColor,
                    dividerColor);

            selectedTextColor = a.getColor(
                    R.styleable.DateWheelPickerView_cy_selectedTextColor,
                    selectedTextColor);

            normalTextColor = a.getColor(
                    R.styleable.DateWheelPickerView_cy_normalTextColor,
                    normalTextColor);

            selectedBackgroundColor = a.getColor(
                    R.styleable.DateWheelPickerView_cy_selectedBackgroundColor,
                    selectedBackgroundColor);

            dividerHeight = a.getDimensionPixelSize(R.styleable.DateWheelPickerView_cy_dividerHeight, ScreenUtils.dpAdapt(getContext(), 0.1f));

            textSize = a.getDimensionPixelSize(R.styleable.DateWheelPickerView_cy_textSize, ScreenUtils.dpAdapt(getContext(), 14f));

            selectedTextSize = a.getDimensionPixelSize(R.styleable.DateWheelPickerView_cy_selectedTextSize, ScreenUtils.dpAdapt(getContext(), 14f));

            rowHeight = a.getDimensionPixelSize(R.styleable.DateWheelPickerView_cy_rowHeight, ScreenUtils.dpAdapt(getContext(), 40f));

            a.recycle();
        }

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        minDate.set(1900, Calendar.JANUARY, 1, 0, 0, 0);
        maxDate.set(2100, Calendar.DECEMBER, 31, 0, 0, 0);
        normalize(minDate);
        normalize(maxDate);

        normalize(selectedDate);

        Calendar today = Calendar.getInstance();
        normalize(today);
        selectedDate.setTimeInMillis(today.getTimeInMillis());

        clampSelectedDate();

        yearWheel = createWheel();
        monthWheel = createWheel();
        dayWheel = createWheel();

        addView(yearWheel, new LayoutParams(0, ScreenUtils.dpAdapt(getContext(), 200), 1.4f));
        addView(monthWheel, new LayoutParams(0, ScreenUtils.dpAdapt(getContext(), 200), 1f));
        addView(dayWheel, new LayoutParams(0, ScreenUtils.dpAdapt(getContext(), 200), 1f));

        updateWheels(false);
    }

    private WheelColumn createWheel() {
        WheelColumn wheel = new WheelColumn(getContext());

        wheel.setColors(
                dividerColor,
                selectedTextColor,
                normalTextColor,
                selectedBackgroundColor);

        wheel.setSizes(textSize, selectedTextSize, rowHeight, dividerHeight);

        return wheel;
    }

    // ==================== 日期接口 ====================
    public void setDateRange(int year_start, int month_start, int day_start,
                             int year_end, int month_end, int day_end) {
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(year_start, month_start - 1, day_start);
        normalize(start);

        Calendar end = Calendar.getInstance();
        end.clear();
        end.set(year_end, month_end - 1, day_end);
        normalize(end);

        setDateRange(start.getTimeInMillis(), end.getTimeInMillis());
    }

    public void setDateRange(long startMillis, long endMillis) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        normalize(start);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);
        normalize(end);

        if (start.after(end)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        minDate.setTimeInMillis(start.getTimeInMillis());
        maxDate.setTimeInMillis(end.getTimeInMillis());

        clampSelectedDate();
        updateWheels(false);
    }
    public void setSelectedDate(long millis) {
        selectedDate.setTimeInMillis(millis);
        normalize(selectedDate);
        clampSelectedDate();
        updateWheels(false);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getYear() {
        return selectedDate.get(Calendar.YEAR);
    }

    public int getMonth() {
        return selectedDate.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    public long getSelectedTimeMillis() {
        return selectedDate.getTimeInMillis();
    }

    // ==================== 样式 set 方法 ====================

    public void setDividerColor(int color) {
        dividerColor = color;
        refreshStyle();
    }

    public void setSelectedTextColor(int color) {
        selectedTextColor = color;
        refreshStyle();
    }

    public void setNormalTextColor(int color) {
        normalTextColor = color;
        refreshStyle();
    }

    public void setSelectedBackgroundColor(int color) {
        selectedBackgroundColor = color;
        refreshStyle();
    }


    public int getDividerColor() {
        return dividerColor;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public int getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public float getDividerHeight() {
        return dividerHeight;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getSelectedTextSize() {
        return selectedTextSize;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    private void refreshStyle() {
        if (yearWheel == null) {
            return;
        }

        WheelColumn[] wheels = {yearWheel, monthWheel, dayWheel};

        for (WheelColumn wheel : wheels) {
            wheel.setColors(
                    dividerColor,
                    selectedTextColor,
                    normalTextColor,
                    selectedBackgroundColor);

            wheel.setSizes(textSize, selectedTextSize, rowHeight, dividerHeight);
        }
    }

    // ==================== 日期处理 ====================

    private void clampSelectedDate() {
        if (selectedDate.before(minDate)) {
            selectedDate.setTimeInMillis(minDate.getTimeInMillis());
        } else if (selectedDate.after(maxDate)) {
            selectedDate.setTimeInMillis(maxDate.getTimeInMillis());
        }
    }

    private void updateWheels(boolean notify) {
        if (yearWheel == null) {
            return;
        }

        updating = true;

        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        int minYear = minDate.get(Calendar.YEAR);
        int maxYear = maxDate.get(Calendar.YEAR);

        yearWheel.setValues(
                makeYears(minYear, maxYear),
                year - minYear,
                false);

        int minMonth = year == minYear
                ? minDate.get(Calendar.MONTH) + 1 : 1;

        int maxMonth = year == maxYear
                ? maxDate.get(Calendar.MONTH) + 1 : 12;

        month = Math.max(minMonth, Math.min(month, maxMonth));

        Calendar target = (Calendar) selectedDate.clone();
        target.set(Calendar.DAY_OF_MONTH, 1);
        target.set(Calendar.MONTH, month - 1);
        target.set(Calendar.YEAR, year);

        int maxDay = target.getActualMaximum(Calendar.DAY_OF_MONTH);
        int minDay = 1;

        if (year == minYear
                && month == minDate.get(Calendar.MONTH) + 1) {
            minDay = minDate.get(Calendar.DAY_OF_MONTH);
        }

        if (year == maxYear
                && month == maxDate.get(Calendar.MONTH) + 1) {
            maxDay = Math.min(
                    maxDay,
                    maxDate.get(Calendar.DAY_OF_MONTH));
        }

        day = Math.max(minDay, Math.min(day, maxDay));

        selectedDate.set(year, month - 1, day, 0, 0, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        String[] months = new String[maxMonth - minMonth + 1];

        for (int i = 0; i < months.length; i++) {
            months[i] = String.format(
                    Locale.getDefault(), "%02d", minMonth + i);
        }

        monthWheel.setValues(months, month - minMonth, false);

        String[] days = new String[maxDay - minDay + 1];

        for (int i = 0; i < days.length; i++) {
            days[i] = String.format(
                    Locale.getDefault(), "%02d", minDay + i);
        }

        dayWheel.setValues(days, day - minDay, false);

        yearWheel.setOnSelectedListener(new SelectedListener() {
            @Override
            public void onSelected(int index) {
                if (updating) {
                    return;
                }

                int newYear = minDate.get(Calendar.YEAR) + index;
                int oldMonth = selectedDate.get(Calendar.MONTH);
                int oldDay = selectedDate.get(Calendar.DAY_OF_MONTH);

                selectedDate.set(Calendar.DAY_OF_MONTH, 1);
                selectedDate.set(Calendar.YEAR, newYear);
                selectedDate.set(Calendar.MONTH, oldMonth);

                int lastDay = selectedDate.getActualMaximum(
                        Calendar.DAY_OF_MONTH);

                selectedDate.set(
                        Calendar.DAY_OF_MONTH,
                        Math.min(oldDay, lastDay));

                clampSelectedDate();
                updateWheels(true);
            }
        });

        monthWheel.setOnSelectedListener(new SelectedListener() {
            @Override
            public void onSelected(int index) {
                if (updating) {
                    return;
                }

                int currentYear = selectedDate.get(Calendar.YEAR);

                int minM = currentYear == minDate.get(Calendar.YEAR)
                        ? minDate.get(Calendar.MONTH) + 1 : 1;

                int newMonth = minM + index;
                int oldDay = selectedDate.get(Calendar.DAY_OF_MONTH);

                selectedDate.set(Calendar.DAY_OF_MONTH, 1);
                selectedDate.set(Calendar.MONTH, newMonth - 1);

                int lastDay = selectedDate.getActualMaximum(
                        Calendar.DAY_OF_MONTH);

                selectedDate.set(
                        Calendar.DAY_OF_MONTH,
                        Math.min(oldDay, lastDay));

                clampSelectedDate();
                updateWheels(true);
            }
        });

        dayWheel.setOnSelectedListener(new SelectedListener() {
            @Override
            public void onSelected(int index) {
                if (updating) {
                    return;
                }

                int currentYear = selectedDate.get(Calendar.YEAR);
                int currentMonth =
                        selectedDate.get(Calendar.MONTH) + 1;

                int minM = currentYear == minDate.get(Calendar.YEAR)
                        ? minDate.get(Calendar.MONTH) + 1 : 1;

                int minD = currentYear == minDate.get(Calendar.YEAR)
                        && currentMonth == minDate.get(Calendar.MONTH) + 1
                        ? minDate.get(Calendar.DAY_OF_MONTH) : 1;

                selectedDate.set(Calendar.DAY_OF_MONTH, minD + index);

                clampSelectedDate();
                updateWheels(true);
            }
        });

        updating = false;

        if (notify) {
            notifyDate();
        }
    }

    private String[] makeYears(int min, int max) {
        String[] years = new String[max - min + 1];

        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(min + i);
        }

        return years;
    }

    private void notifyDate() {
        if (callback != null) {
            callback.onDateSelected(
                    getYear(),
                    getMonth(),
                    getDay(),
                    getSelectedTimeMillis());
        }
    }

    private void normalize(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private interface SelectedListener {
        void onSelected(int index);
    }

    // ==================== 日期滚轮 ====================

    private class WheelColumn extends View {

        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private String[] values = new String[0];
        private int selectedIndex;

        private float row;
        private float offset;
        private float downY;
        private float lastY;
        private float downX;
        private boolean dragging;

        private VelocityTracker velocityTracker;
        private SelectedListener listener;
        private ValueAnimator animator;

        private int lineColor = Color.LTGRAY;
        private int selectedColor = Color.BLACK;
        private int normalColor = Color.GRAY;
        private int selectionBackground = Color.TRANSPARENT;

        private float normalSize;
        private float selectedSize;
        private float lineHeight;

        WheelColumn(Context context) {
            super(context);
        }

        void setColors(int line, int selected, int normal,
                       int background) {
            lineColor = line;
            selectedColor = selected;
            normalColor = normal;
            selectionBackground = background;
            invalidate();
        }

        void setSizes(float normal, float selected,
                      float rowHeight, float divider) {
            normalSize = normal;
            selectedSize = selected;
            row = rowHeight;
            lineHeight = divider;
            invalidate();
        }

        void setValues(String[] values, int index, boolean notify) {
            cancelAnimation();

            this.values = values == null
                    ? new String[0] : values.clone();

            selectedIndex = clamp(index);
            offset = 0;
            invalidate();

            if (notify && listener != null) {
                listener.onSelected(selectedIndex);
            }
        }

        void setOnSelectedListener(SelectedListener listener) {
            this.listener = listener;
        }

        private int clamp(int index) {
            if (values.length == 0) {
                return 0;
            }

            return Math.max(0, Math.min(index, values.length - 1));
        }

        private void cancelAnimation() {
            if (animator != null) {
                animator.cancel();
                animator = null;
            }
        }

        private void settle(int target) {
            cancelAnimation();

            final int targetIndex = clamp(target);
            final float targetOffset =
                    (selectedIndex - targetIndex) * row;
            final float startOffset = offset;
            final float distance =
                    Math.abs(targetOffset - startOffset);

            if (distance < 1f) {
                selectedIndex = targetIndex;
                offset = 0;
                invalidate();

                if (listener != null) {
                    listener.onSelected(selectedIndex);
                }
                return;
            }

            long duration = (long) Math.max(
                    180, Math.min(650, 180 + distance * 0.30f));

            animator = ValueAnimator.ofFloat(
                    startOffset, targetOffset);

            animator.setDuration(duration);
            animator.setInterpolator(
                    new DecelerateInterpolator(2.0f));

            animator.addUpdateListener(
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(
                                ValueAnimator animation) {
                            offset = (float) animation.getAnimatedValue();
                            invalidate();
                        }
                    });

            animator.addListener(new AnimatorListenerAdapter() {
                private boolean cancelled;

                @Override
                public void onAnimationCancel(Animator animation) {
                    cancelled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (cancelled) {
                        return;
                    }

                    selectedIndex = targetIndex;
                    offset = 0;
                    animator = null;
                    invalidate();

                    if (listener != null) {
                        listener.onSelected(selectedIndex);
                    }
                }
            });

            animator.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (values.length == 0 || row <= 0) {
                return;
            }

            float centerY = getHeight() / 2f;
            float centerX = getWidth() / 2f;

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(selectionBackground);

            canvas.drawRect(
                    0, centerY - row / 2,
                    getWidth(), centerY + row / 2, paint);

            paint.setColor(lineColor);
            paint.setStrokeWidth(lineHeight);

            canvas.drawLine(
                    0, centerY - row / 2,
                    getWidth(), centerY - row / 2, paint);

            canvas.drawLine(
                    0, centerY + row / 2,
                    getWidth(), centerY + row / 2, paint);

            paint.setTextAlign(Paint.Align.CENTER);

            for (int i = 0; i < values.length; i++) {
                float y = centerY
                        + (i - selectedIndex) * row + offset;

                if (y < -row || y > getHeight() + row) {
                    continue;
                }

                boolean selected = i == selectedIndex
                        && Math.abs(offset) < row / 2;

                paint.setColor(
                        selected ? selectedColor : normalColor);

                paint.setTextSize(
                        selected ? selectedSize : normalSize);

                paint.setAlpha(selected ? 255 : Math.max(
                        50, 180 - (int) (
                                Math.abs(y - centerY) / row * 45)));

                Paint.FontMetrics fm = paint.getFontMetrics();

                float baseline = y
                        - (fm.ascent + fm.descent) / 2;

                canvas.drawText(
                        values[i], centerX, baseline, paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    cancelAnimation();

                    downY = lastY = event.getY();
                    downX = event.getX();
                    dragging = false;

                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                    }

                    velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);

                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(
                                true);
                    }
                    return true;

                case MotionEvent.ACTION_MOVE:
                    if (velocityTracker != null) {
                        velocityTracker.addMovement(event);
                    }

                    float y = event.getY();
                    float dy = y - lastY;

                    float totalY = Math.abs(y - downY);
                    float totalX = Math.abs(event.getX() - downX);

                    if (totalY > dp_3 && totalY > totalX) {
                        dragging = true;

                        if (getParent() != null) {
                            getParent()
                                    .requestDisallowInterceptTouchEvent(
                                            true);
                        }
                    }

                    if (dragging && row > 0) {
                        offset += dy;

                        while (offset >= row) {
                            if (selectedIndex <= 0) {
                                selectedIndex = 0;
                                offset = 0;
                                break;
                            }

                            offset -= row;
                            selectedIndex--;
                        }

                        while (offset <= -row) {
                            if (selectedIndex >= values.length - 1) {
                                selectedIndex = Math.max(
                                        0, values.length - 1);
                                offset = 0;
                                break;
                            }

                            offset += row;
                            selectedIndex++;
                        }

                        invalidate();
                    }

                    lastY = y;
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    boolean cancelled = event.getActionMasked()
                            == MotionEvent.ACTION_CANCEL;

                    float velocityY = 0;

                    if (velocityTracker != null) {
                        velocityTracker.addMovement(event);
                        velocityTracker.computeCurrentVelocity(1000);

                        if (!cancelled) {
                            velocityY = velocityTracker.getYVelocity();
                        }

                        velocityTracker.recycle();
                        velocityTracker = null;
                    }

                    if (row > 0 && dragging && !cancelled) {
                        float projectedOffset =
                                offset + velocityY * 0.18f;

                        int target = selectedIndex
                                - Math.round(projectedOffset / row);

                        target = Math.max(
                                selectedIndex - 12,
                                Math.min(selectedIndex + 12, target));

                        target = clamp(target);
                        settle(target);
                    } else {
                        int target = row > 0
                                ? selectedIndex - Math.round(offset / row)
                                : selectedIndex;

                        settle(target);
                    }

                    dragging = false;

                    if (getParent() != null) {
                        getParent()
                                .requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
            }

            return true;
        }
    }
}