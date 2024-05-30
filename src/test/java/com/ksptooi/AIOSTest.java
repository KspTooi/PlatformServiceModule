package com.ksptooi;

import com.ksptooi.psm.vk.AdvancedInputOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AIOSTest {



    @Test
    public void threadToggle() throws InterruptedException {

        byte[] b = new byte[8192000];

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1,2,3,4,5,6,7,8,9,10});
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);

        AdvancedInputOutputStream aios = new AdvancedInputOutputStream(bais,baos,null);

        Thread.ofVirtual().start(()->{

            try {
                while (true){
                    Thread.sleep(1000);
                    aios.print("test");
                    aios.flush();
                    System.out.println(Thread.currentThread().threadId()+":"+"写入");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Scanner scanner = new Scanner(System.in);

        while (true){
            Thread.sleep(1000);
            aios.print("test");
            System.out.println(Thread.currentThread().threadId()+":"+"写入");
        }

    }


}
