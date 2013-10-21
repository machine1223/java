<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>电子邮件系统 - 登录</title>
		<style type="text/css">
			html{
				background-color: #D6B99C;
    height: 100%;
			}
			.login{
				margin-left: auto;
			    margin-right: auto;
			    padding-top: 200px;
			    width: 300px;
			    padding-top: 200px;
			}
			form{
				width:300px;
			}
		</style>	
	</head>
	<body>
		<div class="login">	      	
          <form method="post" action="login.action">
			  <table>
		      <tr>
		        <td align="right" width="65">用户名：</td>
		        <td><input id="userName" type="text" name="user.userEmail" class="input_yh" style="width: 200px;"></td>				        
		      </tr>
		      <tr>
		        <td align="right" width="65">密　码：</td>
		        <td><input id="passwords" type="password" name="user.userPassword" class="input_yh" style="width: 200px;"></td>
		      </tr>
		       <tr>
		         <td height="25" align="right">&nbsp;</td>
		         <td align="left"><input type="submit" id="loginBtn" value="登 录" class="ipt_b"> <a href="toRegister.action">注册</a></td>
		      </tr>
		      </table>
		  </form>
   		</div>
	</body>
</html>
