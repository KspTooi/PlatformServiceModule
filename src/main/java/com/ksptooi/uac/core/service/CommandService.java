package com.ksptooi.uac.core.service;

import com.google.inject.Inject;
import com.ksptooi.uac.commons.IdWorker;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.mapper.CommandMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

public class CommandService {

    private Logger logger = LoggerFactory.getLogger(CommandService.class);

    @Inject
    private CommandMapper mapper;

    private final IdWorker idWorker = new IdWorker();

    public Command getCommandByName(String name){

        Command query = new Command();
        query.setName(name);

        List<Command> commandList = mapper.getCommandList(query);

        if(commandList.size() < 1){
            return null;
        }

        return commandList.get(0);
    }

    public List<Command> getCommandByProcessorName(String processorName){

        Command query = new Command();
        query.setExecutorName(processorName);

        return mapper.getCommandList(query);
    }

    //添加指令
    public boolean insert(Command inVo){

        if(this.hasCommand(inVo.getName())){
            logger.info("尝试添加命令"+inVo.getName()+"失败,该命令已存在!");
            return false;
        }

        inVo.setCmdId(idWorker.nextId());
        inVo.setCreateTime(new Date());

        logger.info("命令\""+inVo.getName()+"\"新增成功!");
        mapper.insert(inVo);
        return true;
    }

    public boolean update(Command inVo){

        if(!this.hasCommand(inVo.getName())){
            logger.info("尝试修改命令"+inVo.getName()+"失败,该命令不存在!");
            return false;
        }

        logger.info("命令\""+inVo.getName()+"\"修改成功!");
        mapper.update(inVo);
        return true;
    }


    //数据库是否包含该指令
    public boolean hasCommand(String name){
        return this.getCommandByName(name) != null;
    }


    //移除指令
    public boolean removeById(String id){
        mapper.removeById(id);
        return true;
    }


}
