package com.ksptooi.psm.configset;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.ConfigSetMapper;
import jakarta.inject.Inject;
import org.h2.util.StringUtils;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Unit
public class ConfigSet {

    @Inject
    private ConfigSetMapper mapper;

    @Inject
    private Snowflake snowflake;


    public ConfigSetVo create(String key,String def){
        var i = new ConfigSetVo();
        i.setId(snowflake.nextId());
        i.setKey(key);
        i.setVal(def);
        i.setDef(def);
        i.setUserId(-1L);
        i.setDescription(null);
        i.setCreateTime(new Date());
        mapper.insert(i);
        return i;
    }

    public void remove(String key){

    }

    public String valOf(String key){

        List<ConfigSetVo> byKey = mapper.getByKey(key);

        if(byKey == null || byKey.isEmpty()){
            return null;
        }

        var data = byKey.get(0);

        if(StringUtils.isNullOrEmpty(data.getVal())){
            return data.getDef();
        }

        return data.getVal();
    }

    public String valOf(String key,String def){

        String val = valOf(key);

        if(StringUtils.isNullOrEmpty(val)){
            var i = create(key, def);
            return i.getDef();
        }

        return val;
    }

    public List<String> valsOf(String key){

        var vo = mapper.getByKey(key);

        if(vo == null || vo.isEmpty()){
            return null;
        }

        return vo.stream().map(ConfigSetVo::getVal).toList();
    }

    public List<String> valsOf(String key,String def){

        var vo = mapper.getByKey(key);

        if(vo == null || vo.isEmpty()){
            var i = create(key, def);
            return Collections.singletonList(i.getDef());
        }

        return vo.stream().map(ConfigSetVo::getVal).toList();
    }


}
