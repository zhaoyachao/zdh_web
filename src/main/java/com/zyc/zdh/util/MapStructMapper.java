package com.zyc.zdh.util;

import com.zyc.zdh.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

    /**
     * User 转 Person
     * 只需要定义这个接口就可以，然后直接调用，不需要手动去实现
     */
    TaskGroupLogInstance quartzJobInfoToTaskGroupLogInstance(QuartzJobInfo quartzJobInfo);

    TaskLogInstance taskGroupLogInstanceToTaskLogInstance(TaskGroupLogInstance taskGroupLogInstance);

    StrategyGroupInstance strategyGroupInfoToStrategyGroupInstance(StrategyGroupInfo strategyGroupInfo);

    StrategyInstance strategyGroupInstanceToStrategyInstance(StrategyGroupInstance strategyGroupInstance);

    EtlTaskInfo etlTaskBatchInfoToEtlTaskInfo(EtlTaskBatchInfo etlTaskBatchInfo);

    PermissionUserDimensionValueInfo permissionUserGroupDimensionValueInfoToPermissionUserDimensionValueInfo(PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo);

    ServerTaskInstance serverTaskInfoToServerTaskInstance(ServerTaskInfo serverTaskInfo);

}
