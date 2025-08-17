package com.cy.necessaryviewmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMathUtils {
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
