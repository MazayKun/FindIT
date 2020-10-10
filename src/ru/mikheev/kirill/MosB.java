package ru.mikheev.kirill;

import java.util.Scanner;

/**
 * @author Kirill Mikheev
 * @version 1.0
 */

public class MosB {

    /**
     * На основе переданных данных вычисляет ответ
     * @param number исходное число
     * @param res исходная система
     * @param target целевая система
     * @return значение number в целевой системе
     */
    private static String calc(String number, int res, int target) {
        if( res == target ) {
            return number;
        }
        String answer = "";
        char[] chars = number.toCharArray();
        long sum = 0;
        int power = 0;
        int tmp;
        for (int i = number.length() - 1; i >= 0; i--) {
            tmp = 0;
            if(chars[i] >= '0' && chars[i] <= '9') {
                tmp = chars[i] - '0';
            }else {
                tmp = 10 + (chars[i] - 'a');
            }
            sum += tmp * Math.pow(res, power);
            power++;
        }
        while(sum >= target) {
            answer = (sum % target > 9 ? 'a' + (sum % target) - 10 : sum % target) + answer;
            sum = sum / target;
        }
        answer = (sum > 9 ? 'a' + sum - 10 : sum)  + answer;
        return answer;
    }

    /**
     * Валидирует данные по условию задачи
     * @return true, если все верно, иначе false
     */
    public static boolean isValid(String number, int res, int target) {
        if (res < 2 || res > 32) {
            System.out.println("Неправильная исходная система");
            return false;
        }
        if (target < 2 || target > 32) {
            System.out.println("Неправильная целевая система");
            return false;
        }
        for (char ch : number.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                if (ch - '0' >= res ) {
                    System.out.println("Недопустимое значение для данной системы - " + ch);
                    return false;
                }
            }
            if(ch >= 'a') {
                if(10 + (ch - 'a') >= res) {
                    System.out.println("Недопустимое значение для данной системы - " + ch);
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите основание исходной системы");
        int a = in.nextInt();

        System.out.println("Введите основание целевой системы");
        int b = in.nextInt();

        System.out.println("Введите число, которое нужно перевести");
        String x = in.next();

        if (!isValid(x, a, b)) {
            return;
        }

        System.out.print("Результат - ");
        System.out.println(calc(x, a, b));
    }
}
