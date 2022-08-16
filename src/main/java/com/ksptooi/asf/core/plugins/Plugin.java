package com.ksptooi.asf.core.plugins;


public interface Plugin {

    //插件初始化时执行
    public void onEnabled();

    //插件卸载时执行
    public void onDisable();

}
