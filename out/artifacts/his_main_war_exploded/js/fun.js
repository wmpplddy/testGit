layui.config({
    base: 'treetable-lay/'
}).use(['treeTable','jquery','form','element','layer','table','tree'],function() {
    var treeTable = layui.treeTable;
    var $ = layui.$;
    var form = layui.form ;
    var element = layui.element ;
    var layer = layui.layer ;
    var table = layui.table ;
    var tree = layui.tree ;


    var tt = treeTable.render({
        elem:'#funGrid'
        ,height:'full-120'
        ,url:'fun/list'
        ,toolbar:'#toolbar'
        ,cols:[[        //第一层数组相当于<thead>，第二层数组相当于<tr>，每一个{}表示<td>
            {type:'checkbox',width:'4%'},
            {title:'编号',width:'6%',field:'fid'},
            {title:'功能名称',width:'15%',field:'fname'},
            {title:'功能类别',width:'8%',field:'ftype',templet:'#ftypeBox'},
            {title:'功能链接',width:'12%',field:'furl'},
            {title:'权限标识',width:'15%',field:'auth_flag'},
            {title:'父级编号',width:'8%',field:'pid'},
            {title:'父级名称',width:'10%',field:'pname'},
            {title:'操作',width:'27%',templet:'#tool'}
        ]]
        ,tree:{         //树形结构设置
            isPidData:true // 根据功能id和父功能pid  来实现子父关系
            ,idName:'fid'
            ,pidName:'pid'
            ,iconIndex:2     //图标显示的单元格的索引位置
        }
        ,done:function(){
            tt.expandAll();
        }
    });

    treeTable.on('toolbar(funGrid)',function(obj){
        switch(obj.event){
            case "toExpand" : toExpand();break ;
            case "toFold" : toFold();break ;
            case "toAddRoot":toAddRoot();break ;
        }
    });

    //insTb.expandAll();  // 展开全部
    // insTb.foldAll();  // 折叠全部
    function toExpand(){
        tt.expandAll();
    }

    function toFold(){
        tt.foldAll();
    }

    function toAddRoot(){
        toAdd(-1,'主菜单') ;
    }

    treeTable.on('tool(funGrid)',function(obj){
        switch(obj.event){
            case "toAddChild":toAddChild(obj.data.fid , obj.data.fname , obj.data.auth_flag);break ;
            case "toEdit" : toEdit(obj.data.fid); break ;
            case "toDelete" : toDelete(obj.data.fid) ;break ;
        }
    });

    function toAddChild(pid,pname,auth_flag){
        toAdd(pid,pname,auth_flag);
    }

    function toAdd(pid,pname,auth_flag){
        //访问新建网页模板
        $.post('view/fun/add.jsp',null,function(view){
            layer.open({
                title:'新建功能',
                type:1,
                area:[400,560],
                content:view,
                btn:['保存','取消'],
                yes:function(){
                    //我们想使用form自带的验证机制
                    //就需要使用form自带的提交事件
                    $('#funSubmitBtn').click();
                },
                success:function(){
                    //窗口加载完毕，下拉框需要重新渲染
                    form.render();
                    //将默认的父功能id和name装载到组件中
                    $('#pid').val(pid);
                    $('#pname').val(pname);
                    if(auth_flag){
                        //传递了这个参数，创建子功能时会传递
                        $('#auth_flag').val(auth_flag);
                    }
                }
            });
        });
    }

    function toEdit(fid){
        //先根据fid查找到原始数据
        $.post('fun/edit',{'fid':fid},function(fun){
            //先获得编辑网页模板
            $.post('view/fun/add.jsp',null,function(view){
                //获得的网页模板内容显示在一个弹出层中
                layer.open({
                    title:'编辑功能',
                    type:1,
                    area:[400,560],
                    content:view,
                    btn:['修改','取消'],
                    yes:function(){
                        //点击窗口确定按钮，就相当于点击了触发表单提交的按钮
                        //就可以触发表单提交事件，就可以表单验证了
                        $('#funSubmitBtn').click();
                    },
                    success:function(){
                        //窗口展示完毕时的回调
                        form.render();
                        form.val('funAddForm',fun);
                        //显示默认图标
                        $('#iconBox').attr('class','layui-icon '+fun.yl1);
                        changeParentMenu(fun.fid) ;

                    }
                });
            });
        },'json');
    }

    function toDelete(fid){
        $.post('fun/childrenCount',{'fid':fid},function(count){
            if(count!=0){
                //表示存在子级，不能删除，给出提示
                layer.alert('还有子级功能，无法删除',{icon:0});
            }else{
                //没有子级，是一个叶子功能，可以删除，先询问
                layer.confirm('是否确认删除',{icon:3},function(){
                    //点击了确定
                    $.post('fun/delete',{'fid':fid},function(responseVO){
                        if(responseVO.code==0){
                            layer.alert(responseVO.msg,{icon:6},function(){
                                tt.reload();
                                layer.closeAll();
                            });
                        }else{
                            layer.alert(responseVO.msg,{icon:5});
                        }
                    },'json');
                });
            }
        })
    }

    //----------------调整父级---------------------
    //调整所属的父级菜单
    function changeParentMenu(curr_fid){
        $('#pname').click(function(){
            var index = layer.open({
                type:1,
                title:'更换父级',
                area:[200,300],
                content:'<div id="tree"></div>',
                success:function(){

                    $.post('fun/parentMenuList',{curr_fid:curr_fid},function(data){

                        var inst1 = tree.render({
                            elem: '#tree'  //绑定元素
                            ,data: data
                            ,click: function(obj){
                                $('#pname').val(obj.data.title);
                                $('#pid').val(obj.data.id);
                                layer.close(index);
                            }
                        });

                    },'json');


                }
            });
        });
    }
    //--------------------------------------------

    form.on('submit(*)',function(data){
        //由于模板有添加和修改2个作用
        //根据fid进行判断

        //如果是一个保存操作，就不需要传递fid
        var url ;

        if(data.field.fid==''){
            //保存
            delete data.field.fid;
            url = 'fun/save';
        }else{
            //修改
            url = 'fun/update' ;
        }

        $.post(url,data.field,function(responseVO){
            var code = responseVO.code ;
            var msg = responseVO.msg ;
            if(code == 0){
                layer.alert(msg,{icon:6},function(){
                    layer.closeAll();
                    tt.reload();
                });
            }else{
                layer.alert(msg,{icon:5});
            }
        },'json');

        //不使用form的请求，使用ajax请求
        return false ;
    });


});


function selectIcon(){
    layui.use(['jquery','layer'],function(){
        var $ = layui.$ ;
        var layer = layui.layer ;

        $.post('view/fun/icon.jsp',function(view){
            var i = layer.open({
                title:'选择图标',
                type:1,
                content:view,
                area:[400,400],
                success:function(){
                    $('.iconBox a').dblclick(function(){
                        var icon = $(this).attr('icon');

                        $('#iconBox').attr('class','layui-icon '+icon);
                        $('#yl1').val(icon);
                        layer.close(i);
                    });
                }
            });
        })

    });
}
