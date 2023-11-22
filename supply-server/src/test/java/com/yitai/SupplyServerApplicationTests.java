package com.yitai;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class SupplyServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test01(){
        System.out.println("123");
    }
}
