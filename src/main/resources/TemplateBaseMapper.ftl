package ${BaseMapperPackage};

import com.zyc.notscan.BaseMapper;

public interface ${BaseMapperName}<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "${tableName}";
    }
}