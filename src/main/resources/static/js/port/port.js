/**
 * 用户管理
 */
var pageCurr;
var form;
$(function () {
    layui.use(['table','laydate'], function () {
        var table = layui.table;
        laydate = layui.laydate;
        form = layui.form;
        tableIns = table.render({
            elem: '#portList',
            url: '/port/getList',
            method: 'get', //默认：get请求
            cellMinWidth: 80,
            page: true,
            response: {
                statusName: 'code', //数据状态的字段名称，默认：code
                dataName: 'data' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type: 'numbers'}
                , {field: 'serverName', title: '服务器', align: 'center', edit: 'text'}
                , {field: 'serverHost', title: '服务器IP', align: 'center', edit: 'text'}
                , {field: 'localPort', title: '本地端口', align: 'center', edit: 'text'}
                , {field: 'internetPort', title: '外网端口', align: 'center', edit: 'text'}
                , {title: '操作', width: 300, align: 'center', toolbar: '#optBar'}
            ]]
        });


        //监听工具条
        table.on('tool(portTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'delete') {
                //启动
                del (data, data.id, data.configName);
            }
        });

        table.on('edit(portTable)', function(obj){
            var value = obj.value //得到修改后的值
                ,data = obj.data //得到所在行所有键值
                ,field = obj.field; //得到字段
            console.log("data", data)
            portSubmit(data);
        });

        //监听提交
        form.on('submit(portSubmit)', function (data) {
            portSubmit(data);
            return false;
        });

    });

});

//提交表单
function portSubmit(saveData) {

    $.ajax({
        type: "POST",
        data: JSON.stringify(saveData.field?saveData.field:saveData),
        contentType: "application/json",
        url: "/port/save",
        success: function (data) {
            if (data.code === "0") {
                if(!data.id){
                    layer.msg("保存成功",{
                        time: 1500
                    }, function () {
                        layer.closeAll();
                        load();
                    });
                }

            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                load();//自定义
            });
        }
    });
}

//开通用户
function add() {
    openPort(null, "添加配置");
}

function del(obj, id, name) {
    if (null != id) {
        layer.confirm('您确定要删除吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/port/delete", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.alert("删除成功", function () {
                        layer.closeAll();
                        load(obj);
                    });
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

function openPort(data, title) {
    if (data == null || data == "") {
        $("#id").val("");
    } else {
        $("#id").val(data.id);
        $("#configName").val(data.configName);
        $("#text").val(data.text);
        $("#expireTime").val(data.expireTime);
    }
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['800px'],
        content: $('#portDialog'),
        success: function () {
            getServerList();
            laydate.render({
                elem: "#expireTime",
                type: "date"
            });
        },
    });
}


function load() {
    //重新加载table
    tableIns.reload({});
}

function getServerList(){
    $.get("/server/getList", function (data) {
        if (data.code === "0") {
            $("#serverId").empty();
            layui.form.render("select");
            $.each(data.data, function(index, item) {
                $('#serverId')
                    .append(new Option(item.serverName, item.id));
            });
            layui.form.render("select");
        } else {
            layer.alert(data.msg);
        }
    });
}
