/**
 * 找到列表中的全部复选框的jquery对象，在init_ml()中进行赋值，备用。
 * $("input[name=msgnums]");
 */
var listCheckbox=null;
/**
 * 页数列表标签的jquery对象，在init_ml()中进行赋值，备用。
 * $("#select_page_inbox")
 */
var selectNode = null;
/**
 * 上一页的jquery对象；在init_ml()中进行赋值，备用。
 * $("#btnNxt")
 */
var btnNxt = null;
/**
 * 上一页的jquery对象；在init_ml()中进行赋值，备用。
 * $("#btnPre")
 */
var btnPre = null;
/**
 * 当前邮件列表所在的邮件夹名称，在init_ml()中进行赋值，备用。
 */
var ml_folder_name=null;
/**
 * 构建邮件列表时，每次加载的条数。
 * 在一页显示100条数据的时候，如果一次让浏览器（如：IE）解析并展示全部数据那么会很慢，所以采用循环加载的策略，
 * 每次加载mailsNo条，直至加载完成。
 */
var mailsNo = 25;
$(function(){
	$("body").click(function(event){
		var obj = event.srcElement ? event.srcElement : event.target;
		if(obj.className!="MenuBarItemSubmenu" && obj.className!="showImmediate")
			$(".tabnav .mousein").removeClass("mousein").hide();
	});
})

/**
 * 显示邮件列表，设置一些元素的显示内容：
 * 1,在哪个邮件夹，
 * 2,有多少封未读邮件，
 * 3,保存一些分页用到的数据generateDataPageInfo()
 * 4,构建邮件列表
 * 5,构建导航按钮的内容
 * @param data
 * @return
 */
function showMailListInfo(data){
	$("#main").html(mailListDisplayStructure);
	$("#mpage").height($(window).height()-107);
	if(typeof(data)!='undefined')
		$("#folderText").text(data.folderName);
	$("#unseenMessageCount").text(data.unseenMessageCount);
	$("#unseeSpamMessageCount").text(data.unseenSpamMessageCount);
	$("#_unseenSpCount").val(data.unseenSpamMessageCount);
	selectNode=$("#select_page_inbox");
	ml_folder_name=$.trim($("#folderText").text());
	btnNxt=$("#btnNxt");
	btnPre=$("#btnPre");
	if(data.folderName == '搜索结果' || data.folderName == '未读邮件')
		$("#click_ml_unread_msgNUM").hide();
	/*不是收件箱时，隐藏垃圾邮件数量显示*/
	if(data.folderName != '收件箱') 
	{
		$("#spamMsgNum").hide().prev().removeClass("pointer");
	}
	
	generateDataPageInfo(data);
	$("#mailList").html('');
	generateMailList(data,0);
	init_ml_menu();
	init_ml_mouseover_sort(data); //初始化列表页JS
}
/**
 * 如果当前页面已经显示了邮件列表，那么就不用再初始化一些信息了
 * 这个方法只用来生成邮件列表的内容。
 * 
 * @param data
 * @return
 */
function showContinuePage(data){
	generateDataPageInfo(data);
	$("#mailList").html('');
	generateMailList(data,0);
	init_ml_mouseover_sort(data); //初始化列表页JS
}
/**
 * 保存分页的数据
 * @param data
 * @return
 */
function generateDataPageInfo(data){
	var messageNumberBetween = (data.dataPage.totalCount==0?0:data.dataPage.start+1)+" - "+(data.dataPage.start+data.dataPage.pageSize>data.dataPage.totalCount?data.dataPage.totalCount:data.dataPage.start+data.dataPage.pageSize);
	$("#messageNumberBetween").text(messageNumberBetween);
	$("#messageNumberTotal").text($.trim(data.dataPage.totalCount+""));
	$("#_sortCriterion").val(data.dataPage.sortCriterion);
	$("#_reverseFlag").val(data.dataPage.reverseFlag);
	$("#_totalPageCount").val(data.dataPage.totalPageCount);
	$("#_currentPage").val(data.currentPage);
	if(data.searchMailDto != null){
		$("#_dpcurrentPage").val(data.dataPage.currentPageNo);
		$("#_searchResultScope").val(data.searchMailDto == null?"":data.searchMailDto.scope);
	}
}
/**
 * 构建邮件显示列表
 * @param data
 * @param begin
 * @return
 */
