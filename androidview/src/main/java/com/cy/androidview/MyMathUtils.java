package com.cy.androidview;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMathUtils {
    public static final short COMPARE_EQUAL = 100;
    public static final short COMPARE_MORE = 101;
    public static final short COMPARE_LESS = 102;

    /**
     * flag = -1,表示bigdemical1小于bigdemical2；
     * flag = 0,表示bigdemical1等于bigdemical2；
     * flag = 1,表示bigdemical1大于bigdemical2；
     *
     * @param str1
     * @param str2
     */
    public static int compareDouble(String str1, String str2) {
        BigDecimal bigDecimal1 = new BigDecimal(str1);
        BigDecimal bigDecimal2 = new BigDecimal(str2);
        switch (bigDecimal1.compareTo(bigDecimal2)) {
            case -1:
                return COMPARE_LESS;
            case 0:
                return COMPARE_EQUAL;
            case 1:
                return COMPARE_MORE;
        }
        return COMPARE_EQUAL;
    }

    public static List<Integer> getDivisors(int n) {
        List<Integer> small = new ArrayList<>();
        List<Integer> large = new ArrayList<>();

        int sqrt = (int) Math.sqrt(n);
        for (int i = 1; i <= sqrt; i++) {
            if (n % i == 0) {
                small.add(i);
                if (i != n / i) { // 避免平方数重复添加
                    large.add(n / i);
                }
            }
        }
        // 合并两个列表
        Collections.reverse(large);
        small.addAll(large);
        return small;
    }
}
