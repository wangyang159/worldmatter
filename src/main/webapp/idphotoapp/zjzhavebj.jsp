<%--
  Created by IntelliJ IDEA.
  User: 王洋
  Date: 2022/11/1
  Time: 20:21
  有背景的证件照页面，使用了抠图算法
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>证件照</title>
    <link rel = "icon" href = "/pics/taiji.png">
    <link type="text/css" rel="stylesheet" href="/css/wait.css"/>
    <link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css"/>
</head>
<body>

    <canvas id="canvas1" class="canvas"></canvas>

    <div id="mytext">

        <div id="contentHolder">
            <video id="video" autoplay style="background-color: #000"></video>
            <!-- 拍照画布 -->
            <canvas id="canvas"></canvas>
            <canvas id="canvas_bg" style="display: none"></canvas>
            <img id="imgXX" src=""/>
        </div>

        <div id="budiv">
            <input type="color" oninput="changeBgColor(this.value)" value="#0066cc" />&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-primary" id="btn_snap" onclick="takePhoto()">拍照</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-secondary" id="btn_sc" onclick="push()">保存</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-success" id="budl" onclick="download()" filedir="" >下载结果文件</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-danger" id="btn_xk" onclick="sq()">舍弃</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-info"><a href="/masterhub/" id="sy">回到首页</a></button>
        </div>

    </div>

    <!-- 星空需要的js-->
    <link type="text/css" rel="stylesheet" href="/css/xk1.css"/>
    <script type="text/javascript" src="/js/xk1.js"></script>
    <script type="text/javascript" src="/js/jzsf.js"></script>
    <!-- 证件照背景需要的js -->
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <script src="/idphotosrc/tf.min.js"></script>
    <script src="/js/zjzbjinit.js"></script>

    <style>
        #contentHolder{
            margin-left: 10%;
        }
        #sy{
            text-decoration-line: none;
            color:#000;
        }
        #mytext {
            width: 1100px;
            height: 600px;
            background-image: url("/pics/zjzbg.jpg");
            position: fixed;
            z-index: 1;
            top: 10%;
            left: 15%;
            border-radius: 45px;
            box-shadow: inset 27px 27px 42px #afafaf,
            inset -27px -27px 42px #ffffff;
            padding-top: 40px;
        }
        #budiv{
            display: flex;
            justify-content: center;
            align-items: center;
            width: 600px;
            margin-left: 23%;
            border-radius: 79px;
            background: #e0e0e0;
            box-shadow:  23px 23px 32px #b1b1b1,
            -23px -23px 32px #ffffff;
        }
    </style>

    <script>
        //解决一个bootstrap的bug莫名其妙会给img标签错位
        $("#imgXX").css("vertical-align","baseline")
        //进入页面等待效果给人一个友好的界面
        $("body").append("<div id='wait'><div id='mes'><p>你好骚年，服务正在加载呦，稍等一下</p></div></div>")

        const cvs = document.getElementById('canvas')
        const video = document.getElementById('video')
        const canvasBg = document.getElementById('canvas_bg')
        cvs.width = video.width = canvasBg.width = ${xs_x}
        cvs.height = video.height = canvasBg.height = ${xs_y}
        const ctxBg = canvasBg.getContext('2d')

        function changeBgColor(v) {
            ctxBg.clearRect(0, 0, cvs.width, cvs.height)
            ctxBg.fillStyle = v || '#06c'
            ctxBg.fillRect(0, 0, cvs.width, cvs.height)
        }
        function takePhoto() {
            document.getElementById('imgXX').src = canvas.toDataURL('image/png')
        }
        changeBgColor()
        main()

        function push(){
            var datas = document.getElementById('imgXX').getAttribute("src")

            //ajax
            $.ajax({
                url:"/idphoto/zjzFromBase64",
                data:{base:datas},
                dataType:"json",
                success:function (data){
                    if(data["resultCode"]!=1){
                        alert("resultCode="+data["resultCode"]+"| msg="+data["msg"])
                    }else {
                        document.getElementById("budl").setAttribute("filedir",data["msg"])
                        alert("转换成功，可以下载获得结果啦")
                    }
                },
                error:function (data){
                    alert(data["resultCode"] + data["msg"])
                }
            })
        }

        function sq() {
            document.getElementById('imgXX').src = ""
            document.getElementById("budl").setAttribute("filedir","");
        }

        //下载
        function download(){
            var budl = document.getElementById("budl")
            var b1 = budl.getAttribute("filedir");
            budl.setAttribute("filedir","")

            if(b1 == null || b1 == ""){
                alert("请先美美的拍一张呦")
                return
            }

            window.location.href="/idphoto/download?path="+b1
        }
    </script>
</body>
</body>
</html>
