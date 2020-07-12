/**
 * 登录
 */
$(function () {
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        form.on("submit(login)", function (data) {
            login(data);
            return false;
        });
        var path = window.location.href;
        if (path.indexOf("kickout") > 0) {
            layer.alert("您的账号已在别处登录；若不是您本人操作，请立即修改密码！", function () {
                window.location.href = "/login";
            });
        }
    })
})

function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    $.post("/login", $("#useLogin").serialize(), function (data) {
        if (data.code === "0") {
            console.log(data.data.token)
            setCookie('token', data.data.token);
            window.location = "/home"
        } else {
            layer.msg('登录失败: ' + data.msg, {
                time: 1500
            });
        }
    });
}


function setCookie(name, value) {
    var Days = 7;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
