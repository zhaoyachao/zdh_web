/*!
 * selectInput v1.0
 * author JerryZst
 * qq 1309579432
 * Date: 2020/8/12 0007
 */
layui.define(['jquery', 'laypage'], function (exports) {
    var $ = layui.jquery;
    // var form = layui.form;
    var _this = this;
    var _MOD = 'selectInput';
    var selectedCss = 'layui-form-selected';
    var upCss = 'layui-form-selectup';
    var selectInputClass = 'selectInput';
    var x, y;
    if ($('#ew-css-selectInput').length <= 0) {
        layui.link(layui.cache.base + 'selectInput/selectInput.css');
    }
    // 加载全局鼠标位置
    $(document).mousemove(function (e) {
        x = e.pageX;
        y = e.pageY;
    });
    var selectInput = function (opt) {
        this.version = 'selectInput-1.0';
        this.tmpId = new Date().getTime();
        this.tmpId = opt.uniqueId ? opt.uniqueId : Math.round(Math.random() * 1000 + 9999);
        this._input = 'input-' + this.tmpId;
        this._select = 'select-' + this.tmpId;
        this._class = 'dd' + this.tmpId;
        this.tmpSelectValue = "";
        this.hasData = !!opt.data;
        // 配置项
        this.options = $.extend(true, {
            method: 'GET',
            error: opt.error || null,
            data: opt.data || [],
            on: opt.on || null,
            remoteMethod: opt.remoteMethod || null,
            invisibleMode: opt.invisibleMode || false,
            invisibleData: opt.data || [],
            hasInitShow: opt.hasInitShow || false,
            hasCut: opt.hasCut || true,
            layFilter: opt.layFilter || '',
            layVerType: opt.layVerType || 'msg',
            layVerify: opt.layVerify || '',
            layReqText: opt.layReqText || '',
            ignoreCase: opt.ignoreCase || false,
            autoClose: opt.autoClose || true,
            remoteEvent: opt.remoteEvent || 'input',
        }, opt);
        this.init(); // 初始化
    };

    /**
     * 初始化数据层
     */
    selectInput.prototype.init = function () {
        var that = this;
        var options = this.options;
        // 直接赋值模式
        if (options.data.length >= 0 && that.hasData) {
            that.renderBody(options.data);
            that.bindEvents(); // 绑定事件
        } else if (options.url) {
            // 异步渲染数据
            if (!options.where) options.where = {};
            $.ajax({
                url: options.url,
                data: options.contentType && options.contentType.indexOf('application/json') === 0 ? JSON.stringify(options.where) :
                    options.where,
                headers: options.headers,
                type: options.method,
                dataType: 'json',
                contentType: options.contentType,
                success: function (res) {
                    var __res = options.parseData ? options.parseData(res) : res;
                    if (typeof (__res) === "string") {
                        __res = JSON.parse(__res);
                    }
                    if (!__res.hasOwnProperty('data')) {
                        layui.hint().error('返回数据格式有误，必须满足如下格式：{"code":0,"msg":"success","data":[]}');
                        return false;
                    }
                    var data = __res.data;
                    that.options.invisibleData = data;
                    that.renderBody(data);
                    that.bindEvents(); // 绑定事件
                    options.done && options.done(data, 1);
                },
                error: function (xhr) {
                    return options.error ? options.error({
                        code: xhr.status,
                        msg: xhr.statusText,
                        xhr: xhr
                    }) : console.error(xhr.statusText);
                }
            });
        } else if (options.remoteSearch && options.remoteMethod) {
            that.renderBody([]);
            that.bindEvents(); // 绑定事件
        }
    };


    /** 获取各个组件 */
    selectInput.prototype.getComponents = function () {
        var that = this;
        var $elem = $(that.options.elem);
        var filter = $elem.attr('lay-filter');
        if (!filter) {
            filter = that.options.elem.substring(1);
            $elem.attr('lay-filter', filter);
        }
        return {
            $elem: $elem, // 容器
            filter: filter, // 容器的lay-filter
            $inputElem: $('#' + this._input),
            $selectElem: $('#' + this._select),
            $ddElem: $('.' + this._class)
        };
    };
    /**
     * 加载input
     */
    selectInput.prototype.renderInput = function () {
        var options = this.options;
        var name = options.name ? options.name : this.version + this.tmpId;
        var placeholder = options.placeholder ? options.placeholder : '请输入内容';
        var initValue = options.initValue ? options.initValue : '';
        return '<input type="text" value="' + this.getNameByValue(initValue) + '"  name="' + name + '"  id="' + this._input +
            '"  lay-filter="' + options.layFilter +
            '"  lay-verType="' + options.layVerType +
            '"  lay-verify="' + options.layVerify +
            '"  lay-reqText="' + options.layReqText +
            '"  oncut="' + (!options.hasCut ? 'return false' : '') + '" autocomplete="off" placeholder="' + placeholder + '"  class="layui-input">';
    };

    /**
     * 加载主体内容
     * @param data
     */
    selectInput.prototype.renderBody = function (data) {
        var _options = this.options;
        var _input = this.renderInput();
        var components = this.getComponents();
        var icon = "";
        if (_options.hasSelectIcon) {
            icon = '<div class="layui-form-selectInput-title"><i class="layui-selectInput-edge"></i></div>';
        }
        var bodyHtml = '<div class="layui-form-selectInput" id="' + this._select + '">' + icon +
            '<dl class="layui-anim layui-anim-upbit ' + selectInputClass + '" style="">';
        if (!data) data = [];
        if (data.length > 0) {
            var contentHtml = '';
            for (var i = 0; i < data.length; i++) {
                contentHtml += '<dd lay-index="' + i + '" lay-value="' + data[i]['value'] + '" class="' + this._class + '">' +
                    data[i]['name'] + '</dd>';
            }
            bodyHtml = _input + bodyHtml + contentHtml + '</dl></div>';
        } else {
            bodyHtml = _input + bodyHtml + '</dl></div>';
        }

        components.$elem.html(bodyHtml);
        if (_options.hasInitShow) {
            this.showElem();
        }
        return bodyHtml;
    };

    /**
     * 绑定事件
     */
    selectInput.prototype.bindEvents = function () {
        var that = this;
        var _options = this.options;
        var components = this.getComponents();
        /* 事件公共返回对象 */
        var commonMember = function (ext, isInput) {
            var $item = $(ext);
            var obj = {
                elem: $item, //当前item的dom
                data: isInput ? $item.val() : $item.attr('lay-value'),
                id: $(this).id // 当前item索引
            };
            return $.extend(obj, ext);
        };

        // 执行重载dl数据
        var cb = function (data) {
            if (data && typeof (data) === 'string') {
                data = JSON.parse(data)
            }
            if (!data) data = [];
            // 判断是否为对象
            if (Object.prototype.toString.call(data) === '[object Object]') {
                if (data.hasOwnProperty('data')) {
                    data = data.data;
                } else {
                    data = [];
                    layui.hint().error('返回数据格式有误，必须满足如下格式：{"code":0,"msg":"success","data":[]}');
                    return false;
                }
            }
            if (data.length > 0) {
                data = that.options.parseData ? that.options.parseData(data) : data;
                that.options.invisibleData = data;
                var contentHtml = '';
                for (var i = 0; i < data.length; i++) {
                    contentHtml += '<dd lay-index="' + i + '" lay-value="' + data[i]['value'] + '"  class="' + that._class +
                        '">' +
                        data[i]['name'] +
                        '</dd>';
                }
                components.$selectElem.find('dl').html(contentHtml);
                components.$selectElem.addClass(selectedCss + ' ' + upCss);
                components.$selectElem.find("dl").css({
                    "display": "block"
                });
                that.bindEvents();
            } else {
                components.$selectElem.removeClass(selectedCss + ' ' + upCss);
                components.$selectElem.find("dl").css({
                    "display": "none"
                });
            }
        };

        // 定时器
        let __timer = null;
        // 绑定input输入事件
        components.$inputElem.off('input propertychange').on('input propertychange', function () {
            // 执行我们的正常的操作
            var value = $(this).val();
            if (value !== '' && value !== null && value !== undefined) {
                that.tmpSelectValue = '';
            }
            if (!that.options.remoteSearch) {
                if (value !== '' && value !== null && value !== undefined) {
                    $(this).next().addClass(selectedCss + ' ' + upCss);
                    $(this).next().find("dl").css({
                        "display": "block"
                    });
                    var dl = $(this).next().find("dl").children();
                    var j = -1;
                    for (var i = 0; i < dl.length; i++) {
                        var __tmpInner = dl[i].innerHTML;
                        if (that.options.ignoreCase) {
                            __tmpInner = __tmpInner.toLowerCase();
                            value = value.toLowerCase();
                        }
                        if (__tmpInner.indexOf(value) <= -1) {
                            dl[i].style.display = "none";
                            j++;
                        } else {
                            dl[i].style.display = "block";
                        }
                        if (j === (dl.length - 1)) {
                            $(this).next().find("dl").css({
                                "display": "none"
                            });
                        }
                    }
                } else {
                    $(this).next().removeClass(selectedCss + ' ' + upCss);
                    $(this).next().find("dl").css({
                        "display": "none"
                    });
                }
            } else if (that.options.remoteSearch && that.options.remoteMethod && that.options.remoteEvent === 'input') {
                if (__timer) {
                    clearTimeout(__timer);
                    __timer = null;
                }
                __timer = setTimeout(() => {
                    that.options.remoteMethod(value, cb);
                }, 500);
            }
            layui.event.call(this, _MOD, 'itemInput(' + components.filter + ')', commonMember(this, true));
        });

        // 响应回车事件
        if (that.options.remoteSearch && that.options.remoteMethod && that.options.remoteEvent === 'keydown') {
            components.$inputElem.off('keydown').on('keydown', function (event) {
                var value = $(this).val();
                if (value !== '' && value !== null && value !== undefined) {
                    that.tmpSelectValue = '';
                }
                if (parseInt(event.keyCode) === 13) {
                    // 远程执行搜索
                    if (__timer) {
                        clearTimeout(__timer);
                        __timer = null;
                    }
                    __timer = setTimeout(() => {
                        that.options.remoteMethod(value, cb);
                    }, 500);
                }
            });
        }

        if (_options.hasSelectIcon) {
            components.$elem.find('.layui-selectInput-edge').off('click').on('click', function () {
                that.showElem();
                components.$inputElem.focus();//点击选择图标同时让input获取焦点
            });
        }

        if (_options.autoClose) {
            // 绑定input光标消失事件
            components.$inputElem.off('blur').on('blur', function () {
                var elem = $(this).next().find('dl');
                var y1 = elem.offset().top; //div上面两个的点的y值
                var y2 = y1 + elem.height(); //div下面两个点的y值
                var x1 = elem.offset().left; //div左边两个的点的x值
                var x2 = x1 + elem.width(); //div右边两个点的x的值
                if (x < x1 || x > x2 || y < y1 || y > y2) {
                    $(this).next().removeClass(selectedCss + ' ' + upCss);
                    elem.hide();
                }
            });
        }

        // select 选择事件
        components.$ddElem.off('click').on('click', function () {
            // 执行我们的正常的操作
            var _index = $(this).attr('lay-index');
            var value = $(this).attr('lay-value');
            var name = $(this).text();
            that.tmpSelectValue = value;
            $(this).parent().parent().prev().val(name);
            $(this).parent().parent().removeClass(selectedCss + ' ' + upCss);
            $(this).parent().css({
                "display": "none"
            });
            layui.event.call(this, _MOD, 'itemSelect(' + components.filter + ')', commonMember(this));
        });
    };

    /**
     * 显示实例
     */
    selectInput.prototype.showElem = function (isNone) {
        isNone = isNone || false;
        var that = this;
        var components = this.getComponents();
        var body = components.$elem.find('.' + selectInputClass).html();
        if (body) {
            var _none = components.$elem.find('.' + selectInputClass).css('display');
            if (_none === 'none' && !isNone) {
                components.$elem.find('.layui-form-selectInput').addClass(selectedCss + ' ' + upCss);
                components.$elem.find('.' + selectInputClass).show();
                components.$elem.find('.' + that._class).show();
            } else {
                components.$elem.find('.layui-form-selectInput').removeClass(selectedCss + ' ' + upCss);
                components.$elem.find('.' + selectInputClass).hide();
                components.$elem.find('.' + that._class).hide();
            }
        }
    };

    /**
     * 监听事件
     * @param events
     * @param callback
     * @returns {*}
     */
    selectInput.prototype.on = function (events, callback) {
        return layui.onevent.call(this, _MOD, events, callback);
    };

    /**
     * 获取选中的value值
     * @returns {*}
     */
    selectInput.prototype.getValue = function () {
        var components = this.getComponents();
        var _value = this.tmpSelectValue;
        var __data = this.options.invisibleData;
        var _select = true;
        if (this.tmpSelectValue === '' ||
            this.tmpSelectValue === undefined ||
            this.tmpSelectValue === null) {
            if (_value !== '' && _value !== null && _value !== undefined) {
                _select = false;
            }
        }
        if (__data.length > 0) {
            for (var k = 0; k < __data.length; k++) {
                if ($.trim(__data[k].name) === $.trim(components.$inputElem.val())) {
                    _select = true;
                }
            }
        }
        if (this.options.invisibleMode) {
            return {
                value: _value,
                isSelect: _select
            };
        }
        return _value;
    };

    /**
     * 清空输入框的value值
     * @returns {boolean}
     */
    selectInput.prototype.emptyValue = function () {
        var components = this.getComponents();
        components.$inputElem.val("");
        this.tmpSelectValue = "";
        return true;
    };

    /**
     * 动态添加select选项
     * @param data
     * @param isPush
     * @returns {boolean}
     */
    selectInput.prototype.addSelect = function (data, isPush) {
        isPush = isPush || false;
        if (!data) {
            return false;
        }
        var _this = this;
        var _options = this.options;
        var components = this.getComponents();
        if (typeof (data) === 'string') {
            data = JSON.parse(data)
        }
        if (data.length === 0) {
            return false;
        }
        if (_options.data && _options.data.length > 0 && isPush) {
            data = _options.data.concat(data)
        }
        var contentHtml = '';
        for (var i = 0; i < data.length; i++) {
            contentHtml += '<dd lay-index="' + i + '" lay-value="' + data[i]['value'] + '" class="' + _this._class + '">' +
                data[i]['name'] +
                '</dd>';
        }
        this.options.data = data;
        this.options.invisibleData = data;
        components.$selectElem.find('dl').html(contentHtml);
        components.$selectElem.addClass(selectedCss + ' ' + upCss);
        components.$selectElem.find("dl").css({
            "display": "block"
        });
        _this.bindEvents();
    }

    /**
     * 通过Value获取Name
     * @param _value
     * @returns {*}
     */
    selectInput.prototype.getNameByValue = function (_value) {
        var __data = this.options.invisibleData;
        if (__data.length > 0) {
            for (var k = 0; k < __data.length; k++) {
                if ($.trim(__data[k].value) === $.trim(_value)) {
                    return __data[k].name;
                }
            }
        }
        return _value;
    }

    /**
     * 设置属性值
     * @param _value
     * @param isEmpty
     * @returns {boolean}
     */
    selectInput.prototype.setValue = function (_value, isEmpty) {
        isEmpty = isEmpty || false;
        var __value = this.getNameByValue(_value);
        var components = this.getComponents();
        if ($.trim(__value) === $.trim(_value) && isEmpty) {
            return false;
        }
        components.$inputElem.val(__value);
        if ($.trim(__value) === $.trim(_value)) {
            this.tmpSelectValue = __value;
        }
    }

    var __insObj = {};

    /** 外部方法 */
    var iS = {
        /* 渲染 */
        render: function (options, _options) {
            _options = _options || null;
            if (typeof (options) === "string" && _options) {
                _options['elem'] = options;
                var __ins = new selectInput(_options);
                __insObj[options] = __ins;
                return __ins;
            }
            return new selectInput(options);
        },
        /* 事件监听 */
        on: function (events, callback) {
            return layui.onevent.call(this, _MOD, events, callback);
        },
        /**
         * 获取实例
         * @param id
         * @returns {null|*}
         */
        getInstance: function (id) {
            if (__insObj.hasOwnProperty(id)) {
                return __insObj[id];
            }
            return null;
        }
    };
    exports(_MOD, iS);
});
