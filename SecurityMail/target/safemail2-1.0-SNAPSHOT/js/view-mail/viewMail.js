/**
 * 发送回执
 * @param sendReadReceipt
 * @param recipients
 * @param folderName
 * @param msgnum
 * @return
 */
function vm_ajaxSendReadReceipt(sendReadReceipt, recipients,folderName,msgnum){
	if (sendReadReceipt) {
		nfschinaDialogConfirm("提示", "要求发送已阅通知，现在发送吗?", function(){
			webmail.Utils.ajax({
				url: "sendReadReceipt.action",
				params: {
					"mailDto.recipients[0].nickname": recipients[0].replace(/"/g, ""),
					"mailDto.recipients[0].email": recipients[1].replace(/>/g, ""),
					"mailDto.subject": $(".ck_b").text(),
					"mailDto.folderName": folderName,
					"mailDto.msgnum": msgnum
				},
				method: "post",
				callback: function(data){
					nfschinaDialogAlert("已发送");
				}
			});
			$("#toolConfirm").nfschinaDialog("close");
		})
	}
}
/**
 * 根据参数，读取并显示邮件
 * @param params
 * @param key_folder
 * @param key_mail
 * @return
 */
function vm_display_mail(params,key_folder,key_mail)
{
	var mail_data=null;
	if(typeof(map.get(key_folder))=='undefined'|| typeof(map.get(key_folder).get(key_mail))=='undefined'){
		params = encodeURI(params);
		$.ajax({
			url:"viewMail.action?"+params,
			dataType:"json",
			type:"post",
			cache:false,
			success:function(data){
				if(data.jsonStatus){
					mail_data=data;
					//设置系统反垃圾策略启用/禁用,以便在页面中判断是否可以添加黑白名单
					vm_setAntiSpamEnable(data.antiSpamEnable);
					//考虑到搜索结果翻页时key_mail与真实key_mail是不匹配的，故此处更改之。
					if(key_mail.split("@")[2]=="1")
						key_mail = mail_data.mailDto.folderName+"@"+mail_data.mailDto.msgnum+"@1";
					else
						key_mail = mail_data.mailDto.folderName+"@"+mail_data.mailDto.msgnum+"@0";
					if(!mail_data.mailDto.seen){
						//如果邮件是未读的，读完需修改邮件列表缓存中的状态
						var msgnum = mail_data.mailDto.msgnum;
						var mailFolderName = mail_data.mailDto.folderName;
						if(mailFolderName=="收件箱" || mailFolderName=="垃圾邮件"){
							//处理未读邮件数
							processMainMenuUnreadNum(null,true,mailFolderName) ;
						}
						if(parseInt($("#unseenMessageCount").text())>0)
							$("#unseenMessageCount").text(parseInt($("#unseenMessageCount").text()-1));
						
						var folderText = $.trim($("#folderText").text());
						
						if(folderText=="未读邮件" || folderText=="搜索结果"){
							//do nothing
						}else{
							var listData = map.get(folderText).get(parseInt($("#select_page_inbox").val()));
							if(typeof(listData) != 'undefined'){
								if(listData.unseenMessageCount>0)
									listData.unseenMessageCount--;
								for ( var i = 0; len = listData.dataPage.data.length, i < len; i++) {
									if(listData.dataPage.data[i].msgnum == msgnum){
										listData.dataPage.data[i].seen = true;							
									}
								}
							}
							
							//将要缓存的邮件改为已读
							data.mailDto.seen = true;
					   }
						
					}
					//缓存邮件
					var mailMap=null;
					if(typeof(map.get(key_folder))=='undefined')
						mailMap=new Map();
					else
						mailMap = map.get(key_folder);
					//alert(key_mail+"**cache**"+key_folder);
					mailMap.put(key_mail,data);
					map.put(key_folder,mailMap);
					//显示
					vm_show_mail(mail_data);
				}else{
					nfschinaDialogAlert(data.errorMessage,300000,"系统提示");
				}
			}
		});
	}
	else
	{
		//alert(key_mail+"**show**"+key_folder);
		mail_data=map.get(key_folder).get(key_mail);
		vm_show_mail(mail_data);
	}
}
	

function vm_show_mail(mail_data)
{
	var folderText = $.trim($("#folderText").text());
	//显示邮件信息
	$("#main").hide();
	$("#write-main").hide();
	$("#setup-main").hide();
	vm_analysis_mailDto(mail_data);
	vm_menu_initViewMailDetail(mail_data);
	$("#viewMailCot").show();
	var dheight = $(window).height() ;
	if(dheight<=minHeight)
		dheight=minHeight;
	var fjheight = $("#fj_div").height();
	$("#mailContentDiv").height(dheight-126-fjheight-10);
	//初始化 "上一封/下一封"链接 
	var preMsgnum = mail_data.mailDto.preMsgnum;
	var nextMsgnum = mail_data.mailDto.nextMsgnum;
	var isSearch = mail_data.mailDto.isSearch;
	if(!preMsgnum) {
		$("#navMsgPre").parent().css("visibility","hidden") ;
	}else{
		$("#navMsgPre").parent().css("visibility","visible") ;
	}
	if(!nextMsgnum) {
		$("#navMsgNxt").parent().css("visibility","hidden") ;
	}else{
		$("#navMsgNxt").parent().css("visibility","visible") ;
	}
	$("#navMsgPre,#navMsgNxt").removeAttr("disabled");
	
	//用href传值在ie6下中文folderName会乱码。最终采用form传值。见vm_menu_downloadMail()方法。
//	$("#downloadMail").attr("href","downloadMail.action?folderName="+mail_data.mailDto.folderName+"&msgnum="+mail_data.mailDto.msgnum+"&mailDto.subject="+mail_data.mailDto.subject);
	
	ishashchange = false;
	var realArr = window.location.href.split("#");
	var realUrl = realArr[0];
	if(folderText=="未读邮件" || folderText=="搜索结果")
		window.location.href = realUrl + "#"+mailBoxMap.get(mail_data.mailDto.folderName) + "_" +"search-"+ mail_data.mailDto.mailPageNo + "/" + mail_data.mailDto.msgnum;
	else
		window.location.href = realUrl + "#"+mailBoxMap.get(mail_data.mailDto.folderName) + "_" + mail_data.mailDto.mailPageNo + "/" + mail_data.mailDto.msgnum;
	
}

/**
 * 分析邮件的各个部分，并组成页面显示元素
 * @param data
 * @return
 */
function vm_analysis_mailDto(data)
{
	vm_analysis_summary(data);
	$(document.getElementById('mailDetail').contentWindow.document.body).html(data.mailDto.content);
	//将链接的target设置为"_blank"
	$(document.getElementById('mailDetail').contentWindow.document.body).find("a").attr("target","_blank");
	
	vm_analysis_attachments(data);
}
/**
 * 解析邮件摘要内容，标题发件人，收件人，等等。
 * @param data
 * @return
 */
function vm_analysis_summary(data)
{
	var mailDto=data.mailDto;
	//标题
	var subject=mailDto.subject;
	$("#subjectContent").html(mailDto.subject);
	//发件人
	var fromAddress=mailDto.from.nickname+"&lt;"+mailDto.from.email+"&gt;";
	$("#fromAddress").html(fromAddress);
	//添加联系人
	var isPersonExist=mailDto.isContactPersonExist;
	if(!isPersonExist)
		$("#vm_addContactPerson").show();
	//发送时间
	$("#vm_date").html(mailDto.dateString);
	//收件人
	var recipients=mailDto.recipients;
	if(recipients&&recipients.length>0)
	{
		var arr=[];
		var len=recipients.length;
		for(var i=0;i<len;i++)
		{
			var recipient=recipients[i];
			arr.push('<span onclick="vm_show_option(this)" id="'+recipient.email+'" title="'+recipient.nickname+'&lt;'+recipient.email+'&gt;">'+recipient.nickname+(i!=len-1?",":"")+'</span>');
		}
		$("#vm_recipients").html(arr.join(""));
	}
	//抄送人
	$("#vm_cc_div").hide();
	var cc=mailDto.cc;
	if(cc&&cc.length>0)
	{
		var arr=[];
		var len=cc.length;
		for(var i=0;i<len;i++)
		{
			var c=cc[i];
			arr.push('<span onclick="vm_show_option(this)" id="'+c.email+'" title="'+c.nickname+'&lt;'+c.email+'&gt;">'+c.nickname+(i!=len-1?",":"")+'</span>');
		}
		$("#vm_cc").html(arr.join(""));
		$("#vm_cc_div").show();
	}
	//附件个数
	$("#vm_attachments_div").hide();
	
	//因为背景图片不放在附件处显示，故放在vm_analysis_attachments去控制显示和隐藏。
//	var attachments=mailDto.attachments;
//	if(attachments&&attachments.length>0)
//	{
//		$("#vm_attachments_div").show();
//	}
	//发送回执
	if(mailDto.readReceipt){
		vm_ajaxSendReadReceipt(mailDto.readReceipt,$('.viewMailFromAddress').text().split('<'),mailDto.folderName,mailDto.msgnum);
		var folderText = $.trim($("#folderText").text());
		var key_folder;
		var key_mail;
		if(folderText == '搜索结果' || folderText == '未读邮件') 
		{
			key_folder = "搜索结果-";
			key_mail = mailDto.folderName+"@"+mailDto.msgnum+"@1"; 
		}
		else 
		{ 
			key_folder = mailDto.folderName+"-";
			key_mail = mailDto.folderName+"@"+mailDto.msgnum+"@0"; 
		}
		//alert(key_folder+"  "+key_mail);
		data.mailDto.readReceipt=false;
		map.get(key_folder).put(key_mail,data);
	}
}
/**
 * 分析附件
 * @param data
 * @return
 */
function vm_analysis_attachments(data)
{
	var folderName=data.mailDto.folderName;
	var msgnum=data.mailDto.msgnum;
	var messageTotalCount = data.messageTotalCount;
	var isSearch = data.mailDto.isSearch;
	var attachments=data.mailDto.attachments;
	var arr=[];
	var len=attachments.length;
	for(var i=0;i<len;i++)
	{
		var attach=attachments[i];
		
		var contentType = attach.contentType ;
		var uuid = attach.contentUUID;
		var fileName =attach.fileName;
		var attachIndex = attach.attachIndex;
		
		//背景图片不在附件处显示，其attachIndex为0.n的形式
		if(attachIndex.indexOf(".")!=-1){
			continue;
		}
		//只要有一个附件，即显示附件栏
		$("#vm_attachments_div").show();
		
//		var download="downloadAttach.action?attach.fileName="+fileName+"&attach.contentUUID="+uuid+"&attach.contentType="+contentType;
//		arr.push('<span>'+attach.fileName+'('+fileSize(attach.size)+')&nbsp;<a href="'+download+'">下载</a>');
		
		//IE6下载大附件及文件名为"未命名文件.png"的图片时，个别中文会出现乱码，故改用form传参
		//注意：fileName一定要用引号括起来，否则遇到文件名带空格的就断了。
		var download =  "<div style='display:none;'> " +
						  "<form id='download_form"+attachIndex+"' action='downloadAttach.action' method='get' target='_self'> " +
							"<input type='text' id='down_fileName' name='attach.fileName' value="+"'"+fileName+"'"+"> " +
							"<input type='text' id='down_contentUUID' name='attach.contentUUID' value="+ uuid +"> "+
							"<input type='text' id='down_contentType' name='attach.contentType' value="+contentType+" > "+
						  "</form>"+
						"</div>"; 
		arr.push('<span>'+attach.fileName+'('+fileSize(attach.size)+')&nbsp;'+download+'<a href="#" onclick="downloadAttach('+attachIndex+',event);return false;" >下载</a>');//return false使得href保持不变

		if(attach.contentType.match(/^message\/rfc822/i) != null)
		{
			if(fileName.indexOf('mht') != -1 || fileName.indexOf('mhtml') != -1 )
				return;
			var msgnum_2=msgnum+"."+attachIndex;
			arr.push('<a href=javascript:void(0); onclick="vm_openAttach('+messageTotalCount+',\''+folderName+'\','+isSearch+',\''+msgnum_2+'\')" >打开</a>');	
		}
		arr.push('</span>'+(i!=len-1?'&nbsp;;&nbsp;':''));
	}
	$("#vm_attachments_span").html(arr.join(""));
}
//下载附件
function downloadAttach(attachIndex,event){
	
	var form = "#download_form"+attachIndex;
	$(form).submit();
	//取消冒泡，避免触发backAndForward.js中body的click事件的处理方法
	if(window.event)
		window.event.cancelBubble=true;
	else
		event.stopPropagation();
}
/**
 * 打开附件中的邮件
 * @param messageTotalCount
 * @param folderName
 * @param isSearch
 * @param msgnum
 * @return
 */
function vm_openAttach(messageTotalCount,folderName,isSearch,msgnum)
{
	var action="viewMail.action";
	var params = "messageTotalCount="+messageTotalCount+"&folderName="+folderName+"&isSearch="+isSearch+"&msgnum="+msgnum;
	params = encodeURI(params);
	$.post(action+"?"+params,null,function(data){
		$("#main").hide();
		$("#write-main").hide();
		$("#setup-main").hide();
		vm_analysis_mailDto(data);
		$("#viewMailCot").show();
		var dheight = $(window).height() ;
		if(dheight<=minHeight)
			dheight=minHeight;
		var fjheight = $("#fj_div").height();
		$("#mailContentDiv").height(dheight-126-fjheight-10);
	},"json");
	
	var realArr = window.location.href.split("#");
	var realUrl = realArr[0];
	window.location.href = realUrl + "#viewAttach/"+mailBoxMap.get(folderName)+"/"+isSearch+"/"+msgnum+"/"+messageTotalCount;
}
var antiSpam;
/**
 * 开启或关闭垃圾过滤
 * @param antiSpamEnable
 * @return
 */
function vm_setAntiSpamEnable(antiSpamEnable){
	antiSpam = antiSpamEnable;
}
/**
 * 看信时，将显示的发件人添加到通讯录
 * @return
 */
function vm_addContactPersonAddress(mail){
	var from=mail.split("<");
	addContactPerson(true);
	$("#nickname").val($.trim(from[0].replace(/"/g,"")));
	$("#email").val($.trim(from[1].replace(/>/g,"")));
	if($.trim(from[1].replace(/>/g,""))!=""){
		$("#email").attr("readonly","readonly");
	}
	
	
}
/**
 * 看信时，将显示的发件人添加到黑名单
 * @return
 */
function vm_addBlack(){
	$("#vm_option_panel").nfschinaDialog("close");
	var email=$("#vm_option_email").val();
	email = email.split("<");
	email = $.trim(email[1].replace(/>/g,""));
	var antiSpamEnable = antiSpam;
	if(antiSpamEnable){
		nfschinaDialogConfirm("添加黑名单","您确定要将此用户添加到黑名单吗？",function(){
			var blackemail = email;
			webmail.Utils.ajax({
				url : 'addToBlackList.action',
				params : {"email":blackemail} ,
				method : 'post',
				dataType : 'json',
				callback : function(data){
					var dataInfo = '';
					if(data.jsonStatus){
						dataInfo = "添加"+blackemail+"到黑名单成功";
					} else {
						dataInfo = data.errorMessage;
					}
					$("#saveDraftSuccess span").text(dataInfo);
					$("#saveDraftSuccess").show();
					setTimeout(function(){
						$("#saveDraftSuccess").hide();
					},2000);
				}
			}) ;
			$("#toolConfirm").nfschinaDialog("close");
		});
	} else {
		nfschinaDialogAlert("系统反垃圾策略已禁用，不能添加到黑名单",300000,"系统提示");
	}
}
/**
 * 看信时，将显示的发件人添加到白名单
 * @return
 */
function vm_addWhite(){
	$("#vm_option_panel").nfschinaDialog("close");
	var email=$("#vm_option_email").val();
	email = email.split("<");
	email = $.trim(email[1].replace(/>/g,""));
	var antiSpamEnable = antiSpam;
	if(antiSpamEnable){
		nfschinaDialogConfirm("添加白名单","您确定要将此用户添加到白名单吗？",function(){
			var whiteemail = email;
			webmail.Utils.ajax({
				url : 'addToWhiteList.action',
				params : {"email": whiteemail},
				method : 'post',
				dataType : 'json',
				callback : function(data){
					var dataInfo = '';
					if(data.jsonStatus){
						dataInfo = "添加"+whiteemail+"到白名单成功";
					} else {
						dataInfo = data.errorMessage;
					}
					$("#saveDraftSuccess span").text(dataInfo);
					$("#saveDraftSuccess").show();
					setTimeout(function(){
						$("#saveDraftSuccess").hide();
					},2000);
				}
			}) ;
			$("#toolConfirm").nfschinaDialog("close");
		});
	} else {
		nfschinaDialogAlert("系统反垃圾策略已禁用，不能添加到白名单",300000,"系统提示");
	}
}
/**
 * 初始化看信页的导航
 * @param data
 * @return
 */
function vm_menu_initViewMailDetail(data){
	
	var folderName=data.mailDto.folderName;
	var msgnum				= data.mailDto.msgnum;
	var isSearch 			= data.mailDto.isSearch;
	var preMsgnum 			= data.mailDto.preMsgnum;
	var nextMsgnum 			= data.mailDto.nextMsgnum;
	var preMsgnumName 		= data.mailDto.preMsgnumName;
	var nextMsgnumName 		= data.mailDto.nextMsgnumName;
	var messageTotalCount	= data.messageTotalCount;
	var mailPageNo 			= data.mailDto.mailPageNo;
	var subject				= data.mailDto.subject;
	//缓存该邮件的一些信息，方便其他操作获取参数。
	var vm_menu=$("#vm_menu");
	vm_menu.data("folderName",folderName)
		   .data("msgnum",msgnum)
		   .data("isSearch",isSearch)
		   .data("preMsgnum",preMsgnum)
		   .data("nextMsgnum",nextMsgnum)
		   .data("preMsgnumName",preMsgnumName)
		   .data("nextMsgnumName",nextMsgnumName)
		   .data("messageTotalCount",messageTotalCount)
		   .data("mailPageNo",mailPageNo)
		   .data("subject",subject);
	init_moveto_list("viewMailMoveTo",folderName);
	if(folderName == "草稿箱"){
		$("#comdraftImg").show();
		$("#comdraftMenu").show();
		$("#newMsgMenu").hide();
		$("#newMsgImg").hide();
	} else if(folderName == "已发送"){
		$("#comdraftImg").hide();
		$("#comdraftMenu").hide();
		$("#newMsgMenu").show();
		$("#newMsgImg").show();
	} else {
		$("#comdraftImg").hide();
		$("#comdraftMenu").hide();
		$("#newMsgMenu").hide();
		$("#newMsgImg").hide();
	}
	$("#showMailHeaders").text("显示邮件头");
	$("#forwardMenu1").show();
	$("#forwardMenu2").hide();
	$("#forwardMenu3").show();
}

/**
 * 显示邮件头
 * @param o
 * @return
 */
function vm_menu_showMailHeader(o){
	var text = $(o).text() ;
	if(text.indexOf("头") != -1) {
		$(o).text("显示邮件") ;
		vm_menu_displayMailHeaders(true) ;
	}else {
		$(o).text("显示邮件头") ;
		vm_menu_displayMailHeaders(false) ;
	}
}

/**
 * 返回按钮
 * @return
 */
function vm_menu_back() { //返回事件处理\
	var fold =	$.trim($("#folderText").text());
	ishashchange = true;
	var realArr = window.location.href.split("#");
	var realUrl = realArr[0];
	window.location.href = realUrl + "#"+mailBoxMap.get(fold);
	$("#viewMailCot").hide();
	$("#main").show();
}
/**
 * 移动按钮
 * @param obj
 * @return
 */
function vm_menu_moveMailDetail(obj){
	
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	var isSearch 		= vm_menu.data("isSearch");
	var preMsgnum 		= vm_menu.data("preMsgnum");
	var nextMsgnum 		= vm_menu.data("nextMsgnum");
	var preMsgnumName 	= vm_menu.data("preMsgnumName");
	var nextMsgnumName 	= vm_menu.data("nextMsgnumName");
	var folderText = $("#folderText").text();
	if(folderText=="搜索结果" || folderText=="未读邮件"){
		//清除
		clearCertainMailCache("搜索结果-",folderName+"@"+msgnum+"@1");
		//删除上一封的缓存(使之重新请求，可以更新其nextMsgnum的值)
		if(preMsgnum!="") clearCertainMailCache("搜索结果-",preMsgnumName+"@"+preMsgnum+"@1");
		//删除下一封的缓存(使之重新请求，可以更新其preMsgnum的值)
		if(nextMsgnum!="") clearCertainMailCache("搜索结果-",nextMsgnumName+"@"+nextMsgnum+"@1");
	}
	else{
		//清除自身缓存
		clearCertainMailCache(folderName+"-",folderName+"@"+msgnum+"@0");
		//删除上一封的缓存(使之重新请求，可以更新其nextMsgnum的值)
		if(preMsgnum!="") clearCertainMailCache(folderName+"-",folderName+"@"+preMsgnum+"@0");
		//删除下一封的缓存(使之重新请求，可以更新其preMsgnum的值)
		if(nextMsgnum!="") clearCertainMailCache(folderName+"-",folderName+"@"+nextMsgnum+"@0");
	}
	if(msgnum.indexOf(".") == -1) {
		var destFolderName = $(obj).attr("title");
		var srcFolderName = folderName;
		webmail.Utils.ajax({
			dataType : 'json',
			url		: "moveMails.action" ,
			params  : "isSearch="+isSearch+"&msgnums="+msgnum+"&destFolderName="+destFolderName+"&srcFolderName="+folderName+"&folderText="+folderText+"&currentPage="+parseInt($("#select_page_inbox").val()),
			method  : "post" ,
			callback : function(data){
				if(data.jsonStatus){
					clearCache(destFolderName);							
					clearCache(srcFolderName);
					showMailListInfo(data);
					var pageMap ;
					if(map.get(srcFolderName)){ pageMap= map.get(srcFolderName);}
					else{ pageMap = new Map(); }
					pageMap.put(parseInt($("#select_page_inbox").val()),data);
					map.put(srcFolderName,pageMap);
					
					$("#viewMailCot").hide();
					$("#main").show();
					//清除目标邮件夹的邮件缓存
					clearCache(destFolderName+"-"); 
				}else{nfschinaDialogAlert(data.errorMessage);}
			}
		}) ;
	}
}
var viewMailPros ;
if(!viewMailPros) viewMailPros = { };
/**
 * 显示邮件头
 * @param isHeader
 * @return
 */
function vm_menu_displayMailHeaders(isHeader) {
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	var mailDetail = $("#mailDetail").contents();
	if(isHeader) {
		viewMailPros.mailContent = mailDetail.find("body").html() ;
		$.ajax({
			url : 'viewMailHeader.action',
			data:{"folderName":folderName,"msgnum":msgnum},
			method : 'post',
			async:false,
			cache:false,
			success: function(data) {
				data = data.replace(/\n\r|\n|\r\n|\r/ig,"<br/>") ;
				mailDetail.find("body").html(data) ;
			}
		}) ;
	}else {
		mailDetail.find("body").html(viewMailPros.mailContent) ;
	}
}
/**
 * 下载邮件。用href=""的方式可能乱码(见$("#downloadMail").attr....但是此方式在360浏览器不会触发两次请求)。
 * 此form的method需为get，否则在360浏览器中设置为"迅雷下载"时失败。
 * 且get方法触发多次请求的话会带上相关参数。
 * @return
 */
function vm_menu_downloadMail(event) {
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	var subject			= $("#subjectContent").text();
	
	//取消冒泡，避免触发backAndForward.js中body的click事件的处理方法
	if(window.event)
		window.event.cancelBubble=true;
	else
		event.stopPropagation();
	
	$("<form id='downloadMailForm' method='get' action='downloadMail.action'></form>")
	.append(
			'<input type="hidden" name="folderName" value="'+folderName+'">'+
			'<input type="hidden" name="msgnum" value="'+msgnum+'">'+
			'<input type="hidden" name="mailDto.subject" value="'+subject+'">'
			)
	.appendTo(document.body).trigger("submit").remove() ;
}
/**
 * 删除此封邮件
 * @param permanentlyDelete
 * @return
 */
function vm_menu_deleteMail(permanentlyDelete) {
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	var isSearch 		= vm_menu.data("isSearch");
	var preMsgnum 		= vm_menu.data("preMsgnum");
	var nextMsgnum 		= vm_menu.data("nextMsgnum");
	var preMsgnumName 	= vm_menu.data("preMsgnumName");
	var nextMsgnumName 	= vm_menu.data("nextMsgnumName");
	var folderText = $("#folderText").text();
	if(folderText=="搜索结果" || folderText=="未读邮件"){
		//清除自身缓存
		clearCertainMailCache("搜索结果-",folderName+"@"+msgnum+"@1");
		//删除上一封的缓存(使之重新请求，可以更新其nextMsgnum的值)
		if(preMsgnum!="") clearCertainMailCache("搜索结果-",preMsgnumName+"@"+preMsgnum+"@1");
		//删除下一封的缓存(使之重新请求，可以更新其preMsgnum的值)
		if(nextMsgnum!="") clearCertainMailCache("搜索结果-",preMsgnumName+"@"+nextMsgnum+"@1");
	}
	else{
		//清除自身缓存
		clearCertainMailCache(folderName+"-",folderName+"@"+msgnum+"@0");
		//删除上一封的缓存(使之重新请求，可以更新其nextMsgnum的值)
		if(preMsgnum!="") clearCertainMailCache(folderName+"-",folderName+"@"+preMsgnum+"@0");
		//删除下一封的缓存(使之重新请求，可以更新其preMsgnum的值)
		if(nextMsgnum!="") clearCertainMailCache(folderName+"-",folderName+"@"+nextMsgnum+"@0");
	}
	
	var paramsString = "msgnums=" + msgnum + 
					"&folderName=" + folderName + 
					"&permanentlyDelete=" + permanentlyDelete+
					"&isSearch="+isSearch+"&folderText="+folderText;
	if(permanentlyDelete){
		nfschinaDialogConfirm("彻底删除此邮件","您确定要彻底删除此封邮件",function(){
			$("#toolConfirm").nfschinaDialog("close");
			webmail.Utils.ajax({
				dataType : 'json',
				url : 'deleteMails.action',
				params : paramsString ,
				method : 'post',
				async  : false,
				callback : function(data) {
					if(data.jsonStatus){
						clearCache(folderName);
						showMailListInfo(data);
						$("#viewMailCot").hide();
						$("#main").show();
						if(isSearch == 0)
						{
							var pageMap;
							if(map.get(folderName)){ pageMap= map.get(folderName);}
							else{ pageMap = new Map(); }
							pageMap.put(parseInt($("#select_page_inbox").val()),data);
							map.put(folderName,pageMap);
						}
					}
					else{nfschinaDialogAlert(data.errorMessage);}
					
				}
			}) ;
		});
	}else if(folderName=="废件箱") {
		nfschinaDialogConfirm(
				"废件箱删除邮件","从废件箱删除不可恢复，您确定要删除这些邮件吗？",
				function(){
					webmail.Utils.ajax({
						dataType : 'json',
						url : 'deleteMails.action',
						params : paramsString ,
						method : 'post',
						async  : false,
						callback : function(data) {
							if(data.jsonStatus){
								clearCache(folderName);
								clearCache('废件箱');
								showMailListInfo(data);
								$("#viewMailCot").hide();
								$("#main").show();
								var pageMap;
								if(map.get(folderName)){ pageMap= map.get(folderName);}
								else{ pageMap = new Map(); }
								pageMap.put(1,data);
								map.put(folderText,pageMap);
								//清除废件箱邮件缓存
								clearCache("废件箱-");
							}else{nfschinaDialogAlert(data.errorMessage);}
						}
					}) ;
					$("#toolConfirm").nfschinaDialog("close");
				});
	}
	else {
		webmail.Utils.ajax({
			dataType : 'json',
			url : 'deleteMails.action',
			params : paramsString ,
			method : 'post',
			async  : false,
			callback : function(data) {
				if(data.jsonStatus){
					clearCache(folderName);
					clearCache('废件箱');
					showMailListInfo(data);
					$("#viewMailCot").hide();
					$("#main").show();
					
					var pageMap;
					if(map.get(folderName)){ pageMap= map.get(folderName);}
					else{ pageMap = new Map(); }
					pageMap.put(1,data);
					map.put(folderText,pageMap);
					//清除废件箱邮件缓存
					clearCache("废件箱-");
				}else{nfschinaDialogAlert(data.errorMessage);}
			}
		}) ;
	}
}
/**
 * 作为附件转发
 * @return
 */
function vm_menu_prepareFwdMailAsAttach(){
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	$.ajax({
		url: "prepareFwdMailAsAttach.action",
		data: {"msgnum":msgnum,"folderName":folderName},
		method:'post',
		cache : false,
		success: function(data){
			if(swfu)
				swfu.destroy();
			$("#write-main").html(data);
			wm_init_swf_ckeditor();
			loadContactGroups();
			$(".second-layer").find("li").removeClass('on');
			$("#main").hide();
			$("#viewMailCot").hide();
			$("#write-main").show();
		}
	});		
	window.location.href = window.location.href.split("#")[0]+"#"+"compose";
}
/**
 * 转发（转发邮件内容）
 * @return
 */
function vm_menu_forwardMailInline(){
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	$.ajax({
		url: "prepareForwardMailInline.action",
		data: {"msgnum":msgnum,"folderName":folderName},
		method:'post',
		cache : false,
		success: function(data){
			if(swfu)
				swfu.destroy();
			$("#write-main").html(data);
			wm_init_swf_ckeditor();
			loadContactGroups();
			$(".second-layer").find("li").removeClass('on');
			$("#main").hide();
			$("#viewMailCot").hide();
			$("#write-main").show();
		}
	});
	window.location.href = window.location.href.split("#")[0]+"#"+"compose";
}

/**
 * 回复
 * @param flag
 * @return
 */
function vm_menu_prepareReplyMail(flag){
	if(window.location.href.indexOf("viewAttach")==-1){
		var vm_menu=$("#vm_menu");
		var folderName 		= vm_menu.data("folderName");
		var msgnum 			= vm_menu.data("msgnum");
		
		var decryptedKey="";
		$.ajax({
			url: "prepareReplyMail.action",
			data: {"msgnum":msgnum,"folderName":folderName,"replyToAll":flag,"decryptedKey":decryptedKey},
			method:'post',
			cache : false,
			success: function(data){
				if(swfu)
					swfu.destroy();
				$("#write-main").html(data);
				wm_init_swf_ckeditor();
				loadContactGroups();
				$(".second-layer").find("li").removeClass('on');
				$("#main").hide();
				$("#viewMailCot").hide();
				$("#write-main").show();
			}
		});
		window.location.href = window.location.href.split("#")[0]+"#"+"compose";
	}else{
		
		var mailContent = $(document.getElementById('mailDetail').contentWindow.document.body).text();
		
		var fromAddress = $("#fromAddress").text();
		var subject = $("#subjectContent").text();
		writeMailTo(fromAddress.split("<")[0],fromAddress.split("<")[1].split(">")[0]);
		$("#mailSubject").val(subject);
		$("#cke_contents_content iframe").contents().find("body").text(mailContent);
		window.location.href = window.location.href.split("#")[0]+"#"+"compose";
	}
}
/**
 * 作为新邮件编辑和完成草稿
 * @return
 */
function vm_menu_reeditMail(){
	var vm_menu=$("#vm_menu");
	var folderName 		= vm_menu.data("folderName");
	var msgnum 			= vm_menu.data("msgnum");
	$.ajax({
		url: "reeditMail.action",
		data: {"msgnum":msgnum,"folderName":folderName},
		method:'post',
		cache : false,
		success: function(data){
			if(swfu)
				swfu.destroy();
			$("#write-main").html(data);
			wm_init_swf_ckeditor();
			loadContactGroups();
			$(".second-layer").find("li").removeClass('on');
			$("#main").hide();
			$("#viewMailCot").hide();
			$("#write-main").show();
			var realUrl = window.location.href.split("#")[0];
			window.location.href = realUrl + "#compose";
		}
	});
}
/**
 * 点击上一页下一页执行的方法
 * @param str
 * @return
 */
function vm_menu_nextPre(str){
	
	$("#navMsgPre,#navMsgNxt").attr("disabled","disabled");
	var vm_menu=$("#vm_menu");
	var folderName 			= vm_menu.data("folderName");
	var msgnum 				= vm_menu.data("msgnum");
	var isSearch 			= vm_menu.data("isSearch");
	var preMsgnum 			= vm_menu.data("preMsgnum");
	var nextMsgnum 			= vm_menu.data("nextMsgnum");
	var preMsgnumName 		= vm_menu.data("preMsgnumName");
	var nextMsgnumName 		= vm_menu.data("nextMsgnumName");
	var messageTotalCount 	= vm_menu.data("messageTotalCount");
	var mailPageNo 			= vm_menu.data("mailPageNo");
	var currPageNo 			= mailPageNo;
	var realFolderName = "";
	var key_folder = "";
	var folderText = $.trim($("#folderText").text());
	if(folderText == '搜索结果' || folderText == '未读邮件'){
		currPageNo = 'search';
	}
	if(str=="next")
	{
		if(folderText == '搜索结果' || folderText == '未读邮件') 
		{
			realFolderName = nextMsgnumName; 
			key_folder = "搜索结果-";
			key_mail = realFolderName+"@"+nextMsgnum+"@1"; 
			isSearch = "1";
		}
		else 
		{ 
			realFolderName = folderName; 
			key_folder = folderName+"-";
			key_mail = realFolderName+"@"+nextMsgnum+"@0"; 
			isSearch = "0";
		}
	}else if(str=="pre"){
		if(folderText == '搜索结果' || folderText == '未读邮件') 
		{ 	
			realFolderName = preMsgnumName; 
			key_folder = "搜索结果-";
			key_mail = realFolderName+"@"+preMsgnum+"@1"; 
			isSearch = "1";
		}
		else 
		{	
			realFolderName = folderName; 
			key_folder = folderName+"-";
			key_mail = realFolderName+"@"+preMsgnum+"@0"; 
			isSearch = "0";
		}
	}
	
	if(str=="next")
	{
		params = "messageTotalCount="+messageTotalCount+"&mailPageNo="+mailPageNo+"&folderName="+realFolderName+"&isSearch="+isSearch+"&msgnum="+nextMsgnum+"&_="+get6Rondom();
	}else if(str=="pre"){
		params = "messageTotalCount="+messageTotalCount+"&mailPageNo="+mailPageNo+"&folderName="+realFolderName+"&isSearch="+isSearch+"&msgnum="+preMsgnum+"&_="+get6Rondom();
	}
	vm_display_mail(params, key_folder, key_mail);
}
/**
 * 当鼠标点击收件人或者发送人上时，出现操作选项
 * @return
 */
function vm_show_option(obj)
{
	var email="";
	var jobj=$(obj);
	if(jobj.attr("id")=="fromAddress")
		email=jobj.text();
	else
		email=jobj.attr("title");
	var dialogOption = {
                       title: "联系人选项",/*标题*/
                       delay: -1,
                       width:280
                   };
   	$("#vm_option_panel").nfschinaDialog(dialogOption);
   	$("#vm_option_email").val(email);
}
/**
 * 添加联系人
 * @return
 */
function vm_option_add()
{
	$("#vm_option_panel").nfschinaDialog("close");
	vm_addContactPersonAddress($("#vm_option_email").val());
}
/**
 * 给某人写信
 * @return
 */
function vm_writeMailTo()
{
	$("#vm_option_panel").nfschinaDialog("close");
	var email=$("#vm_option_email").val();
	email=email.split("<");
	writeMailTo($.trim(email[0].replace(/"/g,"")),$.trim(email[1].replace(/>/g,"")));
}