var nfschinaAjaxObject=new Object();
nfschinaAjaxObject.propertys=new Object();
/**
 * 异步错误提示信息，
 * 1，当异步请求数据时可能出现权限不足，session或者cookie过期等异常，这时服务器会重定向到相应的页面,并将这个页面的代码作为XMLHttpRequest的responseText返回。
 *  1.1如果datatype是html,没有问题，jQuery能够将其解析。将继续执行success回调函数,可以在success函数中对这样的返回结果进行相应的处理。
 *  1.2如果datatype是json,由于XMLHttpRequest的responseText不能被jQuery成功转换称json格式的数据，将抛出异常，调用ajaxError，这样可以在ajaxError函数中对返回的结果进行解析并执行相关的操作。
 * 2，其他情况，无论datatype是什么，对于其他的相应状态比如找不到资源的404或者服务器错误的500，jQuery都会调用ajaxError。对于这些错误也在ajaxError函数中对返回的结果进行解析并执行相关的操作。
 * 
 * 
 * 对于上述内容的处理过程。
 *  1.1 编写公用方法filterHtmlResult，并在success回调函数中使用，对datatype为html的请求结果data进行过滤。
 *  1.2 在全局ajaxError的回调函数中执行相应的过滤（其实过滤方法的实现和上述filterHtmlResult一样）。
 *  2   在全局ajaxError中处理。
 *  举例：
 * 		一、当服务器重定向到相应的页面
 *			1，权限不足，重定向到noAuth.jsp 该页面中包含noAuth的字段，用于判断当前的重定向是因为没有权限
 * 			2，session或者cookie过期 重定向到登陆界面 login.jsp ，该页面中包含loginPage的字段，用于判断当前的重定向是因为session或者cookie过期
 *		二、当服务器重定向到固定的页面
 * 			只定义一个页面，ajaxError.jsp，当重定向的时候根据情况在链接后面添加相应的错误信息，如noAuth或者errorSession
 *			
 */
 
/*
 * 初始化插件，在使用该工具的时候应该调用这个方法，对ajax全局请求进行规范。
 */
