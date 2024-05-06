<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forget</title>
    <link rel="stylesheet" href="layui/css/layui.css" media="all">
    <script src="layui/layui.js"></script>
    <style>
        body {
            background-image: url("images/420.webp");
            background-size: 100% 100%;
            background-repeat: no-repeat;
            height: 100%;
        }

        .laybox {
            width: 380px;
            margin: 110px auto;
            border-radius: 12px;
            background:rgba(255,255,255,0.4);
            padding: 20px;
            height: 300px;
        }

        .layui-form {
            padding: 20px;
        }

        .layui-header {
            text-align: center;
            padding-bottom: 0;
            font-size: 20px;
        }

        label.layui-icon {
            position: relative;
            width: 38px;
            height: 38px;
            top: 28px;
            left: 10px;
        }

        .layui-input {
            padding-left: 38px;
            border-color: #eee;
            border-radius: 2px;
        }
        html{
            background:#f2f2f2;
            height:100%;
            position: relative;
        }

    </style>
</head>

<body>
<div>
    <form class="layui-form">
        <div class="laybox">
            <div class="layui-header a">
                <h2 style="opacity: 0.8;color: #666666">忘记密码</h2>
            </div>
            <div class="layui-form-item b" style="margin-top: 20px;margin-bottom: 40px;">
                <label class="layui-icon-user layui-icon layui-icon-username"></label>
                <input type="text" name="uname" lay-verify="required" placeholder="用户名/手机号/邮箱" autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-form-item c layui-row">

                <button class="layui-btn layui-col-xs12" lay-submit lay-filter="demo1">立即提交</button>


            </div>
            <div class="layui-form-item d layui-row">

                <button type="reset" class="layui-btn layui-btn-primary  layui-col-xs12">重置</button>

            </div>
        </div>
    </form>

</div>



<script>
    layui.use(function () {
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.$;

        $('.a').hide();
        $('.b').hide();
        $('.c').hide();
        $('.d').hide();

        $('.a').fadeIn(1000);
        $('.b').fadeIn(1300);
        $('.c').fadeIn(1600);
        $('.d').fadeIn(2000);

        // 自定义验证规则
        // form.verify({
        //     pass: function (value) {
        //         if (value==null||value.length===1) {
        //             return '请填写您的账号信息';
        //         }
        //     }
        // });
        // 提交事件
        form.on('submit(demo1)', function (data) {
            var field = data.field; // 获取表单字段值
            // 显示填写结果，仅作演示用
            console.log(123)
            $.ajax({
                url:'forget',
                type:'post',
                data:field,
                dataType:'json',
                success:function(data){
                    var res = data;
                    if(res.code==200){
                        //验证成功
                        location.href="mailTip.jsp?p=邮件已发送，请及时查收&v=1";
                    }else{
                        layer.msg(res.msg,{
                            end:function (){
                            }
                        });
                    }
                },
                error:function(data){

                }
            })
            return false; // 阻止默认 form 跳转
        });
    });
</script>

</body>

</html>
