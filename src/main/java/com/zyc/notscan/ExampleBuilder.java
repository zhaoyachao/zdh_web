package com.zyc.notscan;

import tk.mybatis.mapper.entity.Example;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.Collection;

/**
 * Example 查询条件构建器
 * 用于简化 tk.mapper 的 Example 查询条件构建
 *
 * 简单例子
 *             select * from push_template_info where id = xxx and template_id = xxx and is_delete = 0
 *             Example build = ExampleBuilder.forEntity(PushTemplateInfo.class)
 *                     .andNotEqualTo("id", pushTemplateInfo.getId())
 *                     .andEqualTo("template_id", pushTemplateInfo.getTemplate_id())
 *                     .andEqualTo("is_delete", Const.NOT_DELETE)
 *                     .build();
 *
 * 复杂嵌套
 *             select * from push_template_info where id = xxx and template_id = xxx and is_delete = 0  and ( content = xxx or template_name = xxx )
 *
 *             ExampleBuilder builder = ExampleBuilder.forEntity(PushTemplateInfo.class);
 *             Example build = builder
 *                     .andNotEqualTo("id", xxx)
 *                     .andEqualTo("template_id", xxx)
 *                     .andEqualTo("is_delete", Const.NOT_DELETE)
 *                     .build();
 *
 *              Criteria criteria = builder.createCriteria();
 *              builder.orEqualTo(creiteria, "content",xxx)
 *              builder.orEqualTo(creiteria, "template_name",xxx)
 *
 *              build.and(criteria);
 *
 *
 *
 */
public class ExampleBuilder<T> {
    
    private Example example;
    private Example.Criteria criteria;
    private Class<T> entityClass;
    
    /**
     * 创建 ExampleBuilder 实例
     * 复杂实例需要创建一个 ExampleBuilder 实例，然后链式调用方法构建查询条件
     */
    public static <T> ExampleBuilder<T> forEntity(Class<T> entityClass) {
        return new ExampleBuilder<>(entityClass);
    }
    
