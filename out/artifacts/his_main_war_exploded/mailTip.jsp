<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mail</title>
    <link rel="stylesheet" href="./layui/css/layui.css" />
    <script src="./layui/layui.js"></script>
</head>
<body>
<script>
    layui.use('layer',function(){
        var layer = layui.layer;
        layer.alert('${param.p}',{
            icon:${param.v},
            time: 5*1000,
            success: function(layero, index){
                var timeNum = this.time/1000, setText = function(start){
                    layer.title('<span class="layui-font-green">'+ (start ? timeNum : --timeNum) + '</span> 秒后自动返回登录页面', index);
                };
                setText(!0);
                this.timer = setInterval(setText, 1000);
                if(timeNum <= 0) clearInterval(this.timer);
            },
            end: function(){
                clearInterval(this.timer);
                location.href="login.jsp"
            }
        },function (index) {
            location.href="login.jsp"
        },function () {
            location.href="login.jsp"
        })
    })
</script>
</body>
</html>
