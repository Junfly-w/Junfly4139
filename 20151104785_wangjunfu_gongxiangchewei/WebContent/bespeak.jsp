<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>车位预定</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script src="bootstrap/js/jquery-1.9.0.js"></script>
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<script src="bootstrap/js/bootstrap.min.js"></script>
<style type="text/css">
	table {
	max-width: 100%;
	background-color: transparent;
	border-collapse: collapse;
	border-spacing: 0
	}

.table {
	width: 100%;
	margin-bottom: 20px;
	margin:auto
}

.table th,.table td {
	padding: 8px;
	line-height: 80px;
	text-align: left;
	vertical-align: top;
	border-top: 1px solid #ddd
}

.table th {
	font-weight: bold
}

.table thead th {
	vertical-align: bottom
}

.table-bordered {
	border: 1px solid #ddd;
	border-collapse: separate;
	*border-collapse: collapse;
	border-left: 0;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px
}
.table .table {
	background-color: #fff
}
.td_color{
	background-color: #f0ad4e;
}

.td_add_color{
	background-color: green;
}

.td_class {
	height: 20px;
	width: 20px;
	border-bottom-left-radius: 4px;
	border-top-left-radius: 4px;
	border-top: 0 none;
	line-height: 80px;
	padding: 8px;
	text-align: left;
	vertical-align: top;
	border-left: 1px solid #ddd;
}
</style>
<script type="text/javascript">
	var flagp;
	function getParkNumber(id) {
		flagp = false;
		window.setTimeout(getParkNumber_Click,500);
		function getParkNumber_Click(){
		if (flagp != false) {
				return;
		}
	    var td = $("#" + id);
		var value = td.text();
		if (confirm("是否确定预定")) {
		var type = $("#type").val();
		var name = $("#parkName").val();
		var count = id.split("_")[1];
		$.ajax({
			url : "addCarNumberByNameAndType.action",
			type : "post",
			dataType : "json",
			data : "name=" + name + "&type=" + type + "&count=" + count,
			success : function(result) {
				if ("no" == result.isOk) {
					alert("该用户不可预约");
				} else {
					td.addClass("td_color");
					td.attr("value", "true");
				}
			}
		});
		}
		}
	}

	function removeParkNumber1(id) {
		if (confirm("是否确定取消预定")) {
		    flagp = true;
			var td = $("#" + id);
			var value = td.text();
			var type = $("#type").val();
			var name = $("#parkName").val();
			var count = id.split("_")[1];
			$.ajax({
				url : "removeCarNumberByNameAndType.action",
				type : "post",
				dataType : "json",
				data : "name=" + name + "&type=" + type + "&count=" + count,
				success : function(result) {
					if ("no_permission" == result.isOk) {
						alert("你无权修改");
					} else {
						td.attr("value", "false");
						td.removeClass("td_color");
						td.removeAttr("style");
					}
				}
			});
		}
	}

	function queryParkNumberByName() {
		var type = $("#type").val();
		var name = $("#parkName").val();
		$.ajax({
					url : "queryCarNumberByNameAndType.action",
					type : "post",
					dataType : "json",
					data : "name=" + name + "&type=" + type,
					success : function(result) {
						if (result.userInfo.length == 0 && result.count=="0") {
							alert("查询结果为空");
							$("#tbody").empty();
						} else {
							var arry = new Array();
							if (result.userInfo.length != 0) {
								arry = result.userInfo.split(",");
							}
							var innerHtmls = "";
							var sum = (result.count / 8 == 0 ? parseInt(result.count / 8)
									: parseInt(result.count / 8 + 1));
							for (var i = 0; i < sum * 8; i++) {
								if (i == 0) {
									var flag = "";
									var bool = false;
									for (var j = 0; j < arry.length; j++) {
										if ((i + 1) == arry[j]) {
											bool = true;
											break;
										}
									}
									if (bool) {
										innerHtmls += '<tr><td id=d_'
												+ (i + 1)
												+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" style="background-color:#f0ad4e" value="true">'
												+ (i + 1) + '号停车位</td>';
									} else {
										innerHtmls += '<tr><td id=d_'
												+ (i + 1)
												+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" value="false">'
												+ (i + 1) + '号停车位</td>';
									}
								} else if (i % 8 == 0 && i != 0
										&& (i != (result.count - 1) || (result.count % 8 != 0))) {
									var bool = false;
									for (var j = 0; j < arry.length; j++) {
										if ((i + 1) == arry[j]) {
											bool = true;
											break;
										}
									}
									if (bool) {
										innerHtmls += '</tr><tr>'
												+ '<td id=d_'
												+ (i + 1)
												+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" style="background-color:#f0ad4e" value="true">'
												+ (i + 1) + '号停车位</td>';
									} else {
										innerHtmls += '</tr><tr>'
												+ '<td id=d_'
												+ (i + 1)
												+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" value="false">'
												+ (i + 1) + '号停车位</td>';
									}
								} else if (i == (result.count - 1)
										&& result.count == sum * 8) {
									innerHtmls += '</tr>';
								} else {
									if ((i + 1) > result.count)
										innerHtmls += '<td></td>';
									else {
										var bool = false;
										for (var j = 0; j < arry.length; j++) {
											if ((i + 1) == arry[j]) {
												bool = true;
												break;
											}
										}
										if (bool) {
											innerHtmls += '<td id=d_'
													+ (i + 1)
													+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" style="background-color:#f0ad4e" value="true">'
													+ (i + 1) + '号停车位</td>';
										} else {
											innerHtmls += '<td id=d_'
													+ (i + 1)
													+ ' onclick="getParkNumber(id)" ondblclick="removeParkNumber1(id)" value="false">'
													+ (i + 1) + '号停车位</td>';
										}
									}
								}
							}
							$("#tbody").empty();
							$("#tbody").prepend(innerHtmls);
						}
					}
				});
	}
	function test(){
		console.log("219");
	}
