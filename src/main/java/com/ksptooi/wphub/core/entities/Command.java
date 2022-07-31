package com.ksptooi.wphub.core.entities;

import java.util.Date;

public class Command {

    //命令ID
    private Long cmdId;

    //命令名称
    private String name;

    //该命令所属执行器的名称
    private String executorName;

    //命令元数据
    private String metadata;

    //创建时间
    private Date createTime;

    //描述
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getCmdId() {
        return cmdId;
    }

    public void setCmdId(Long cmdId) {
        this.cmdId = cmdId;
    }

    @Override
    public String toString() {
        return "Command{" +
                "cmdId=" + cmdId +
                ", name='" + name + '\'' +
                ", executorName='" + executorName + '\'' +
                ", metadata='" + metadata + '\'' +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                '}';
    }
}
