/*为用户分配角色时才引入的这个js文件*/
function toDistributionRoles(uid){

    var rids = {} ;

    layui.use(['layer','table','jquery'],function(){
        var layer = layui.layer ;
        var table = layui.table ;
        var $ = layui.$ ;

        $.post('view/user/distribution.jsp',function(view){
            layer.open({
                title:'分配角色',
                type:1,
                area:[800,540],
                content:view,
                btn:['确定分配','取消'],
                yes:function(){
                    distributionSubmit();
                },
                success:function(){
                    roleTableInit();
                }
            });
        });

        //角色列表的初始化
        function roleTableInit(){
            //先加载用户相关的信息（用户基本信息，角色信息+用户上一次分配的角色信息）
            var user ;
            var roles ;

            $.ajax({
                async:false,
                url:'auth/distributionInfoForRole',
                type:'post',
                data:{'uid':uid},
                dataType:'json',
                success:function(obj){
                    user = obj.user ;
                    roles = obj.roles ;
                }
            });

            //保存初始信息中默认勾选的角色编号（上一次分配的角色编号）
            for(var i=0;i<roles.length;i++){
                var role = roles[i] ;
                if(role.LAY_CHECKED){
                    //勾选了
                    rids[role.rid]=role.rid ;
                }else{
                    //当前这个角色没有勾选，后面也一定没有勾选（java端处理了）
                    break ;
                }
            }

            //装载用户默认信息
            $('#distribution_uno').val(user.uid);
            $('#distribution_uname').val(user.uname);
            $('#distribution_truename').val(user.truename);

            //加载表格
            table.render({
                elem:'#roleGrid',
                height:320,
                cols:[[
                    {type:'checkbox',width:'10%'},
                    {title:'角色编号',width:'20%',field:'rid'},
                    {title:'角色名称',width:'20%',field:'rname'},
                    {title:'角色描述',width:'30%',field:'description'},
                    {title:'角色状态',width:'20%',field:'yl1',templet:'#yl1Column'},
                ]],
                data:roles,
                page:{
                    limit:6,
                    limits:[6,10,15]
                }
            });

            table.on('checkbox(roleGrid)', function(obj){
                if(obj.type == 'one'){
                    var rid = obj.data.rid ;
                    if(obj.checked){
                        //选中了角色，加一个编号
                        rids[rid]=rid ;
                    }else{
                        //取消了这个角色，删除编号
                        delete rids[rid] ;
                    }
                }else{
                    //all,全选，obj本身不包含数据
                    var rows = table.getData('roleGrid');
                    if(obj.checked){
                        //选中，rows中的记录都加入rids
                        for(var i=0;i<rows.length;i++){
                            var row = rows[i];
                            rids[row.rid]=row.rid ;
                        }
                    }else{
                        //取消，rows中的记录都在rids中移除
                        for(var i=0;i<rows.length;i++){
                            var row = rows[i];
                            delete rids[row.rid] ;
                        }
                    }
                }
            });
        }

        function distributionSubmit(){
            var obj = table.checkStatus('roleGrid');

            var ridStr = '' ;

            for(p in rids){
                ridStr += p+',' ;
            }

            $.post('auth/distributionRole',{'uid':uid,'ridStr':ridStr},function(responseVO){
                var code = responseVO.code ;
                var msg = responseVO.msg ;
                if(code == 0){
                    layer.alert(msg,{icon:6},function(){
                        layer.closeAll();
                    });
                }else{
                    layer.alert(msg,{icon:5});
                }
            },'json');
        }

    });


}