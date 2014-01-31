/**
 * draggable 1.0 - jQuery Plug-in
 * 
 * Licensed under the GPL:
 *   http://gplv3.fsf.org
 *
 * Copyright 2009 stworthy [ stworthy@gmail.com ] 
 */
(function($){
	$.fn.draggable = function(options){
		function doDown(e){
			$.data(e.data.target, 'draggable').options.onStartDrag(e);
			return false;
		}
		
		function getX(obj){
			var ParentObj=obj;
			var left=obj.offsetLeft;
			while(ParentObj=ParentObj.offsetParent){
				left+=ParentObj.offsetLeft;
			}
			return left;
		}
		
		function getY(obj){
			var ParentObj=obj;
			var top=obj.offsetTop;
			while(ParentObj=ParentObj.offsetParent){
				top+=ParentObj.offsetTop;
			}
			return top;
		}
		
		function doMove(e){
			
			var dragData = e.data;
			
			var x=dragData.offsetX;
			var y=dragData.offsetY;
			var width=dragData.handleW;
			var height=dragData.handleH;
			
			var left = dragData.startLeft + e.pageX - dragData.startX;
			var top = dragData.startTop + e.pageY - dragData.startY;
			
			if (e.data.parnet != document.body) {
				if ($.boxModel == true) {
					left += $(e.data.parent).scrollLeft();
					top += $(e.data.parent).scrollTop();
				}
			}
			var documentScroll={l:document.documentElement.scrollLeft,t:document.documentElement.scrollTop};
			if(e.clientX<=x)
			{
				left=$(document).scrollLeft();
			}
			if(e.clientY<=y)
			{
				top=$(document).scrollTop()+3;
			}
			if(e.clientX>=($(window).width()-width-9+x))
			{
				left=$(window).width()-width-10+documentScroll.l;
			}
			if(e.clientY>=($(window).height()-$(dragData.target).height()-5+y))
			{
				top=$(window).height()-$(dragData.target).height()-3+documentScroll.t;
			}
			
			//e.clientY>=($(window).height-height)||
			
			var opts = $.data(e.data.target, 'draggable').options;
			if (opts.axis == 'h') {
				$(dragData.target).css('left', left);
			} else if (opts.axis == 'v') {
				$(dragData.target).css('top', top);
			} else {
				$(dragData.target).css({
					left: left,
					top: top
				});
			}
			$.data(e.data.target, 'draggable').options.onDrag(e);
			return false;
		}
		
		function doUp(e){
			$(document).unbind('.draggable');
			$.data(e.data.target, 'draggable').options.onStopDrag(e);
			return false;
		}
		
		
		return this.each(function(){
			$(this).css('position','absolute');
			
			var opts;
			var state = $.data(this, 'draggable');
			if (state) {
				state.handle.unbind('.draggable');
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.draggable.defaults, options || {});
			}
			
			if (opts.disabled == true) {
				$(this).css('cursor', 'default');
				return;
			}
			
			var handle = null;
            if (typeof opts.handle == 'undefined' || opts.handle == null){
                handle = $(this);
            } else {
                handle = (typeof opts.handle == 'string' ? $(opts.handle, this) : handle);
            }
			$.data(this, 'draggable', {
				options: opts,
				handle: handle
			});
			
			// bind mouse event using event namespace draggable
			handle.bind('mousedown.draggable', {target:this}, onMouseDown);
			handle.bind('mousemove.draggable', {target:this}, onMouseMove);
			
			function onMouseDown(e) {
				if (checkArea(e) == false) return;

				var position = $(e.data.target).position();
				
				var x=e.clientX-getX($.data(e.data.target, 'draggable').handle[0])+$(document).scrollLeft(); 
				var y=e.clientY-getY($.data(e.data.target, 'draggable').handle[0])+$(document).scrollTop();
				
				var width=$.data(e.data.target, 'draggable').handle.width();
				var height=$.data(e.data.target, 'draggable').handle.height();
				var data = {
					startLeft: position.left,
					startTop: position.top,
					startX: e.pageX,
					startY: e.pageY,
					target: e.data.target,
					offsetX:x,
					offsetY:y,
					handleW:width,
					handleH:height,
					parent: $(e.data.target).parent()[0]
				};
				
				$(document).bind('mousedown.draggable', data, doDown);
				$(document).bind('mousemove.draggable', data, doMove);
				$(document).bind('mouseup.draggable', data, doUp);
			}
			
			function onMouseMove(e) {
				if (checkArea(e)){
					$(this).css('cursor', 'move');
				} else {
					$(this).css('cursor', 'default');
				}
			}
			
			// check if the handle can be dragged
			function checkArea(e) {
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				
				return Math.min(t,r,b,l) > opts.edge;
			}
			
		});
	};
	
	$.fn.draggable.defaults = {
			handle: null,
			disabled: false,
			edge:0,
			axis:null,	// v or h
			onStartDrag: function(){},
			onDrag: function(){},
			onStopDrag: function(){}
	};
})(jQuery);