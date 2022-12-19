<%--
  Created by IntelliJ IDEA.
  User: 王洋
  Date: 2022/11/5
  Time: 18:28
  sr技术转PDF
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>sr技术转word</title>
    <link rel = "icon" href = "/pics/xianjian.png">
    <link type="text/css" rel="stylesheet" href="/css/wait.css"/>
    <link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css" />
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
</head>
<body>
    <style>
        #mytext {
            width: 700px;
            height: 400px;
            background-image: url("/pics/xjsn.png");
            position: fixed;
            z-index: 1;
            top: 25%;
            left: 28%;
            border-radius: 45px;
            box-shadow: inset 27px 27px 42px #afafaf,
            inset -27px -27px 42px #ffffff;
            padding-top: 40px;
        }
        #formdiv{
            background-color: aliceblue;
            padding-left: 10%;
            padding-right: 10%;
            padding-top: 15px;
            height: 75px;
        }
        #filenames{
            white-space:nowrap;
            -o-text-overflow: ellipsis;
            text-overflow:ellipsis;
            overflow: hidden;
        }

        #budiv{
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 20px;
            margin-bottom: 20px;
        }
        #dz{
            width: 700px;
            height: 180px;
            background-color: aliceblue;
            border-radius: 88% 12% 88% 12% / 0% 100% 0% 100%  ;
            padding-left: 20px;
            padding-right: 20px;
        }

        #dz p{
            padding-left: 50px;
        }
    </style>

    <canvas id="canvas1" class="canvas"></canvas>

    <div id="mytext">
        <form enctype="multipart/form-data" method="post" >
            <div class="form-group" id="formdiv">
                <label for="exampleFormControlFile1">请上传你需要转换的PDF文件</label>
                <input type="file" class="form-control-file" id="exampleFormControlFile1" name="file" multiple="multiple" >
                <p id="filenames">您以选择的文件如下：</p>
            </div>
        </form>

        <div id="budiv">
            <button type="button" class="btn btn-outline-primary" onclick="saveObject()">上传文件</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-success" id="budl" onclick="download()" filedir="" >下载结果文件</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-danger"><a href="/masterhub/">回到首页</a></button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-warning" onclick="reset()">重置</button>
        </div>

        <div id="dz">
            <p>
                功能说明：<br/>
                1、有文本框水印，不过可以删除 <br/>
                2、对应WPD-PDF可以转换成单个的文字图片 <br/>
                3、对于正常的文字格式和图片格式PDF，结果是在段落的格式上每一行是一个文本框，并且基本不现AP那样多张图片识别为一张的情况，但图文一起解析时结果中的英文字体被所用技术定死为了Arial-OKITQ，而且由于本技术属于免费技术所以转换结果可能出现某次无法使用问题需要重新转换<br/>
            </p>
        </div>
    </div>

    <link type="text/css" rel="stylesheet" href="/css/xk1.css"/>
    <script type="text/javascript" src="/js/xk1.js"></script>
    <script type="text/javascript" src="/js/jzsf.js"></script>

    <script>
        //选择文件时展示文件名
        $("#exampleFormControlFile1").on("change",function(){
            let files = $(this).prop("files")
            let names = $.map(files,function (v) {
                return v.name
            })
            let result = ""
            for (let i in names){
                if(i == names.length-1){
                    result+=names[i]
                }else {
                    result+=names[i]+" | "
                }
            }
            $("#filenames").html("您以选择的文件如下："+result)
        })

        //上传
        function saveObject() {
            let f = $("#exampleFormControlFile1")[0].files
            if(f==null){
                alert("您还没有选择要上传的文件哦")
                return
            }
            //封装文件
            let formData = new FormData();
            for(let i=0;i<f.length;i++){
                formData.append("files",f[i]);
            }

            // ajax请求
            $.ajax({
                type: "POST",
                url: "/pdfs/srpdftodocx",
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function () {
                    $("body").append("<div id='wait'><div id='mes'><p>你好骚年，服务正在加载呦，稍等一下</p></div></div>")
                },
                success: function (data) {
                    $("#wait").remove()
                    if(data["resultCode"]!=5){
                        alert("resultCode="+data["resultCode"]+"| msg="+data["msg"])
                    }else {
                        document.getElementById("budl").setAttribute("filedir",data["msg"])
                        alert("转换成功，可以下载获得结果啦")
                    }
                },
                error: function (data) {
                    $("#wait").remove()
                    if(data==null){
                        alert("请确定您的PDF文件无异常,或文件内容被加密等 ! ")
                    }else{
                        alert("resultCode="+data["resultCode"]+"| msg="+data["msg"])
                    }
                }
            });
        }

        //下载
        function download(){
            var budl = document.getElementById("budl")
            var b1 = budl.getAttribute("filedir");
            budl.setAttribute("filedir","")

            if(b1 == null || b1 == ""){
                alert("请先上传PDF文件")
                return
            }

            window.location.href="/pdfs/download?path="+b1
        }

        //重置方法
        function reset() {
            var obj = document.getElementById("exampleFormControlFile1") ;
            obj.outerHTML=obj.outerHTML;
        }
    </script>
</body>
</html>
