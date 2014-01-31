var webmail ;
if(!webmail) webmail = {} ;
if(typeof webmail != "object") throw new Error("无法创建名字空间！") ;
if(!webmail.Utils) webmail.Utils = {} ;
if(typeof webmail.Utils != "object") throw new Error("无法创建名字空间！") ;

//初始化公用的全局变量
$(document).ready(function(){
	window.ctxPath = $("#ctxPath").val() ;
}) ;
/**
 * 当出现异常的时候，会返回消息,即如果返回的内容中包含此信息表示后台抛出了
 * 异常，应该特殊显示。
 * @type {String}
 */
webmail.Utils.ErrorTitle = "<div>ErrorInfo</div>" ;
/**
 * 让destination对象继承source对象
 * 即：将source对象中的属性都拷贝到destination对象中。
 * @param {} destination
 * @param {} source
 * @return {}
 */
webmail.Utils.extend = function(destination, source) {
  for (var property in source)
    destination[property] = source[property];
  return destination;
} ;
/**
 * 构建动态下拉菜单，动态根据数据拼装成下拉列表。
 * 目前需要动态构建下拉菜单的只有：邮件列表和邮件查看
 * 页面中的”移动“ 两处地方。
 * 
 * @param {} name
 */
init_moveto_list = function(domId,folderName) {
	var boxs=$("#mailBoxs").data("hasExistMailBoxs");
	var lis=[];
	if(domId == "viewMailMoveTo")
	{
		for(var i=0;i<boxs.length;i++)
		{
			if(folderName!=boxs[i])
				lis.push("<li><a href='javascript:void(0)' title='"+boxs[i]+"' onclick='vm_menu_moveMailDetail(this)'>"+boxs[i].cut(7)+"</a></li>");
		}
		$("#viewMailMoveTo").html(lis.join("")) ;
	}
	if(domId == "mailListMoveTo")
	{
		for(var i=0;i<boxs.length;i++)
		{
			if(folderName!=boxs[i])
				lis.push("<li><a href='javascript:void(0)' title='"+boxs[i]+"'>"+boxs[i].cut(7)+"</a></li>");
		}
		$("#mailListMoveTo").html(lis.join(""));
	}
} ;
/**
 * 发信前检查域名为本地域(邮件地址后缀的域名由集中管理系统添加)的用户
 * （如收件人地址中包括xx@nfschina.com和yy@163.com，只检查以nfschina.com为后缀的用户）
 * @param {} recipients 收件人的Email地址数组
 * @return 所有不存在Email地址集合
 */
webmail.Utils.checkEmails = function(recipients) {
	var notExistedArray = [] ;
	webmail.Utils.ajax({dataType:"json", method: "post",
		url : "areCheckEmails.action",
		params : {"emailAddressArray":recipients},
		async  : false,
		callback : function(data) {
			if(data.jsonStatus)
			{
				$.each(data.result,function(i,isExisted){
					if(!isExisted) notExistedArray.push(recipients[i]) ;
				});
			}else{nfschinaDialogAlert(data.errorMessage);}
		}
	}) ;
	return notExistedArray ;
} ;
/**
 * 内部使用的ajax操作，封装了对Session过期的判断
 * @param {} options
 */
