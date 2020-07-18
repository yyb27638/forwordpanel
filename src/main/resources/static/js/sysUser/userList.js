/**
 * 用户管理
 */
var pageCurr;
var form;
$(function () {
    layui.use(['table','laydate'], function () {
        var table = layui.table;
        form = layui.form;
        laydate = layui.laydate;
        tableIns = table.render({
            elem: '#uesrList',
            url: '/user/getUserList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response: {
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type: 'numbers'}
                , {field: 'username', title: '用户名', align: 'center'}
                , {field: 'userPhone', title: '手机号', align: 'center'}
                , {field: 'telegram', title: 'TG', align: 'center'}
                , {field: 'dataLimit', title: '流量限制', align: 'center'}
                , {field: 'regTime', title: '注册时间', align: 'center'}
                , {field: 'expireTime', title: '到期时间', align: 'center'}
                , {field: 'disabled', title: '是否禁用', align: 'center'}
                , {title: '操作', width:300,  align: 'center', toolbar: '#optBar'}
            ]],
            done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码

                $("[data-field='disabled']").children().each(function () {
                    if ($(this).text() === true) {
                        $(this).text("禁用")
                    } else if ($(this).text() === false) {
                        $(this).text("启用")
                    }
                });
                //得到数据总量
                //console.log(count);
                pageCurr = curr;
            }
        });

        userPortList = table.render({
            elem: "#userPortTableList",
            url: '/userport/getPortList',
            cols: [[
                {field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'localPort', title: '端口', align: 'center'}
                , {field: 'dataLimit', title: '流量限制', align: 'center'}
                , {field: 'disabled', title: '是否禁用', align: 'center'}
                , {field: 'createTime', title: '创建时间', align: 'center'}
                , {field: 'updateTime', title: '修改时间', align: 'center'}
                , {title: '操作', width: 200, align: 'center', toolbar: '#assignPortBar'}
            ]],
            response: {
                statusName: 'code', //数据状态的字段名称，默认：code
                dataName: 'data' //数据列表的字段名称，默认：data
            },
            data: [],
            done: function (res, curr, count) {
                $("[data-field='disabled']").children().each(function () {
                    if ($(this).text() == true) {
                        $(this).text("禁用")
                    } else if ($(this).text() == false) {
                        $(this).text("启用")
                    }
                });
            }
        })

        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                //删除
                delUser(data, data.id, data.username);
            } else if (obj.event === 'edit') {
                //编辑
                openUser(data, "编辑");
            } else if (obj.event === 'enable') {
                //恢复
                enableUser(data, data.id);
            } else if (obj.event === 'disable') {
                //禁用
                disableUser(data, data.id);
            } else if (obj.event === 'assignPort') {
                showUserPortList(data)
            }
        });

        //监听提交
        form.on('submit(userSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });

        //监听工具条
        table.on('tool(userPortTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                //删除
                delUserPort(data, data.id);
            } else if (obj.event === 'enable') {
                enableUserPort(data, data.id)
            } else if (obj.event === 'disable') {
                disableUserPort(data, data.id)
            }
        });

        //监听提交
        form.on('submit(userPortSubmit)', function (data) {
            // TODO 校验
            userPortFormSubmit(data);
            return false;
        });


    });

});

//提交表单
function formSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#userForm").serialize(),
        url: "/user/setUser",
        success: function (data) {
            if (data.code === "0") {
                layer.alert("保存成功", function () {
                    layer.closeAll();
                    load(obj);
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

//提交表单
function userPortFormSubmit(data) {
    console.log(data)
    data.field.id = data.field.userPortId
    const userId = data.field.userId;
    $.ajax({
        type: "POST",
        data: JSON.stringify(data.field),
        contentType: "application/json",
        dataType: "json",
        url: "/userport/save",
        success: function (data) {
            if (data.code == '0') {
                layer.close(addPortDialog)
                loadUserPort(userId)
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试");
        }
    });
}

//开通用户
function addUser() {
    openUser(null, "开通用户");
}

function openUser(data, title) {
    if (data == null || data == "") {
        $("#id").val("");
    } else {
        $("#id").val(data.id);
        $("#addUsername").val(data.username);
        $("#expireTime").val(data.expireTime);
        $("#mobile").val(data.userPhone);
        $("#addTelegram").val(data.telegram);
        $("#addDataLimit").val(data.dataLimit);
        $("#mobile").val(data.userPhone);
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['600px'],
        content: $('#setUser'),
        success: function () {
            laydate.render({
                elem: "#expireTime",
                type: "date"
            })
        },
        end: function () {
            cleanUser();
        }
    });
}


function showUserPortList(data) {
    $("#userId").val(data.id);
    loadUserPort(data.id)
    layer.open({
        type: 1,
        title: "分配端口",
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['1000px', '500px'],
        content: $('#userPortList'),
        end: function () {
            cleanUser();
        }
    });
}

function addUserPort(data) {
    if (data == null || data == "") {
        $("#userPortId").val("");
    } else {
        $("#userId").val(data.userId);
        $("#userPortId").val(data.id);
        $("#localPort").val(data.localPort);
    }
    addPortDialog = layer.open({
        type: 1,
        title: "分配端口",
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['600px'],
        content: $('#setUserPort'),
        end: function () {
            cleanUser();
        }
    });
}

function delUser(obj, id, name) {
    if (null != id) {
        layer.confirm('您确定要删除' + name + '用户吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/user/delete", {"id": id}, function (data) {
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


function delUserPort(obj, id) {
    if (id) {
        const confirmDialog = layer.confirm('您确定要删除端口:' + obj.localPort + '？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.get("/userport/delete", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.close(confirmDialog)
                    loadUserPort(obj.userId);
                } else {
                    layer.close(confirmDialog)
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

//恢复
function enableUser(obj, id) {
    if (null != id) {
        layer.confirm('您确定要启用吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/user/enable", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.closeAll();
                    load(obj);
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

//恢复
function disableUser(obj, id) {
    if (null != id) {
        layer.confirm('您确定要禁用吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/user/disable", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.closeAll();
                    load(obj);
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

function enableUserPort(obj, id) {
    if (null != id) {
        var enableDialog =  layer.confirm('您确定要启用吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/userport/enable", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.close(enableDialog)
                    loadUserPort(obj.userId);
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

//恢复
function disableUserPort(obj, id) {
    if (null != id) {
       var disableDialog = layer.confirm('您确定要禁用吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/userport/disable", {"id": id}, function (data) {
                if (data.code === "0") {
                    layer.close(disableDialog)
                    loadUserPort(obj.userId);
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

function load(obj) {
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function loadUserPort(id) {
    //重新加载table
    userPortList.reload({
        where: {"userId": id}
    });
}

function cleanUser() {
    $("#username").val("");
    $("#mobile").val("");
    $("#password").val("");
    $('#roleId').html("");
    $('#telegram').html("");
    $('#userPhone').html("");
}
