/**
 * @author Administrator
 */
(function($){
    /*默认参数*/
    $.fn.nfschinaDialog = function(options){
		var _thisDomId=$(this).attr("id");/*获取对象ID*/
		
		if(options=='close')
		{
			destroyDialog($("#nfschinaDialog_"+_thisDomId),$(this),_thisDomId);
			return;
		}
		/*如果已出现了同样的弹出框，则不继续进行。*/
		if($("#nfschinaDialog_"+_thisDomId).size()>0)
		{return;}
		/*默认值*/
		$.fn.nfschinaDialog.defaultOptions = {
	        position: "absolute",/*如果浏览器支持position:fixed属性*/
	        top: "",/*带单位*/
	        left: "",/*带单位*/
	        width: 350,/*如果带单位必须是字符串的形式，否则只能写数字*/
	        height: "",/*如果带单位必须是字符串的形式，否则只能写数字*/
	        title: "默认对话框",
			delay:-1,
			drag:true,/*能否拖动*/
			bgframe:true,
			bgframe_minW:996,/*当改变浏览器的宽式，背景最小为多宽*/
			bgframe_minH:400,/*当改变浏览器的宽式，背景最小为多高*/
			buttonAlign:"",
	        buttons: {}
    	}
		
		var _jbody=$("body");
		var _jthis=$(this).clone(true);/*备份，对话框取消时还原对象*/
		$(this).remove();/*移除页面上填充内容的元素*/
        var _finalOptions = $.extend({}, $.fn.nfschinaDialog.defaultOptions, options);
		var _isIE6=true;
		if(window.XMLHttpRequest)
			_isIE6=false;
		if(_finalOptions.position=="fixed" && _isIE6)
			_finalOptions.position="absolute";
		
		/*往body中添加弹出框*/
		_jbody.append(initNfschinaDialog(_thisDomId,_finalOptions));
		var _nfschinaDialog=$("#nfschinaDialog_"+_thisDomId);
		var _nfschinaDialogBody=$("#nfschinaDialogBody_"+_thisDomId);
		var _nfschinaDialogBodyIframe=$("#nfschinaDialogBodyIframe"+_thisDomId);
		
		/*在弹出框中添加操作元素*/
		$('#nfschinaDialogBody_'+_thisDomId).append(_jthis);
		
		/*
		 * 下面的操作是对弹出框根据参数进行设置
		 */
		var documentWH={w:$(document).width(),h:$(document).height()};
		var windowWH={w:$(window).width(),h:$(window).height()};
		/*设置弹出框的宽度*/
		_nfschinaDialog.width(_finalOptions.width);
		_nfschinaDialog.find(".nfschinaDialogTop").width(_finalOptions.width-6);
		_nfschinaDialog.find(".nfschinaDialogBottom").width(_finalOptions.width-6);
		
		if(_finalOptions.height)
		{
			_nfschinaDialog.height(_finalOptions.height);
		}
		
		/*先将清空按钮所在的div*/
		$('.nfschinaDialogButton',_nfschinaDialog).empty().css("text-align",_finalOptions.buttonAlign);
		
		/*为弹出框添加按钮*/
		hasButtons=false;
		/*判断是否要添加按钮*/
		(typeof(_finalOptions.buttons)=="object"&&_finalOptions.buttons!=null&&$.each(_finalOptions.buttons,function(){return !(hasButtons=true);}));
		
		if(hasButtons)
		{
			$.each(_finalOptions.buttons,function(name,callBack){
				$('<button>'+name+'</button>')
				.click(function(){callBack.apply(_jthis[0]);
				}).appendTo($("#nfschinaDialogButton_"+_thisDomId));
			});
		}
		else{
			$("#nfschinaDialogButton_"+_thisDomId).remove();
		}
		
		/*判断是否显示遮盖*/
		if(_finalOptions.bgframe)
		{
			var _bgWidth=0;
			var _bgHeight=0;
			if(document.body.clientHeight>windowWH.h){_bgHeight=document.body.clientHeight;}else{_bgHeight=windowWH.h;}
			if(document.body.clientWidth>windowWH.w){_bgWidth=document.body.clientWidth;}else{_bgWidth=windowWH.w;}
			if(!_isIE6)
			{
				$('<div id="nfschinaDialogBg_'+_thisDomId+'" class="nfschinaDialogBg" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;"></div>').css("opacity", 0.1).appendTo(_jbody);
			}
			else
			{
				$('<div id="nfschinaDialogBg_'+_thisDomId+'" class="nfschinaDialogBg" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;"><iframe class="nfschinaDialogBgIframe" style="width:'+_bgWidth+'px;height:'+_bgHeight+'px;">'+
				'</iframe></div>').css("opacity", 0.1).appendTo(_jbody).find("iframe").css("opacity", 0.1);
			}
		}
		
		_jthis.show();
		_nfschinaDialog.show();
		
		
		/*为了准确的添加iframe，这里要设置width和height*/
		_nfschinaDialogBodyIframe.css("width",_nfschinaDialog.width()).css("height",_nfschinaDialog.height());
		
		/*注意，这个地方把弹出框位置的设定放在弹出框显示之后
		 * 因为如果隐藏时获取高和宽，可能不准确
		 */
		var _documentScroll={l:document.documentElement.scrollLeft,t:document.documentElement.scrollTop};
		//设置距离document顶端的高度
		if(_finalOptions.top)
		{
			_nfschinaDialog.css("top",top);		
		}
		else{
			var top=(windowWH.h)/2-(_nfschinaDialog.height())/2;
			top=top*2/3;
			top=top>5?top:5;
			top=top+_documentScroll.t
			_nfschinaDialog.css("top",top);
		}
		/*设置距离document最左边的位移*/
		if(_finalOptions.left)
		{
			var left=(windowWH.w)/2-(_nfschinaDialog.width())/2+_finalOptions.left;
			left=left>0?left:0;
			_nfschinaDialog.css("left",left);
		}
		else{
			var left=(windowWH.w)/2-(_nfschinaDialog.width())/2+_documentScroll.l;
			_nfschinaDialog.css("left",left);
		}
		
		/*调节内部显示，当对话框的nfschinaDialogBody部分太长或者太宽是，应该出现滚动条*/
		if(_nfschinaDialog.height()+5>windowWH.h)
		{
			_nfschinaDialogBody.height(windowWH.h-10-(_nfschinaDialog.height()-_nfschinaDialogBody.height()));
			_nfschinaDialogBodyIframe.height(windowWH.h-(10));
			_nfschinaDialog.height(windowWH.h-(10));
		}
		/*弹出框自动延迟消失*/
		if(_finalOptions.delay>0)
		{
			setTimeout(function(){
				_nfschinaDialog.hide();
				destroyDialog(_nfschinaDialog,_jthis,_thisDomId);
			}, _finalOptions.delay);
		}
		
		/*为右上角关闭添加onclick事件*/
		$('#nfschinaDialogClose_'+_thisDomId).click(function(){
			destroyDialog(_nfschinaDialog,_jthis,_thisDomId);
			
			if(_thisDomId=="searchMailsList"){
				//搜索弹出层中点击关闭时，清空搜索条件
				$("#searchMailsForm").find("input").val("");
				$("#searchMailsForm").find("select").val("");
				$("#searchMailScope").find("option:first").attr("selected","true");	
			}
		});
		
		/*拖动*/
		if(_finalOptions.drag)
		{
			try{
				_nfschinaDialog.draggable({handle:'#nfschinaDialogHead_'+_thisDomId});
			}catch(e){}
		}
		/*为背景添加自适应高和宽*/
		if(windowWH.w<_finalOptions.bgframe_minW)
			windowWH.w=_finalOptions.bgframe_minW;
		if(windowWH.h<_finalOptions.bgframe_minH)
			windowWH.h=_finalOptions.bgframe_minH;
		$("#nfschinaDialogBg_"+_thisDomId).width(windowWH.w).height(windowWH.h).find(".nfschinaDialogBgIframe").width(windowWH.w).height(windowWH.h);
		/*为弹出框里的form添加按下回车事件处理*/
		_nfschinaDialog.find("form").keypress(function(eve){
			if(eve.keyCode==13)
				return false;
		})
		_nfschinaDialog.find("form").keydown(function(eve){
			if(eve.keyCode==13)
				return false;
		})
		_nfschinaDialog.find("form").keyup(function(eve){
			if(eve.keyCode==13)
				return false;
		})
    };
	
	 var initNfschinaDialog = function(_thisDomId,_finalOptions){
	 	
	 return '<div class="nfschinaDialog" style="margin:0;z-index:1002;display:none;position: '+_finalOptions.position+';left: -1000px;top: -1000px;" id="nfschinaDialog_'+_thisDomId+'">'+
		   		'<div class="nfschinaDialogTop"></div>'+
		   		'<div class="nfschinaDialogHead" id="nfschinaDialogHead_'+_thisDomId+'">'+
		     		'<h2 class="nfschinaDialogH2"><span class="nfschinaDialogSpan">'+_finalOptions.title+'</span></h2>'+
		     		'<div class="nfschinaDialogClose"><a id="nfschinaDialogClose_'+_thisDomId+'" href="javascript:void(0)">关闭</a></div>'+
		   		'</div>'+
		   		'<div class="nfschinaDialogBody" id="nfschinaDialogBody_'+_thisDomId+'"></div>'+
		   		'<div class="nfschinaDialogButton" id="nfschinaDialogButton_'+_thisDomId+'"></div>'+
	   			'<div id="nfschinaDialogBodyIframe'+_thisDomId+'" style="position:absolute;visibility:inherit;z-index:-1;top:0;left:0"><iframe frameborder="no" style="border:none;width:100%;height:100%"></iframe></div>'+
	 			'<div class="nfschinaDialogBottom"></div>'+
	 		'</div>';
	 }
	/*销毁对话框，用originally替换dialogBody，并隐藏
	  * 
	  *parameters 
	  *dialogBody:对话框div的jquery对象
	  *originally:对话框中用户处理内的jquery对象
	  */
	 var destroyDialog=function(jo_dialogBody,jo_originally,_thisDomId){
	 	/*
	 	 * 如果存在对话框才销毁，这个地方不能写成这样：if(jo_dialogBody.attr('id'))
		 *因为jo_dialogBody在对话框已经销毁之后还存在值，所以必须用$("#nfschinaDialogBody_"+_thisDomId)
		 *才能取到实际的对话框到底有没有。
		 */
		if($("#nfschinaDialogBody_"+_thisDomId).attr('id'))
		{
			jo_dialogBody.replaceWith(jo_originally);
			jo_originally.hide();
			$("#nfschinaDialogBg_"+_thisDomId).remove();
		}
	 }
})(jQuery);