function generateMailList(data, begin){
	var folderName = data.folderName;
	$("#list_moveImg,#list_move").show();
	var listInfo = new Array();
	var len = data.dataPage.data.length;
	var start = begin;
	var end;
	if((len - start)/mailsNo >= 1){
		end = start + mailsNo;
	} else {
		end = len;
	}
	var checkStrLength = webmail.Utils.checkStrLength ;
	while(start < end){
		var mailData = data.dataPage.data[start];
		if(!mailData.seen && mailData.flagged)//未读且标记
			listInfo.push("<tr folderName='"+mailData.folderName+"' class='mail-bottom-line not-seen flagged' onClick='click_ml_msg(this)'>");
		else if(!mailData.seen && !mailData.flagged)//未读但没标记
			listInfo.push("<tr class='mail-bottom-line not-seen' onClick='click_ml_msg(this)'>");
		else if(mailData.seen && mailData.flagged)//已读但标记
			listInfo.push("<tr class='mail-bottom-line flagged' onClick='click_ml_msg(this)'>");
		else
			listInfo.push("<tr class='mail-bottom-line' onClick='click_ml_msg(this)'>");
		if(folderName == '搜索结果' ){
			listInfo.push("<td onclick='click_ml_i1c(this,event)' class='i1c' align='center'><input onclick='click_ml_checkbox(this,event)' onfocus='this.blur()' type='checkbox' name='msgnums' value='"+mailData.msgnum+"@"+mailData.folderName+"' />");
		    listInfo.push("<input type='hidden' name='isSearch' value='"+mailData.isSearch+"' />");
		    listInfo.push("</td>");
		} else {
			listInfo.push("<td onclick='click_ml_i1c(this,event)' class='i1c' align='center'><input onclick='click_ml_checkbox(this,event)' onfocus='this.blur()' type='checkbox' name='msgnums' value='"+mailData.msgnum+"' />");
		    listInfo.push("<input type='hidden' name='isSearch' value='"+mailData.isSearch+"' />");
		    listInfo.push("</td>");
		}
	    listInfo.push("<td class='i2'>");
	   	listInfo.push("<div class='div-border-none'>");
	   	listInfo.push("<table cellpadding='0' cellspacing='0' border='0' width='100%'>");
	   	listInfo.push("<tr>");
	   	listInfo.push("<td width='20%' align='center'>");
	    if(!mailData.seen){
	    	listInfo.push("<img title='未读' src='images/com/mail.gif'/>");
	   	} else {
	   		listInfo.push("<div class='haveRd'></div>");
	   	}
	    listInfo.push("</td><td width='20%' align='center'>");
	    if(mailData.haveAttachments){
	    	listInfo.push("<img title='含附件' src='images/com/r_9.gif'/>");
	    }
	    listInfo.push("</td><td width='20%' align='center'>");
	    if(mailData.flagForReFw=="3"){
	    listInfo.push("<img title='已回复且转发' src='images/com/refw.gif'/>");
	    } else if(mailData.flagForReFw=="2"){
	    listInfo.push("<img title='已转发' src='images/com/fw.gif'/>");
	    } else if(mailData.flagForReFw=="1"){
	    listInfo.push("<img title='已回复' src='images/com/re.gif'/>");
	    }
	    
	    listInfo.push("</td><td width='20%' align='center'>");
	    if(mailData.priority=='紧急'){
	    listInfo.push("<img title='紧急' src='images/com/jinji.gif'/>");
	    }
	    listInfo.push("</td></tr></table></div>");
	    listInfo.push("</td>");
	    
	    var recipVal ;
    	if(typeof mailData.recipients[0] == 'undefined'){
    		recipVal = "(收件人未填写)" ;
    	} else {
    		var tempRecipVal="";
    		for(var i=0;i<mailData.recipients.length;i++){
    			tempRecipVal+=mailData.recipients[i].nickname+(i==mailData.recipients.length-1?"":",");
    		}
    		var recipVal=tempRecipVal;
	    	if(recipVal == null) recipVal = "(收件人未填写)" ;
    	}
	    if(folderName == '草稿箱' || folderName == '已发送') {
	    	listInfo.push("<td title='"+recipVal+"'>"+checkStrLength($.trim(recipVal),24));
		    listInfo.push("<input type='hidden' name='msg-recipients' value='"+recipVal+"' />");
		    listInfo.push("</td>");
	    } else {
	    	listInfo.push("<td title='"+mailData.from.nickname+"'>"+checkStrLength($.trim(mailData.from.nickname),24));
		    listInfo.push("<input type='hidden' name='msg-recipients' value='"+recipVal+"' />");
		    listInfo.push("</td>");
	    }
	    
	    
	    listInfo.push("<td class='i6c' onclick='click_ml_i6c(this,event)' folderName='"+mailData.folderName+"' msgnum='"+mailData.msgnum+"'><div");
	    if(mailData.flagged){
	    	listInfo.push(" class='img-span flagtrue' title='已标记'>");
	    } else {
	    	listInfo.push(" class='img-span flagfalse' title='未标记'>");
	    }
	    listInfo.push("</div></td>");
	    if(folderName == '搜索结果'){
	    	listInfo.push("<td title='"+escapeHtml(mailData.subject)+"'>"+"<font style='color:#777777'>["+mailData.folderName+"]</font>"+escapeHtml(checkStrLength($.trim(mailData.subject),48))+"<input type='hidden' name='folderName' value='["+mailData.folderName+"]' />&nbsp;");
	    	listInfo.push("</td>");
	    }
	    else {
	    	listInfo.push("<td title='"+escapeHtml(mailData.subject)+"'>"+escapeHtml(checkStrLength($.trim(mailData.subject),48))+"<input type='hidden' name='folderName' value='["+mailData.folderName+"]' />&nbsp;");
	    	listInfo.push("</td>");
	    }
	    listInfo.push("<td>");
	    listInfo.push(mailData.formatDate);
	    listInfo.push("</td>");
	    listInfo.push("</tr>");
	    start++;
	}
	var mailString = listInfo.join('');
	$("#mailList").append(mailString);
    if(end < len){
    	setTimeout(function() {
    		generateMailList(data, end);
    		$(".mail-bottom-line").hover(//为邮件列表添加鼠标滑过事件
    				function(){$(this).addClass("mousein");},
    				function(){$(this).removeClass("mousein");}
    			);
}, 50);
    }
    else
    {
    	//获取一些后面用到的元素jquery对象，备用
        listCheckbox=$("input[name=msgnums]");
    }
}
/**
 * 初始化分页情况
 * @param totalPage
 * @param ajaxOptions
 * @param currentPageNo
 * @return
 */
function init_ml_pagination(totalPage,currentPageNo){
	var optArr = new Array();
	for(var i = 1 ; i <= totalPage ; i++) {
		if(i!=currentPageNo)
			optArr.push("<option value='"+i+"'>"+i+"/"+totalPage+"</option>") ;
		else
			optArr.push("<option selected='selected' value='"+i+"'>"+i+"/"+totalPage+"</option>") ;
	}		
	if(totalPage == 0) {
		optArr.push("<option value='1'>0/0</option>") ;
	}
	var htmlStr = optArr.join('');
	selectNode.html(htmlStr);
	if(totalPage <= 1) {
		btnNxt.css("visibility","hidden") ;
	}
	if(currentPageNo == 1) {
		btnPre.css("visibility","hidden") ;
	}else {
		btnPre.css("visibility","visible") ;
	}
}
/**
 * 初始化邮件列表，判断并处理一些显示方案：
 * 读取当前的列表中的邮件，并赋值给listCheckbox，备用。
 * 鼠标滑过。
 * 不用邮件夹显示不同的排序条件。
 */
function init_ml_mouseover_sort(data){
	if(ml_folder_name == '草稿箱' || ml_folder_name == '已发送') {//如果当前邮件箱是草稿箱,已发送或者搜索结果，则需要调整显示列的信息
		$("#relatedPersonHeader").text("收件人").parent().unbind("click").click(function(){sort('to','收件人');}) ;
		if($("#_sortCriterion").val() == "FROM")
			$("#_sortCriterion").val("TO");
	} else if(ml_folder_name == '搜索结果') {
//		$("#msgnum-checkbox").parent().hide();
		$("#relatedPersonHeader").text("发件人").parent().unbind("click").click(function(){sort('from','发件人');}) ;
		$("#i2ForBorder").css("border-left","1px solid #ccc");
	} else {
		$("#relatedPersonHeader").text("发件人").parent().unbind("click").click(function(){sort('from','发件人');}) ;
	}
	setSortIco(data) ;
    //根据当前页面的最新未读邮件数初始化主菜单未读邮件数,如果缓存的值比当前显示的要大，则是缓存中的数目在读信时未减小，不进行更新
	var folderText = $("#folderText").text();
	if(ml_folder_name != '搜索结果' && ml_folder_name != '未读邮件'){
		//如果邮件列表已缓存，则将以下2个值置为null，以免未读数变化后缓存中的值未变化，导致显示未读数不准确。置为null则text(..)时页面显示不会更新。
		if(typeof(map.get(folderText))!='undefined' && typeof(map.get(folderText).get(1))!='undefined'){
			data.unseenInboxMessageCount = null;
			data.unseenSpamMessageCount = null;
			map.get(folderText).put(parseInt($("#select_page_inbox").val()),data);
		}
	}
	$("#inboxUnreadNum").text(data.unseenInboxMessageCount) ;
	$("#spamboxUnreadNum").text(data.unseenSpamMessageCount) ;
	if(ml_folder_name == "收件箱" || ml_folder_name == '垃圾邮件') {
		if(data.unseenSpamMessageCount==null){
			$("#unseeSpamMessageCount").text($("#spamboxUnreadNum").text());
		}else{
			$("#unseeSpamMessageCount").text(data.unseenSpamMessageCount) ;
		}
	}
}
/**
 * 初始化导航栏的一些显示
 * @param folderName
 * @return
 */
