var webmail ;
if(!webmail) webmail = {} ;
if(typeof webmail != "object") throw new Error("无法创建名字空间！") ;
if(!webmail.components) webmail.components = {} ;
if(typeof webmail.components != "object") throw new Error("无法创建名字空间！") ;
jQuery(document).ready(function(){
	webmail.components.Tab.ctxPath = jQuery("#ctxPath").val() ;
}) ;
/**
 *  <!--Tab导航 开始-->
     <div id="nav">
         <ul>
           <li class="tab">
            <ul class="on">               
              <li class="bdy-left"></li>
              <li class="bdy-center">首&nbsp;页</li>                  
              <li><a class="bdy-close"></a></li>  
              <li class="bdy-right"></li>
            </ul>       
           </li>
           <li class="tab">
             <ul>               
              <li class="bdy-left"></li>
              <li class="bdy-center">收件箱</li>   
              <li><a class="bdy-close"></a></li>          
              <li class="bdy-right"></li>
            </ul>
           </li>	 
         </ul>
    </div>      
    <!--Tab导航 结束-->
 * @param {} options options参数定义了如下几个配置项，一般情况下只需要指定type，name和params参数就可以了。
 * 					target : 内容显示的目标区域
					type   : Tab的类型,目前定义了四种类型，参考代码注释
					name   : 此Tab的名字，如："收件箱"
					url	   : Tab取得内容要调用的URL，如："/svn-nfswebmail-web/testLoding.action"
					method : 要调用的请求方法，如："get", "post"
					params : 请求参数 key/value 的形式。如：{name:'张三',password:'123'}
					
 */
webmail.components.Tab = function(options) {
	options = options || { } ;
	this.config = {
		target : "#main",
		type   : 1,
		name   : "收件箱",
		method : "get",
		params : {}
	} ;
	webmail.Utils.extend(this.config,options) ;
	this.config.url = options.url || webmail.components.Tab.urlMapper(this.config.type) ;
	/*做两件事：
	 * 1. 判断当前的Tab集合中是否已经存在同种类型的Tab了，如果存在，再次判断是否名字也相同
	 *    如果名字相同，则直接显示；如果名字不同，则替换当前的同种类型的Tab，并到服务器重新取数据进行显示。
	 *    如果不存在同种类型的Tab，则克隆一个Tab，设置Id，到服务器取数据进行显示,并将当前Tab放入Tab集合
	 * 2. 将当前的Tab置为on的状态，将集合中所有其他的Tab都取消on的状态
	 */
	this.init() ;
	var thisObj = this ;
	//此处先unbind是因为如果是同种类型的Tab会覆盖前面的Tab，这个时候此元素已经绑定了事件。
	jQuery("#"+thisObj.id).find("a").unbind().click(function(event) {
		thisObj.close() ;
		event.stopPropagation() ;
	}).hover(function() {
		var aObj = $(this) ;
		aObj.removeClass("bdy-close") ;
		aObj.addClass("bdy-close-hover") ;
		}, function(){
			var aObj = $(this) ;
			aObj.removeClass("bdy-close-hover") ;
			aObj.addClass("bdy-close") ;
		}
	);
	jQuery("#"+thisObj.id).unbind("click").click(function(){
		thisObj.display() ;
	}) ;
} ;
/**
 * 定义四种Tab类型：	邮件夹浏览,搜索结果:	(type=1)
 * 					写信	:		(type=2)
 * 					个人设置:	(type=3)
 * 					系统设置:	(type=4)
 * 					阅读邮件：	(type=5)
 */
webmail.Utils.extend(webmail.components.Tab,{
	FOLDER : 1,
	WRITE_MAIL : 2,
	PERSONAL_SETTING : 3,
	SYSTEM_SETTING : 4,
	READ_MAIL : 5
}) ;
/**
 * 定义Tab对象的集合，使得下次再访问同样的Tab的时候能够直接取得集合中
 * 存在的Tab对象进行显示
 */
webmail.Utils.extend(webmail.components.Tab,{
	tabs : []
}) ;
/**
 * 定义类的静态方法
 */
webmail.Utils.extend(webmail.components.Tab,{
	getUniqueId : (function() {
    	var id = 0;           
    	return function() { return id++; };  
	})(),
	urlMapper : function(type) {
		var ctxPath = webmail.components.Tab.ctxPath ;
		switch(type) {
			case 1 : return ctxPath + "/viewMailList.action" ;
			case 2 : return ctxPath + "/writeMail.action" ;
			default : throw new Error(-1,"必须指定url！") ;
		}
	},
	tabCloseBtnStatus : function() {
		var tabs = webmail.components.Tab.tabs ;
		var closeBtn = jQuery("#nav").find("a") ;
		if(tabs.length == 1) {
			closeBtn.hide() ;
		}else if(tabs.length > 1) {
			closeBtn.show() ;
		}
	},
	indexOf : function(array,obj) {
		var result = -1 ; 
		jQuery.each(array,function(i,o){
			if(obj == o) {
				result = i ;
				return false;
			}
		}) ;
		return result ;
	},
	currentTab : function() {
		var cur ;
		jQuery(webmail.components.Tab.tabs).each(function(){
			if(jQuery("#"+this.id).hasClass("on")){
				cur = this ;
				return false ;
			}
		}) ;
		return cur ;
	}
}) ;
/**
 * 定义Tab对象的方法
 */
