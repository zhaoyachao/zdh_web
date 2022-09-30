let api = [];
api.push({
    alias: 'api',
    order: '1',
    desc: 'api登录服务',
    link: 'api登录服务',
    list: []
})
api[0].list.push({
    order: '1',
    desc: '登录',
});
api[0].list.push({
    order: '2',
    desc: '测试登录',
});
api[0].list.push({
    order: '3',
    desc: '退出',
});
api[0].list.push({
    order: '4',
    desc: '已废弃',
});
api.push({
    alias: 'PermissionApi',
    order: '2',
    desc: 'api权限服务',
    link: 'api权限服务',
    list: []
})
api[1].list.push({
    order: '1',
    desc: '申请产品 获取ak,sk, 暂未实现',
});
api[1].list.push({
    order: '2',
    desc: '新增用户',
});
api[1].list.push({
    order: '3',
    desc: '更新用户信息',
});
api[1].list.push({
    order: '4',
    desc: '启用/禁用用户',
});
api[1].list.push({
    order: '5',
    desc: '批量启用/禁用用户',
});
api[1].list.push({
    order: '6',
    desc: '获取用户信息',
});
api[1].list.push({
    order: '7',
    desc: '批量获取用户信息',
});
api[1].list.push({
    order: '8',
    desc: '获取产品下所有用户',
});
api[1].list.push({
    order: '9',
    desc: '新增用户组',
});
api[1].list.push({
    order: '10',
    desc: '增加角色',
});
api[1].list.push({
    order: '11',
    desc: '禁用/启用 角色',
});
api[1].list.push({
    order: '12',
    desc: '角色增加资源  tips: 每次角色增加资源以全量方式增加,会提前删除当前角色下的资源配置',
});
api[1].list.push({
    order: '13',
    desc: '根据role_code 获取角色',
});
api[1].list.push({
    order: '14',
    desc: '获取产品线下所有角色',
});
api[1].list.push({
    order: '15',
    desc: '获取角色下的用户列表',
});
api[1].list.push({
    order: '16',
    desc: '新增资源',
});
api[1].list.push({
    order: '17',
    desc: '批量增加资源',
});
api[1].list.push({
    order: '18',
    desc: '通过用户账户 获取资源信息',
});
api[1].list.push({
    order: '19',
    desc: '通过角色code获取资源',
});
api[1].list.push({
    order: '20',
    desc: '新增数据标识',
});
api[1].list.push({
    order: '21',
    desc: '新增数据标识',
});
api[1].list.push({
    order: '22',
    desc: '新增数据组标识',
});
api[1].list.push({
    order: '23',
    desc: '新增数据标识',
});
api.push({
    alias: 'ProcessFlowApi',
    order: '3',
    desc: 'api审批流服务',
    link: 'api审批流服务',
    list: []
})
api[2].list.push({
    order: '1',
    desc: '测试流程审批回调  外部系统调用审批流,可自实现回调接口',
});
api[2].list.push({
    order: '2',
    desc: '创建审批流',
});
api[2].list.push({
    order: '3',
    desc: '获取审批流信息',
});
api[2].list.push({
    order: '4',
    desc: '审批流-操作,审批,不通过,撤销',
});
api.push({
    alias: 'AuthorController',
    order: '4',
    desc: '联系开发者服务',
    link: '联系开发者服务',
    list: []
})
api[3].list.push({
    order: '1',
    desc: '邮件页面',
});
api[3].list.push({
    order: '2',
    desc: '发送邮件',
});
api[3].list.push({
    order: '3',
    desc: '下载地址',
});
api[3].list.push({
    order: '4',
    desc: 'ZDH历史版本查询',
});
api.push({
    alias: 'DataTagController',
    order: '5',
    desc: '数据标识服务',
    link: '数据标识服务',
    list: []
})
api[4].list.push({
    order: '1',
    desc: '数据标识首页',
});
api[4].list.push({
    order: '2',
    desc: '数据标识列表',
});
api[4].list.push({
    order: '3',
    desc: '根据产品代码获取数据标识',
});
api[4].list.push({
    order: '4',
    desc: '数据标识新增首页',
});
api[4].list.push({
    order: '5',
    desc: '数据标识明细',
});
api[4].list.push({
    order: '6',
    desc: '数据标识更新',
});
api[4].list.push({
    order: '7',
    desc: '数据标识新增',
});
api[4].list.push({
    order: '8',
    desc: '数据标识删除',
});
api.push({
    alias: 'DataTagGroupController',
    order: '6',
    desc: '数据标识组服务',
    link: '数据标识组服务',
    list: []
})
api[5].list.push({
    order: '1',
    desc: '数据标识组首页',
});
api[5].list.push({
    order: '2',
    desc: '数据标识组列表',
});
api[5].list.push({
    order: '3',
    desc: '数据标识组新增首页',
});
api[5].list.push({
    order: '4',
    desc: '数据标识组明细',
});
api[5].list.push({
    order: '5',
    desc: '数据标识组更新',
});
api[5].list.push({
    order: '6',
    desc: '数据标识组新增',
});
api[5].list.push({
    order: '7',
    desc: '数据标识组删除',
});
api.push({
    alias: 'CrowdRuleController',
    order: '7',
    desc: '智能营销-标签服务',
    link: '智能营销-标签服务',
    list: []
})
api[6].list.push({
    order: '1',
    desc: '人群规则列表首页',
});
api[6].list.push({
    order: '2',
    desc: '人群规则列表',
});
api[6].list.push({
    order: '3',
    desc: '人群规则新增首页',
});
api[6].list.push({
    order: '4',
    desc: '人群规则明细',
});
api[6].list.push({
    order: '5',
    desc: '人群规则更新',
});
api[6].list.push({
    order: '6',
    desc: '人群规则新增',
});
api[6].list.push({
    order: '7',
    desc: '人群规则删除',
});
api[6].list.push({
    order: '8',
    desc: '规则模板页面',
});
api[6].list.push({
    order: '9',
    desc: '人群规则手动执行页面',
});
api[6].list.push({
    order: '10',
    desc: '手动执行人群规则,单独生成人群文件',
});
api[6].list.push({
    order: '11',
    desc: '人群文件模板页面',
});
api[6].list.push({
    order: '12',
    desc: '人群运算模板页面',
});
api[6].list.push({
    order: '13',
    desc: '人群规则模板页面',
});
api[6].list.push({
    order: '14',
    desc: '过滤模板页面',
});
api[6].list.push({
    order: '15',
    desc: '分流模板页面',
});
api[6].list.push({
    order: '16',
    desc: '权益模板页面',
});
api[6].list.push({
    order: '17',
    desc: '人群文件列表',
});
api[6].list.push({
    order: '18',
    desc: '过滤列表',
});
api.push({
    alias: 'LabelController',
    order: '8',
    desc: '智能营销-标签服务',
    link: '智能营销-标签服务',
    list: []
})
api[7].list.push({
    order: '1',
    desc: '标签列表首页',
});
api[7].list.push({
    order: '2',
    desc: '标签列表',
});
api[7].list.push({
    order: '3',
    desc: '标签新增首页',
});
api[7].list.push({
    order: '4',
    desc: '标签明细',
});
api[7].list.push({
    order: '5',
    desc: '根据code查询标签明细',
});
api[7].list.push({
    order: '6',
    desc: '标签更新',
});
api[7].list.push({
    order: '7',
    desc: '标签新增',
});
api[7].list.push({
    order: '8',
    desc: '标签删除',
});
api.push({
    alias: 'StrategyGroupController',
    order: '9',
    desc: '智能营销-策略组服务',
    link: '智能营销-策略组服务',
    list: []
})
api[8].list.push({
    order: '1',
    desc: '策略组列表首页',
});
api[8].list.push({
    order: '2',
    desc: '策略组列表',
});
api[8].list.push({
    order: '3',
    desc: '策略组新增首页',
});
api[8].list.push({
    order: '4',
    desc: '策略组明细',
});
api[8].list.push({
    order: '5',
    desc: '策略组更新',
});
api[8].list.push({
    order: '6',
    desc: '策略组新增',
});
api[8].list.push({
    order: '7',
    desc: '策略组删除',
});
api[8].list.push({
    order: '8',
    desc: '策略组手动执行页面',
});
api[8].list.push({
    order: '9',
    desc: '策略组手动执行',
});
api.push({
    alias: 'LoginController',
    order: '10',
    desc: '登录服务',
    link: '登录服务',
    list: []
})
api[9].list.push({
    order: '1',
    desc: '系统根页面',
});
api[9].list.push({
    order: '2',
    desc: '注册页面',
});
api[9].list.push({
    order: '3',
    desc: '注册',
});
api[9].list.push({
    order: '4',
    desc: '登录页面',
});
api[9].list.push({
    order: '5',
    desc: '登录  2022-06-18 因登录时提示不友好,删除ResponseBody注解,并在session中存储错误信息',
});
api[9].list.push({
    order: '6',
    desc: '获取错误信息',
});
api[9].list.push({
    order: '7',
    desc: '校验验证码',
});
api[9].list.push({
    order: '8',
    desc: '首页',
});
api[9].list.push({
    order: '9',
    desc: '下线系统',
});
api[9].list.push({
    order: '10',
    desc: '个人信息页面',
});
api[9].list.push({
    order: '11',
    desc: '个人信息更新',
});
api[9].list.push({
    order: '12',
    desc: '获取用户信息',
});
api[9].list.push({
    order: '13',
    desc: '找回密码',
});
api[9].list.push({
    order: '14',
    desc: '获取账号名',
});
api[9].list.push({
    order: '15',
    desc: '退出',
});
api[9].list.push({
    order: '16',
    desc: '验证码',
});
api.push({
    alias: 'MyErrorConroller',
    order: '11',
    desc: '异常页面服务',
    link: '异常页面服务',
    list: []
})
api[10].list.push({
    order: '1',
    desc: '404页面',
});
api[10].list.push({
    order: '2',
    desc: '403页面',
});
api[10].list.push({
    order: '3',
    desc: '503页面',
});
api.push({
    alias: 'NodeController',
    order: '12',
    desc: '升级扩容服务',
    link: '升级扩容服务',
    list: []
})
api[11].list.push({
    order: '1',
    desc: 'server构建首页',
});
api[11].list.push({
    order: '2',
    desc: 'server构建列表',
});
api[11].list.push({
    order: '3',
    desc: 'server服务列表',
});
api[11].list.push({
    order: '4',
    desc: 'server服务更新上下线',
});
api[11].list.push({
    order: '5',
    desc: 'server新增模板首页',
});
api[11].list.push({
    order: '6',
    desc: 'server新增',
});
api[11].list.push({
    order: '7',
    desc: 'server模板更新',
});
api[11].list.push({
    order: '8',
    desc: '手动构建配置',
});
api[11].list.push({
    order: '9',
    desc: 'server 一键部署',
});
api[11].list.push({
    order: '10',
    desc: '获取server部署记录',
});
api[11].list.push({
    order: '11',
    desc: '获取server部署记录',
});
api[11].list.push({
    order: '12',
    desc: '',
});
api.push({
    alias: 'PermissionController',
    order: '13',
    desc: '权限服务',
    link: '权限服务',
    list: []
})
api[12].list.push({
    order: '1',
    desc: '权限首页(废弃)',
});
api[12].list.push({
    order: '2',
    desc: '权限用户列表',
});
api[12].list.push({
    order: '3',
    desc: '启用/禁用用户',
});
api[12].list.push({
    order: '4',
    desc: '菜单资源权限配置页面',
});
api[12].list.push({
    order: '5',
    desc: '新增用户页面',
});
api[12].list.push({
    order: '6',
    desc: '查询权限用户明细',
});
api[12].list.push({
    order: '7',
    desc: '权限用户信息更新',
});
api[12].list.push({
    order: '8',
    desc: '新增用户组页面',
});
api[12].list.push({
    order: '9',
    desc: '新增用户组',
});
api[12].list.push({
    order: '10',
    desc: '',
});
api[12].list.push({
    order: '11',
    desc: '角色首页',
});
api[12].list.push({
    order: '12',
    desc: '新增角色页面',
});
api[12].list.push({
    order: '13',
    desc: '角色列表',
});
api[12].list.push({
    order: '14',
    desc: '角色批量启用',
});
api[12].list.push({
    order: '15',
    desc: '角色明细',
});
api[12].list.push({
    order: '16',
    desc: '根据角色code查询角色信息',
});
api[12].list.push({
    order: '17',
    desc: '资源树页面',
});
api[12].list.push({
    order: '18',
    desc: '资源树-新增资源',
});
api[12].list.push({
    order: '19',
    desc: '资源新增根节点',
});
api[12].list.push({
    order: '20',
    desc: '获取资源信息',
});
api[12].list.push({
    order: '21',
    desc: '根据主键获取资源信息',
});
api[12].list.push({
    order: '22',
    desc: '更新资源信息',
});
api[12].list.push({
    order: '23',
    desc: '删除资源信息',
});
api[12].list.push({
    order: '24',
    desc: '更新资源层级',
});
api[12].list.push({
    order: '25',
    desc: '角色绑定资源',
});
api[12].list.push({
    order: '26',
    desc: '通过角色id获取资源',
});
api[12].list.push({
    order: '27',
    desc: '通过用户id获取资源',
});
api[12].list.push({
    order: '28',
    desc: '权限用户首页',
});
api[12].list.push({
    order: '29',
    desc: '获取当前系统的数据组标识',
});
api[12].list.push({
    order: '30',
    desc: '权限申请列表首页',
});
api[12].list.push({
    order: '31',
    desc: '权限新增申请首页',
});
api[12].list.push({
    order: '32',
    desc: '权限申请',
});
api[12].list.push({
    order: '33',
    desc: '权限申请列表',
});
api[12].list.push({
    order: '34',
    desc: '新建申请,并创建审批流',
});
api[12].list.push({
    order: '35',
    desc: '删除申请,并撤销审批流',
});
api[12].list.push({
    order: '36',
    desc: '权限申请明细',
});
api.push({
    alias: 'ProductTagController',
    order: '14',
    desc: '产品标识服务',
    link: '产品标识服务',
    list: []
})
api[13].list.push({
    order: '1',
    desc: '产品列表首页',
});
api[13].list.push({
    order: '2',
    desc: '产品列表',
});
api[13].list.push({
    order: '3',
    desc: '产品新增首页',
});
api[13].list.push({
    order: '4',
    desc: '产品明细',
});
api[13].list.push({
    order: '5',
    desc: '产品更新',
});
api[13].list.push({
    order: '6',
    desc: '产品新增',
});
api[13].list.push({
    order: '7',
    desc: '产品删除',
});
api.push({
    alias: 'SystemController',
    order: '15',
    desc: '系统服务',
    link: '系统服务',
    list: []
})
api[14].list.push({
    order: '1',
    desc: '获取平台名称',
});
api[14].list.push({
    order: '2',
    desc: '帮助页',
});
api[14].list.push({
    order: '3',
    desc: 'quartz表达式页面',
});
api[14].list.push({
    order: '4',
    desc: '文件服务器设置页面',
});
api[14].list.push({
    order: '5',
    desc: '获取文件服务器',
});
api[14].list.push({
    order: '6',
    desc: '文件服务器信息更新',
});
api[14].list.push({
    order: '7',
    desc: '系统通知信息',
});
api[14].list.push({
    order: '8',
    desc: '删除系统任务',
});
api[14].list.push({
    order: '9',
    desc: '通知信息详情页面',
});
api[14].list.push({
    order: '10',
    desc: '通知信息明细',
});
api[14].list.push({
    order: '11',
    desc: '通知首页',
});
api[14].list.push({
    order: '12',
    desc: '通知信息列表',
});
api[14].list.push({
    order: '13',
    desc: '删除通知信息',
});
api[14].list.push({
    order: '14',
    desc: '通知信息标记已读',
});
api[14].list.push({
    order: '15',
    desc: '使用帮助',
});
api[14].list.push({
    order: '16',
    desc: '解析quartz表达式',
});
api[14].list.push({
    order: '17',
    desc: '系统登录通知页面',
});
api[14].list.push({
    order: '18',
    desc: '系统登录通知',
});
api[14].list.push({
    order: '19',
    desc: '更新系统登录通知',
});
api[14].list.push({
    order: '20',
    desc: '获取当前系统版本',
});
api.push({
    alias: 'TestController',
    order: '16',
    desc: 'ClassName: TestController',
    link: 'classname:_testcontroller',
    list: []
})
api[15].list.push({
    order: '1',
    desc: '',
});
api[15].list.push({
    order: '2',
    desc: '',
});
api.push({
    alias: 'WeMockController',
    order: '17',
    desc: 'mock数据服务',
    link: 'mock数据服务',
    list: []
})
api[16].list.push({
    order: '1',
    desc: 'mock数据首页',
});
api[16].list.push({
    order: '2',
    desc: 'mock数据新增页面',
});
api[16].list.push({
    order: '3',
    desc: '',
});
api[16].list.push({
    order: '4',
    desc: 'mock数据更新',
});
api[16].list.push({
    order: '5',
    desc: 'mock数据新增',
});
api[16].list.push({
    order: '6',
    desc: 'mock数据删除',
});
api.push({
    alias: 'ZdhApprovalController',
    order: '18',
    desc: '审批流服务',
    link: '审批流服务',
    list: []
})
api[17].list.push({
    order: '1',
    desc: '审批节点首页',
});
api[17].list.push({
    order: '2',
    desc: '审批节点配置',
});
api[17].list.push({
    order: '3',
    desc: '审批节点新增页面',
});
api[17].list.push({
    order: '4',
    desc: '审批节点明细',
});
api[17].list.push({
    order: '5',
    desc: '审批节点新增',
});
api[17].list.push({
    order: '6',
    desc: '审批节点更新',
});
api[17].list.push({
    order: '7',
    desc: '审批人首页',
});
api[17].list.push({
    order: '8',
    desc: '审批人列表',
});
api[17].list.push({
    order: '9',
    desc: '审批人新增首页',
});
api[17].list.push({
    order: '10',
    desc: '审批流首页',
});
api[17].list.push({
    order: '11',
    desc: '审批流新增首页',
});
api[17].list.push({
    order: '12',
    desc: '审批人新增',
});
api[17].list.push({
    order: '13',
    desc: '审批人说明',
});
api[17].list.push({
    order: '14',
    desc: '审批人更新',
});
api[17].list.push({
    order: '15',
    desc: '审批人明细',
});
api[17].list.push({
    order: '16',
    desc: '审批流列表',
});
api[17].list.push({
    order: '17',
    desc: '审批流新增',
});
api[17].list.push({
    order: '18',
    desc: '审批流删除',
});
api[17].list.push({
    order: '19',
    desc: '审批流-更新',
});
api[17].list.push({
    order: '20',
    desc: '审批流明细',
});
api[17].list.push({
    order: '21',
    desc: '审批事件首页(废弃,使用审批流代替)',
});
api[17].list.push({
    order: '22',
    desc: '审批事件新增首页(废弃,使用审批流代替)',
});
api[17].list.push({
    order: '23',
    desc: '审批事件新增',
});
api[17].list.push({
    order: '24',
    desc: '审批事件明细',
});
api[17].list.push({
    order: '25',
    desc: '审批事件列表',
});
api[17].list.push({
    order: '26',
    desc: '审批事件更新',
});
api.push({
    alias: 'ZdhBloodSourceController',
    order: '19',
    desc: '血缘服务',
    link: '血缘服务',
    list: []
})
api[18].list.push({
    order: '1',
    desc: '血缘首页',
});
api[18].list.push({
    order: '2',
    desc: '血缘分析首页',
});
api[18].list.push({
    order: '3',
    desc: '生成血缘关系',
});
api[18].list.push({
    order: '4',
    desc: '模糊查询输入源',
});
api[18].list.push({
    order: '5',
    desc: '数据血缘明细',
});
api.push({
    alias: 'ZdhDataSourcesController',
    order: '20',
    desc: '数据源服务',
    link: '数据源服务',
    list: []
})
api[19].list.push({
    order: '1',
    desc: '数据源列表首页',
});
api[19].list.push({
    order: '2',
    desc: '数据源列表',
});
api[19].list.push({
    order: '3',
    desc: '数据源列表(带参数)',
});
api[19].list.push({
    order: '4',
    desc: '根据数据源id(主键)获取数据源',
});
api[19].list.push({
    order: '5',
    desc: '批量删除数据源',
});
api[19].list.push({
    order: '6',
    desc: '数据源新增页面',
});
api[19].list.push({
    order: '7',
    desc: '新增数据源',
});
api[19].list.push({
    order: '8',
    desc: '更新数据源',
});
api[19].list.push({
    order: '9',
    desc: '获取所有的数据源类型',
});
api[19].list.push({
    order: '10',
    desc: '测试数据源联通性',
});
api.push({
    alias: 'ZdhDataWareController',
    order: '21',
    desc: '新数据仓库服务',
    link: '新数据仓库服务',
    list: []
})
api[20].list.push({
    order: '1',
    desc: '数据资产首页',
});
api[20].list.push({
    order: '2',
    desc: '获取已发布的数据',
});
api[20].list.push({
    order: '3',
    desc: '获取数据仓库标签参数',
});
api[20].list.push({
    order: '4',
    desc: '数据抽样',
});
api[20].list.push({
    order: '5',
    desc: '导出数据',
});
api[20].list.push({
    order: '6',
    desc: '获取当前数据的申请人列表',
});
api[20].list.push({
    order: '7',
    desc: '通知下游页面',
});
api[20].list.push({
    order: '8',
    desc: '通知',
});
api.push({
    alias: 'ZdhDataxController',
    order: '22',
    desc: 'ETL-datax服务',
    link: 'etl-datax服务',
    list: []
})
api[21].list.push({
    order: '1',
    desc: 'datax 任务首页',
});
api[21].list.push({
    order: '2',
    desc: 'datax任务新增首页',
});
api[21].list.push({
    order: '3',
    desc: 'datax任务列表',
});
api[21].list.push({
    order: '4',
    desc: '删除datax任务',
});
api[21].list.push({
    order: '5',
    desc: '新增datax任务',
});
api[21].list.push({
    order: '6',
    desc: '更新datax任务',
});
api.push({
    alias: 'ZdhDispatchController',
    order: '23',
    desc: '调度服务',
    link: '调度服务',
    list: []
})
api[22].list.push({
    order: '1',
    desc: '调度任务首页',
});
api[22].list.push({
    order: '2',
    desc: '调度任务列表',
});
api[22].list.push({
    order: '3',
    desc: '模糊匹配调度任务明细',
});
api[22].list.push({
    order: '4',
    desc: '新增调度任务首页',
});
api[22].list.push({
    order: '5',
    desc: '调度手动执行页面',
});
api[22].list.push({
    order: '6',
    desc: '调度任务新增页面',
});
api[22].list.push({
    order: '7',
    desc: 'hdfs任务页面',
});
api[22].list.push({
    order: '8',
    desc: 'etl任务页面',
});
api[22].list.push({
    order: '9',
    desc: 'jdbc任务页面',
});
api[22].list.push({
    order: '10',
    desc: '调度任务组页面',
});
api[22].list.push({
    order: '11',
    desc: 'shell任务页面',
});
api[22].list.push({
    order: '12',
    desc: 'http任务页面',
});
api[22].list.push({
    order: '13',
    desc: 'email任务页面',
});
api[22].list.push({
    order: '14',
    desc: 'fluem任务页面',
});
api[22].list.push({
    order: '15',
    desc: '调度执行器首页',
});
api[22].list.push({
    order: '16',
    desc: '新增调度任务',
});
api[22].list.push({
    order: '17',
    desc: '批量删除调度任务',
});
api[22].list.push({
    order: '18',
    desc: '更新调度任务',
});
api[22].list.push({
    order: '19',
    desc: '手动执行调度任务  手动执行都会提前生成实例信息,串行会生成组依赖关系  串行并行都需要提前选择时间,调度本身时间和手动执行时间分开处理,手动执行不能影响调度时间,  手动重试一定会确定好时间',
});
api[22].list.push({
    order: '20',
    desc: '',
});
api[22].list.push({
    order: '21',
    desc: '自动执行调度任务',
});
api[22].list.push({
    order: '22',
    desc: '暂停调度任务',
});
api[22].list.push({
    order: '23',
    desc: '删除调度任务,如果是单源 ETL的流任务需要做单独处理',
});
api[22].list.push({
    order: '24',
    desc: '获取server实例列表',
});
api[22].list.push({
    order: '25',
    desc: '调度器列表',
});
api[22].list.push({
    order: '26',
    desc: '更新调度器状态',
});
api[22].list.push({
    order: '27',
    desc: '新增系统调度任务首页',
});
api[22].list.push({
    order: '28',
    desc: '查询系统调度任务列表',
});
api[22].list.push({
    order: '29',
    desc: '新增系统调度任务',
});
api[22].list.push({
    order: '30',
    desc: '更新系统调度任务',
});
api[22].list.push({
    order: '31',
    desc: '重新创建系统调度任务',
});
api[22].list.push({
    order: '32',
    desc: '删除系统调度任务',
});
api.push({
    alias: 'ZdhDownController',
    order: '24',
    desc: '文件下载服务',
    link: '文件下载服务',
    list: []
})
api[23].list.push({
    order: '1',
    desc: '文件下载首页',
});
api[23].list.push({
    order: '2',
    desc: '文件下载列表',
});
api[23].list.push({
    order: '3',
    desc: '删除下载',
});
api[23].list.push({
    order: '4',
    desc: '下载文件',
});
api.push({
    alias: 'ZdhDroolsController',
    order: '25',
    desc: 'drools服务',
    link: 'drools服务',
    list: []
})
api[24].list.push({
    order: '1',
    desc: 'drools任务首页',
});
api[24].list.push({
    order: '2',
    desc: 'drools任务新增首页',
});
api[24].list.push({
    order: '3',
    desc: '获取drools任务明细',
});
api[24].list.push({
    order: '4',
    desc: '模糊查询drools任务明细',
});
api[24].list.push({
    order: '5',
    desc: '新增drools任务',
});
api[24].list.push({
    order: '6',
    desc: '删除drools任务',
});
api[24].list.push({
    order: '7',
    desc: '更新drools 任务',
});
api.push({
    alias: 'ZdhEnumController',
    order: '26',
    desc: '枚举服务',
    link: '枚举服务',
    list: []
})
api[25].list.push({
    order: '1',
    desc: '枚举首页',
});
api[25].list.push({
    order: '2',
    desc: '获取枚举详情',
});
api[25].list.push({
    order: '3',
    desc: '获取枚举列表',
});
api[25].list.push({
    order: '4',
    desc: '批量删除枚举信息',
});
api[25].list.push({
    order: '5',
    desc: '枚举新增首页',
});
api[25].list.push({
    order: '6',
    desc: '新增枚举',
});
api[25].list.push({
    order: '7',
    desc: '枚举更新',
});
api[25].list.push({
    order: '8',
    desc: '查询枚举',
});
api.push({
    alias: 'ZdhEtlApplyController',
    order: '27',
    desc: '申请源ETL任务服务',
    link: '申请源etl任务服务',
    list: []
})
api[26].list.push({
    order: '1',
    desc: '申请源首页',
});
api[26].list.push({
    order: '2',
    desc: '申请源明细',
});
api[26].list.push({
    order: '3',
    desc: '根据条件模糊查询申请源ETL任务信息',
});
api[26].list.push({
    order: '4',
    desc: '批量删除申请源ETL任务',
});
api[26].list.push({
    order: '5',
    desc: '新增Apply ETL任务首页',
});
api[26].list.push({
    order: '6',
    desc: '新增申请源ETL任务  如果输入数据源类型是外部上传,会补充文件服务器信息',
});
api[26].list.push({
    order: '7',
    desc: '申请源ETL任务更新',
});
api.push({
    alias: 'ZdhEtlBatchController',
    order: '28',
    desc: '批量任务服务',
    link: '批量任务服务',
    list: []
})
api[27].list.push({
    order: '1',
    desc: '批量任务首页',
});
api[27].list.push({
    order: '2',
    desc: '批量任务新增首页',
});
api[27].list.push({
    order: '3',
    desc: '批量任务明细',
});
api[27].list.push({
    order: '4',
    desc: '根据条件模糊查询批量任务信息',
});
api[27].list.push({
    order: '5',
    desc: '批量删除'批量任务信息',
});
api[27].list.push({
    order: '6',
    desc: '新增批量任务',
});
api[27].list.push({
    order: '7',
    desc: '批量任务更新',
});
api[27].list.push({
    order: '8',
    desc: '批量任务新增',
});
api.push({
    alias: 'ZdhEtlController',
    order: '29',
    desc: '单源ETL任务服务',
    link: '单源etl任务服务',
    list: []
})
api[28].list.push({
    order: '1',
    desc: '单源ETL首页',
});
api[28].list.push({
    order: '2',
    desc: '获取单源ETL任务明细',
});
api[28].list.push({
    order: '3',
    desc: '根据条件模糊查询单源ETL任务信息',
});
api[28].list.push({
    order: '4',
    desc: '批量删除单源ETL任务',
});
api[28].list.push({
    order: '5',
    desc: '新增单源ETL任务首页',
});
api[28].list.push({
    order: '6',
    desc: '新增单源ETL任务  如果输入数据源类型是外部上传,会补充文件服务器信息',
});
api[28].list.push({
    order: '7',
    desc: '单源ETL任务输入数据源是外部上传时,上传文件服务',
});
api[28].list.push({
    order: '8',
    desc: '单源ETL任务更新  todo 此次是否每次都更新文件服务器信息,待优化',
});
api[28].list.push({
    order: '9',
    desc: '根据数据源id 获取数据源下所有的表名字',
});
api[28].list.push({
    order: '10',
    desc: '根据数据源id,表名获取表的schema',
});
api.push({
    alias: 'ZdhEtlLogController',
    order: '30',
    desc: '日志采集服务',
    link: '日志采集服务',
    list: []
})
api[29].list.push({
    order: '1',
    desc: '日志采集首页',
});
api[29].list.push({
    order: '2',
    desc: '获取日志任务明细',
});
api[29].list.push({
    order: '3',
    desc: '根据条件模糊查询任务信息',
});
api[29].list.push({
    order: '4',
    desc: '批量删除日志采集任务',
});
api[29].list.push({
    order: '5',
    desc: '新增日志采集任务首页',
});
api[29].list.push({
    order: '6',
    desc: '新增日志采集任务  如果输入数据源类型是外部上传,会补充文件服务器信息',
});
api[29].list.push({
    order: '7',
    desc: '日志采集任务更新  todo 此次是否每次都更新文件服务器信息,待优化',
});
api.push({
    alias: 'ZdhFlinkController',
    order: '31',
    desc: 'FLINK采集服务',
    link: 'flink采集服务',
    list: []
})
api[30].list.push({
    order: '1',
    desc: 'flink任务首页',
});
api[30].list.push({
    order: '2',
    desc: 'flink任务新增首页',
});
api[30].list.push({
    order: '3',
    desc: '模糊查询flink任务',
});
api[30].list.push({
    order: '4',
    desc: '批量删除sql任务',
});
api[30].list.push({
    order: '5',
    desc: '新增flink任务',
});
api[30].list.push({
    order: '6',
    desc: '更新flink任务',
});
api.push({
    alias: 'ZdhIssueDataController',
    order: '32',
    desc: '数据仓库服务  发布服务,申请服务',
    link: '数据仓库服务  发布服务,申请服务',
    list: []
})
api[31].list.push({
    order: '1',
    desc: '数据发布首页',
});
api[31].list.push({
    order: '2',
    desc: '数据发布新增首页',
});
api[31].list.push({
    order: '3',
    desc: '数据集市查询首页',
});
api[31].list.push({
    order: '4',
    desc: '数据申请首页',
});
api[31].list.push({
    order: '5',
    desc: '数据明细首页',
});
api[31].list.push({
    order: '6',
    desc: '根据id获取对应的数据明细',
});
api[31].list.push({
    order: '7',
    desc: '根据条件模糊查询发布数据',
});
api[31].list.push({
    order: '8',
    desc: '根据条件模糊查询发布数据源,只查询当前用户',
});
api[31].list.push({
    order: '9',
    desc: '发布数据删除',
});
api[31].list.push({
    order: '10',
    desc: '发布数据表信息  走审批流  1 创建数据表-&gt; 2 根据当前接口从功能资源列表查询绑定的审批事件 -&gt; 3 根据审批事件查询所有审批节点 -&gt; 4 创建审批流 -&gt; 审批人在审批页面审批',
});
api[31].list.push({
    order: '11',
    desc: '发布数据更新',
});
api[31].list.push({
    order: '12',
    desc: '发布数据删除',
});
api[31].list.push({
    order: '13',
    desc: '申请数据表信息',
});
api[31].list.push({
    order: '14',
    desc: '数据申请列表',
});
api[31].list.push({
    order: '15',
    desc: '数据申请',
});
api[31].list.push({
    order: '16',
    desc: '根据主键查询数据申请信息',
});
api[31].list.push({
    order: '17',
    desc: '取消申请',
});
api[31].list.push({
    order: '18',
    desc: '审批列表',
});
api[31].list.push({
    order: '19',
    desc: '数据审批',
});
api.push({
    alias: 'ZdhJdbcController',
    order: '33',
    desc: 'MPP jdbc服务',
    link: 'mpp_jdbc服务',
    list: []
})
api[32].list.push({
    order: '1',
    desc: 'jdbc任务首页',
});
api[32].list.push({
    order: '2',
    desc: 'jdbc任务新增首页',
});
api[32].list.push({
    order: '3',
    desc: '模糊查询jdbc任务',
});
api[32].list.push({
    order: '4',
    desc: '批量删除sql任务',
});
api[32].list.push({
    order: '5',
    desc: '新增sql 任务',
});
api[32].list.push({
    order: '6',
    desc: '更新sql 任务',
});
api.push({
    alias: 'ZdhMonitorController',
    order: '34',
    desc: '监控服务',
    link: '监控服务',
    list: []
})
api[33].list.push({
    order: '1',
    desc: '监控首页',
});
api[33].list.push({
    order: '2',
    desc: '调度任务监控',
});
api[33].list.push({
    order: '3',
    desc: '',
});
api[33].list.push({
    order: '4',
    desc: '',
});
api[33].list.push({
    order: '5',
    desc: '任务实例删除',
});
api[33].list.push({
    order: '6',
    desc: '组任务删除',
});
api[33].list.push({
    order: '7',
    desc: '杀死单个任务',
});
api[33].list.push({
    order: '8',
    desc: '手动跳过任务',
});
api[33].list.push({
    order: '9',
    desc: '杀死任务组',
});
api[33].list.push({
    order: '10',
    desc: '重试任务实例(废弃)',
});
api[33].list.push({
    order: '11',
    desc: '重试任务组',
});
api[33].list.push({
    order: '12',
    desc: '获取正在执行中调度任务',
});
api[33].list.push({
    order: '13',
    desc: '获取spark历史服务器地址',
});
api[33].list.push({
    order: '14',
    desc: '获取任务总览',
});
api[33].list.push({
    order: '15',
    desc: '获取任务组实例首页',
});
api[33].list.push({
    order: '16',
    desc: '获取任务实例首页',
});
api[33].list.push({
    order: '17',
    desc: '任务组重试页面',
});
api[33].list.push({
    order: '18',
    desc: '任务实例列表',
});
api[33].list.push({
    order: '19',
    desc: '任务组实例列表',
});
api[33].list.push({
    order: '20',
    desc: '任务组实例列表-分页',
});
api[33].list.push({
    order: '21',
    desc: '任务组实例列表',
});
api[33].list.push({
    order: '22',
    desc: '获取任务执行日志',
});
api[33].list.push({
    order: '23',
    desc: '调度任务日志首页',
});
api[33].list.push({
    order: '24',
    desc: '下载日志',
});
api[33].list.push({
    order: '25',
    desc: '系统监控',
});
api.push({
    alias: 'ZdhMoreEtlController',
    order: '35',
    desc: '多源ETL服务',
    link: '多源etl服务',
    list: []
})
api[34].list.push({
    order: '1',
    desc: '多源ETL任务首页',
});
api[34].list.push({
    order: '2',
    desc: '根据指定任务id,或者查询当前用户下的所有多源任务',
});
api[34].list.push({
    order: '3',
    desc: '模糊查询多源ETL任务信息',
});
api[34].list.push({
    order: '4',
    desc: '新增多源ETL任务首页',
});
api[34].list.push({
    order: '5',
    desc: '新增多源ETL任务',
});
api[34].list.push({
    order: '6',
    desc: '删除多源ETL任务',
});
api[34].list.push({
    order: '7',
    desc: '更新多源ETL任务',
});
api.push({
    alias: 'ZdhOperateLogController',
    order: '36',
    desc: '操作日志服务',
    link: '操作日志服务',
    list: []
})
api[35].list.push({
    order: '1',
    desc: '操作日志首页',
});
api[35].list.push({
    order: '2',
    desc: '操作日志列表    bootstrap-table 分页   设置            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）            pageNumber: 1,                       //初始化加载第一页，默认第一页            pageSize: 10,                       //每页的记录行数（*）           queryParams: function (params) {                // 此处使用了LayUi组件 是为加载层                loadIndex = layer.load(1);                let resRepor = {                    //服务端分页所需要的参数                    limit: params.limit,                    offset: params.offset                };                return resRepor;            },            responseHandler: res =&gt; {                // 关闭加载层                layer.close(loadIndex);                return {                            //return bootstrap-table能处理的数据格式                    "total":res.total,                    "rows": res.rows                }            },            data-content-type="application/x-www-form-urlencoded" data-query-params="queryParams"',
});
api.push({
    alias: 'ZdhParamController',
    order: '37',
    desc: '参数配置服务',
    link: '参数配置服务',
    list: []
})
api[36].list.push({
    order: '1',
    desc: '系统参数首页',
});
api[36].list.push({
    order: '2',
    desc: '系统参数新增首页',
});
api[36].list.push({
    order: '3',
    desc: '系统参数列表',
});
api[36].list.push({
    order: '4',
    desc: '系统参数明细',
});
api[36].list.push({
    order: '5',
    desc: '系统参数新增',
});
api[36].list.push({
    order: '6',
    desc: '系统参数更新',
});
api[36].list.push({
    order: '7',
    desc: '系统参数删除',
});
api[36].list.push({
    order: '8',
    desc: '系统参数写入缓存',
});
api.push({
    alias: 'ZdhProcessFlowController',
    order: '38',
    desc: '审批服务',
    link: '审批服务',
    list: []
})
api[37].list.push({
    order: '1',
    desc: '流程审批页面',
});
api[37].list.push({
    order: '2',
    desc: '我的发起流程',
});
api[37].list.push({
    order: '3',
    desc: '审批列表',
});
api[37].list.push({
    order: '4',
    desc: '我的发起流程列表  根据流程表process_flow_info 按用户+flow_id去重',
});
api[37].list.push({
    order: '5',
    desc: '审批',
});
api[37].list.push({
    order: '6',
    desc: '撤销申请',
});
api[37].list.push({
    order: '7',
    desc: '流程审批进度',
});
api[37].list.push({
    order: '8',
    desc: '审批明细',
});
api.push({
    alias: 'ZdhQualityController',
    order: '39',
    desc: '质量检测服务',
    link: '质量检测服务',
    list: []
})
api[38].list.push({
    order: '1',
    desc: '质量检测规则首页',
});
api[38].list.push({
    order: '2',
    desc: '质量检测规则新增首页',
});
api[38].list.push({
    order: '3',
    desc: '质量检测规则列表',
});
api[38].list.push({
    order: '4',
    desc: '质量检测规则新增',
});
api[38].list.push({
    order: '5',
    desc: '质量检测规则更新',
});
api[38].list.push({
    order: '6',
    desc: '数据质量任务',
});
api[38].list.push({
    order: '7',
    desc: '质量检测任务新增首页',
});
api[38].list.push({
    order: '8',
    desc: '质量检测列表',
});
api[38].list.push({
    order: '9',
    desc: '质量检测任务新增',
});
api[38].list.push({
    order: '10',
    desc: '质量检测任务更新',
});
api[38].list.push({
    order: '11',
    desc: '质量检测任务删除',
});
api[38].list.push({
    order: '12',
    desc: '质量报告首页',
});
api[38].list.push({
    order: '13',
    desc: '指标首页',
});
api[38].list.push({
    order: '14',
    desc: '指标明细',
});
api[38].list.push({
    order: '15',
    desc: '质量报告明细',
});
api[38].list.push({
    order: '16',
    desc: '删除质量报告',
});
api.push({
    alias: 'QueueController',
    order: '40',
    desc: '优先级队列服务  具体zdh_queue服务可参见zdh_queue项目',
    link: '优先级队列服务  具体zdh_queue服务可参见zdh_queue项目',
    list: []
})
api[39].list.push({
    order: '1',
    desc: '队列消息列表首页',
});
api[39].list.push({
    order: '2',
    desc: '队列消息列表',
});
api[39].list.push({
    order: '3',
    desc: '队列消息明细',
});
api[39].list.push({
    order: '4',
    desc: '队列新增消息首页',
});
api[39].list.push({
    order: '5',
    desc: '新增队列消息',
});
api[39].list.push({
    order: '6',
    desc: '更新优先级',
});
api.push({
    alias: 'ZdhRedisApiController',
    order: '41',
    desc: 'redis服务',
    link: 'redis服务',
    list: []
})
api[40].list.push({
    order: '1',
    desc: '获取参数',
});
api[40].list.push({
    order: '2',
    desc: '删除参数',
});
api[40].list.push({
    order: '3',
    desc: '获取所有参数名',
});
api[40].list.push({
    order: '4',
    desc: '新增参数',
});
api.push({
    alias: 'ZdhSelfServiceController',
    order: '42',
    desc: '自助服务',
    link: '自助服务',
    list: []
})
api[41].list.push({
    order: '1',
    desc: '自助服务首页',
});
api[41].list.push({
    order: '2',
    desc: '自助服务列表',
});
api[41].list.push({
    order: '3',
    desc: '自助服务新增首页',
});
api[41].list.push({
    order: '4',
    desc: '自助服务明细',
});
api[41].list.push({
    order: '5',
    desc: '自助服务更新',
});
api[41].list.push({
    order: '6',
    desc: '自助服务新增',
});
api[41].list.push({
    order: '7',
    desc: '自助服务删除',
});
api[41].list.push({
    order: '8',
    desc: '自助服务执行',
});
api[41].list.push({
    order: '9',
    desc: '自助服务导出',
});
api.push({
    alias: 'ZdhSqlController',
    order: '43',
    desc: 'spark sql服务',
    link: 'spark_sql服务',
    list: []
})
api[42].list.push({
    order: '1',
    desc: 'spark sql任务首页',
});
api[42].list.push({
    order: '2',
    desc: 'spark sql任务新增首页',
});
api[42].list.push({
    order: '3',
    desc: '模糊查询Sql任务',
});
api[42].list.push({
    order: '4',
    desc: '批量删除sql任务',
});
api[42].list.push({
    order: '5',
    desc: '新增sql 任务',
});
api[42].list.push({
    order: '6',
    desc: '更新sql 任务',
});
api[42].list.push({
    order: '7',
    desc: '加载元数据信息',
});
api[42].list.push({
    order: '8',
    desc: '查询当前数据仓库的所有数据库',
});
api[42].list.push({
    order: '9',
    desc: '此服务停用',
});
api[42].list.push({
    order: '10',
    desc: '获取表结构说明',
});
api.push({
    alias: 'ZdhSshController',
    order: '44',
    desc: 'SSH服务',
    link: 'ssh服务',
    list: []
})
api[43].list.push({
    order: '1',
    desc: 'SSH 任务首页',
});
api[43].list.push({
    order: '2',
    desc: 'SSH任务新增首页',
});
api[43].list.push({
    order: '3',
    desc: '文件上传(废弃)',
});
api[43].list.push({
    order: '4',
    desc: '文件更新(废弃)',
});
api[43].list.push({
    order: '5',
    desc: '文件删除(废弃)',
});
api[43].list.push({
    order: '6',
    desc: '获取文件列表(废弃)',
});
api[43].list.push({
    order: '7',
    desc: 'ssh任务明细',
});
api[43].list.push({
    order: '8',
    desc: '删除ssh任务',
});
api[43].list.push({
    order: '9',
    desc: '新增ssh任务',
});
api[43].list.push({
    order: '10',
    desc: '更新ssh任务',
});
api[43].list.push({
    order: '11',
    desc: 'ssh任务删除文件',
});
api[43].list.push({
    order: '12',
    desc: 'ssh任务已上传文件明细',
});
api.push({
    alias: 'ZdhTestController',
    order: '45',
    desc: '测试非结构化上传',
    link: '测试非结构化上传',
    list: []
})
api[44].list.push({
    order: '1',
    desc: '非结构化任务首页',
});
api[44].list.push({
    order: '2',
    desc: '',
});
api.push({
    alias: 'ZdhUnstructureController',
    order: '46',
    desc: '非结构化数据服务',
    link: '非结构化数据服务',
    list: []
})
api[45].list.push({
    order: '1',
    desc: '非结构化任务首页',
});
api[45].list.push({
    order: '2',
    desc: '非结构化任务新增首页',
});
api[45].list.push({
    order: '3',
    desc: '非结构化任务上传首页',
});
api[45].list.push({
    order: '4',
    desc: '非结构化任务日志首页',
});
api[45].list.push({
    order: '5',
    desc: '非结构化任务列表',
});
api[45].list.push({
    order: '6',
    desc: '删除 非结构化任务',
});
api[45].list.push({
    order: '7',
    desc: '新增 非结构化任务',
});
api[45].list.push({
    order: '8',
    desc: '更新 非结构化任务',
});
api[45].list.push({
    order: '9',
    desc: '手动上传文件-生成源信息',
});
api[45].list.push({
    order: '10',
    desc: '非结构化任务删除文件',
});
api[45].list.push({
    order: '11',
    desc: '非结构化任务已上传文件明细',
});
api[45].list.push({
    order: '12',
    desc: '非结构化任务日志列表',
});
api[45].list.push({
    order: '13',
    desc: '删除 非结构化任务日志',
});
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        const search = document.getElementById('search');
        const searchValue = search.value;
        let searchArr = [];
        for (let i = 0; i < api.length; i++) {
            let apiData = api[i];
            const desc = apiData.desc;
            if (desc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                searchArr.push({
                    order: apiData.order,
                    desc: apiData.desc,
                    link: apiData.link,
                    alias: apiData.alias,
                    list: apiData.list
                });
            } else {
                let methodList = apiData.list || [];
                let methodListTemp = [];
                for (let j = 0; j < methodList.length; j++) {
                    const methodData = methodList[j];
                    const methodDesc = methodData.desc;
                    if (methodDesc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                        methodListTemp.push(methodData);
                        break;
                    }
                }
                if (methodListTemp.length > 0) {
                    const data = {
                        order: apiData.order,
                        desc: apiData.desc,
                        alias: apiData.alias,
                        link: apiData.link,
                        list: methodListTemp
                    };
                    searchArr.push(data);
                }
            }
        }
        let html;
        if (searchValue === '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchArr,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            let $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiData, liClass, display) {
    let html = "";
    if (apiData.length > 0) {
         for (let j = 0; j < apiData.length; j++) {
            html += '<li class="'+liClass+'">';
            html += '<a class="dd" href="' + apiData[j].alias + '.html#header">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
            html += '<ul class="sectlevel2" style="'+display+'">';
            let doc = apiData[j].list;
            for (let m = 0; m < doc.length; m++) {
                html += '<li><a href="' + apiData[j].alias + '.html#_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + doc[m].desc + '</a> </li>';
            }
            html += '</ul>';
            html += '</li>';
        }
    }
    return html;
}