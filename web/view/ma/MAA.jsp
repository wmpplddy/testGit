<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="../../layui/css/layui.css">
    <script src="../../js/jquery-3.6.0.js"></script>
    <script src="../../layui/layui.js"></script>
    <style>
        fieldset {
            border: 0;
            border-top: 1px solid #ddd;
            margin-left: 10px;
        }

        fieldset legend {
            font-size: 14px;
        }
    </style>
</head>

<body>
<blockquote class="layui-elem-quote layui-text" style="font-weight: bold;">
    <fieldset>
        <legend>预约</legend>
    </fieldset>
</blockquote>
<div class="layui-tab layui-tab-card" style="height: auto;">
    <ul class="layui-tab-title">
        <li class="layui-this" style="background: rgba(255,255 ,255 ,0.5);">按日期预约</li>
        <li>按医生预约</li>
    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show" id="showd">
            <div class="layui-row">
                <div class="layui-col-md3">
                    <div class="ws-demo-static" style="margin-left: 10px;" id="dateShow">
                        <div class="layui-inline" id="ID-laydate-static-1"></div>
                    </div>
                </div>
                <div class="layui-col-md9">
                    <div class="layui-card" style="height: 323px;padding: 9px 9px 9px 9px;">
                        <h1>时间选择</h1>
                        <hr>
                        <div class="layui-btn-container layui-card-body times"
                             style="overflow:auto; height: 250px;">

                        </div>
                    </div>
                </div>
                <hr>
                <div class="layui-row">
                    <div class="layui-card" style="height: 323px;padding: 9px 9px 9px 9px;">
                        <div class="layui-card-body">
                            <form class="layui-form" action="">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">就诊科室：</label>
                                    <div class="layui-input-inline">
                                        <select name="unit" lay-verify="required" lay-search>
                                            <option value="">选择或搜索科室</option>
                                            <option value="1">layer</option>
                                            <option value="2">form</option>
                                            <option value="3">layim</option>
                                            <option value="4">element</option>
                                            <option value="5">laytpl</option>
                                            <option value="6">upload</option>
                                            <option value="7">laydate</option>
                                            <option value="8">laypage</option>
                                            <option value="9">flow</option>
                                            <option value="10">util</option>
                                            <option value="11">code</option>
                                            <option value="12">tree</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">就诊日期：</label>
                                    <div class="layui-input-inline layui-input-wrap">
                                        <input type="text" name="dates" id="dated" lay-verify="required" readonly
                                               placeholder="请在上方选择日期" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">就诊时间：</label>
                                    <div class="layui-input-inline layui-input-wrap">
                                        <input type="text" name="times" id="timed" lay-verify="required" readonly
                                               placeholder="请在上方选择时间" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-tab-item">
            1123
        </div>
    </div>
</div>
<script id="button-template" type="text/template">
    <button onmouseover="timeHover(this);" onclick="selectTime(this)" class="layui-btn layui-btn-normal layui-btn-sm">
        <i class="layui-icon layui-icon-time"></i>
        <span>{{timeRange}}</span>
    </button>
</script>
<script>
    var index = 0;
    var intervalId;
    function selectTime(obj) {
        $('#timed').val('')
        index = 0;
        clearInterval(intervalId)
        var time = $(obj).find('span').text();
        var chars = time.split('');
        intervalId = setInterval(function () {
            var c = chars;
            var f = $('#timed').val()
            console.log(index)
            if (index < chars.length) {
                $('#timed').val(f + c[index])
                index++;
            } else {
                clearInterval(intervalId);
                index = 0;
            }
        }, 20)
    }

    function timeHover(obj) {
        layui.use(function () {
            var $ = layui.$;
            var span = $(obj).find('span');
            span.stop().animate({fontSize: '14px'}, 200); // 放大字体大小
            $(obj).mouseleave(function () {
                span.stop().animate({fontSize: '12px'}, 200); // 还原字体大小
            });
        });
    }

    function generateButtons(startTime, endTime) {
        layui.use(function () {
            var $ = layui.$;
            var buttonContainer = $('.times');
            var buttonTemplate = $('#button-template').html();

            var currentTime = startTime;
            if (startTime <= '12:00') {
                buttonContainer.append("<fieldset><legend>上午</legend></fieldset>")
            } else {
                buttonContainer.append("<fieldset><legend>下午</legend></fieldset>")
            }
            while (currentTime < endTime) {
                var time = new Date('2000-01-01T' + currentTime + ':00');
                time.setMinutes(time.getMinutes() + 15);
                var timeRange = time.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                var range = currentTime + '-' + timeRange;
                currentTime = timeRange;

                var buttonHtml = buttonTemplate.replace('{{timeRange}}', range);
                buttonContainer.append(buttonHtml);
            }
            buttonContainer.append("<br>")
        })
    }

    layui.use(function () {
        var element = layui.element;
        var layer = layui.layer;
        var $ = layui.$;
        var laydate = layui.laydate;
        $('#dateShow').hide().fadeIn();
        $('#showd').animate({}, 1000)

        $(function () {

            var morningStart = '08:00';
            var morningEnd = '12:00';
            var afternoonStart = '14:30';
            var afternoonEnd = '18:30';

            generateButtons(morningStart, morningEnd);
            generateButtons(afternoonStart, afternoonEnd);
        })


        laydate.render({
            elem: '#ID-laydate-static-1',
            position: 'static',
            theme: 'molv',
            autoConfirm: false,
            disabledDate: function (date, type) {
                return date.getTime() < Date.now();
            },
            done: function (value, date, endDate) {
                console.log("123")
            },
            onConfirm: function (value, date, endDate) {
                layer.msg(value)
                $('#dated').val(value)
            }
        });

    });
</script>
</body>

</html>