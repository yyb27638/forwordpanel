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
            elem: '#forwardList',
            url: '/getPortForwardList',
            method: 'get', //默认：get请求
            cellMinWidth: 80,
            page: true,
            response: {
                statusName: 'code', //数据状态的字段名称，默认：code
                dataName: 'data' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type: 'numbers'}
                , {field: 'localPort', title: '本地端口', align: 'center'}
                , {field: 'remoteHost', title: '中转域名(IP)', align: 'center'}
                , {field: 'remoteIp', title: '中转IP', align: 'center'}
                , {field: 'remotePort', title: '中转端口', align: 'center'}
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
        table.on('tool(forwardTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'start') {
                //启动
                startForward(data, "启动中转");
            } else if (obj.event === 'stop') {
                //编辑
                stopForward(data, "编辑");
            }
        });

        //监听提交
        form.on('submit(portForwardSubmit)', function (data) {
            startPortForwardSubmit(data);
            return false;
        });

    });

});

//提交表单
function startPortForwardSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#startForwardForm").serialize(),
        url: "/startForward",
        success: function (data) {
            if (data.code === "0") {
                layer.alert("启动成功", function () {
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

function startForward(data, title) {
    $("#localPort").val(data.localPort);
    $("#remoteHost").val(data.remoteHost);
    $("#remotePort").val(data.remotePort);
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['600px'],
        content: $('#startForward')
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

