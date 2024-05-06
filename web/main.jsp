<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%--<% if(session.getAttribute("user")==null)response.sendRedirect("login.jsp"); %>--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>创新医疗信息管理系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="./layui/css/layui.css" />
    <script src="./layui/layui.js"></script>
    <script src="./js/jquery-3.6.0.js"></script>
    <style>
        .layui-tree-txt,
        .layui-tree-txt a {
            color: #eee;
        }
    </style>
</head>

<body>
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo layui-hide-xs layui-bg-black">医院预约管理系统</div>
        <!-- 头部区域（可配合layui 已有的水平导航） -->
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item layui-hide layui-show-sm-inline-block">
                <a href="javascript:;">
                    <img id="avatar" src="<%=request.getContextPath()%>/download_a?uid=${sessionScope.user.uid}" alt="" class="layui-nav-img">
                    ${sessionScope.user.uname}
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:changePath('view/user/updatePwd.jsp');">修改密码</a></dd>
                    <dd><a href="javascript:;">帮助</a></dd>
                    <dd><a href="javascript:toExit();">注销</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item" lay-header-event="menuRight" lay-unselect>
                <a href="javascript:;">
                    <i class="layui-icon layui-icon-more-vertical"></i>
                </a>
            </li>
        </ul>
    </div>



    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" lay-filter="test">
                <li class="layui-nav-item">
                    <a href="">主页</a>
                </li>
                <li class="layui-nav-item layui-nav-itemed">
                    <a class="" href="javascript:userList();;">用户管理</a>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">数据中心</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:dataCenter();">医生数据</a></dd>
                        <dd><a href="javascript:;">在线用户</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item"><a href="javascript:maa();">预约中心</a></li>
                <li class="layui-nav-item"><a href="javascript:;">预约管理</a></li>
            </ul>
        </div>
    </div>
    <div class="layui-body">
        <iframe id="bd" width="100%" height="100%" name="content" src="home.jsp"></iframe>
    </div>
    <div class="layui-footer">
        <p align="center">&copy; 版权所有：创新医疗信息集团</p>
    </div>
</div>

<script>
    var vf = 0;
    var f = setTimeout(function(){
        $('#avatar').attr('src','<%=request.getContextPath()%>/download_a?uid=${sessionScope.user.uid}&v='+vf++);
    },10000);
    function changePath(path){
        console.log(path)
        $('#bd').attr('src',''+path);
    }
    function toExit() {
        layui.use(function () {
            var layer = layui.layer;
            layer.confirm('是否注销？', { icon: 0 }, function () {
                location.href="exit";
                layer.msg('已注销', { icon: 1 });
            }, function () {
                layer.msg('取消注销', { icon: 1 });
            })
        })
    }
    function userList(){
        $('#bd').attr('src','view/user/list.jsp');
    }
    function maa(){
        $('#bd').attr('src','view/ma/MAA.jsp');
    }
    function dataCenter(){
        $('#bd').attr('src','view/sys/dataCenter.jsp');
    }
    //JS
    layui.use(['element', 'layer', 'util'], function () {
        var element = layui.element;
        var layer = layui.layer;
        var util = layui.util;
        var $ = layui.$;

        //头部事件
        util.event('lay-header-event', {
            menuLeft: function (othis) { // 左侧菜单事件
                layer.msg('展开左侧菜单的操作', { icon: 0 });
            },
            menuRight: function () {  // 右侧菜单事件
                layer.open({
                    type: 1,
                    title: '更多',
                    content: `<div id="LAY_adminPopupAbout" class="layui-layer-content">
    <div class="layui-card-header">公告</div>
    <div class="layui-card-body layui-text layadmin-about">
      <blockquote class="layui-elem-quote" style="border: none;">

  <p>公告内容：`+"最近有不法分子试图混入医院，盗窃患者及家属财产。请各位保管好各自的财产。"+`</p>
  </blockquote>
  </div>
  <div class="layui-card-header">创新医疗集团</div>
  <div class="layui-card-body layui-text layadmin-about">
    <blockquote class="layui-elem-quote" style="border: none;">
      公司发源于湖南衡阳，现在中国上海设立总部,并且公司现在配备有全球最先进的现代医疗化设备，以及全球最顶尖的主治医师,集团从成立至今已收纳13,000名贤士,并于2013年上榜全球500强。
    </blockquote>
  </div>
  </div>
  <div class="layui-layer-setwin"></div><span class="layui-layer-resize"></span>
  `,
                    area: ['260px', '100%'],
                    offset: 'rt', // 右上角
                    anim: 'slideLeft', // 从右侧抽屉滑出
                    shadeClose: true,
                    scrollbar: false
                });
            }



        });
    });
</script>

</body>

</html>