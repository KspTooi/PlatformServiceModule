package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.entities.PreparedCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandDispatcher implements CommandScheduler {


    private final List<Listener> listenerList = new ArrayList<>();


    @Override
    public boolean addListener(String listenerName, Listener listener) {
        listener.setName(listenerName);
        this.listenerList.add(listener);
        return true;
    }


    @Override
    public void publish(PreparedCommand command) {

        for(Listener item:this.listenerList){
            item.onCommand(command);
        }

    }

}
