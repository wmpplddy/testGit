<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="../../layui/css/layui.css">
    <script src="../../layui/layui.js"></script>
    <script src="../../js/jquery-3.6.0.js"></script>

</head>
<body onload="loadUserInfo()">

<div class="layui-row">
    <div class="layui-panel">
        <div class="layui-col-md12" style="">
            <blockquote class="layui-elem-quote layui-text">
                用户详细信息
            </blockquote>
        </div>
        <div class="layui-col-xs4" style="padding: 12px 12px 12px 32px;">
            <div class="layui-panel">
                <div class="layui-row" style="margin: 21px 0;">
                    <div style="text-align: center; font-size: large;font-weight: 700;">头像</div>
                </div>
                <hr>
                <div class="layui-row" style="text-align: center;padding: 20px 0;">
                    <img class="layui-col-xs6 layui-col-md-offset3" id="ava" style="border: 2px solid rgb(168, 225, 168);"
                         src="" alt="没有头像哦">
                </div>
                <div class="layui-row" style="padding-bottom: 30px;">
                    <div class="layui-col-xs4 layui-col-md-offset4">
                        <button type="button" style="color: black;"
                                class="layui-btn layui-btn-primary layui-btn-fluid layui-border-green"
                                id="ID-upload-demo-btn">
                            <i class="layui-icon layui-icon-upload" style="font-size: 12px;"></i> 上传
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-xs8" style="padding: 12px 12px 12px 32px;">
            <div class="layui-form">
                <div class="layui-form-item">
                    <label class="layui-form-label">姓名</label>
                    <div class="layui-input-inline layui-input-wrap">
                        <input type="text" name="uname" id="uname" readonly autocomplete="off" value=""
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">真实姓名</label>
                    <div class="layui-input-inline layui-input-wrap">
                        <input type="text" name="truename" id="tname" readonly autocomplete="off" value=""
                               class="layui-input">
                        <input type="text" name="zjm000" id="u_zjm" hidden value="">
                    </div>

                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">年龄</label>
                    <div class="layui-input-inline layui-input-wrap" style="width: 120px;">
                        <input type="text" name="age" id="age" readonly autocomplete="off" value=""
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">性别
                    </label>
                    <input type="radio"  name="sex" id="M" value="0" title="默认">
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
                        <div class="layui-input-inline">
                            <input type="text" name="mail" id="mail" placeholder="有值时才校验" readonly value=""
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">手机号</label>
                        <div class="layui-input-inline layui-input-wrap">
                            <input type="tel" name="phone" id="phone" lay-verify="required|phone" value="" readonly
                                   autocomplete="off" lay-affix="clear" class="layui-input demo-phone">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">注册时间</label>
                    <div class="layui-inline">
                        <div class="layui-input-inline layui-input-wrap">
                            <div class="layui-input-prefix">
                                <i class="layui-icon layui-icon-date"></i>
                            </div>
                            <input type="text" name="create_time" id="create_time" value="2024-04-10" readonly
                                   placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="layui-row" style="padding: 13px;">
    <div class="layui-col-xs3 layui-col-md-offset4">
        <button class="layui-btn layui-btn-primary layui-border-green layui-btn-fluid" id="review"
                onclick="rev()">返回</button>
    </div>
</div>
<script>
    var uidv = null;
    function loadUserInfo() {
        // 获取 URL 中的参数
        const urlParams = new URLSearchParams(window.location.search);
        const uid = urlParams.get('uid');
        uidv = uid;
        $('#ava').attr('src','<%=request.getContextPath()%>/download_a?uid='+uid);
        // 发送 HTTP GET 请求
        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // 请求成功，将返回的数据显示在页面上
                    var Result = JSON.parse(xhr.responseText);
                    if(Result.code==200){
                        var user = Result.data;
                        infoAdd(user);
                    }
                } else {
                    console.error('Failed to load user info. Status: ' + xhr.status);
                }
            }
        };
        xhr.open('POST', '<%=request.getContextPath()%>/userInfos?uid=' + uid, true);
        xhr.send();
    }
    function infoAdd(user){
        $('#uname').val(user.uname);
        $('#tname').val(user.truename);
        $('#age').val(user.age);
        if(user.sex === '男'){
            // $("#M").removeAttr("disabled");
            // $("#F").removeAttr("checked");
            $("#F").attr("disabled","disabled");
            $("#M").attr("checked","checked");
        }else{
            // $("#F").removeAttr("disabled");
            // $("#M").removeAttr("checked");
            $("#M").attr("disabled","disabled");
            $("#F").attr('checked','checked')
        }
        $('#mail').val(user.mail);
        $('#phone').val(user.phone);
        $('#u_zjm').val(user.zjm);
        var date = new Date(user.create_time);
        var year = date.getFullYear(); // 获取年份
        var month = date.getMonth() + 1; // 获取月份（注意月份是从 0 开始计数的，所以需要加 1）
        var day = date.getDate();
        var formattedTime = year + '-' + month + '-' + day;
        $('#create_time').val(formattedTime);
    }
    function rev() {
        window.history.back();
    }
    var v = 0;
    layui.use(function () {
        var upload = layui.upload;
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        //……
        //但是，如果你的HTML是动态生成的，自动渲染就会失效
        //因此你需要在相应的地方，执行下述方法来进行渲染
        form.render();
        upload.render({
            elem: '#ID-upload-demo-btn',
            url: '<%=request.getContextPath()%>/upload_a', // 此处配置你自己的上传接口即可
            size: 5120, // 限制文件大小，单位 KB
            data:{
                u_zjm:function(){
                    console.log($('#u_zjm').val())
                    return $('#u_zjm').val();
                }
            },
            done: function(res){
                if(res.code==200){
                    layer.msg('上传成功');
                    $('#ava').attr('src','<%=request.getContextPath()%>/download_a?uid='+uidv+'&v='+v++);
                }else{
                    layer.msg(res.msg);
                }
            }
        });
    });
</script>
</body>

</html>