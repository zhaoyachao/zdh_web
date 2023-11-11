package com.zyc.zdh.util;




import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
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
}
