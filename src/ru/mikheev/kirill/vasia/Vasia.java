package ru.mikheev.kirill.vasia;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Kirill Mikheev
 * @version 1.0
 */

public class Vasia {
    public static void main (String[] args) throws java.lang.Exception
    {
        BufferedReader r = new BufferedReader (new InputStreamReader(System.in));
        String s;
        s=r.readLine(); //получаем строку
        r.close();
        String answer = "";
        int curr = 0;
        char[] seq = {'I', 'B', 'S'};
        int num = 0;
        for (char ch : s.toCharArray()) {
            if(ch == seq[curr]) {
                if(curr == 0 && num == 0) {
                    num++;
                }
                answer += ch;
                continue;
            }
            if(curr > 0 && ch == seq[curr-1]){
                System.out.println(0);
                return;
            }
            if(curr < seq.length-1 && ch == seq[curr+1]) {
                answer += ch;
                curr++;
                num++;
                continue;
            }

        }
        if(num < seq.length) {
            System.out.println(0);
            return;
        }
        System.out.println(answer); //в качестве ответа возвращаем строку
    }
}
