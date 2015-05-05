<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/jquery.min.js"></script>
<script>
	function nextRoute() {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				//alert(xmlhttp.responseText);
				document.getElementById("myDiv").innerHTML =xmlhttp.responseText;
			}
		}
		$("#nextroute").hide();
		$("#nextrouteack").show();
		xmlhttp.open("GET", "NextRoute", true);
		xmlhttp.send();
	}
	
	function nextRouteAck() {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				//alert(xmlhttp.responseText);
				document.getElementById("myDiv").innerHTML =xmlhttp.responseText;
			}
		}
		$("#nextroute").show();
		$("#nextrouteack").hide();
		var route=document.getElementById("myDiv").innerHTML;
		setPoint(route);
		xmlhttp.open("GET", "NextRouteAck?route="+route, true);
		xmlhttp.send();
	}
</script>

</head>
<body onload="nextRoute();">

<h1 align="centre">Ambulance navigation</h1>
<canvas id="myCanvas" width="1000" height="600" align="centre" style="border:1px solid #000000;"></canvas>

	<h1>Map Loading Soon</h1>
	<div id="myDiv"></div>

	<input id="nextroute" type="button" value="nextRoute" onclick="nextRoute()">
	<input id="nextrouteack" type="button" value="AcknowledgeRoute" onclick="nextRouteAck()">


<script type="text/javascript">
var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");

var map={};
map["1"]="500,100,500,200";
map["2"]="500,200,450,300";
map["3"]="500,200,550,300";
map["4"]="450,300,500,400";
map["5"]="550,300,500,400";
map["6"]="550,300,600,500";
map["7"]="500,400,600,500";
var key;
var value;
var co;
var a;
var b;
var c;
var d;
var x;
var y;
var ft;
var m;
var n;
var p;
var q;



render();

function render()
{
for(i=1;i<=7;i++)
{

	key=i.toString();
	value=map[key];
	co=value.split(",");
	a=parseInt(co[0]);
	b=parseInt(co[1]);
	c=parseInt(co[2]);
	d=parseInt(co[3]);
	ctx.fillStyle = 'black';
	ctx.moveTo(a,b);
	ctx.lineTo(c,d);
	ctx.font = "20px Arial";
	ctx.fillText(i,(a+c)/2,(b+d)/2);
}

ctx.stroke();
}

function setPoint(rid)
{
	key=rid.toString();
	value=map[key];
	co=value.split(",");
	m=parseInt(co[0]);
	n=parseInt(co[1]);
	p=parseInt(co[2]);
	q=parseInt(co[3]);
	
	x=m;
	y=n;	
	loop();
}

function move()
{
	ctx.clearRect(0,0,1000,600);
	render();
	ctx.fillStyle = 'green';
	ctx.beginPath();
	ctx.arc(x,y, 6, 0, 2 * Math.PI, false);
	ctx.closePath();
	ctx.fill();
	

	x=x+(p-m)/100;
	y=y+(q-n)/100;
	
	
	check();
}

function loop() {
      ft=window.setTimeout(loop, 50);
      move();
   }

function check()
{
	if(x==p && y==q)
		window.clearTimeout(ft);
}
</script>
</body>
</html>