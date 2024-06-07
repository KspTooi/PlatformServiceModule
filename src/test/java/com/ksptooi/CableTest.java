package com.ksptooi;

import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.AdvInputOutputPort;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.vk.VK;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CableTest {


    public static void main(String[] args) throws IOException {

        var aio = new AdvInputOutputPort(System.in,System.out,null);

        var fileAio = new AdvInputOutputPort(new FileInputStream("E://a"),new FileOutputStream("E://a"),null);

        var tCable = aio.createCable();
        var cable = aio.createCable();

        Thread.ofVirtual().start(()->{
            tCable.println("tCable输出1");
            tCable.println("tCable输出2");
            tCable.println("tCable输出3");
            tCable.flush();
        });

        cable.connect(ConnectMode.OUTPUT);
        cable.println("Hello Cable").flush();

        tCable.bindPort(fileAio);
        tCable.connect(ConnectMode.OUTPUT);
    }

}