nfschinaAjaxObject.init=function(options)
{
	/*默认值*/
		var _defaultOptions = {
			message:"数据加载中...",
			bgframe:true,
	        e0:"",/*不能与服务器建立连接*/
	        e404:"",
	        e500:"",
	        eOther:"",
	        filterErrorMap: {
	        	"noAuth":"对不起，您没有权限执行这个操作！",
	        	"loginPage":"对不起，您没有权限执行这个操作！"
	        }
    	}
    	nfschinaAjaxObject.propertys.options=$.extend({},_defaultOptions,options);
    	nfschinaAjaxObject.propertys.body=$("body");
    	nfschinaAjaxObject.ajaxStart(nfschinaAjaxObject.propertys.options.message);
    	nfschinaAjaxObject.ajaxStop();
    	nfschinaAjaxObject.ajaxError();
}
/*创建信息显示的容器*/
nfschinaAjaxObject.createMessageContainer=function(message){
	var _jbody=nfschinaAjaxObject.propertys.body;
	if(nfschinaAjaxObject.propertys.options.bgframe)
	{
		var _bgWidth=0;
		var _bgHeight=0;
		var windowWH={w:$(window).width(),h:$(window).height()};
		if(document.body.clientHeight>windowWH.h){_bgHeight=document.body.clientHeight;}else{_bgHeight=windowWH.h;}
		if(document.body.clientWidth>windowWH.w){_bgWidth=document.body.clientWidth;}else{_bgWidth=windowWH.w;}
		var _isIE6=true;
		if(window.XMLHttpRequest)
			_isIE6=false;
		if(!_isIE6)
		{
			$('<div id="nfschinaAjax_bg" class="nfschinaAjax_bg" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;"></div>')
			.css("opacity", 0.1).appendTo(_jbody);
		}
		else
		{
			$('<div id="nfschinaAjax_bg" class="nfschinaAjax_bg" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;"><iframe class="nfschinaAjax_bg_iframe" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;" id="nfschinaAjax_bg_iframe" frameborder="no"></iframe></div>')
			.css("opacity", 0.1).appendTo(_jbody).find("iframe").css("opacity", 0.1);;
		}
	}
	nfschinaAjaxObject.propertys.nfschinaAjaxBackground=$("#nfschinaAjax_bg");
	
	/*定义一个变量，先获取浏览器的高度和宽度*/
    var windowWH = {w: $(window).width(),h: $(window).height()};
    /*获取滚动条的滚动距离*/
    var _documentScroll={l:document.documentElement.scrollLeft,t:document.documentElement.scrollTop};
	
	var _container='<div id="nfschinaAjaxMessageContainer" class="nfschinaAjaxMessageContainer">'+
					'<div class="nfschinaAjaxMessageContainerTop"></div>'+
				  '<div class="nfschinaAjaxMessageContainerBody"><div>'+message+'</div></div>'+
				  '<div class="nfschinaAjaxMessageContainerBottom"></div>'+
				  '</div>';
	_jbody.append(_container);
	var _nfschinaAjaxMessageContainer=$("#nfschinaAjaxMessageContainer");
	var width=_nfschinaAjaxMessageContainer.width();
	var top = (windowWH.h) / 2 - (_nfschinaAjaxMessageContainer.height()) / 2;
	top = top * 2 / 3;
	_nfschinaAjaxMessageContainer.css("top",(_documentScroll.t+top)+"px");
	_nfschinaAjaxMessageContainer.css("left",((windowWH.w-_nfschinaAjaxMessageContainer.width())/2)+_documentScroll.l+"px");
	nfschinaAjaxObject.propertys.nfschinaAjaxMessageContainer=_nfschinaAjaxMessageContainer;
}
/*ajax开始时注册的方法*/
nfschinaAjaxObject.ajaxStart=function(message){
	nfschinaAjaxObject.propertys.body.ajaxStart(function(){
		try{
			if(ajaxFlag){
				window.clearTimeout(timeID);
				timeID = -1;
				//parent.document.title = "NfSChina-企业版电子邮件系统";
				keepConstantTitle();//基于在ie中存在title会偶尔改变的原因，调用保持title不变的方法
				ajaxFlag = false;
			}
		}
		catch(err){}
		nfschinaAjaxObject.open(message);
	})
}
/*ajax结束时注册的方法*/
nfschinaAjaxObject.ajaxStop=function(){
	nfschinaAjaxObject.propertys.body.ajaxStop(function(){
		nfschinaAjaxObject.close();
	})
}
/*ajax出现错误时注册的方法*/
nfschinaAjaxObject.ajaxError=function(){
	nfschinaAjaxObject.propertys.body.ajaxError(function(event,request,ajaxOptions,thrownError)
	{
		var _responseStatus=request.status;
		var _responseText=request.responseText;
		var _finalOptions=nfschinaAjaxObject.propertys.options;
		if(_responseStatus=="200"||_responseStatus=="304")
		{
			for(var error in _finalOptions.filterErrorMap)
			{
				if(_responseText.indexOf(error)>-1)
				{
					if(typeof(_finalOptions.filterErrorMap[error])=="string")
						nfschinaDialogAlert(_finalOptions.filterErrorMap[error]);
					else if(typeof(_finalOptions.filterErrorMap[error])=="function")
					{
						_finalOptions.filterErrorMap[error]();
					}
					return;
				}
			}
			if(_responseText.indexOf("Struts Problem Report")>-1)
			{
				nfschinaDialogAlert(_responseText);
			}else
			{
				nfschinaDialogAlert("操作已成功执行，但还存在错误！\n 出现该提示的原因可能是返回到页面的数据不能被正确的解析，如：要解析的内容必须为json类型，却返回了Html代码！");
			}
		}else if(_responseStatus=="0"||_responseStatus=="12029"||_responseStatus=="12007")
		{
			if(_finalOptions.e0)
			{
				if(typeof(_finalOptions.e0)=="function")
				{
					_finalOptions.e0();
				}
				else
					nfschinaDialogAlert(_finalOptions.e0);
			}
			else
				nfschinaDialogAlert("不能与服务器建立连接，可能服务器已停止或网络有问题！");
		}else if(_responseStatus=="404")
		{
			if(_finalOptions.e404)
			{
				if(typeof(_finalOptions.e404)=="function")
				{
					_finalOptions.e404();
				}
				else
					nfschinaDialogAlert(_finalOptions.e404);
			}
			else
				nfschinaDialogAlert("找不到相应的资源，请检查URL是否正确。");
		}else if(_responseStatus=="500")
		{
			if(_finalOptions.e500)
			{
				if(typeof(_finalOptions.e500)=="function")
				{
					_finalOptions.e500();
				}
				else
					nfschinaDialogAlert(_finalOptions.e500);
			}
			else
				nfschinaDialogAlert("服务器500错误，请刷新页面重试。");
		}
		else{
			if(_finalOptions.eOther)
			{
				if(typeof(_finalOptions.eOther)=="function")
				{
					_finalOptions.eOther();
				}
				else
					nfschinaDialogAlert(_finalOptions.eOther);
			}
			else
				nfschinaDialogAlert("未能处理的其他异常！");
		}
		
	});
};
/*
 * 当datatype为html时，在success中分析返回页面的方法。
 * @return boolean 
 * 		true 表示返回的是正常的页面
 * 		false 表示返回的页面属于包含自定义异常标志的页面，即存在异常信息的页面。
 * */
nfschinaAjaxObject.filterHtmlResult=function(data)
{
	var _options=nfschinaAjaxObject.propertys.options;
	for(var error in _options)
	{
		if(data.indexOf(error)>-1)
		{
			if(typeof(_options.filterErrorMap[error])=="string")
				nfschinaDialogAlert(_options.filterErrorMap[error]);
			else if(typeof(_options.filterErrorMap[error])=="function")
			{
				_options.filterErrorMap[error]();
			}
			return false;
		}
	}
	return true;
}
nfschinaAjaxObject.open=function(message)
{
	nfschinaAjaxObject.createMessageContainer(message);
	nfschinaAjaxObject.propertys.nfschinaAjaxBackground.show();
	nfschinaAjaxObject.propertys.nfschinaAjaxMessageContainer.show();
}
nfschinaAjaxObject.close=function()
{
	nfschinaAjaxObject.propertys.nfschinaAjaxBackground.remove();
	nfschinaAjaxObject.propertys.nfschinaAjaxMessageContainer.remove();
}
$(function(){
	var options={message:"操作处理中...",
			bgframe:true,
			e0:"不能与服务器建立连接，可能服务器已停止或网络有问题！",
	        e404:function(){nfschinaDialogAlert("404错误！，找不到您请求的地址！")},
	        e500:function(){nfschinaDialogAlert("500错误！，服务器发生异常！")},
	        eOther:function(){nfschinaDialogAlert("未处理的错误！")},
	        filterErrorMap: {
	        	"noAuth":"对不起，您没有权限执行这个操作！",
	        	"loginPage":function(){
		        	window.onbeforeunload=function(){window.event.preventdefault;};
		    		window.onunload = function(){window.event.preventdefault; };
	        		nfschinaDialogAlert("会话已过期，请重新登录！");
	        		setTimeout(function(){window.location.href="needlogin.action";},3000);
	        	}
	        }
	        };
	nfschinaAjaxObject.init(options);
})