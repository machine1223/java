<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册页面</title>
<style type="text/css">
			html{
				background-color: #D6B99C;
    height: 100%;
			}
			.register{
				margin-left: auto;
			    margin-right: auto;
			    padding-top: 200px;
			    width: 300px;
			    padding-top: 200px;
			}
			form{
				width:300px;
			}
			input{
				width: 200px;
			}
		</style>	
</head>
<body>
<div class="register">
<form action="register.action" method="post">
<table>
	<tbody>
		<tr>
			<td>Email</td>
			<td><input type="text" name="user.userEmail" /></td>
		</tr>
		<tr>
			<td>密码</td>
			<td><input type="password" name="user.userPassword" /></td>
		</tr>
		<tr>
			<td>公司</td>
			<td><input type="text" name="user.company" /></td>
		</tr>
		<tr>
			<td>组织</td>
			<td><input type="text" name="user.organization" /></td>
		</tr>
		<tr>
			<td>市</td>
			<td><input type="text" name="user.city" /></td>
		</tr>
		<tr>
			<td>省</td>
			<td><input type="text" name="user.provence" /></td>
		</tr>
		<tr>
			<td>国家</td>
			<td><input type="text" name="user.country" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="提交" /></td>
		</tr>

	</tbody>
</table>
</form>
</div>
</body>
</html>