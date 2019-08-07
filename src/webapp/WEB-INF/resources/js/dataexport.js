$(function (){
    layui.use('form', function(){
        var form = layui.form;
        form.render();
        form.on('select(device)', function (data) {
//        var = $("#shebei").options.length;
            var device = $("#shebei");
            console.log(data.value);
            debugger;
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
    layui.use('form', function () {
        var form = layui.form;

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
    var initStartTime = year + seperator1 + month + seperator1 + strDate+" "+(date.getHours()-1)+":"+minute+":"+date.getSeconds();
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
    console.log(shebei);
    console.log(congji);
    console.log(data);
    console.log(stime);
    console.log(etime);
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
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            myChart.hideLoading();
        }


    });

}

function onChange1(){
    var x = document.getElementById("congji");
    var y = document.getElementById("data");

    y.options.length = 0;

    if(x.selectedIndex == 0)
    {
        y.options.add(new Option("出水温度","32442",false,true));
        y.options.add(new Option("出水累计流量小数","30948"));
        y.options.add(new Option("出水累计流量整数","30946"));
        y.options.add(new Option("出水瞬时流量","30945"));
    }
    else if(x.selectedIndex == 1)
    {
        y.options.add(new Option("远程启动","35290"));
        y.options.add(new Option("COD即时数据","30947"));
    }
    else if(x.selectedIndex == 2)
    {
        y.options.add(new Option("进水瞬时流量","32445"));
        y.options.add(new Option("进水温度","32443"));
        y.options.add(new Option("进水正累计流量小数","32279"));
        y.options.add(new Option("进水正累计流量整数","32278"));
    }
    else if(x.selectedIndex == 3)
    {
        y.options.add(new Option("罐壁温度","32270"));
        y.options.add(new Option("罐内温度","32269"));
        y.options.add(new Option("设定温度","32268"));
    }
    else{
        y.options.add(new Option("高浓排进水温度","41610"));
        y.options.add(new Option("高浓排瞬时流量","41609"));
        y.options.add(new Option("高浓排正积累流量小数","41607"));
        y.options.add(new Option("高浓排正积累流量整数","41608"));
    }
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