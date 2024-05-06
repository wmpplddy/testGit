<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>登入</title>
    <link rel="stylesheet" href="layui/css/layui.css" media="all">
    <script src="./layui/layui.js"></script>
    <style>
        html{
            background:#f2f2f2;
            height:100%;
            position: relative;
        }
        .loginBox{
            width:380px;
            margin:110px auto ;
        }
        .layui-header{
            text-align: center;
            padding:20px;
            padding-bottom:0;
            font-size:20px;
        }
        .layui-form{
            padding:20px;
        }
        .layui-input{
            padding-left:38px;
            border-color: #eee;
            border-radius: 2px;
        }
        label.layui-icon{
            position:relative ;
            width:38px;
            height:38px;
            top:28px;
            left:10px;
        }
        .vercode-box{
            position: relative;
            top:12px;
            border:1px solid #ccc;
        }
        .right-link{
            float:right;
            color:#029789;
            position: relative;
            top:5px;
        }
        .copy-footer{
            position: absolute;
            text-align: center;
            width:100%;
            bottom:0;
            padding-bottom:20px;
        }
        .loginBox{
            transform: rotate(-10deg);
            transition: transform 1s; /* 添加过渡效果，使旋转动画更平滑 */
            border-radius: 12px;
            background:rgba(255,255,255,0.4);

        }
        .ft{
            background:rgba(255,255,255,0.2);
        }
        body{
            background-image: url("images/420.webp");
            background-size: 100% 100%;
            background-repeat: no-repeat;
            height: 100%;
        }

    </style>

</head>
<body>

<div class="loginWin">
    <form class="layui-form">
        <div class="loginBox">

            <div class="layui-header">
                <h2 id="wel" style="opacity: 0.8;color: #666666">Welcome</h2>
                <p style="padding-top:10px;color:red;font-size:14px;height:14px;">
<%--                    ${param.f==9?'验证码错误':(param.f==8?'用户名错误':(param.f==7?'密码错误':''))}--%>
                </p>
            </div>

            <div class="layui-form">

                <div class="layui-form-item a">
                    <label class="layui-icon-user layui-icon layui-icon-username" for="user-login-username"></label>
                    <input type="text" name="uname" id="user-login-username" lay-verify="required" placeholder="用户名" class="layui-input">
                </div>

                <div class="layui-form-item b">
                    <label class="layui-icon layui-icon-password" for="user-login-password"></label>
                    <input type="password" name="upass" id="user-login-password" lay-verify="required" placeholder="密码" class="layui-input">
                </div>

                <div class="layui-form-item c">
                    <div class="layui-row">
                        <div class="layui-col-xs7">
                            <label class="layui-icon-vercode layui-icon" for="user-login-vercode"></label>
                            <input type="text" name="vercode" id="user-login-vercode" lay-verify="required" placeholder="图形验证码" class="layui-input">
                        </div>
                        <div class="layui-col-xs5">
                            <div class="vercode-box" style="margin-left: 10px;">
                                <img src="verifyCode" class="" id="user-get-vercode" onclick="changeCode()">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="layui-form-item d" style="margin-bottom: 20px;">
                    <input type="checkbox" name="remember" lay-skin="primary" title="记住密码">
                    <a href="forget.jsp" target="_blank" class="right-link" style="margin-top: 7px;">忘记密码？</a>
                </div>

                <div class="layui-form-item e">
                    <button class="layui-btn layui-btn-fluid sub" lay-submit lay-filter="user-login-submit">登 入</button>
                </div>
            </div>
        </div>
    </form>
    <div class="layui-trans copy-footer ft">
        <br>
        <p>&copy; 版权所有：wm</p>
    </div>
</div>



<script>
    var v=0;
    var f = true ; //可以切换验证码
    function changeCode(){
        //我们需要通过id，获得img标签，更换img.src属性值，发送新的请求，并获得新的验证码
        //我们想用jquery获得标签
        //layui中内置了jquery，所以就不需要额外引入jquery了
        //layui中的jquery不能直接用，必须声明
        layui.use('jquery',function(){
            var $ = layui.$ ;
            if(f == true){
                $('#user-get-vercode').attr('src','verifyCode?v='+v++);
                f = false ;
                setTimeout(function(){
                    f = true ;
                },1000);
            }
        });
    }
    layui.use(function(){
        var form = layui.form;
        var $ = layui.$;
        var layer = layui.layer;
        $('.loginBox').css('transform','rotate(0deg)')
        $(function(){
            var t = $('#wel');
            t.hide();
            t.fadeIn('slow');
            // t.animate({
            //   opacity:1
            // },2000)
            $('.a').hide();
            $('.b').hide();
            $('.c').hide();
            $('.d').hide();
            $('.e').hide();
            $('.a').fadeIn(1000);
            $('.b').fadeIn(1300);
            $('.c').fadeIn(1600);
            $('.d').fadeIn(2000);
            $('.e').fadeIn(2500);

            $('.copy-footer').hide();
            $('.copy-footer').fadeIn(3000);
        })

        form.on('submit(user-login-submit)',function (data) {
            var field = data.field;
            $.ajax({
                url:'login',
                type:'post',
                data:field,
                dataType:'json',
                success:function(data){
                    var res = data;
                    if(res.code==200){
                        $('.loginBox').fadeOut('slow')
                        layer.msg("登录成功",{
                            time:800,
                            shadeClose:true,
                            end:function(){
                                main(res.data);
                            }
                        });
                    }else{
                        if(res.msg === "您已被禁用，请联系管理员。"){
                            layer.alert(res.msg, {
                                title:'失败',
                                icon:2,
                                anim:6,
                                time:700,
                                end:function(){
                                    location.reload();
                                }
                            });
                        }else{
                            layer.msg(res.msg,{
                                time:800,
                                shadeClose:true,
                                end:function (){
                                    $('.sub').removeAttr("disabled");
                                    location.reload();
                                }
                            });
                        }
                    }
                },
                error:function(data){

                }
            })
            return false;
        })
    })
    function main(data){
        location.href="main.jsp?token="+data
    }
</script>
</body>
</html>
