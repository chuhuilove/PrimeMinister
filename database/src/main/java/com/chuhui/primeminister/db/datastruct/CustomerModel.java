package com.chuhui.primeminister.db.datastruct;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * CustomerModel
 *
 * @author: 纯阳子
 * @Date: 2019/7/16 0016
 * @Description:TODO
 */
@Getter
@Setter
public class CustomerModel {

    int a;
    int b;
    int c;
    int d;


    byte e;


    public static void main(String[] args) {


        CustomerModel customerModel = new CustomerModel();


        System.err.println(customerModel);
        System.err.println(customerModel);
        System.err.println(customerModel);


    }



}
