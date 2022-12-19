<%--
  Created by IntelliJ IDEA.
  User: 王洋
  Date: 2022/11/5
  Time: 14:45
  无背景证件照的页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人照</title>
    <link rel = "icon" href = "/pics/long.png">
    <link type="text/css" rel="stylesheet" href="/css/wait.css"/>
    <link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css" />
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
</head>
<body>

    <!-- 星空画布 -->
    <canvas id="canvas1" class="canvas"></canvas>

    <div id="mytext">

        <div id="contentHolder">
            <video id="video" autoplay style="background-color: #000"></video>
            <!-- 拍照画布 -->
            <canvas id="canvas" style="display: none;"></canvas>
            <img id="imgXX" src="" />
        </div>

        <div id="budiv">
            <button type="button" class="btn btn-outline-primary" id="btn_snap" onclick="takePhoto()">拍照</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-secondary" id="btn_sc" onclick="push()">保存</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-success" id="budl" onclick="download()" filedir="" >下载结果文件</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-danger" id="btn_xk" onclick="sq()">舍弃</button>&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-info"  id="sy"><a href="/masterhub/">回到首页</a></button>
        </div>

    </div>

    <link type="text/css" rel="stylesheet" href="/css/xk1.css"/>
    <script type="text/javascript" src="/js/xk1.js"></script>
    <script type="text/javascript" src="/js/jzsf.js"></script>

    <style>
        #contentHolder{
            margin-left: 15%;
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

        //证件照渲染
        const cvs = document.getElementById('canvas')
        const video = document.getElementById('video')
        cvs.width = video.width = ${xs_x}
        cvs.height = video.height = ${xs_y}
        const {width, height} = cvs
        const ctx = cvs.getContext('2d')

        const constraints = {
            video: {
                width,
                height
            }
        }
        navigator.mediaDevices.getUserMedia(constraints).then(stream => {
            video.srcObject = stream
            video.onloadedmetadata = () => video.play()
        })
        function takePhoto() {
            ctx.drawImage(video, 0, 0, width, height)
            document.getElementById('imgXX').src = canvas.toDataURL('image/png')
        }

        function push(){
            var datas = document.getElementById('imgXX').getAttribute("src")

            //ajax
            $.ajax({
                url:"/idphoto/zjzFromBase64",
                data:{"base":datas},
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

        //舍弃
        function sq() {
            document.getElementById('imgXX').src = ""
            document.getElementById("budl").setAttribute("filedir","")
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
</html>
