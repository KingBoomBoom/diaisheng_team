$(function (){
    getDevices();
    layui.use('form', function(){
        var form = layui.form;
        form.render();
        form.on('select(device)', function (data) {
            getModels(data.value);
        });
        form.on('select(model)', function (data) {
            getDataPoints(data.value);
        });

    });
    laydate.render({
        elem: '#start_time'
        ,type: 'datetime'
    });
    laydate.render({
        elem: '#end_time'
        ,type: 'datetime'
    });
    var urlData = decodeURI(location.search); //获取url中"?"符后的字串
    var theRequest = new Object();
    if (urlData.indexOf("?") != -1) {
        var str = urlData.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
        }
    }
    //获取当前时间
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var minute=date.getMinutes()<=9?"0"+date.getMinutes():date.getMinutes();
    var initEndTime = year + seperator1 + month + seperator1 + strDate+" "+date.getHours()+":"+minute+":"+date.getSeconds();
    var lastHour = (date.getHours()-1)<0?0:(date.getHours()-1);//判断0点时结果-1异常
    var initStartTime = year + seperator1 + month + seperator1 + strDate+" "+lastHour+":"+minute+":"+date.getSeconds();
    $("#start_time").val(initStartTime);
    $("#end_time").val(initEndTime);
    initEcharts();
    myCharts.showLoading();
    var name = [];
    var series = [];
    $.ajax({
        type:"post",
        url:"/diaisheng/valueadmin/getdatabetweendate",
        data:{"data":theRequest.data,"stime":initStartTime,"etime":initEndTime},
        dataType:"json",
        success:function(data){
                $.each(data.dataValue,function(key,value){
                    console.log(data.dataValue);
                    name.push(value.createTime);
                });
                $.each(data.dataValue,function(key,value){
                    series.push(value.value);
                });
                myCharts.hideLoading();
                myCharts.setOption({
                    xAxis:{
                        data:name
                    },
                    series:{
                        data:series
                    }
                });
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            myChart.hideLoading();
        }
    });
});

function query(){
    initEcharts();
    var shebei = document.getElementById("shebei").value;
    var congji = document.getElementById("congji").value;
    var data = document.getElementById("data").value;
    var stime = document.getElementById("start_time").value;
    var etime = document.getElementById("end_time").value;
    myCharts.showLoading();
    var name = [];
    var series = [];
    $.ajax({
        type:"post",
        url:"/diaisheng/valueadmin/getdatabetweendate",
        data:{"data":data,"stime":stime,"etime":etime},
        dataType:"json",
        success:function(data){
                $.each(data.dataValue,function(key,value){
                    console.log(data.dataValue);
                    name.push(value.createTime);
                });
                $.each(data.dataValue,function(key,value){
                    series.push(value.value);
                });
                myCharts.hideLoading();
                myCharts.setOption({
                    xAxis:{
                        data:name
                    },
                    series:{
                        data:series
                    }
                });
        },
        error: function (data) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            myChart.hideLoading();
        }
    });

}

function getDataPoints(modelId){//获取对应数据点列表
    $("#data").html('<option value="">直接选择或搜索选择</option>');
    $.ajax({
        type: "post",
        url: "/diaisheng/datapointadmin/getdatapoint",
        data: {"dataModelId": modelId},
        dataType: "json",
        success: function (data) {
            $.each(data.dataPointList, function (key, value) {
                //      $("#data").options.add(new Option(value.dataPointName, value.dataPointID));
                $("#data").append("<option value='"+value.dataPointId+"'>"+value.dataPointName+"</option>");
                renderForm();
            });
        },
        error: function (data) {
            alert("数据请求失败");
        }
    });
}
function getModels(deviceId) {//获取对应从机列表
    $("#congji").html('<option value="">直接选择或搜索选择</option>');
    $.ajax({
        type: "POST",
        url: "/diaisheng/modeladmin/getdatamodel",
        data: {"deviceId": deviceId},
        dataType: "json",
        success: function (data) {
            $.each(data.dataModelList, function (key, value) {
                //$("congji").options.add(new Option(value.dataModelName, value.dataModelId));
                $("#congji").append("<option value='"+value.dataModelId+"'>"+value.dataModelName+"</option>");
                renderForm();
            });
        },
        error: function (data) {
            alert("数据请求失败");
        }
    });
}
function getDevices(){//获取设备列表
    $("#shebei").html('<option value="">直接选择或搜索选择</option>');
    $.ajax({
        url: '/diaisheng/deviceadmin/getdevice',
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            if (!data.success){
                if (data.redirect){
                    window.location.href=data.redirect;
                }
            }else{
                $.each(data.device, function (key, value) {
                    $("#shebei").append("<option value='"+value.deviceId+"'>"+value.deviceName+"</option>");
                    renderForm();
                });
            }
        },
        error:function (data) {
            console.log(data.errMsg);
        }
    });
}


function downLoad(){
    var shebei = document.getElementById("shebei").value;
    var congji = document.getElementById("congji").value;
    const selectedValueArr = document.querySelector("#data").options;
    var stime = document.getElementById("start_time").value;
    var etime = document.getElementById("end_time").value;
    var data = document.getElementById("data").value;
    console.log(shebei);
    console.log(congji);
    console.log(data);
    console.log(stime);
    console.log(etime);
    myCharts.showLoading();
    /*
    $.ajax({
        type:"post",
        url:"/diaisheng/valueadmin/downLoadExcel",
        data:{"data":data,"stime":stime,"etime":etime},
        dataType:"json",
        success:function(result){
            myCharts.hideLoading();
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("请求数据失败!");
            alert(errorMsg);
            myChart.hideLoading();
        }=
    })*/
    var str="/diaisheng/valueadmin/downLoadExcel?"+"data="+data+"&stime="+stime+"&etime="+etime;
    window.open(str);
}
function initEcharts() {
    var option = {
        title:{
            text:'Line Test',
            subtext:"Mountain Technology"
        },
        tooltip:{
            trigger:'axis'
        },
        legend:{
            data:'出水量'
//                data:[]
        },
        grid:{
            left:'3%',
            right:'4%',
            bottom:'3%',
            containLabel:true
        },
        xAxis:{
            type:'category',
            //data:['18:00','18:20','18:40','18:50','19:00','19:05','19:10','19:20','19:30','19:40','19:50'],
            data:[],
            axisLine:{
                lineStyle:{
//                        color:''
                }
            },
        },
        yAxis: {
            type: 'value'
        },
        dataZoom: [
            {
                type: 'slider',
                xAxisIndex: 0,
                start: 10,
                end: 60
            },
            {
                type: 'inside',
                xAxisIndex: 0,
                start: 10,
                end: 60
            },
            {
                type: 'slider',
                yAxisIndex: 0,
                start: 30,
                end: 80
            },
            {
                type: 'inside',
                yAxisIndex: 0,
                start: 30,
                end: 80
            }
        ],

        series:{
            name:'出水量',
            type:'line',
            symbolSize:3,
            color:['red'],
            //data:[1000,200,500,600,800,1100,500,800,600,500,600],
            data:[],
            //关键点：实线虚线
            smooth:true,
            itemStyle:{
                normal:{
                    lineStyle:{
                        witdth:2,
                        type:'solid'
                    }
                }
            }

        }

    }
    myCharts.setOption(option);
}
function renderForm(){
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}