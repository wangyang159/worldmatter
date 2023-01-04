<%--
  Created by IntelliJ IDEA.
  User: 王洋
  Date: 2022/11/4
  Time: 21:57
  首页 程序入口
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>江湖事-江湖了</title>
    <link rel = "icon" href = "/pics/xiannv.png">
    <link type="text/css" rel="stylesheet" href="/css/wait.css"/>

    <style>
        #mytext {
            width: 700px;
            height: 350px;
            background-image: url("/pics/index_gb.png");
            position: fixed;
            top: 15%;
            left: 27%;
            border-radius: 45px;
            box-shadow: inset 27px 27px 42px #afafaf,
            inset -27px -27px 42px #ffffff;
            padding-top: 40px;
        }

        #xj1{
            width: 200px;
            height: 500px;
            position: fixed;
            top: 0%;
            left: 10%;
            transform:rotate(330deg);
        }

        #xj2{
            width: 200px;
            height: 500px;
            position: fixed;
            top: 0%;
            right: 10%;
            transform:rotate(30deg);
        }

        #dz{
            width: 100%;
            height: 150px;
            position: fixed;
            bottom: 0%;
            z-index: -5;
        }

        #dz-text{
            position: relative;
            z-index: -5;
            width: 100%;
            height: 100%;
        }

        #dz-text img{
            position: absolute;
            z-index: -5;
            width: 100%;
            height: 100%;
            top: 0%;
        }
        #dz-text p{
            padding-top: 20px;
            font-family: arial, sans-serif;   /*字体*/
            color: #02A69E;   /*字体颜色*/
            font-size: 20px;   /*字体大小*/
            font-weight: bold;   /*字体粗细*/
            text-decoration: underline;   /*字体下划线*/
            text-align: center;   /*对齐方式*/
            letter-spacing: 12.2pt;   /*字体间距*/
            word-spacing: 19pt;   /*字体空格间距*/
            line-height: 2.1;   /*行间距*/
        }

        #head-text{
            width: 600px;
            height: 50px;
            box-shadow: 0px 0px 10px #00cdfe inset;
            margin: 0 auto;
            margin-top: 20px;
            border-radius: 14% 86% 17% 83% / 82% 14% 86% 18% ;
        }

        #head-text p{
            padding-top: 10px;
            font-family: times, serif;   /*字体*/
            color: #12C7C4;   /*字体颜色*/
            font-size: 20px;   /*字体大小*/
            font-weight: bold;   /*字体粗细*/
            text-align: center;   /*对齐方式*/
            letter-spacing: 3.8pt;   /*字体间距*/
            line-height: 1.5;   /*行间距*/
        }
        #pageDiv{
            position: absolute;
            left: 0px;
            bottom: 0px;
            width: 100%;
            padding-left: 20px;
        }
    </style>
</head>
<body>
    <!--
    controls显示播放控件
	autoplay页面加载完成后自动播放
	loop循环播放
    -->
    <c:if test="${mainMic}">
        <audio src="/yp/sybj.mp3" autoplay="autoplay" loop="loop"></audio>
    </c:if>

    <canvas id="starfield"></canvas>

    <div id="head-text">
        <p>江湖? 不! 是一个人的武林!!!</p>
    </div>

    <img src="/pics/xj1.png" id="xj1" />

    <div id="mytext">
        <table class="table table-hover">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">功能点</th>
                <th scope="col">描述</th>
                <th scope="col">跳转</th>
            </tr>
            </thead>
            <tbody>
<%--            <tr>--%>
<%--                <th scope="row">1</th>--%>
<%--                <td>证件照功能</td>--%>
<%--                <td>目前有两种分为有背景色证件照和无背景色个人照</td>--%>
<%--                <td>--%>
<%--                    <div class="btn-group" role="group" aria-label="Basic example">--%>
<%--                        <button type="button" class="btn btn-light"><a href="/idphoto/zjzhavebj">证件照</a></button>--%>
<%--                        <button type="button" class="btn btn-light"><a href="/idphoto/zjz">个人照</a></button>--%>
<%--                    </div>--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <th scope="row">2</th>--%>
<%--                <td>PDF转Word</td>--%>
<%--                <td>目前提供两种技术转Word,各自的优缺点见详情页</td>--%>
<%--                <td>--%>
<%--                    <div class="btn-group" role="group" aria-label="Basic example">--%>
<%--                        <button type="button" class="btn btn-light"><a href="/pdfs/appdf">AP技术</a></button>--%>
<%--                        <button type="button" class="btn btn-light"><a href="/pdfs/srpdf">SR技术</a></button>--%>
<%--                    </div>--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <th scope="row">3</th>--%>
<%--                <td>图片文字识别</td>--%>
<%--                <td>使用tess4j图片文字识别</td>--%>
<%--                <td>--%>
<%--                    <div class="btn-group" role="group" aria-label="Basic example">--%>
<%--                        <button type="button" class="btn btn-light"><a href="/imgword/toimgword">图片转文字</a></button>--%>
<%--                    </div>--%>
<%--                </td>--%>
<%--            </tr>--%>

                <c:forEach items="${data}" var="n">
                    <tr>
                        <th scope="row">${n.xh}</th>
                        <td>${n.gnName}</td>
                        <td>${n.gnMeg}</td>
                        <td>
                            <c:forEach items="${n.path}" var="j">
                                <div class="btn-group" role="group" aria-label="Basic example">
                                    <button type="button" class="btn btn-light"><a href="${j.hrefPath}">${j.hrefName}</a></button>
                                </div>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!--分页 -->
        <div id="pageDiv"><jsp:include page="/common/bookstappages.jsp"></jsp:include></div>
    </div>

    <img src="/pics/xj2.png" id="xj2" />

    <div id="dz">
        <div id="dz-text">
            <p>
                击筑复击筑，欲歌双泪横。<br />
                宝刀重如命，命如鸿毛轻。
            </p>
            <img src="/pics/dz.jpg" />
        </div>
    </div>

    <link type="text/css" rel="stylesheet" href="/css/xk2.css" />
    <link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css" />
    <script type="text/javascript" src="/js/xk2.js"></script>
    <script type="text/javascript" src="/js/jzsf.js"></script>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <script type="text/javascript">
        function goPage(pageNum) {
            location.href="/masterhub?pageNum="+pageNum;
        }
    </script>
</body>
</html>