    private ExampleBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.example = new Example(entityClass);
        this.criteria = example.createCriteria();
    }


    public Example.Criteria createCriteria() {
        return example.createCriteria();
    }

    // ==================== AND 条件添加方法 ====================
    
    /**
     * 添加等于条件
     */
    public ExampleBuilder<T> andEqualTo(String property, Object value) {
        if (value != null) {
            criteria.andEqualTo(property, value);
        }
        return this;
    }
    
    /**
     * 添加不等于条件
     */
    public ExampleBuilder<T> andNotEqualTo(String property, Object value) {
        if (value != null) {
            criteria.andNotEqualTo(property, value);
        }
        return this;
    }
    
    /**
     * 添加大于条件
     */
    public ExampleBuilder<T> andGreaterThan(String property, Object value) {
        if (value != null) {
            criteria.andGreaterThan(property, value);
        }
        return this;
    }
    
    /**
     * 添加大于等于条件
     */
    public ExampleBuilder<T> andGreaterThanOrEqualTo(String property, Object value) {
        if (value != null) {
            criteria.andGreaterThanOrEqualTo(property, value);
        }
        return this;
    }
    
    /**
     * 添加小于条件
     */
    public ExampleBuilder<T> andLessThan(String property, Object value) {
        if (value != null) {
            criteria.andLessThan(property, value);
        }
        return this;
    }
    
    /**
     * 添加小于等于条件
     */
    public ExampleBuilder<T> andLessThanOrEqualTo(String property, Object value) {
        if (value != null) {
            criteria.andLessThanOrEqualTo(property, value);
        }
        return this;
    }
    
    /**
     * 添加模糊匹配条件
     */
    public ExampleBuilder<T> andLike(String property, String value) {
        if (StringUtils.isNotEmpty(value)) {
            criteria.andLike(property, "%" + value + "%");
        }
        return this;
    }
    
    /**
     * 添加 IN 条件
     */
    public ExampleBuilder<T> andIn(String property, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            criteria.andIn(property, values);
        }
        return this;
    }
    
    /**
     * 添加 IN 条件（数组形式）
     */
    public ExampleBuilder<T> andIn(String property, Object... values) {
        if (values != null && values.length > 0) {
            criteria.andIn(property, Arrays.asList(values));
        }
        return this;
    }
    
    /**
     * 添加 NOT IN 条件
     */
    public ExampleBuilder<T> andNotIn(String property, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            criteria.andNotIn(property, values);
        }
        return this;
    }
    
    /**
     * 添加 NOT IN 条件（数组形式）
     */
    public ExampleBuilder<T> andNotIn(String property, Object... values) {
        if (values != null && values.length > 0) {
            criteria.andNotIn(property, Arrays.asList(values));
        }
        return this;
    }
    
    /**
     * 添加 IS NULL 条件
     */
    public ExampleBuilder<T> andIsNull(String property) {
        criteria.andIsNull(property);
        return this;
    }
    
    /**
     * 添加 IS NOT NULL 条件
     */
    public ExampleBuilder<T> andIsNotNull(String property) {
        criteria.andIsNotNull(property);
        return this;
    }
    
    // ==================== OR 条件添加方法 ====================
    
    /**
     * 添加 OR 等于条件
     */
    public ExampleBuilder<T> orEqualTo(String property, Object value) {
        if (value != null) {
            criteria.orEqualTo(property, value);
        }
        return this;
    }

    /**
     * 添加 OR 不等于条件
     */
    public ExampleBuilder<T> orNotEqualTo(String property, Object value) {
        if (value != null) {
            criteria.orNotEqualTo(property, value);
        }
        return this;
    }
    
    /**
     * 添加 OR 模糊匹配条件
     */
    public ExampleBuilder<T> orLike(String property, String value) {
        if (StringUtils.isNotEmpty(value)) {
            criteria.orLike(property, "%" + value + "%");
        }
        return this;
    }
    
    /**
     * 添加 OR IN 条件
     */
    public ExampleBuilder<T> orIn(String property, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            criteria.orIn(property, values);
        }
        return this;
    }
    
    /**
     * 添加 OR IN 条件（数组形式）
     */
    public ExampleBuilder<T> orIn(String property, Object... values) {
        if (values != null && values.length > 0) {
            criteria.orIn(property, Arrays.asList(values));
        }
        return this;
    }

    /**
     * 添加 OR 等于条件
     */
    public ExampleBuilder<T> orEqualTo(Example.Criteria criteria, String property, Object value) {
        if (value != null) {
            criteria.orEqualTo(property, value);
        }
        return this;
    }

    /**
     * 添加 OR 不等于条件
     */
    public ExampleBuilder<T> orNotEqualTo(Example.Criteria criteria, String property, Object value) {
        if (value != null) {
            criteria.orNotEqualTo(property, value);
        }
        return this;
    }

    /**
     * 添加 OR 模糊匹配条件
     */
    public ExampleBuilder<T> orLike(Example.Criteria criteria, String property, String value) {
        if (StringUtils.isNotEmpty(value)) {
            criteria.orLike(property, "%" + value + "%");
        }
        return this;
    }

    /**
     * 添加 OR IN 条件
     */
    public ExampleBuilder<T> orIn(Example.Criteria criteria, String property, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            criteria.orIn(property, values);
        }
        return this;
    }

    /**
     * 添加 OR IN 条件（数组形式）
     */
    public ExampleBuilder<T> orIn(Example.Criteria criteria, String property, Object... values) {
        if (values != null && values.length > 0) {
            criteria.orIn(property, Arrays.asList(values));
        }
        return this;
    }
    
    // ==================== 排序方法 ====================
    
    /**
     * 添加升序排序
     */
    public ExampleBuilder<T> orderByAsc(String... properties) {
        if (properties != null && properties.length > 0) {
            StringBuilder orderByClause = new StringBuilder();
            for (int i = 0; i < properties.length; i++) {
                if (i > 0) {
                    orderByClause.append(", ");
                }
                orderByClause.append(properties[i]).append(" ASC");
            }
            example.setOrderByClause(orderByClause.toString());
        }
        return this;
    }
    
    /**
     * 添加降序排序
     */
    public ExampleBuilder<T> orderByDesc(String... properties) {
        if (properties != null && properties.length > 0) {
            StringBuilder orderByClause = new StringBuilder();
            for (int i = 0; i < properties.length; i++) {
                if (i > 0) {
                    orderByClause.append(", ");
                }
                orderByClause.append(properties[i]).append(" DESC");
            }
            example.setOrderByClause(orderByClause.toString());
        }
        return this;
    }
    
    // ==================== 构建方法 ====================
    
    /**
     * 获取构建完成的 Example 对象
     */
    public Example build() {
        return example;
    }
    
    /**
     * 获取当前的 Criteria 对象（如果需要进一步自定义）
     */
    public Example.Criteria getCriteria() {
        return criteria;
    }
    
    /**
     * 获取当前的 Example 对象（如果需要进一步自定义）
     */
    public Example getExample() {
        return example;
    }
}
