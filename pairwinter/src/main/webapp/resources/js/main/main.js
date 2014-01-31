/**
 * 定义窗口最小的宽度
 */
var minWidth = 996 ;
/**
 * 定义窗口最小的高度
 */
var minHeight= 400 ;
/*随窗口的变动调整页面显示情况*/
function setContentSize() {
	var jwindow=$(window);
	var jbody=$("#body");
	var windowWidth = jwindow.width() ;
	var windowHeight = jwindow.height() ;
	if(windowWidth <= minWidth)
		windowWidth=minWidth;
	if(windowHeight <= minHeight)
		windowHeight=minHeight;
	if((windowWidth <= minWidth) && (windowHeight <= minHeight))
		jbody.attr("scroll","auto").attr("style","overflow-x:auto;overflow-y:auto");
	if((windowWidth <= minWidth) && (windowHeight > minHeight))
		jbody.attr("scroll","auto").attr("style","overflow:hidden;overflow-x:auto");
	if((windowWidth > minWidth) && (windowHeight <= minHeight))
		jbody.attr("scroll","auto").attr("style","overflow:hidden;overflow-y:auto");
	if((windowWidth > minWidth) && (windowHeight > minHeight))
		jbody.attr("scroll","no").attr("style","overflow:hidden;");
	windowWidth = jwindow.width() ;
	windowHeight = jwindow.height() ;
	if(windowWidth <= minWidth)
		windowWidth=minWidth;
	if(windowHeight <= minHeight)
		windowHeight=minHeight;
	jbody.height(windowHeight);
	$("#menu,#sider").height(windowHeight-70) ;
	$("#mainAll").height(windowHeight-70).width(windowWidth-189);
	$("#global").width(windowWidth) ;
	$("#mpage").height(windowHeight-107);
	$("#mail-xxlb-table").height(windowHeight-120);
	$("#perList").height(windowHeight-151);
	
	/*如果当前正在写信*/
	if(!$("#write-main").is(":hidden"))
	{
		$("#rightMain").width(windowWidth-189-20-12);
		$("#mail-xxlb-table").width(windowWidth-189-12);
		
		var topHeight = $("#cke_top_content").height();
		$("#cke_contents_content").height(windowHeight-126-$("#write-mail-form").height()-topHeight);
	}
}
function toWriteMail()
{
	$("#main").hide();
	$("#write-main").show();
	setContentSize();
}