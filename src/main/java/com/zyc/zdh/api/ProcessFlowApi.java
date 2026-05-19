package com.zyc.zdh.api;

import com.zyc.zdh.controller.ZdhProcessFlowController;
import com.zyc.zdh.dao.ApprovalEventMapper;
import com.zyc.zdh.dao.PermissionMapper;
import com.zyc.zdh.dao.ProcessFlowMapper;
import com.zyc.zdh.dao.ProductTagMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.SessionDao;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.Encrypt;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * api审批流服务
 *
 * @author zyc-admin
 * @date 2022年6月11日
 * @Description: api包下的服务 不需要通过shiro验证拦截，需要自定义的token验证, 通过此api 提供外部平台权限验证
 */
@Controller("processFlowApi")
@RequestMapping("api")
public class ProcessFlowApi {

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private ZdhProcessFlowController zdhProcessFlowController;
    @Autowired
    private ProductTagMapper productTagMapper;
    @Autowired
    private ApprovalEventMapper approvalEventMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private ProcessFlowMapper processFlowMapper;


    /**
     * 测试流程审批回调
     * 外部系统调用审批流,可自实现回调接口
     * @param processFlowInfo
     * @param request
     * @return
     */
    @RequestMapping(value = "call_back_test", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo call_back_test(@RequestBody ProcessFlowInfo processFlowInfo, HttpServletRequest request) {
        try{
            System.out.println(JsonUtil.formatJsonString(request.getParameterMap()));
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "审批流回调成功", processFlowInfo);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "审批流回调失败", e.getMessage());
        }
    }

    /**
     * 创建审批流
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "create_process_by_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo create_process(String user_account, String event_code,String event_context,String event_id,String product_code,String ak, String sk) {

        try{
            check_aksk(product_code,ak,sk);

            //检查event_code 是否合法
            ApprovalEventInfo aei=new ApprovalEventInfo();
            aei.setProduct_code(product_code);
            aei.setEvent_code(event_code);
            aei = approvalEventMapper.selectOne(aei);
            if(aei == null){
                throw new Exception("event_code无效,请检查event_code是否正确,若无event_code可在审批管理->事件配置模块增加相应事件");
            }

            //校验账号是否有效
            PermissionUserInfo pui=new PermissionUserInfo();
            pui.setUser_account(user_account);
            pui.setProduct_code(product_code);
            pui=permissionMapper.selectOne(pui);
            if(pui == null){
                throw new Exception("user_account无效,请检查user_account是否正确,若无user_account可在权限管理->用户配置模块增加用户信息");
            }
            if(StringUtils.isEmpty(pui.getUser_group())){
                throw new Exception("无法找到用户对应的组信息,请检查权限管理->用户配置模块中,用户组信息是否未配置");
            }

            //判断是否跳过审批
            //获取事件配置,校验是否跳过
            Example example1=new Example(ApprovalEventInfo.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("event_code", event_code);
            criteria1.andEqualTo("product_code", product_code);
            List<ApprovalEventInfo> approvalEventInfos = approvalEventMapper.selectByExample(example1);
            if(approvalEventInfos!=null && approvalEventInfos.size()>0){
                if(Arrays.asList(approvalEventInfos.get(0).getSkip_account().split(",")).contains(user_account)){
                    return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功,且跳过审批", "skip");
                }
            }

            String flow_id = zdhProcessFlowController.createProcess(user_account,pui.getUser_group(),event_code, event_context, event_id, product_code);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "创建审批流成功,返回审批流ID", flow_id);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "创建审批流失败", e.getMessage());
        }
    }

    /**
     * 获取审批流信息
     * @param user_account 用户账号
     * @param flow_id 审批流ID
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "process_detail_by_flow_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo process_detail_by_flow_id(String user_account,String flow_id,String product_code,String ak, String sk) {
        try {
            check_aksk(product_code,ak,sk);
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByFlowId(flow_id, user_account);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取流程进度", pfis);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "获取流程进度", e);
        }

    }

    /**
     * 审批流-操作,审批,不通过,撤销
     * @param id 流程ID
     * @param status 状态 0:未审批,1:审批完成,2:不通过,3:撤销
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "process_status_by_flow_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo process_flow_status(String id, String status, String product_code,String ak, String sk) {
        ProcessFlowInfo pfi_old = processFlowMapper.selectByPrimaryKey(id);
        processFlowMapper.updateStatus(id, status);
        if (status.equalsIgnoreCase(Const.STATUS_SUCCESS)) {
            //设置下游流程可展示
            processFlowMapper.updateIsShow(id, Const.SHOW);
        }
        //最后一个审批,表示审批流程结束,用户决定审批完成的动作(other_handle 状态给调用方使用,每次用户触发审批,需要检查other_handle),此处不加回调,因当前属于同步操作,用户可直接感知到审批结果
        ProcessFlowInfo processFlowInfo = processFlowMapper.selectByPrimaryKey(id);
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "审批", processFlowInfo);
    }


    private void check_user(PermissionUserInfo permissionUserInfo) throws Exception {
        if(permissionUserInfo == null){
            throw new Exception("用户信息为空");
        }

        if(StringUtils.isEmpty(permissionUserInfo.getUser_account())){
            throw new Exception("用户账户为空");
        }
        if(StringUtils.isEmpty(permissionUserInfo.getUser_name())){
            throw new Exception("用户名为空");
        }

    }


    /**
     * ak,sk 结构md5_秒级时间戳
     * @param product_code
     * @param ak
     * @param sk
     * @throws Exception
     */
    private void check_aksk(String product_code,String ak, String sk) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品为空");
        }
        if(StringUtils.isEmpty(ak)){
            throw new Exception("AK为空");
        }
        if(StringUtils.isEmpty(sk)){
            throw new Exception("SK为空");
        }

        //解密ak,sk ,根据时间
        String akdec = Encrypt.AESdecrypt(ak);
        String[] aks = akdec.split("_");
        if(aks.length!=2 || (System.currentTimeMillis()/1000 - Integer.parseInt(aks[1])) <= 300 ){
            throw new Exception("AK异常");
        }

        String skdec = Encrypt.AESdecrypt(ak);
        String[] sks = skdec.split("_");
        if(sks.length!=2 || (System.currentTimeMillis()/1000 - Integer.parseInt(sks[1])) <= 300  ){
            throw new Exception("SK异常");
        }

        //验证ak,sk
        ProductTagInfo productTagInfo=new ProductTagInfo();
        productTagInfo.setProduct_code(product_code);
        productTagInfo.setAk(aks[0]);
        productTagInfo.setSk(sks[0]);
        productTagInfo.setStatus(Const.PRODUCT_ENABLE);
        ProductTagInfo pti = productTagMapper.selectOne(productTagInfo);
        if(pti == null){
            throw new Exception("无效的ak/sk,请确认产品与ak/sk是否匹配");
        }
    }

    /**
     * 验证token 是否有效
     * @param token
     * @return
     */
    private boolean valid(String token) {
        try {
            Session session = sessionDao.readSession(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
