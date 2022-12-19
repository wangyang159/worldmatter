<%--
  Created by IntelliJ IDEA.
  User: 王洋
  Date: 2022/11/15
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>识别图片文字</title>
    <link rel = "icon" href = "/pics/long.png">
</head>
<body>
    <!--
			作者:王洋
			时间:2022-11-07 14:54:16
			业务含义: 图片转文字效果
		-->
    <style>
        #mytext {
            width: 700px;
            height: 350px;
            background-image: url("/pics/index_gb.png");
            position: fixed;
            top: 20%;
            left: 35%;
            border-radius: 45px;
            box-shadow: inset 27px 27px 42px #afafaf,
            inset -27px -27px 42px #ffffff;
            padding-top: 40px;
            padding-left: 50px;
        }
        #yuan_img{
            margin-top: 20px;
            width: 250px;
            height: 250px;
        }
        #f_img{
            margin-left: 20px;
        }
        #control_div{
            width: 100px;
            height: 150px;
            display: inline-block;
            position: fixed;
            margin-top: 50px;
            margin-left: 10px;
            margin-right: 20px;
            padding-left: 20px;
        }
        #text_div{
            width: 250px;
            height: 250px;
            background-color: antiquewhite;
            display: inline-block;
            margin-left: 150px;
            overflow:auto;
        }
        #con_se{
            margin-left: 10px;
        }
        #con_se_rate{
            margin-left: 5px;
        }
        .con_bu{
            margin-top: 10px;
            display: block;
            width: 70px;
            background-color: beige;
        }
        select{
            margin-top: 5px;
        }
        #xq{
            width: 400px;
            height: 200px;
            background-color: #ffffff;
            position: fixed;
            padding-top: 10px;
            padding-left: 20px;
            padding-right: 20px;
            top: 30%;
            left: 5%;
            border-radius: 50px;
            background: #e0e0e0;
            box-shadow:  20px 20px 60px #bebebe,
            -20px -20px 60px #ffffff;
        }
    </style>

    <canvas id="starfield"></canvas>

    <div id="mytext">
        <!-- 左边的图片内容 -->
        <form enctype="multipart/form-data" method="post" id="f_img">
            <input type="file" id="tmp_imgfile"/><br/>
        </form>
        <img src="" id="yuan_img" style="" onerror="this.src='/pics/long.png';this.onerror=null"/>

        <!-- 中间的操作控件 -->
        <div id="control_div">
            <form method="post" id="con_se">
                <!-- 语言 -->
                <select name="lang" id="con_se_lang">
                    <option selected="selected" value=1>中文</option>
                    <option value=2>英文</option>
                </select>
                <!-- 语速度 -->
                <select name="rate" id="con_se_rate">
                    <option value=0.5>0.5</option>
                    <option value=0.7>0.7</option>
                    <option selected="selected" value=1.0>1.0</option>
                    <option value=1.2>1.2</option>
                    <option value=1.5>1.5</option>
                </select>
            </form>
            <button class="con_bu" onclick="load_zh()">转换</button>
            <button class="con_bu" onclick="load_ld()">朗读</button>
            <button class="con_bu" onclick="load_zt()">暂停</button>
            <button class="con_bu" onclick="load_jx()">继续</button>
            <button class="con_bu" onclick="load_cz()">重置声音</button>
        </div>

        <!-- 右边的文字展示 -->
        <div id="text_div">
            <p id="text_div_p"></p>
        </div>
    </div>

    <div id="xq">
        <p>
            功能说明：<br/>
            1、当前功能采用tess4j-java语言架构转换,如果你的图片本身存在字体模糊的情况，则会发生识别错误,对此情况为了方便使用者,对功能结果的纠错,本功能自带了语音朗读功能,帮助使用者进行文本的纠错,避免视觉疲劳。<br/>
            2、目前仅支持中文、英文图片识别，并请务必在使用本功能转换过程中，选择好语言类型。<br/>
            3、当对结果检查时，如果要使用语音辅助，请选择合适的语速,不支持中途修改
        </p>
    </div>

    <link rel="stylesheet" type="text/css" href="/css/xk2.css" />
    <script type="text/javascript" src="/js/xk2.js"></script>
    <script type="text/javascript" src="/js/jzsf.js"></script>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>

    <script type="text/javascript">
        //初始化语言对象
        var utterance = new SpeechSynthesisUtterance()
        // utterance.lang = "zh-CN"
        // utterance.rate = 0.5
        utterance.pitch = 1.2
        //获取声音列表并设置声音为普通话
        speechSynthesis.addEventListener("voiceschanged", () => {
            var voices = speechSynthesis.getVoices();
            utterance.voice = voices[0]
        })
        //初始状态确保是没有开始朗读的
        speechSynthesis.cancel()

        //文件input修改时文件转base64并回写到img
        $("#tmp_imgfile").on("change",function(){
            //修改一个图片以为着如果有声音立马停止
            speechSynthesis.cancel()
            //右侧内容盒子也要清空
            document.getElementById("text_div_p").innerHTML=""
            //把文件转为base64字符串
            let f = $("#tmp_imgfile")[0].files[0]
            if(window.FileReader){
                let reader = new FileReader()
                reader.readAsDataURL(f)
                reader.onloadend = function (e){
                    document.getElementById("yuan_img").setAttribute("src",e.target.result)
                }
            }else{
                alert("文件回写异常")
            }
        })

        //转换方法
        function load_zh(){
            //获取当前选定的语言、文件对象的base64字符串
            let lang_tmp = $("#con_se_lang option:selected").val()
            let filebase64 = document.getElementById("yuan_img").getAttribute("src")

            //传回后台拿到转换结果，回写到文本展示的div中
            $.ajax({
                url:"/imgword/toword",
                dataType:"json",
                data:{"lang":lang_tmp,"filebase64":filebase64},
                success:function (data){
                    if(data["resultCode"]==3){
                        document.getElementById("text_div_p").innerHTML=data["msg"]
                    }else{
                        alert("发生异常："+data["resultCode"])
                    }
                }
            })
        }

        //播放控制:朗读
        function load_ld(){
            //如果文本还没有转换不开始朗读
            if($("#text_div p").html()=="" || $("#text_div p").html()==null){
                return
            }
            //获取语言
            let lang_tmp = $("#con_se_lang option:selected").val()
            //获取语速
            utterance.rate = $("#con_se_rate option:selected").val();
            //设置语言文化代码
            if(lang_tmp==1){
                utterance.lang = "zh-CN"
            }else if(lang_tmp==2){
                utterance.lang = "en-US"
            }
            //获取文本并朗读
            utterance.text=$("#text_div p").html()
            speechSynthesis.speak(utterance)
        }
        //播放控制:暂停
        function load_zt(){
            speechSynthesis.pause()
        }
        //播放控制:继续
        function load_jx(){
            speechSynthesis.resume()
        }
        //重置声音
        function load_cz() {
            speechSynthesis.cancel()
        }
    </script>

</body>
</html>
