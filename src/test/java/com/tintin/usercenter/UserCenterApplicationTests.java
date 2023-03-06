package com.tintin.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {


    @Test
    void regTest(){
        String regEx="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“'。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher("edas");
        if (matcher.find()){
            System.out.println("find");
        }
    }

    @Test
    void contextLoads() {
    }

}