</script>
</head>
<body>
	<div class="container">
		<div>
			<ul class="breadcrumb">

				<li><i class="icon-list"></i> <a href="index.jsp">返回主页</a>

					<i class="icon-angle-right">></i></li>

				<li><a href="#">车位预定</a></li>

			</ul>
		</div>
	</div>
	<div class="container" >
	<div style="color: red">
	*注意:预约车位请单击相应车位号，若取消使用请双击。
	</div>
	<div style="border: solid 1px; border-color: #DDDDDD">
			<table class="tableSet">
				<tr>
					<td>车库名：</td>
					<td><select name="carParkInfo.parkName" id="parkName" required>
					      <s:iterator var="list" value="listData" status="status">
							<option value="${list}">${list}</option>
						  </s:iterator>
					</select> 
					<span style="color: red;">*</span>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;消费类型：</td>
					<td><select name="carParkInfo.type" id="type" required>
							<option value="0">租</option>
							<option value="1">售</option>
					</select> 
					<span style="color: red;">*</span>
					</td>
					<td><a data-dismiss="modal" id="find" style="width: 100px"
						onclick="queryParkNumberByName()" class="btn btn-success" data-toggle="modal">
							<i class="icon-white icon-search"></i> 查询
					</a>
					</td>
				</tr>
			</table>
		</div>
		<br>
		<table style="margin:0px auto;text-align: center;">
			<tr>
				<td>可选</td>
				<td style="background-color: white;border:1px solid #ccc;" class="td_class"></td>
				<td>已预订</td>
				<td style="background-color: #f0ad4e;" class="td_class" ondblclick="test()"></td>
			</tr>
		</table>
		<br>
		<table class="table table-bordered">
			<tbody id="tbody" >
			
			</tbody>	
		</table>
	</div>
</body>
</html>