function init_ml_menu(){
	var totalPageCount=parseInt($("#_totalPageCount").val());
	var currentPageCount=$("#_currentPage").val();
	if(ml_folder_name != '搜索结果' && ml_folder_name != '未读邮件')
	{
		init_moveto_list("mailListMoveTo",ml_folder_name);
		init_ml_pagination(totalPageCount,currentPageCount);
		$("#list-menu-bar").show();
		$("#deleteAll").show();
	}
	else if(ml_folder_name == '搜索结果' || ml_folder_name == '未读邮件')
	{
		init_moveto_list("mailListMoveTo",ml_folder_name);
		init_ml_pagination(totalPageCount,currentPageCount);
		$("#list-menu-bar").show();
		$("#deleteAll").hide();
	}
}
/**
 * 初始化邮件列表和查看邮件导航中“移动到”选项的列表，要列出不包含当前邮件夹的其他邮件夹
 * @param domId 邮件列表中的“移动到”id和查看邮件中的“移动到”id
 * @return
 */
function init_moveto_list(domId,folderName)
{
	if(folderName=="未读邮件") folderName = "收件箱";
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
				lis.push("<li><a href='javascript:void(0)' onclick='click_ml_moveto_a(this)' title='"+boxs[i]+"'>"+boxs[i].cut(7)+"</a></li>");
		}
		$("#mailListMoveTo").html(lis.join(""));
	}
}
/**
 * 更具当前邮件列表的排序规则，设置排序图标的显示方案。
 * @return
 */
function setSortIco(data){
	$(".padding_ico").children().remove("img");
	var sortCriterion = data.sortCriterion;//$("#_sortCriterion").val();
	var reverseFlag = data.reverseFlag;//$("#_reverseFlag").val();
	if(reverseFlag){
		switch (sortCriterion) {
			//降序排列
			case "subject":
				$("#theme").text("主题").append("<span class='padding_ico'><img title='' src='images/com/up.gif'/></span>") ;
				break;
			case "arrival":
				$("#time").text("时间").append("<span class='padding_ico'><img title='' src='images/com/up.gif'/></span>") ;
				break;
			case "from":
				$("#relatedPersonHeader").text("发件人").append("<span class='padding_ico'><img title='' src='images/com/up.gif'/></span>") ;
				break;
			case "to":
				$("#relatedPersonHeader").text("收件人").append("<span class='padding_ico'><img title='' src='images/com/up.gif'/></span>") ;
				break;
			default:
				break;
		}
	} else {
		switch (sortCriterion) {
			//升序排序
			case "subject":
				$("#theme").text("主题").append("<span class='padding_ico'><img title='' src='images/com/down.gif'/></span>") ;
				break;
			case "arrival":
				$("#time").text("时间").append("<span class='padding_ico'><img title='' src='images/com/down.gif'/></span>") ;
				break;
			case "from":
				$("#relatedPersonHeader").text("发件人").append("<span class='padding_ico'><img title='' src='images/com/down.gif'/></span>") ;
				break;
			case "to":
				$("#relatedPersonHeader").text("收件人").append("<span class='padding_ico'><img title='' src='images/com/down.gif'/></span>") ;
				break;
			default:
				break;
		}
	}
}
/**
 * 分页的select标签change事件触发的函数，读取选择页的数据，规划要显示的其他元素（如，上一页，下一页）
 * @return
 */
function pageSelectChange(pageNo)
{
	
	$("#msgnum-checkbox").removeAttr("checked");//翻页后清空全选框的勾选
	btnNxt.attr("disabled",true);
	btnPre.attr("disabled",true);
	var currentPage = pageNo;//parseInt($(obj).val());
	var totalPage = parseInt($("#_totalPageCount").val());
	if(currentPage == totalPage) btnNxt.css("visibility","hidden");
	else btnNxt.css("visibility","visible");
	if(currentPage == 1) btnPre.css("visibility","hidden");
	else btnPre.css("visibility","visible");
	var url="viewMailListContinue.action";
	if(ml_folder_name!="搜索结果"&&ml_folder_name!="未读邮件")
	{
		//如果当前选择的页已经不存在缓存数据
		if(typeof map.get(ml_folder_name).get(currentPage) == 'undefined')
		{
			//发送请求
			$.post(url,{folderName:ml_folder_name,currentPage:currentPage},function(data){
				showContinuePage(data);
				var pageMap = map.get(ml_folder_name);
				pageMap.put(currentPage,data);
				map.put(ml_folder_name,pageMap);
				btnNxt.attr("disabled",false) ;
				btnPre.attr("disabled",false) ;
			},"json");
		}
		else
		{
			var data = map.get(ml_folder_name).get(currentPage);
			showContinuePage(data);
			btnNxt.attr("disabled",false) ;
			btnPre.attr("disabled",false) ;
		}
	}
	else
	{
		url="searchMails.action";
		//发送请求
		$.post(url,{currentPage:currentPage},function(data){
			//如果是未读邮件，修改data中的folderName
			if(ml_folder_name=="未读邮件"){data.folderName="未读邮件";}
			showContinuePage(data);
			btnNxt.attr("disabled",false) ;
			btnPre.attr("disabled",false) ;
		},"json");
	}
}
/**
 * 上一页点击事件触发的函数
 * @return
 */
function click_ml_btnPre(){
	var pageNo = parseInt(selectNode.val())-1;
	selectNode.val(pageNo) ;
	pageSelectChange(pageNo);
	//翻页后清空全选框的勾选
	$("#msgnum-checkbox").removeAttr("checked");

}
/**
 * 下一页点击事件触发的函数
 * @return
 */
