<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="../../layui/css/layui.css" rel="stylesheet">
    <script src="../../layui/layui.js"></script>
    <script src="../../js/jquery-3.6.0.js"></script>
</head>

<body>
<div class="layui-row">
    <div class="layui-panel">
        <div class="layui-col-xs12" style="padding: 42px 12px 12px 32px;">
            <div class="layui-form">
                <div class="layui-form-item">
                    <label class="layui-form-label">姓名</label>
                    <div class="layui-input-inline layui-input-wrap" style="width: 300px;">
                        <input type="text" name="uname" id="uname" placeholder="必填/不可重复" autocomplete="off" value="" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">真实姓名</label>
                    <div class="layui-input-inline " style="width: 300px;">
                        <input type="text" name="truename" id="tname" placeholder="必填/不可重复" autocomplete="off" value=""
                               class="layui-input">
                        <input type="text" name="zjm000" id="u_zjm" hidden value="">
                    </div>
                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">年龄</label>
                    <div class="layui-input-inline layui-input-wrap" style="width: 300px;">
                        <input type="text" name="age" id="age" placeholder="必填" lay-verify="required|number" autocomplete="off"
                               value="" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">性别
                    </label>
                    <input type="radio" name="sex" id="M" value="0" title="默认">
                    <div lay-radio>
                        <span style="color: rgb(189, 189, 255);">男 ♂</span>
                    </div>
                    <input type="radio" name="sex" id="F" value="1" title="默认">
                    <div lay-radio>
                        <span style="color: pink;">女 ♀</span>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">邮箱</label>
                        <div class="layui-input-inline" style="width: 300px;">
                            <input type="text" name="mail" id="mail" lay-verify="required|email"
                                   placeholder="必填/不可重复" value="" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">手机号</label>
                        <div class="layui-input-inline layui-input-wrap" style="width: 300px;">
                            <input type="tel" name="phone" id="phone" lay-verify="required|phone" value=""
                                   autocomplete="off" lay-affix="clear" placeholder="必填/不可重复" class="layui-input demo-phone">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="layui-row" style="padding: 13px;color: rgb(236, 103, 103);">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-primary layui-border-orange layui-btn-fluid" onclick="sub();" id="sub">提交
        </button>
    </div>
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-primary layui-border-green layui-btn-fluid" id="review">返回</button>
    </div>
</div>
</body>
<script>
    var index = parent.layer.getFrameIndex(window.name);
    $('#review').click(function () {
        parent.layer.close(index);
    });

    function sub() {
        layui.use(function () {
            var layer = layui.layer;
            var form = layui.form;
            console.log(1111)
            var uname = $('#uname').val();
            var truename = $('#tname').val();
            var zjm = $('#u_zjm').val();
            var age = $('#age').val();
            var sex = $("input[name='sex']:checked").val();
            var mail = $('#mail').val();
            var phone = $('#phone').val();
            var user = {
                uname: uname,
                zjm: zjm,
                truename: truename,
                age: age,
                sex: sex,
                mail: mail,
                phone: phone
            }
            if (form.validate('#age') && form.validate('#mail') && form.validate('#phone')) {
                $.ajax({
                    url: '<%=request.getContextPath()%>/add_u',
                    type: 'POST',
                    data: user,
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 200) {
                            parent.layer.msg('修改成功');
                            parent.layui.table.reloadData('test')
                            parent.layer.close(index);
                        } else {
                            var msg = res.msg;
                            var f = msg == 'phone' ? '电话号码重复，请修改手机号' : '邮箱已有绑定账号，请重新输入';
                            if(res.msg === 'uname'){
                                f = '用户名重复，请修改用户名'
                            }
                            layer.tips(f, '#' + msg, {
                                tips: [2, 'rgb(236, 103, 103)']
                            });
                        }
                    },
                    error: function (res) {
                        return false;
                    }
                })

            }
        })
    }

    var v = 0;
    layui.use(function () {
        var form = layui.form;
        form.render();

    })
</script>

</html>