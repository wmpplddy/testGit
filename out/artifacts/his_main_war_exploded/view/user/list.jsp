<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="../../layui/css/layui.css" rel="stylesheet">
    <script src="../../layui/layui.js"></script>
    <style>
        fieldset{border:0;border-top:1px solid #ddd;margin-left:10px;}
        fieldset legend{font-size:14px;}
    </style>
</head>
<body>
<blockquote class="layui-elem-quote layui-text" style="font-weight: bold;">
    <fieldset>
        <legend>用户列表</legend>
    </fieldset>
</blockquote>
<script type="text/html" id="find">
    <form class="layui-form layui-row layui-col-space4" lay-filter="whe">
        <div class="layui-col-md3">
            <input type="text" name="uname" placeholder="用户名 / 姓名 / 手机号 / 邮箱" class="layui-input">
        </div>
        <div class="layui-col-md2">
            <div class="layui-input-wrap">
                <input type="text" name="phone" placeholder="年龄 / 性别" lay-affix="clear" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md6 layui-col-space4" style="padding-left: 5px">
            <div class="layui-col-md2" style="padding-top:5px; "><input type="checkbox" title="模糊查询" name="isLike" lay-filter="likeFind" id="likeC" lay-skin="tag"></div>
            <div class="layui-col-md6 layui-col-md-offset1">
                <button class="layui-btn" lay-submit lay-filter="table-search">Search</button>
                <button type="reset" onclick="resetBtn()" class="layui-btn layui-btn-primary">Clear</button>
            </div>
        </div>
        <div class="layui-col-md12" style="margin-left: 2px">
            <button type="button"
                    class="layui-btn layui-btn-sm layui-border-green layui-btn-primary layui-btn-radius"
                    lay-event="addUser"><i class="layui-icon layui-icon-add-1"></i>添加用户
            </button>
            <button type="button"
                    class="layui-btn-sm layui-btn layui-border-red layui-btn-primary layui-btn-radius"
                    lay-event="deletes"><i class="layui-icon layui-icon-template-1"></i>批量删除
            </button>
        </div>

    </form>
</script>


<div id="tableBox">
    <table class="layui-hide" id="test" lay-filter="test" style="border-top: 0px;"></table>
</div>

<script type="text/html" id="switc">
    <div class="layui-form">
        <input type="checkbox" name="CCC" value="{{= d.uid }}" lay-filter="switch-user" class="layui-form-switch"
               lay-skin="switch"
               title="已启用|已禁用" {{=d.delete_flag==1?"checked":"" }}>
    </div>
</script>
<script type="text/html" id="operation">
    <div class="layui-clear-space">
        <a class="layui-btn layui-btn-primary layui-border-orange layui-btn-xs" lay-event="edit">
            <i class="layui-icon">&#xe642;</i>
            编辑
        </a>

        <a class="layui-btn layui-btn-primary layui-border-green layui-btn-xs" lay-event="reset">
            <i class="layui-icon layui-icon-senior"></i>重置密码
        </a>
        <a class="layui-btn layui-btn-primary layui-border-red layui-btn-xs" lay-event="delete">
            <i class="layui-icon">&#xe640;</i>删除
        </a>
    </div>
</script>

</body>

<script>
    function resetBtn() {
        layui.use('table', function () {
            var table = layui.table;
            table.reloadData('test', {
                page: {
                    curr: 1 // 重新从第 1 页开始
                },
                where: {
                    uname: null,
                    phone: null
                },
                url: '<%=request.getContextPath()%>/user/list'
            });
        })
    }

    layui.use(function () {
        var table = layui.table;
        var form = layui.form;
        var $ = layui.$;
        var layer = layui.layer;
        table.render({
            elem: '#test',
            toolbar: '#find',
            page: true,
            limits: [5, 8, 12, 20, 30, 40],
            cellMinWidth: 60,
            css: [ // 直接给当前表格主容器重置 css 样式
                '.layui-table-page{text-align: center;}' // 让分页栏居中
            ].join(''),
            cols: [[
                {type: 'checkbox', fixed: 'left'},
                {type: 'numbers', title: '序号'},
                {field: 'uid', hide: true, width: 60, title: 'ID', sort: true},
                {
                    field: 'uname',
                    width: 90,

                    title: '用户名称',
                    templet: '<span><a href="userInfo.jsp?uid={{=d.uid}}" style="color:green;text-decoration:underline;">{{d.uname}}<a/></span>'
                },
                {field: 'zjm', title: '助记码', width: 120},
                {field: 'truename', title: '真实姓名', minWidth: 90},
                {
                    field: 'sex', hide: true, title: '性别', width: 60, templet: function (d) {
                        if (d.sex === 'M') {
                            return '<span style="color: blue">♂</span>';
                        } else {
                            return '<span style="color: pink">♀</span>';
                        }
                    }
                },
                {field: 'age', hide: true, title: '年龄', width: 60},
                {field: 'phone', width: 125, hide: true, title: '手机号'},
                {
                    field: 'mail', title:
                        '邮箱 <i class="layui-icon layui-icon-tips layui-font-14" lay-event="email-tips" title="该字段开启了编辑功能" style="margin-left: 5px;"></i>',
                    fieldTitle: '邮箱', hide: true, width: 170, expandedMode: 'tips', edit: 'text', align: 'center'
                },
                {
                    field: 'create_time', title: '创建时间', minWidth: 105, templet: function (d) {
                        var date = new Date(d.create_time);
                        var year = date.getFullYear(); // 获取年份
                        var month = date.getMonth() + 1; // 获取月份（注意月份是从 0 开始计数的，所以需要加 1）
                        var day = date.getDate();
                        var formattedTime = year + '-' + month + '-' + day;
                        return formattedTime;
                    }
                },
                {field: 'create_uname', title: '创建人', minWidth: 75, maxWidth: 100},
                {
                    field: 'update_time', title: '修改时间', minWidth: 105, templet: function (d) {
                        console.log(d.update_time)
                        if(d.update_time==null){return '';}
                        var date = new Date(d.update_time);
                        var year = date.getFullYear();
                        var month = date.getMonth() + 1;
                        var day = date.getDate();
                        var formattedTime = year + '-' + month + '-' + day;
                        return formattedTime;
                    }
                },
                {field: 'update_uname', title: '修改人', minWidth: 75, maxWidth: 85},
                {field: 'delete_flag', title: '状态', minWidth: 110, width: 110, templet: '#switc'},
                {fixed: 'right', title: '操作', minWidth: 220, toolbar: '#operation'}
            ]],
            url: '<%=request.getContextPath()%>/user/list',
            method: 'post',
            where: form.val('whe')
            ,
            done: function () {
                form.on('submit(table-search)', function (data) {
                    var field = data.field; // 获得表单字段
                    console.log($('#likeC').prop("checked"))
                    if ($('#likeC').prop("checked")) {
                        field.likeC = 1;
                    }
                    table.reloadData('test', {
                        page: {
                            curr: 1 // 重新从第 1 页开始
                        },
                        where: field, // 搜索的字段
                        url: '<%=request.getContextPath()%>/user/list'
                    });
                    layer.msg('搜索成功<br>');
                    return false; // 阻止默认 form 跳转
                });

            }
        });
        form.on('checkbox(likeFind)', function (obj) {
            var value = obj.elem.checked ? 1 : 2;
            layer.tips(value == 1 ? '已启用模糊搜索' : '已关闭模糊搜索', obj.othis)
            return false; // 阻止默认 form 跳转
        });
        table.on('toolbar(test)', function (obj) {
            var id = obj.config.id;
            var checkStatus = table.checkStatus(id);
            if (obj.event === 'deletes') {
                if (checkStatus.data.length == 0) {
                    layer.msg('您当前还未选中数据！')
                } else {
                    var names = [];
                    var uids = "";
                    var datas = checkStatus.data;
                    for (let i = 0; i < checkStatus.data.length; i++) {
                        names.push(datas[i].uname);
                        uids += datas[i].uid + ","
                    }
                    layer.confirm('确定删除' + JSON.stringify(names) + '的信息吗', {
                        title: '<span style="color:deepskyblue;">批量删除</span>',
                        btn: ['确认删除', '取消']
                    }, function () {
                        layer.confirm('从数据库删除，还是只修改删除标识呢？', {
                            title: '选择',
                            icon: 1,
                            btn: ['从数据库', '从标识'],
                        }, function () {
                            //数据库删除接口 param int[]
                            $.ajax({
                                url: '<%=request.getContextPath()%>/DBdel',
                                data: {
                                    uids: uids
                                },
                                type: 'post',
                                dataType: 'json',
                                success: function (res) {
                                    layer.msg(res.msg)
                                    if (res.code === 200) {
                                        table.cache.test = null;
                                        if(checkStatus.isAll){
                                            var c = 1;
                                            if(obj.config.page.curr>1){
                                                c = obj.config.page.curr-1;
                                            }
                                            table.reloadData('test',{
                                                page: {
                                                    curr:c
                                                },
                                            })
                                        }else{
                                            table.reloadData('test')
                                        }
                                    } else {
                                        layer.msg('删除失败，请稍后再试')
                                    }
                                },
                                error: function () {

                                }
                            })
                            layer.msg('数据库删除啦！')

                        }, function () {
                            //修改标识删除接口 param int[]
                            $.ajax({
                                url: '<%=request.getContextPath()%>/flagDel',
                                data: {
                                    uids: uids
                                },
                                type: 'post',
                                dataType: 'json',
                                success: function (res) {
                                    layer.msg(res.msg)
                                    if (res.code === 200) {
                                        layer.msg('标识已经修改啦！')

                                        table.cache.test = null;
                                        table.reloadData('test')
                                    } else {
                                        layer.msg('删除失败，请稍后再试')

                                    }
                                },
                                error: function () {

                                }
                            })
                        });
                    }, function () {
                        layer.msg('取消删除');
                    });

                }
            } else if (obj.event === 'addUser') {
                layer.open({
                    title: ['添加用户','font-size:18px;font-weight:800;'],
                    type: 2,
                    shade: 0.6, // 遮罩透明度
                    shadeClose: false,
                    anim:4,
                    area: ['45%', '90%'],
                    content: 'addUser.jsp'
                });
            }

        })

        table.on('tool(test)', function (obj) {
            // 获得当前行数据
            var values = obj.data;
            var eventName = obj.event;
            if (eventName == 'edit') {
                layer.open({
                    title: ['用户信息', 'font-size:18px;font-weight:800;text-align:center;'],
                    type: 2,
                    shade: 0.6, // 遮罩透明度
                    shadeClose: false,
                    anim: 2,
                    area: ['90%', '90%'],
                    content: 'updateUserInfo.jsp?uid=' + values.uid,
                });
            } else if (eventName == 'reset') {
                layer.confirm('是否重置密码？', {
                    btn: ['确定', '关闭'] //按钮
                }, function () {
                    //重置密码
                    layer.msg('重置完成', {icon: 1});
                }, function () {

                });
            } else if (eventName == 'delete') {
                layer.confirm('确认永久删除该用户吗？[' + values.uname + ']', {
                    btn: ['确定', '关闭'] //按钮
                }, function () {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/DBdel',
                        data: {
                            uids: values.uid
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function (res) {
                            layer.msg(res.msg)
                            if (res.code === 200) {
                                table.cache.test = null;
                                table.reloadData('test', {
                                    page: {
                                        curr: 1 // 重新从第 1 页开始
                                    },
                                })
                            } else {
                                layer.msg('删除失败，请稍后再试')
                            }
                        },
                        error: function () {

                        }
                    })
                    layer.msg('删除成功', {icon: 1});
                }, function () {

                });
            }
        });


        form.on('switch(switch-user)', function (obj) {
            var id = this.value;
            var value = obj.elem.checked ? 1 : 2;
            //用户标识修改操作
            $.ajax({
                url: '<%=request.getContextPath()%>/flagDel',
                data: {
                    uids: id,
                    flag: value
                },
                type: 'post',
                dataType: 'json',
                success: function (res) {
                    if (res.code == 200) {
                        layer.tips(value == 1 ? '已启用' : '已禁用', obj.othis);
                        table.reloadData('test')
                    } else {
                        layer.tips('启用失败，请稍后再试', obj.othis)
                    }
                }, error: function (res) {

                }
            })
        });
    });
</script>

</html>