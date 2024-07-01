package com.ksptooi.inner;

import com.google.inject.Inject;
import com.ksptooi.psm.mapper.RequestHandlerMapper;
import com.ksptooi.psm.modes.RequestHandlerVo;
import com.ksptooi.psm.processor.EventHandler;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.processor.event.AfterVirtualTextAreaChangeEvent;
import com.ksptooi.psm.processor.event.UserTypingEvent;
import com.ksptooi.psm.shell.VirtualTextArea;
import com.ksptooi.psm.utils.aio.color.ColorfulDye;
import com.ksptooi.psm.utils.aio.color.GreenDye;

@ServiceUnit("bundled:CommandLineUpgrade")
public class CommandLineUpgradeUnit {


    private ColorfulDye dye = new ColorfulDye(115, 217, 173);

    @Inject
    private RequestHandlerMapper requestHandlerMapper;

    @EventHandler(global = true)
    public void userCompletion(AfterVirtualTextAreaChangeEvent typing){

        var cable = typing.getUserShell().getCable();
        var vt = typing.getUserShell().getVirtualTextArea();
        var query = requestHandlerMapper.query(vt.getContent().toLowerCase());

        if(query == null){
            return;
        }

        var pattern = query.getPattern().toLowerCase();

        if(pattern.equals("*")){
            return;
        }

        cable.dye(dye)
                .w("  ")
                .w(query.getPattern())
                .wash()
                .flush();

    }

}
