package ru.mikheev.kirill.secure;

/**
 * @author Kirill Mikheev
 * @version 1.0
 */

public class Secure {

    private static int _linesDensity = 1;

    private static int GetMaxDefense_DefaultOrDense(int[][] cityMap) {
        int maxSum = Integer.MIN_VALUE;

        for (int i = 1; i < cityMap.length - 1; i++) {
            int emergnSum = 0;

            for(int j = 0; j < cityMap[i].length; j++) {
                if(j == 0 || j %3 ==0) {
                    emergnSum += cityMap[j][i];
                }else {
                    emergnSum += cityMap[j][i - 1];
                    emergnSum += cityMap[j][i + 1];
                }
            }
            if(emergnSum > maxSum) {
                maxSum = emergnSum;
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[][] map = {
                {5,7,7,5,5,6,6},
                {2,2,3,3,1,8,9},
                {7,8,4,6,3,8,5},
                {4,9,9,2,7,6,1},
                {9,2,7,1,9,0,1},
                {3,1,9,7,8,5,2},
                {9,6,5,2,8,3,6}
        };

        System.out.println(GetMaxDefense_DefaultOrDense(map));
    }
}
