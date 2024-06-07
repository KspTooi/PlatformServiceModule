package com.ksptooi;

import com.ksptooi.psm.utils.aio.AdvInputOutputStream;

import java.io.*;
import java.nio.file.Files;

public class AioJoinTest {

    public static void main(String[] args) throws IOException {

        File fa = new File("E://a");
        File fb = new File("E://b");

        var fia = Files.newInputStream(fa.toPath());
        var foa = Files.newOutputStream(fa.toPath());

        var fib = Files.newInputStream(fb.toPath());
        var fob = Files.newOutputStream(fb.toPath());

        var aio1 = new AdvInputOutputStream(fia,foa,null);
        var aio2 = new AdvInputOutputStream(fib,fob,null);

        //使用AIO1创建sub
        var subStream = aio1.createSubStream();
        subStream.attachOutput(); //挂载输出
        subStream.println("test1").flush();

        //关闭AIO1
        aio1.destroy();

        //热迁移subStream到AIO2
        aio2.joinSubStream(subStream);
        subStream.attachOutput();//挂载输出
        subStream.println("test2").flush();
    }



}
