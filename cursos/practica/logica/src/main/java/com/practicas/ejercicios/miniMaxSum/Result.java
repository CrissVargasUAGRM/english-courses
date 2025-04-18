/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.ejercicios.miniMaxSum;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Result {
    /*
     * Complete the 'miniMaxSum' function below.
     *
     * The function accepts INTEGER_ARRAY arr as parameter.
     */

    public static void miniMaxSum(List<Integer> arr) {
    // Write your code here
        Long max = 0L;
        Long min = 0L;
        Long sum = 0L;
        
        List<Long> result = new ArrayList<>();
        Integer pos = 0;
        
        while (pos < arr.size()) {
            for (Integer i = 0; i < arr.size(); i++) {
                if(!pos.equals(i)){
                    sum = sum + arr.get(i);
                }
            }
            result.add(sum);
            sum = 0L;
            pos = pos + 1;
        }
        max = result.get(0);
        min = result.get(0);
        for (Long element : result) {
            if(element > max){
                max = element;
            }
            if(element < min){
                min = element;
            }
        }
        System.out.println(min + " " + max);
    }

}