webmail.Utils.extend(webmail.components.Tab.prototype,{
	init : function() {
		var tabs = webmail.components.Tab.tabs ;
		var thisObj = this ;
		var origIndex = -1 ; //保存了对同种类型的Tab的索引
		jQuery(tabs).each(function(index){
			if(thisObj.config.type == this.config.type) {
				origIndex = index ;
				return false  ;
			}
		}) ;
		if(origIndex != -1) {
			var origTab = tabs[origIndex] ;
			this.id = origTab.id ;
			this.contentId = origTab.contentId ;
			jQuery("#"+this.id).find("li.bdy-center").text(this.config.name) ;
			tabs[origIndex] = this ;
		} else {
			tabs.push(this) ;
			var uniqueId = webmail.components.Tab.getUniqueId() ;
			this.id      = "tab" + uniqueId ;
			this.contentId = "tabContent" + uniqueId ;
			
			var newTabObj = jQuery("#nav>ul>li:eq(0)").clone()
									.appendTo("#nav>ul").attr("style","")
									.find("ul").attr("id",this.id).find("li.bdy-center")
									.text(this.config.name) ;
									
			var newContentObj = jQuery("<div id='"+this.contentId+"'></div>")
									.appendTo(this.config.target).hide() ;
			
		}
		webmail.components.Tab.tabCloseBtnStatus() ;
		this.syncObtainData() ;//获得数据并显示
	},
	display : function() {
		var tabs = webmail.components.Tab.tabs ;
		var thisObj = this ;
		jQuery.each(tabs,function(index,obj){
			if(obj.id != thisObj.id) {
				jQuery("#"+obj.id).removeClass("on") ;
				jQuery("#"+obj.contentId).hide() ;
			}
		}) ;
		jQuery("#"+thisObj.contentId).show() ;
		jQuery("#"+thisObj.id).addClass("on").show() ;
	},
	syncObtainData : function() {
		var thisObj = this ;
		webmail.Utils.ajax({
			method 	: thisObj.config.method,
			url		: thisObj.config.url,
			params	: thisObj.config.params,
			async	: thisObj.config.async,
			cache	: thisObj.config.cache,
			callback: thisObj.config.callback,
			target	: "#"+thisObj.contentId
		}) ;
		this.display() ;
	},
	close : function() {
		jQuery("#"+this.id).parent().remove() ;
		jQuery("#"+this.contentId).remove() ;
		var tabs = webmail.components.Tab.tabs ;
		var index = -1 ;
		if((index=webmail.components.Tab.indexOf(tabs,this)) != -1){
			tabs.splice(index,1) ;
		}
		webmail.components.Tab.tabCloseBtnStatus() ;
		tabs[tabs.length - 1].display() ;
		//如果为邮件列表类型的Tab，则清理全局域内的对象
		if(window.tab && this.config.type == 1) {
			window.tab = null ;
		}
	},
	onlyClose : function() { //只是关闭当前标签，不负责关掉之后的状态维护
		jQuery("#"+this.id).parent().remove() ;
		$("#searchText").focus() ;//这里为了避免ie在删除display=none的dom时的异常
		jQuery("#"+this.contentId).remove() ; 
		var tabs = webmail.components.Tab.tabs ;
		var index = -1 ;
		if((index=webmail.components.Tab.indexOf(tabs,this)) != -1){
			tabs.splice(index,1) ;
		}
		webmail.components.Tab.tabCloseBtnStatus() ;
		//清理全局域内的对象
		if(window.tab && this.config.type == 1) {
			window.tab = null ;
		}
	},
	setParams : function(params) {
		this.config.params = params || {} ;
	},
	setUrl : function(url) {
		this.config.url = url || '' ;
	},
	setOptions : function(options) {
		webmail.Utils.extend(this.config,options) ;
	},
	setName : function(name) {
		jQuery("#"+this.id).find("li.bdy-center").text(this.config.name) ;
		this.config.name = name || '' ;
	},
	getName : function() {
		return this.config.name ;
	},
	getContentId : function() {
		return this.contentId ;
	},
	getContent : function() {
		return jQuery("#" + this.contentId) ;
	}
}) ;