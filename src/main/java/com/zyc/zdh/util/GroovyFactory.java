package com.zyc.zdh.util;




import com.google.common.collect.Lists;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.codehaus.groovy.runtime.InvokerHelper;

import javax.script.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 执行groovy规则工厂
 *
 * 当前工厂未实现缓存功能,后续可通过缓存方向进行性能优化
 */
public class GroovyFactory {


    /**
     * 执行groovy 脚本, 使用groovy 语法
     * @param script
     * @param params
     * @return
     * @throws ScriptException
     */
    public static Object execExpress(String script, Map<String,Object> params) throws ScriptException {
        GroovyScriptEngineFactory scriptEngineFactory = new GroovyScriptEngineFactory();
        ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
        Bindings bindings = scriptEngine.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
        if(params != null && params.size()>0){
            for (String key: params.keySet()){
                bindings.put(key, params.get(key));
            }
        }
        return scriptEngine.eval(script);
    }

    public static Object execExpress(String script, String function_name, Map<String,Object> params) throws ScriptException, NoSuchMethodException {
        GroovyScriptEngineFactory scriptEngineFactory = new GroovyScriptEngineFactory();
        ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
        Bindings bindings = scriptEngine.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
        List<String> list = new ArrayList<>();

        if(params != null && params.size()>0){
            for (String key: params.keySet()){
                bindings.put(key, params.get(key));
                list.add(params.get(key).toString());
            }
        }
        scriptEngine.eval(script);
        return ((Invocable)scriptEngine).invokeFunction(function_name, list.toArray());
    }

}