function click_ml_btnNxt(){
	var pageNo = parseInt(selectNode.val())+1;
	selectNode.val(pageNo);
	pageSelectChange(pageNo);
	//翻页后清空全选框的勾选
	$("#msgnum-checkbox").removeAttr("checked");
}

/**
 * 点击"移动到"触发的函数.需要处理未读邮件数，由于暂时无法判断邮件的已读未读，目前只处理了从未读邮件列表移动的情况
 * @return
 */
function click_ml_moveto_a(obj)
{
	var msgnums = get_ml_selected_msgs();
	if(!msgnums) return;
	var destFolderName = obj.title;
	var srcFolderName = ml_folder_name;
	
	var folderName = $.trim($("#folderText").text());
	//清除邮件缓存(需重新请求以更新上下封关系)
	if(folderName=='未读邮件'){
		srcFolderName = "收件箱";
		clearCache("搜索结果-");
		clearCache(destFolderName+"-");
	}else{
		clearCache(srcFolderName+"-");
		clearCache(destFolderName+"-");
	}
	var spCout = $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val();
	if(folderName == '未读邮件') {
		
		if(folderName=='未读邮件') folderName = "收件箱";
		
		webmail.Utils.ajax({
			dataType : "json",
			url		: "moveMails.action" ,
			params  : msgnums+"&destFolderName="+destFolderName+"&isSearch=0"+"&srcFolderName="+srcFolderName+"&unseenSpamMessageCount="+spCout+"&currentPage="+parseInt($("#select_page_inbox").val()),
			method  : "post",
			callback : function(data){
				if(data.jsonStatus){
					clearCache(destFolderName);
					clearCache(srcFolderName);			
					click_ml_unread_msg();
				}else{nfschinaDialogAlert(data.errorMessage);}
			}
		});
	}else if(folderName != "搜索结果")
	{
		webmail.Utils.ajax({
			dataType : "json",
			url		: "moveMails.action" ,
			params  : msgnums+"&destFolderName="+destFolderName+"&isSearch=0"+"&srcFolderName="+srcFolderName+"&unseenSpamMessageCount="+spCout+"&currentPage="+parseInt($("#select_page_inbox").val()),
			method  : "post",
			callback : function(data){
				if(data.jsonStatus){
					clearCache(destFolderName);
					clearCache(srcFolderName);
					showMailListInfo(data);
					var pageMap = map.get(srcFolderName);
					pageMap.put(parseInt($("#select_page_inbox").val()),data);
					map.put(srcFolderName,pageMap);
				}else{nfschinaDialogAlert(data.errorMessage);}
			}
		});
	}else{
		var msgnumFolderNames = msgnums.replace(/msgnums/g,"msgnumFolderNames");
		webmail.Utils.ajax({
			dataType : "json",
			url		: "moveMailsInSearch.action" ,
			params  : msgnumFolderNames+"&destFolderName="+destFolderName+"&isSearch=0"+"&srcFolderName="+srcFolderName+"&unseenSpamMessageCount="+spCout+"&currentPage="+parseInt($("#select_page_inbox").val()),
			method  : "post",
			callback : function(data){
				if(data.jsonStatus){
					clearCache(destFolderName);
					//清除缓存
					for(var i=0;i<data.folderNames.length;i++){
						clearCache(data.folderNames[i]);
					}
					showMailListInfo(data);
				}else{nfschinaDialogAlert(data.errorMessage);}
			}
		});
	}
	
}
/**
 * 当右侧（邮件列表和邮件内容显示）导航栏中的三角号被点击时触发的函数
 * @return
 */
function click_menu_triangle(obj)
{
	var thisObj=$(obj);
	$(".mousein").removeClass("mousein").hide();
	thisObj.nextAll("ul").addClass("mousein").show();
}

/**
 * 当右侧（邮件列表和邮件内容显示）导航栏中的A标签被点击时触发的函数
 * 这些A标签点击时没有默认的处理函数，这里是为其添加类似上面三角号被点击时执行的操作，既显示可执行的操作。
 * @param obj
 * @return
 */
function click_menu_a(obj,evt)
{
	var thisObj=$(obj);
	$(".mousein").removeClass("mousein").hide();
	thisObj.parent().nextAll("ul").addClass("mousein").show();
	var e=(evt)?evt:window.event; 
	if(window.event)
		e.cancelBubble=true;
	else
		e.stopPropagation(); 
}

/**
 * 点击查看邮件详细信息
 * @param obj
 * @param encrypted
 * @return
 */
function click_ml_msg(obj){
	var thisObj = $(obj);
	var folderName = $.trim($("#folderText").text());
	var msgNum = thisObj.find("input[name=msgnums]").get(0).value;
	if(folderName=="搜索结果") msgNum = msgNum.split("@")[0];
	var isSearch;
	var realFolderName = "";
	var key_folder = "";
	var key_mail = "";
	//将邮件显示设置成已读.(细字体)
	if(thisObj.hasClass("not-seen")){
		thisObj.removeClass("not-seen").find("img[src*='mail.gif']").replaceWith("<div class='haveRd'></div>") ;
	}
	
	$("#newMsgImg,#newMsgMenu,#comdraftImg,#comdraftMenu,#replayImg,#replayMenu,#forwardImg,#forwardMenu,#deleteImg,#deleteMenu,#moveImg,#moveMenu").show();
	var currPageNo = $("#select_page_inbox").val();
	var mailPageNo = $("#select_page_inbox").val();
	if(folderName == '搜索结果'){
		//获取邮件所在的邮件夹真实名称
		realFolderName = $.trim(thisObj.find("input[name='folderName']").val().replace(/\[|\]/g,'')) ;
		currPageNo = 'search';
		key_folder = "搜索结果-";
		key_mail = realFolderName + "@" + msgNum+"@1";
		isSearch = '1';
	}
	else if(folderName == '未读邮件'){
		realFolderName = $.trim(thisObj.find("input[name='folderName']").val().replace(/\[|\]/g,'')) ;
		currPageNo = 'search';
		key_folder = "搜索结果-";
		key_mail = realFolderName + "@" + msgNum+"@1";
		isSearch = '1';
	}else{
		realFolderName = folderName;
		key_folder = folderName+"-";
		key_mail = realFolderName + "@" + msgNum+"@0";
		isSearch = '0';
	}
	var params = "messageTotalCount="+parseInt($("#messageNumberTotal").text())+"&mailPageNo="+mailPageNo+"&folderName="+realFolderName+"&isSearch="+isSearch+"&msgnum="+msgNum+"&_="+get6Rondom();
	vm_display_mail(params,key_folder,key_mail);
	try{
		if(thisObj.hasClass("not-seen"))
		{
			thisObj.removeClass("not-seen").find("img[src*='mail.gif']").replaceWith("<div class='haveRd'></div>") ;
			var listData = map.get(folderName).get(parseInt($("#select_page_inbox").val()));
			for ( var i = 0; len = listData.dataPage.data.length, i < len; i++) {
				if(listData.dataPage.data[i].msgnum == msgNum){
					listData.dataPage.data[i].seen = true;
				}
			}
		}
	}catch(e){}
}
/**
 * 邮件列表中全选checkbox点击触发的事件
 */
