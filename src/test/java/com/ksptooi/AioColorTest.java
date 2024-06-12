package com.ksptooi;

import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.AdvInputOutputPort;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.utils.aio.color.*;
import org.junit.Test;

public class AioColorTest {


    public static void main(String[] args) {

        AdvInputOutputPort port = new AdvInputOutputPort(System.in,System.out,null);
        var cable = port.createCable();
        cable.connect(ConnectMode.OUTPUT);

        var colorfulDye = new ColorfulDye(49,65,170);

        cable.dye(RedDye.pickUp)
                .w("H").dye(OrangeDye.pickUp)
                .w("e").dye(YellowDye.pickUp)
                .w("l").dye(GreenDye.pickUp)
                .w("l").dye(CyanDye.pickUp)
                .w("o").dye(colorfulDye)
                .w(" world").wash().flush();

        cable.flush();

    }


}
