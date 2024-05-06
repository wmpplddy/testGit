<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改密码</title>
    <%--
        request.getContextPath()  可以获得工程名 /his
     --%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/layui/css/layui.css" media="all">
    <script src="<%=request.getContextPath()%>/layui/layui.js"></script>
    <style>

        html{
            background:#f2f2f2;
            height:100%;
            position: relative;
        }
        body{
            background-image: url("../../images/his_bg.jpg");
            background-size: 100% 100%;
            background-repeat: no-repeat;
            height: 100%;
        }
        .loginBox{
            width:380px;
            margin:110px auto ;
            border-radius: 12px;
            background:rgba(255,255,255,0.4);
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

        .right-link{
            float:right;
            color:#029789;
            position: relative;
            top:5px;
        }

    </style>
</head>
<body>

<div class="loginWin" >
    <form class="layui-form">
        <div class="loginBox" >

            <div class="layui-header">
                <h2>修改密码</h2>

            </div>

            <div class="layui-form">

                <div class="layui-form-item">
                    <label class="layui-icon layui-icon-password" for="user-login-opassword"></label>
                    <input type="password" name="opass" id="user-login-opassword" lay-verify="required" placeholder="原密码" class="layui-input">
                </div>


                <div class="layui-form-item">
                    <label class="layui-icon layui-icon-password" for="user-login-npassword"></label>
                    <input type="password" name="upass" id="user-login-npassword" lay-verify="required" placeholder="新密码" class="layui-input">
                </div>

                <div class="layui-form-item">
                    <label class="layui-icon layui-icon-password" for="user-login-repassword"></label>
                    <input type="password" name="repass" id="user-login-repassword" lay-verify="required" placeholder="确认密码" class="layui-input">
                </div>


                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="user-login-submit">确  认</button>
                </div>

            </div>
        </div>
    </form>

</div>
<script>
    layui.use(function(){
        var form = layui.form;
        var $ = layui.$;
        var layer = layui.layer;
        form.on('submit(user-login-submit)',function (data) {
            console.log('<%=request.getContextPath()%>')
            var field = data.field;
            $.ajax({
                url:'<%=request.getContextPath()%>/updatePwd',
                type:'post',
                data:field,
                dataType:'json',
                success:function(data){
                    var res = data;
                    if(res.code==200){
                        layer.msg("修改成功",{
                            time:600,
                            end:function(){
                                location.reload();
                            }
                        });
                    }else{
                        layer.msg(res.msg,{
                            time:600,
                            end:function (){
                                location.reload();
                            }
                        });
                    }
                },
                error:function(data){

                }
            })
            return false;
        })
    })
</script>

</body>
</html>