function click_ml_msgnumcheckbox(obj)
{
	if(obj.checked)
		listCheckbox.attr("checked",true) ;
	else
		listCheckbox.attr("checked",false) ;
}
/**
 * 点击邮件列表中的复选框触发的事件
 */
function click_ml_checkbox(obj,evt)
{
	var allcheck=true;//start如果当前列表全部选中，则全选框选中
	listCheckbox.each(function(){
		if(!this.checked)
			allcheck=false;
	});
	if(allcheck)
		$("#msgnum-checkbox").attr("checked",true);
	else
		$("#msgnum-checkbox").attr("checked",false);//end
	var e=(evt)?evt:window.event; 
	if(window.event)
		e.cancelBubble=true;
	else
		e.stopPropagation(); 
}
/**
 * 邮件列表中，复选框的父元素td被点击时触发的事件,他会触发内部的checkbox的点击事件
 */
function click_ml_i1c(obj,evt)
{
	var checkbox=$(obj).children(":checkbox");
	checkbox.attr("checked",!(checkbox.attr("checked")));
	var allcheck=true;//start如果当前列表全部选中，则全选框选中
	listCheckbox.each(function(){
		if(!this.checked)
			allcheck=false;
	});
	if(allcheck)
		$("#msgnum-checkbox").attr("checked",true);
	else
		$("#msgnum-checkbox").attr("checked",false);//end
	var e=(evt)?evt:window.event;
	if(window.event)
		e.cancelBubble=true;
	else
		e.stopPropagation();
}
/**
 * 邮件列表中，点击小红旗所在的td触发的事件。
 * @param obj
 * @param evt
 * @return
 */
function click_ml_i6c(obj,evt)
{
	var thisObj = $(obj);
	var name = $.trim($("#folderText").text());
	if(name == '搜索结果' || name =='未读邮件') {
		name = $.trim(thisObj.attr("folderName"));
	}
	var msgnum = thisObj.attr("msgnum");
	if(thisObj.find(".flagfalse")[0]){					
		webmail.Utils.ajax({
			url		: "markMails.action",
			params  : "msgnums="+msgnum+"&folderName="+name+"&flag=flagged&setFlag=true",
			global  : false,
			method  : "post",
			callback : function() {
				marked(thisObj.parents("tr"));
				var und = map.get(name);
				if(typeof und != 'undefined'){
					var listData = und.get(parseInt($("#select_page_inbox").val()));
					for ( var i = 0; len = listData.dataPage.data.length, i < len; i++) {
						if(listData.dataPage.data[i].msgnum == msgnum){
							listData.dataPage.data[i].flagged = true;
						}
					}
					und.put(parseInt($("#select_page_inbox").val()),listData);
				}
			}
		});
	} else {
		webmail.Utils.ajax({
			url		: "markMails.action",
			params  : "msgnums="+msgnum+"&folderName="+name+"&flag=flagged&setFlag=false",
			global  : false,
			method  : "post",
			callback : function() {
				unmarked(thisObj.parents("tr"));
				var und = map.get(name);
				if(typeof und != 'undefined'){
					var listData = und.get(parseInt($("#select_page_inbox").val()));
					for ( var i = 0; len = listData.dataPage.data.length, i < len; i++) {
						if(listData.dataPage.data[i].msgnum == msgnum){
							listData.dataPage.data[i].flagged = false;
						}
					}
					und.put(parseInt($("#select_page_inbox").val()),listData);
				}
			}
		}) ;
	}
	var e=(evt)?evt:window.event; 
	if(window.event)
		e.cancelBubble=true;
	else
		e.stopPropagation();
}
/**
 * 未读邮件列表
 * @return
 */
function click_ml_unread_msg(){
    var folderName =	$("#folderText").text();
    if(folderName=="未读邮件") folderName = "收件箱";
    
	if(folderName =='收件箱'){//收件箱显示未读邮件，其余的不显示
		$.ajax({
			url:	 "searchMails.action",
			type:	 "POST",
			dataType:"json",
			data:	{"searchMailDto.flag":"seen-false","searchMailDto.scope":"收件箱","searchMailDto.searchFlag":"seenFalseSearch"},
			success	:function(data){
				ishashchange = false;
				var realArr = window.location.href.split("#");
				var realUrl = realArr[0];
				window.location.href = realUrl + "#"+mailBoxMap.get("未读邮件")+"_"+data.currentPage;
				//清除邮件缓存
				clearCache("搜索结果-");
				clearCache("收件箱");
				data.folderName="未读邮件";
				showMailListInfo(data);
				$("#click_ml_unread_msgNUM").html("未读邮件");
				
				//去除邮件夹的选中状态
				$(".second-layer").find("li").removeClass('on');
				$("#main").show();
				$("#write-main").hide();
				$("#setup-main").hide();
				$("#viewMailCot").hide();
			}
		});
	}
}
/**
 * 收件箱的未读垃圾邮件链接处理
 * @param name
 * @return
 */
function click_ml_spam_unread_msg(name){
	$("#sReList").removeClass('on');
	$("#menu-mailbox").children().eq(3).addClass("on");
	$("#personalMailBoxs li").removeClass("on");
	getMailList(name);
	ishashchange = false;
	var realArr = window.location.href.split("#");
	var realUrl = realArr[0];
	window.location.href = realUrl + "#"+mailBoxMap.get(name);
};

/**
 * 标记 处理函数
 * @param flag
 * @param setFlag
 * @return
 */
