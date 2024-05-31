package com.ksptooi;

import com.ksptooi.psm.vk.AdvInputOutputStream;
import com.ksptooi.psm.vk.VK;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

public class AIOSTest {

    public static void main(String[] args) throws IOException {

        AIOSTest test = new AIOSTest();
        test.subStreamTest();

    }

    @Test
    public void subStreamTest() throws IOException {

        AdvInputOutputStream aios = new AdvInputOutputStream(System.in,System.out,null);

        //创建新线程
        Thread.ofVirtual().start(()->{
            AdvInputOutputStream sub = aios.createSub();
            sub.attachInput();
            sub.attachOutput();

            while (true){
                try {
                    sub.read();
                    if(sub.match(VK.USER_INPUT)){
                        System.out.println("[SUB]用户输入长度:"+sub.getReadLen());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        while (true){

            aios.println("AAA").flush();
            aios.read();

            if(aios.match(VK.USER_INPUT)){
                System.out.println("用户输入长度:"+aios.getReadLen());
            }

        }


    }







    @Test
    public void outTest() throws IOException {

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream(1);
        BufferedOutputStream bos = new BufferedOutputStream(baos1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1);
        PrintWriter w = new PrintWriter(baos);

        for (int i = 0; i < 2048; i++) {
            w.print("HLOW");
            w.flush();
        }

        baos.writeTo(bos);
        baos.reset();

        System.out.println(baos1.toString());
    }



    @Test
    public void threadToggle() throws InterruptedException {

        byte[] b = new byte[8192000];

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1,2,3,4,5,6,7,8,9,10});
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);

        AdvInputOutputStream aios = new AdvInputOutputStream(bais,baos,null);

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