var nfschinaDialogConfirm=function(title,content,callBack){
	if($("#toolConfirm").attr("id")=="toolConfirm")
	{
		$("#toolConfirm").html(content);
	}
	else
	{
		content='<div id="toolConfirm" style="display: none;padding:10px;height:50px;">'+content+'</div>';
		$("body").append(content);
	}
	 var dialogOption = {
                        title: title||"确认对话框",
                        delay: -1,
						buttons:{
							'确定':function(){$(this).nfschinaDialog("close");callBack();},
							'取消':function(){$(this).nfschinaDialog("close");}
						},
						buttonAlign:"right"                     
                    };
     $("#toolConfirm").nfschinaDialog(dialogOption);
     $("#nfschinaDialogButton_toolConfirm").children(":last").focus();
};

var nfschinaDialogAlert=function(content,delay,title,width,bgframe){
	if($("#toolAlert").attr("id")=="toolAlert")
	{
		$("#toolAlert").html(content);
	}
	else
	{
		content='<div id="toolAlert" style="display: none;padding:10px;height:50px;">'+content+'</div>';
		$("body").append(content);
	}
	 var dialogOption = {
                        title: title||"提示对话框",
                        delay: delay||3000,
                        bgframe: bgframe||false,
                        width:width,
                        buttons:{
							'确定':function(){$(this).nfschinaDialog("close")}
						},
						buttonAlign:"center"
                    };
     $("#toolAlert").nfschinaDialog(dialogOption);
     $("#nfschinaDialogButton_toolAlert").children().eq(0).focus();
};