function click_ml_menu_mark_msg(flag,setFlag){
	var msgnums = get_ml_selected_msgs();
	if(!msgnums) return;
	var name = $.trim($("#folderText").text());
	var folderText = $.trim($("#folderText").text());
	if(name=='未读邮件') {
		name = "收件箱";
	}
	var htmlProccessFunc;
	switch(flag) {
		case 'seen' : 
			if(setFlag){ 
				htmlProccessFunc = afterBeenRead ;
			} else {
				htmlProccessFunc = unread ;
			}
			break ;
		case 'flagged' : 
			if(setFlag){ 
				htmlProccessFunc = marked ;
			} else {
				htmlProccessFunc = unmarked ;
			}
			break ;
	}
	if(name != "搜索结果"){
		webmail.Utils.ajax({
			url		: "markMails.action",
			params  : msgnums+"&folderName="+name+"&flag="+flag+"&setFlag="+setFlag,
			callback : function() {
					var selectedMsgs = $("#mailList input:checked[name='msgnums']") ;
					selectedMsgs.parents("tr").each(function(){
					if(folderText=="未读邮件"){
						//按用户条件搜索出的结果因为经常变化，不做邮件列表缓存
						if(flag == 'seen'){
							if(!setFlag){
								//如果将邮件标记为未读 ，清除邮件缓存(标记为未读，下次点击时需要发起访问并改变邮件标记为已读。)
								if(folderText=='未读邮件')
								{
									clearCache("搜索结果-");
								}
								else{
									clearCache(name+"-");
								}
							}
						}
					}else{
						//邮件夹的邮件列表做了缓存，批量修改标记需要修改缓存
						//过滤相反的操作中的msgnum
						var msnum = $(this).find("input:checked[name='msgnums']").get(0).value;
						var listData = map.get(folderText).get(parseInt($("#select_page_inbox").val()));
						for ( var i = 0; len = listData.dataPage.data.length, i < len; i++) {
							if(listData.dataPage.data[i].msgnum == msnum){
								if(flag == 'seen'){
									if(setFlag){
										if($(this).hasClass("not-seen")) {
											listData.unseenMessageCount--;
										}
										//标记为已读
										if(!listData.dataPage.data[i].seen){
											listData.dataPage.data[i].seen = true;												
										}
									}else{
										if(!$(this).hasClass("not-seen")){
											listData.unseenMessageCount++;
										}
										//标记为未读
										if(listData.dataPage.data[i].seen){
											listData.dataPage.data[i].seen = false;
										}
										//清除邮件缓存(标记为未读，下次点击时需要发起访问，改变邮件标记为已读。)
										clearCache(name+"-");
									}
								} else if (flag == 'flagged'){
									if(setFlag){
										listData.dataPage.data[i].flagged = true;
									}else{
										listData.dataPage.data[i].flagged = false;
									}
								}
							}
						}
						map.get(name).put(parseInt($("#select_page_inbox").val()),listData);
					}
					//处理标记显示
					htmlProccessFunc($(this)) ;
				}) ;
					selectedMsgs.attr("checked",false) ;
					$("#msgnum-checkbox").attr("checked",false) ;
				},
			method  : "post"
		}) ;
	}else{
		var msgnumFolderNames = msgnums.replace(/msgnums/g,"msgnumFolderNames");
		webmail.Utils.ajax({
			url		: "markMailsInSearch.action",
			method  : "post",
			dataType : 'json',
			params  : msgnumFolderNames+"&folderName="+name+"&flag="+flag+"&setFlag="+setFlag,
			callback : function(data) {
							var selectedMsgs = $("#mailList input:checked[name='msgnums']") ;
							selectedMsgs.parents("tr").each(function(){
								htmlProccessFunc($(this)) ;
								clearCache("搜索结果-");
								//清除缓存
								for(var i=0;i<data.folderNames.length;i++){
									clearCache(data.folderNames[i]);
								}
							}) ;
						selectedMsgs.attr("checked",false) ;
						$("#msgnum-checkbox").attr("checked",false) ;
					}
		}) ;
	}
	
}
/**
 * 对邮件列表中的邮件进行转发操作。
 * @return
 */
function click_ml_menu_forward_msg(){
	var _folderName = $.trim($("#folderText").text());
	if(_folderName=='未读邮件'){
		_folderName ="收件箱";
		}
	var msgnumsParamStr = $("#mailList input[name='msgnums']").serialize() ;
	if(msgnumsParamStr == "") {
		nfschinaDialogAlert("请至少选择一封邮件！",2000,"系统提示");
		return ;
	}
	window.location.href = window.location.href.split("#")[0]+"#"+"compose";
	if(_folderName != "搜索结果"){
		$.ajax({
			url: "prepareForwardMails.action",
			data:msgnumsParamStr+"&folderName="+_folderName,
			type:'post',
			cache:false,
			success: function(data){
				if(swfu)
					swfu.destroy();
				$("#main").hide();
				$(".second-layer").find("li").removeClass('on');
				$("#write-main").html(data).show();
				loadIdentifyAndFrom();
				wm_init_swf_ckeditor();
			}
		});
	}else{
		var msgnumFolderNames = msgnumsParamStr.replace(/msgnums/g,"msgnumFolderNames");
		$.ajax({
			url: "prepareForwardMailsInSearch.action",
			data:msgnumFolderNames+"&folderName="+_folderName,
			type:'post',
			cache:false,
			success: function(data){
				if(swfu)
					swfu.destroy();
				$("#main").hide();
				$(".second-layer").find("li").removeClass('on');
				$("#write-main").html(data).show();
				loadIdentifyAndFrom();
				wm_init_swf_ckeditor();
			}
		});
	}
	
}
/**
 * 对邮件列表中的邮件进行删除操作
 * @param permanentlyDelete 是否彻底删除邮件
 * @param deleteAll 是否清空当前邮件夹中的邮件
 * @return
 */
