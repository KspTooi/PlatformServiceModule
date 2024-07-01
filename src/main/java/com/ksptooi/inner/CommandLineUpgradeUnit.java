package com.ksptooi.inner;

import com.google.inject.Inject;
import com.ksptooi.psm.mapper.RequestHandlerMapper;
import com.ksptooi.psm.modes.RequestHandlerVo;
import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.processor.event.AfterVirtualTextAreaChangeEvent;
import com.ksptooi.psm.processor.event.ShellInputEvent;
import com.ksptooi.psm.processor.event.UserTypingEvent;
import com.ksptooi.psm.shell.VirtualTextArea;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.color.ColorfulDye;
import com.ksptooi.psm.utils.aio.color.GreenDye;
import com.ksptooi.psm.vk.VK;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@ServiceUnit("bundled:CommandLineUpgrade")
public class CommandLineUpgradeUnit {


    private ColorfulDye dye = new ColorfulDye(97, 212, 164);

    @Inject
    private RequestHandlerMapper requestHandlerMapper;


    private final Map<String,String> completionMap = new HashMap<>();


    @EventHandler(global = true)
    public void doCompletion(ShellInputEvent e){

        var sessionId = e.getUserShell().getSessionId();

        if(e.match(VK.TAB)){

            if(completionMap.containsKey(sessionId)){
                var vt = e.getUserShell().getVirtualTextArea();
                vt.setContent(completionMap.get(sessionId));
                vt.render();
                completionMap.remove(sessionId);
            }

        }

    }


    @EventHandler(global = true)
    public void userCompletion(AfterVirtualTextAreaChangeEvent event){

        var sessionId = event.getShell().getSessionId();
        var cable = event.getUserShell().getCable();

        if(StringUtils.isBlank(event.getNewContent())){
            completionMap.remove(sessionId);
            return;
        }


        ParsedStatements ps = null;

        try {
            ps = new StatementResolver().resolve(event.getNewContent());
        } catch (StatementParsingException e) {
            return;
        }


        /**
         * 补全逻辑
         * 1.全字匹配后(用户输入Pattern与数据库中某一个模式完全一致)不再进行Pattern补全. 需根据Pattern查询数据库中的参数进行参数补全
         * 2.全字匹配无命中则首先进行顺序匹配 根据用户Pattern右匹配数据库中最短的一条模式
         * 3.顺序匹配无命中则进行"首字母匹配" 取用户Pattern第一个字符模糊查询数据库获取到以……开头的所有模式 取查询结果集模式第一个字母与每个空格后的首字母与用户Pattern匹配
         * 例: 数据库模式:uac user add. 首字母匹配用户Pattern为:uua
         *
         */

        //已全字匹配(需要匹配参数)
        if(! requestHandlerMapper.getByPattern(ps.getPattern()).isEmpty()){
            completionMap.remove(sessionId);
            return;
        }

        //顺序匹配
        var query = requestHandlerMapper.queryMany(ps.getPattern());

        //顺序匹配成功 取最短的一条模式
        if(query!=null && !query.isEmpty()){

            String selected = null;

            for(var item : query){

                var pattern = item.getPattern().toLowerCase();
                var patternLen = item.getPattern().length();

                if(selected == null){
                    selected = pattern;
                    continue;
                }
                if(patternLen < selected.length()){
                    selected = pattern;
                }
            }

            renderMatchResult(cable,selected);
            completionMap.put(sessionId,selected);
            return;
        }

        //顺序匹配失败 开始首字母匹配
        var many = requestHandlerMapper.queryMany(ps.getPattern().toLowerCase().substring(0,1));

        if(many == null || many.isEmpty()){
            completionMap.remove(sessionId);
            return;
        }

        var collect = many.stream().map(RequestHandlerVo::getPattern).toList();
        var match = firstLetterMatch(ps.getPattern(), collect);

        if(match == null){
            completionMap.remove(sessionId);
            return;
        }

        renderMatchResult(cable,match);
        completionMap.put(sessionId,match);
    }


    private void renderMatchResult(AdvInputOutputCable cable,String result){
        cable.dye(dye)
                .w("  :")
                .w(result)
                .wash()
                .flush();
    }

    private String firstLetterMatch(String text, List<String> vo){

        for(var item : vo){

            var firstLetter = new StringBuilder();
            var cs = item.toCharArray();
            var state = 0;

            for (int i = 0; i < cs.length; i++) {

                if(i == 0){
                    firstLetter.append(cs[i]);
                }

                if(cs[i] == ' '){
                    state = 1;
                    continue;
                }
                if(state == 1){
                    firstLetter.append(cs[i]);
                    state = 0;
                }

            }

            if(text.toLowerCase().contentEquals(firstLetter)){
                return item;
            }

        }

        return null;
    }

}
