/**
 * 用户管理
 */
var pageCurr;
var form;
$(function () {
    layui.use('table', function () {
        var table = layui.table;
        form = layui.form;
        tableIns = table.render({
            elem: '#clashList',
            url: '/getClashList',
            method: 'get', //默认：get请求
            cellMinWidth: 80,
            page: true,
            response: {
                statusName: 'code', //数据状态的字段名称，默认：code
                dataName: 'data' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type: 'numbers'}
                , {field: 'configName', title: '配置名称', align: 'center'}
                , {field: 'expireTime', title: '到期时间', align: 'center'}
                , {field: 'disabled', title: '是否禁用', align: 'center'}
                , {title: '操作', width: 300, align: 'center', toolbar: '#optBar'}
            ]],
            done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                console.log(curr);
                $("[data-field='disabled']").children().each(function () {
                    if ($(this).text() == true) {
                        $(this).text("停用")
                    } else if ($(this).text() == false) {
                        $(this).text("启用")
                    }
                });
            }
        });


        //监听工具条
        table.on('tool(clashTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'delete') {
                //启动
                delClash (data, data.id, data.configName);
            } else if (obj.event === 'edit') {
                //编辑
                openCLash(data, "编辑配置");
            } else if (obj.event === 'copyLink') {
                //编辑
                copyLink(data);
            }
        });

        //监听提交
        form.on('submit(clashSubmit)', function (data) {
            clashSubmit(data);
            return false;
        });

    });

});

//提交表单
function clashSubmit(data) {
    $.ajax({
        type: "POST",
        data: JSON.stringify(data.field),
        contentType: "application/json",
        url: "/saveClash",
        success: function (data) {
            if (data.code === "0") {
                layer.alert("保存成功", function () {
                    layer.closeAll();
                    load();
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}

//开通用户
function addClash() {
    openCLash(null, "添加配置");
}

function copyLink(data) {
  let link = window.location.host+'/clash/'+data.id;
    layer.alert(link);
}

function delClash(obj, id, name) {
    if (null != id) {
        layer.confirm('您确定要删除' + name + '配置吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/deleteClash", {"id": id}, function (data) {
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

function openCLash(data, title) {
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
        content: $('#clashDialog')
    });
}

function stopForward(data) {
    console.log(data)
    $.ajax({
        type: "POST",
        data: data,
        url: "/stopForward",
        success: function (data) {
            if (data.code === "0") {
                layer.alert("停止成功", function () {
                    layer.closeAll();
                    load();
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}


function load() {
    //重新加载table
    tableIns.reload({});
}