function click_ml_menu_delete_msg(permanentlyDelete,deleteAll) {
	var folderName = $.trim($("#folderText").text());
	
	//清除邮件缓存(需重新请求以更新上下封关系)
	if(folderName=='未读邮件'){
		clearCache("搜索结果-");
	}else{
		clearCache(folderName+"-");
	}
	//不彻底删除时需要清除废件箱的邮件缓存
	if(!permanentlyDelete)
	{
		clearCache("废件箱-");
	}
	if(deleteAll)//清空操作
	{
		nfschinaDialogConfirm(
		"清空邮件夹","您确定要清空当前的邮件夹吗？",
		function(){
			var paramsString = "folderName="+folderName+"&isSearch=0"+"&permanentlyDelete=" + false ;
			webmail.Utils.ajax({
				url : 'deleteMails.action',
				params : paramsString ,
				method : 'post',
				dataType : 'json',
				callback : function(data){
					if(data.jsonStatus){
								clearCache(folderName);
								clearCache('废件箱');
								showMailListInfo(data);
								$("#main").show();
								$("#write-main").hide();
								$("#setup-main").hide();
								$("#viewMailCot").hide();
					}else{nfschinaDialogAlert(data.errorMessage);}
				}
			});
			$("#toolConfirm").nfschinaDialog("close");
		});
	}
	else
	{
		if(folderName == '未读邮件') {
			var msgnums = get_ml_selected_msgs();
			if(!msgnums) return;
			folderName = "收件箱";
			var spCout = $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val();
			var currentPage=parseInt($("#select_page_inbox").val());
			var paramsString = msgnums + "&folderName="+folderName+"&isSearch=0"+"&permanentlyDelete=" + permanentlyDelete +"&unseenSpamMessageCount="+spCout+"&currentPage="+currentPage;
			if(!permanentlyDelete){//不彻底删除
				webmail.Utils.ajax({
					url : 'deleteMails.action',
					params : paramsString ,
					method : 'post',
					dataType : 'json',
					callback : function(data){
							if(data.jsonStatus){
								clearCache("收件箱");
								clearCache('废件箱');			
								click_ml_unread_msg();
							}else{nfschinaDialogAlert(data.errorMessage);}
					}
				});
			} else 
			{
				nfschinaDialogConfirm(
					"彻底删除邮件","您确定要彻底删除这些邮件吗？",
					function(){
						webmail.Utils.ajax({
							url : 'deleteMails.action',
							params : paramsString ,
							method : 'post',
							dataType : 'json',
							callback : function(data){
								if(data.jsonStatus){
									clearCache("收件箱");
									click_ml_unread_msg();
								}else{nfschinaDialogAlert(data.errorMessage);}
							}
						});
						$("#toolConfirm").nfschinaDialog("close");
					});
			}
			
		}
		else if(folderName!='搜索结果')
		{
			var msgnums = get_ml_selected_msgs();
			if(!msgnums) return;
			var spCout = $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val();
			var currentPage=parseInt($("#select_page_inbox").val());
			var paramsString = msgnums + "&folderName="+folderName+"&isSearch=0"+"&permanentlyDelete=" + permanentlyDelete +"&unseenSpamMessageCount="+spCout+"&currentPage="+currentPage;
			if(permanentlyDelete){//彻底删除
				nfschinaDialogConfirm(
						"彻底删除邮件","您确定要彻底删除这些邮件吗？",
						function(){
							webmail.Utils.ajax({
								url : 'deleteMails.action',
								params : paramsString ,
								method : 'post',
								dataType : 'json',
								callback : function(data){
									if(data.jsonStatus){
											clearCache(folderName);
											showMailListInfo(data);
											$("#main").show();
											$("#write-main").hide();
											$("#setup-main").hide();
											$("#viewMailCot").hide();
											var pageMap = map.get(folderName);
											pageMap.put(data.currentPage,data);
											map.put(folderName,pageMap);
									}else{nfschinaDialogAlert(data.errorMessage);}
								}
							});
							$("#toolConfirm").nfschinaDialog("close");
						});
				
			}else if(folderName=="废件箱"){
				nfschinaDialogConfirm(
						"废件箱删除邮件","从废件箱删除不可恢复，您确定要删除这些邮件吗？",
						function(){
							webmail.Utils.ajax({
								url : 'deleteMails.action',
								params : paramsString ,
								method : 'post',
								dataType : 'json',
								callback : function(data){
									if(data.jsonStatus){
											clearCache(folderName);
											clearCache('废件箱');
											showMailListInfo(data);
											$("#main").show();
											$("#write-main").hide();
											$("#setup-main").hide();
											$("#viewMailCot").hide();
											var pageMap = map.get(folderName);
											pageMap.put(data.currentPage,data);
											map.put(folderName,pageMap);
									}
									else{nfschinaDialogAlert(data.errorMessage);}
								}
							});
							$("#toolConfirm").nfschinaDialog("close");
						});
			} 
			else 
			{
				webmail.Utils.ajax({
					url : 'deleteMails.action',
					params : paramsString ,
					method : 'post',
					dataType : 'json',
					callback : function(data){
						if(data.jsonStatus){
								clearCache(folderName);
								clearCache('废件箱');
								showMailListInfo(data);
								$("#main").show();
								$("#write-main").hide();
								$("#setup-main").hide();
								$("#viewMailCot").hide();
								var pageMap = map.get(folderName);
								pageMap.put(data.currentPage,data);
								map.put(folderName,pageMap);
						}
						else{nfschinaDialogAlert(data.errorMessage);}
					}
				});
			}
		}else{
			//搜索结果的批量删除（不提供全部清除）
			var msgnums = get_ml_selected_msgs();
			if(!msgnums) return;
			var msgnumFolderNames = msgnums.replace(/msgnums/g,"msgnumFolderNames");
			var spCout = $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val();
			var currentPage=parseInt($("#select_page_inbox").val());
			var paramsString = msgnumFolderNames + "&folderName="+folderName+"&permanentlyDelete=" + permanentlyDelete +"&unseenSpamMessageCount="+spCout+"&currentPage="+currentPage;
			if(!permanentlyDelete){//不彻底删除
				webmail.Utils.ajax({
					url : 'deleteMailsInSearch.action',
					params : paramsString ,
					method : 'post',
					dataType : 'json',
					callback : function(data){
						if(data.jsonStatus){
								clearCache('废件箱');
								//清除缓存
								for(var i=0;i<data.folderNames.length;i++){
									clearCache(data.folderNames[i]);
								}
								showMailListInfo(data);
								$("#main").show();
								$("#write-main").hide();
								$("#setup-main").hide();
								$("#viewMailCot").hide();
						}else{nfschinaDialogAlert(data.errorMessage);}
					}
				});
			} else 
			{
				nfschinaDialogConfirm(
					"彻底删除邮件","您确定要彻底删除这些邮件吗？",
					function(){
						webmail.Utils.ajax({
							url : 'deleteMailsInSearch.action',
							params : paramsString ,
							method : 'post',
							dataType : 'json',
							callback : function(data){
								if(data.jsonStatus){
										//清除缓存
										for(var i=0;i<data.folderNames.length;i++){
											clearCache(data.folderNames[i]);
										}	
										showMailListInfo(data);
										$("#main").show();
										$("#write-main").hide();
										$("#setup-main").hide();
										$("#viewMailCot").hide();
								}else{nfschinaDialogAlert(data.errorMessage);}
							}
						});
						$("#toolConfirm").nfschinaDialog("close");
					});
			}
		}
	}
}

/**
 * 处理主菜单上的未读邮件数
   read 表示是否在读取邮件之后
 * @param obj
 * @param read
 * @return
 */