webmail.Utils.ajax = function(options) {
	if(options.async ===  undefined) {
		options.async = true ;
	}
	if(options.cache === undefined) {
		options.cache = true ;
	}
	if(options.global === undefined) {
		options.global = true ;
	}
	var opts = {
		dataType : options.dataType || "html",
		type	 : options.method || "post",
		url		 : options.url || "",
		data	 : options.params || {},
		async	 : options.async,
		cache	 : options.cache,
		global	 : options.global,
		error	 : function(XMLHttpRequest, textStatus, errorThrown) {
			parseXmlHttpRequest(XMLHttpRequest,textStatus);
		},
		success  : function(data, textStatus) {
				if(typeof data == "string" && new RegExp(webmail.Utils.ErrorTitle,"i").test(data)) {
					var patt = new RegExp("<div id=\"errMsg\">.*</div>","i");
					var result = patt.exec(data)+"";
					result = result.replace(/id=\"errMsg\"/, "");
					nfschinaDialogAlert(result,300000,"错误提示");
					return ;
				}
				if(options.callback && $.isFunction(options.callback)) {
					options.callback(data,textStatus) ;
				}
				var tar = options.target ;
				if(tar)
					$(tar).html(data) ;
		}
	} ;
 	$.ajax(opts) ;
} ;
//验证邮箱是否正确
webmail.Utils.checkEmail = function(email){
	if(/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(email)){
		if(email.split("@")[0].length>40)
			return false;
		return true;
	}
	return false;
} ;
//验证输入框输入的值是否正确，只允许字母，数字，-，_，=和汉字
webmail.Utils.checkInput = function(input){
	if(/^[\w-=\u4E00-\u9FA5]*$/.test(input)){
		return true;
	}
	return false;
} ;
/**
 * 分页组件初始化
 */
webmail.Utils.init_ml_pagination = function(totalPage,ajaxOptions,currentPageNo) {
	var selectNode = $("#select_page_inbox") ;
	var btnNxt = $(".btnNxt") ;
	var btnPre = $(".btnPre") ; 
	var optArr = new Array();
	for(var i = 1 ; i <= totalPage ; i++) {
		optArr.push("<option value='"+i+"'>"+i+"/"+totalPage+"</option>") ;
	}	
	if(totalPage == 0) {
		optArr.push("<option value='1'>0/0</option>") ;
	}
	var htmlStr = optArr.join('');
	selectNode.html(htmlStr);
	selectNode.val(currentPageNo) ;
	btnPre.css("visibility","hidden").click(function(){
		selectNode.val(parseInt(selectNode.val()) - 1) ;
		selectNode.trigger("change") ;
		//翻页后清空全选框的勾选
		$("#msgnum-checkbox").removeAttr("checked");
	}) ;

	if(totalPage <= 1) {
		btnNxt.css("visibility","hidden") ;
	}
	if(currentPageNo == 1) {
		btnPre.css("visibility","hidden") ;
	}else {
		btnPre.css("visibility","visible") ;
	}
	btnNxt.click(function(){
		selectNode.val(parseInt(selectNode.val()) + 1) ;
		selectNode.trigger("change") ;
		//翻页后清空全选框的勾选
		$("#msgnum-checkbox").removeAttr("checked");
	}) ;
	selectNode.change(function(){
		//翻页后清空全选框的勾选
		$("#msgnum-checkbox").removeAttr("checked");
		//做两件事：
		//1. 根据当前的页数来显示和隐藏上一页和下一页按钮
		//2. 根据当前的页数来发送异步请求
		var currentPage = parseInt($(this).val()) ;
		if(currentPage == totalPage) {
			btnNxt.css("visibility","hidden") ;
		}else {
			btnNxt.css("visibility","visible") ;
		}
		if(currentPage == 1) {
			btnPre.css("visibility","hidden") ;
		}else {
			btnPre.css("visibility","visible") ;
		}
		var options = {
			url    :   "",
			params :   {},
			method :   "post",
			//target :   "#mailListInclude",
			dataType : 'json',
			callback : function(data,textStatus){
				btnNxt.attr("disabled",false) ;
				btnPre.attr("disabled",false) ;
			}
		} ;
		$.extend(options,ajaxOptions || {}) ;
		options.params = options.params || {} ;
		if(typeof options.params == "string") {
			if($.trim(options.params) == "") {options.params = {} ;}
			else { options.params += "&currentPage="+currentPage; }
		}
		if(typeof options.params == "object") {
			$.extend(options.params,{'currentPage':currentPage}) ;
		}
		btnNxt.attr("disabled",true) ;
		btnPre.attr("disabled",true) ;
		if($.trim(options.url) == "viewMailListContinue.action"){
			if(typeof map.get(options.params.folderName).get(parseInt(options.params.currentPage)) == 'undefined'){
				$.extend(options,{callback:function(data,textStatus){
					showContinuePage(data);
					btnNxt.attr("disabled",false) ;
					btnPre.attr("disabled",false) ;
					var pageMap = map.get(options.params.folderName);
					pageMap.put(parseInt(options.params.currentPage),data);
					map.put(options.params.folderName,pageMap);
				}}); 
				webmail.Utils.ajax(options) ;
			} else {
				var data = map.get(options.params.folderName).get(parseInt(options.params.currentPage));
				showContinuePage(data);
				btnNxt.attr("disabled",false) ;
				btnPre.attr("disabled",false) ;
			}
			ishashchange = false;
			var realArr = window.location.href.split("_");
			var realUrl = realArr[0];
			if(realUrl.indexOf("#") != -1){
				window.location.href = realArr[0] + "_" + options.params.currentPage;
			} else {
				window.location.href = realArr[0] + "#" + mailBoxMap.get(options.params.folderName) + "_" + options.params.currentPage;
			}			
		} else {
			$.extend(options,{callback:function(data,textStatus){
				showContinuePage(data);
				btnNxt.attr("disabled",false) ;
				btnPre.attr("disabled",false) ;
			}});
			webmail.Utils.ajax(options) ;//搜索不缓存
		}
		$("body").focus();
	}) ;
} ;
/**
 * 调整高度，使得元素的高度撑满整个窗口
 * @param cssRule   要设置的元素的css定位器
 * @param winHeight 要设置的减去头的高度之后的实际高度
 */
webmail.Utils.fixHeight = function(cssRule, winHeight) {
	var forSettingNode = $(cssRule) ;
	var realHeight = forSettingNode.data("realHeight") ;
	if(realHeight == null) {
		realHeight = forSettingNode.height() ;
		forSettingNode.data("realHeight",realHeight) ;
	}
	var forSettingHeight = winHeight > realHeight ? winHeight : realHeight ;
	forSettingNode.height(forSettingHeight) ;
} ;

webmail.Utils.fixSystemSettingHeight = function(cssRule) {
	//添加高度自适应处理
	webmail.Utils.fixHeight(cssRule,$(window).height() - 70) ;
	$(window).resize(function(){
		webmail.Utils.fixHeight(cssRule,$(window).height() - 70) ;
	}).trigger("resize") ;
} ;

webmail.Utils.escapeHtml = function(content) {
	if(content == null) return null ;
	return content.replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(new RegExp(" ","g"),"&nbsp;").replace(/"/g,"&quot;") ;
} ;
/**
 * 检查字符串的长度，如果超过限制则截断，并返回限定范围内的字符串
 * @param text 原始字符串
 * @param len  字符串限定长度
 * @return 如果超过限制则返回限定范围内的字符串，反之返回原字符串
 */
webmail.Utils.checkStrLength = function(text,len) {
	var strLength = text.length ;
	var fixedLength = 0 ;
	for(var i = 0 ; i < strLength ; i++) {
		if(/^[\u4E00-\u9FA5]$/.test(text.charAt(i))) {fixedLength += 2 ;}
		else {fixedLength += 1 ;}
		if(fixedLength >= len) {
			return text.slice(0, i).concat("...") ;
		}
	}
	return text ;
};
/**
 * 格式化文件的大小
 * @param size 单位为B
 */
webmail.Utils.formatSize=function(size)
{
	size=parseInt(size);
	if(size>=1024*1024)
	{
		return size=(size/(1024*1024)).toFixed(2)+"M";
	}else if(size>=1024)
	{
		return size=(size/(1024)).toFixed(2)+"K";
	}
	else
		return size+"B";
};
/**
 * 将格式化后的字符串转换回原始以B为单位的大小
 * @param formatSize
 */
webmail.Utils.escapeFormatSize=function(formatSize)
{
	var sizeNumber=parseInt(formatSize.substring(0,formatSize.length-1));
	var sizeUnit=formatSize.substring(formatSize.length-1);
	if(sizeUnit=="M"){
		sizeNumber=sizeNumber*1024*1024;
	}else if(sizeUnit=="K"){
		sizeNumber=sizeNumber*1024;
	}
	return sizeNumber;
};
/**
 * startwith与endwith扩展
 * @param str
 * @return
 */
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

String.prototype.cut=function(cut_length){
	var lenth=cut_length;
	for (var i = 0; i < cut_length; i++) 
	{
		if (this.charCodeAt(i) >= 0x4e00 && this.charCodeAt(i) <= 0x9fa5){ 
		}else {
			lenth++;
		}
	}
	
	var result="";
	if(this.length>lenth)
		result = this.substring(0,lenth-1)+"...";
	else
		result=this;
	return result;
};
function escapeHtml(str){
	var jdiv=$("<div></div>");
	jdiv.text(str);
	return jdiv.html();
};

var oldValue=new Array();
//判断textarea值的长度
function checkMaxLen(obj,maxlength,num){
   if(obj.value.length>maxlength){
   		if(oldValue[num] == undefined){
   			obj.value="";
   		}else{
      		obj.value=oldValue[num];
      	}
   }else{
   	  oldValue[num]=obj.value;
   }
}

/*解析异步error的结果*/
function parseXmlHttpRequest(request,errorTextStatus)
{
	var _responseStatus=request.status;
	var _responseText=request.responseText;
	if(_responseStatus=="200"||_responseStatus=="304")
	{
		if(_responseText!=null&&_responseText.indexOf("loginPage")>-1)
		{
			nfschinaDialogAlert("会话已过期，请重新登录！");
	        setTimeout(function(){window.location.href="needlogin.action";},3000);
		}
		else
		{
			nfschinaDialogAlert(_responseText);
		}
	}else if(_responseStatus=="0"||_responseStatus=="12029"||_responseStatus=="12007")
	{
		nfschinaDialogAlert("不能与服务器建立连接，可能服务器已停止或网络有问题！");
	}else if(_responseStatus=="404")
	{
		nfschinaDialogAlert("找不到相应的资源，请检查URL是否正确。");
	}else if(_responseStatus=="500")
	{
		nfschinaDialogAlert("服务器500错误，请刷新页面重试。");
	}
	else{
		nfschinaDialogAlert("未能处理的其他异常！");
	}
}

//获取6位随机数
function get6Rondom(){
	return Math.round(Math.random()*1000000);
}

//计算保留两位小数
function formatFloat(src){
    return Math.round(src*Math.pow(10,2))/Math.pow(10,2);
}
//计算文件大小
function fileSize(size){
	var fileSize;
	if(size>=1048576)
		fileSize=formatFloat(size/1048576)+"M";
	else if(size>=1024)
		fileSize=formatFloat(size/1024) + "K";
	else
		fileSize=formatFloat(size) + "B";
	return fileSize;
}