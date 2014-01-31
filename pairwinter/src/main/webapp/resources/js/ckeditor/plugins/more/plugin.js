(function(){
    //Section 1 : 按下自定义按钮时执行的代码
    var a= {
        exec:function(editor){
    		var item=$("#"+this.uiItems[0]["_"]["id"]);/*获得当前的按钮元素*/
    		//item.parent().parent().prev().remove();
    		var more=$(".cke_toolbar").eq(0);/*更多操作的工具栏*/
    		var td=$("#cke_contents_content");/*容纳IFRAME的td*/
    		var icon=item.find(".cke_icon");
			var style=icon.attr("style");
    		if(item.attr("title")=="精简模式")
    		{
    			item.attr("title","完全模式");
    			more.hide();
    			td.height(td.height()+26);
    			style=style.replace("w.gif","j.gif");
    			icon.attr("style",style);
    		}
    		else
    		{
    			item.attr("title","精简模式");
    			td.height(td.height()-26);
    			more.show();
    			style=style.replace("j.gif","w.gif");
    			icon.attr("style",style);
    		}
        }
    },
    //Section 2 : 创建自定义按钮、绑定方法
    b='more';
    CKEDITOR.plugins.add(b,{
        init:function(editor){
            editor.addCommand(b,a);
            editor.ui.addButton('more',{
                label:'完全模式',
                icon: this.path + 'j.gif',
                command:b
            });
        }
    });
})();