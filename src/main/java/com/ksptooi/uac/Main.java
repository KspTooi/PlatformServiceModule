package com.ksptooi.uac;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class Main {

    public static void main(String[] args) {
        Module moduleA = new ModuleA();
        Module moduleB = new ModuleB();

        Injector injector = Guice.createInjector(moduleA, moduleB);
        // 现在Module C 中的配置也会被添加到 injector 中

        // 使用 injector 进行依赖注入
    }

    static class ModuleA extends AbstractModule {
        @Override
        protected void configure() {
            // 安装 Module C
            install(new ModuleC());
            // 其他配置
        }
    }

    static class ModuleB extends AbstractModule {
        @Override
        protected void configure() {
            // 安装 Module C
            install(new ModuleC());
            // 其他配置
        }
    }

    static class ModuleC extends AbstractModule {
        @Override
        protected void configure() {
            // Module C 的配置
            System.out.println("配置ModuleC");
        }
    }
}