function processMainMenuUnreadNum(obj,read,mailFolderName) {
	if(typeof(mailFolderName)!='undefined'){
		var folderName = mailFolderName;
	}else{
		var folderName = $.trim($("#folderText").text());
		if(folderName == '搜索结果' || folderName == '未读邮件') {
			folderName = $.trim(obj.find("input[name='folderName']").val().replace(/\[|\]/g,'')) ;
		}
	}
	var inboxUnreadNum = parseInt($("#inboxUnreadNum").text()) ;
	var spamboxUnreadNum = parseInt($("#spamboxUnreadNum").text()) ;
	if(read) {
		inboxUnreadNum -= 1 ;
		spamboxUnreadNum -= 1;
	}else {
		inboxUnreadNum += 1 ;
		spamboxUnreadNum += 1;
	}
	if(inboxUnreadNum<0) inboxUnreadNum = 0;
	if(spamboxUnreadNum<0) spamboxUnreadNum = 0;
	
	if(folderName == "收件箱") {
		$("#inboxUnreadNum").text(inboxUnreadNum) ;
	}else if(folderName == '垃圾邮件') {
		$("#spamboxUnreadNum").text(spamboxUnreadNum) ;	
	}
}
/**
 * 读取邮件之后的html处理
 * @param obj
 * @return
 */
function afterBeenRead(obj){
	if(!obj.hasClass("not-seen")) return ;
	if(parseInt($("#unseenMessageCount").text())>0){
		$("#unseenMessageCount").text(parseInt($("#unseenMessageCount").text()) - 1) ;
	}
	processMainMenuUnreadNum(obj,true) ;
	obj.removeClass("not-seen").find("img[src*='mail.gif']").replaceWith("<div class='haveRd'></div>") ;
}

/**
 * 设置为"未读"后的html处理
 * @param obj
 * @return
 */
function unread(obj) {
	if(obj.hasClass("not-seen")) return ;
	$("#unseenMessageCount").text(parseInt($("#unseenMessageCount").text()) + 1) ;
	processMainMenuUnreadNum(obj,false) ;
	obj.addClass("not-seen").find("div.haveRd").replaceWith("<img title='未读' src='images/com/mail.gif'/>") ;
}
/**
 * 设置为"已标记"后的html处理
 * @param obj
 * @return
 */
function marked(obj) {
	if(obj.find(".flagtrue").size()>0) return ;
	obj.addClass("flagged").find(".flagfalse").removeClass("flagfalse").addClass("flagtrue").attr("title","已标记") ;
}
/**
 * 设为"未标记"后的html处理
 * @param obj
 * @return
 */
function unmarked(obj) {
	if(obj.find(".flagfalse").size()>0) return ;
	obj.removeClass("flagged").find(".flagtrue").removeClass("flagtrue").addClass("flagfalse").attr("title","未标记") ;
}

/**
 * 判断当前邮件列表中选中的checkbox。
 * @param deleteAll 是否清空邮件
 * @return
 */
function get_ml_selected_msgs() {
	var msgnums = listCheckbox.serialize() ;
	if(msgnums == "")
		nfschinaDialogAlert("请选择邮件！",3000,"系统提示");
	return msgnums ;
}
/**
 * @param criterion 排序字段后台需要的名字
 * @param name 排序字段名字
 * @return
 */
function sort(criterion,name) {
	//去除全选框的勾选
	$("#msgnum-checkbox").removeAttr("checked");
	//如果为搜索结果则不提供排序，因为其每封邮件所属邮箱不一定一样
	var folderName = $.trim($("#folderText").text());
	if(folderName == "搜索结果"){
		//清除邮件缓存(需重新请求以更新上下封关系)
		clearCache("搜索结果-");
		var pageNumberNodeSort = $("#select_page_inbox") ;
		if(pageNumberNodeSort.data("reverse") == null) pageNumberNodeSort.data("reverse",true) ;
		var reverse = !pageNumberNodeSort.data("reverse") ;
		pageNumberNodeSort.data("reverse",reverse) ;
		pageNumberNodeSort.data("name",name) ;
		var options = {
				url : "sortSearchMails.action",
				params: {"folderName":$("#searchResultScope").val(),
							"sortCriterion": criterion, "reverseFlag" : reverse, "unseenSpamMessageCount" : $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val()},
				method: "post",
				dataType : 'json',
				callback : function(data){
					showContinuePage(data);
					$("#select_page_inbox").val(1);
					$(".btnPre").css("visibility","hidden");
					var totPage = parseInt($("#_totalPageCount").val());
					if(totPage>1){
						$(".btnNxt").css("visibility","visible") ;
					}
				}
			} ;
		webmail.Utils.ajax(options) ;
	} else 	if(folderName == "未读邮件"){
		//清除邮件缓存(需重新请求以更新上下封关系)
		clearCache("搜索结果-");
		var pageNumberNodeSort = $("#select_page_inbox") ;
		if(pageNumberNodeSort.data("reverse") == null) pageNumberNodeSort.data("reverse",true) ;
		var reverse = !pageNumberNodeSort.data("reverse") ;
		pageNumberNodeSort.data("reverse",reverse) ;
		pageNumberNodeSort.data("name",name) ;	
		var options = {
				url : "sortSearchMails.action",
				params: {"folderName":"收件箱",
							"sortCriterion": criterion, "reverseFlag"  : reverse, "unseenSpamMessageCount" : $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val(),"unSee":"is"},
				method: "post",
				dataType : 'json',
				callback : function(data){
					data.folderName="未读邮件";
					showContinuePage(data);
					$("#select_page_inbox").val(1);
					$(".btnPre").css("visibility","hidden");
					var totPage = parseInt($("#_totalPageCount").val());
					if(totPage>1){
						$(".btnNxt").css("visibility","visible") ;
					}
				}
			} ;
		webmail.Utils.ajax(options) ;
	}
	else {
		//清除邮件缓存(需重新请求以更新上下封关系)
		clearCache(folderName+"-");
		var pageNumberNode = $("#select_page_inbox") ;
		if(pageNumberNode.data("reverse") == null) pageNumberNode.data("reverse",true) ;
		var reverse = !pageNumberNode.data("reverse") ;
		pageNumberNode.data("reverse",reverse) ;
		pageNumberNode.data("name",name) ;
		
		var options = {
				url : "sortMails.action",
				params: {"folderName":folderName, "currentPage":parseInt(pageNumberNode.val()),
							"sortCriterion": criterion, "reverseFlag" : reverse, "unseenSpamMessageCount" : $("#_unseenSpCount").val()==null?'':$("#_unseenSpCount").val()},
				method: "post",
				dataType : 'json',
				callback : function(data){
					showContinuePage(data);
					map.get(folderName).clear();
					map.get(folderName).put(parseInt(pageNumberNode.val()),data);
				}
			} ;
		webmail.Utils.ajax(options) ;
	}
}