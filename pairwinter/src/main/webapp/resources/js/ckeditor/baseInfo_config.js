/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	 config.skin = 'v2';
	 config.toolbar = 'PPTools';
	// config.extraPlugins = 'myAddImage'; //就是这里  
	 config.toolbar_PPTools = [
        ['Bold','Italic','Underline','TextColor'],
        ['Font','FontSize']
    ];
    config.resize_enabled = false;
   
     //编辑器中回车产生的标签
      config.enterMode = CKEDITOR.ENTER_P; //可选：CKEDITOR.ENTER_BR或CKEDITOR.ENTER_DIV
      config.startupFocus = true;
    //当用户键入TAB时，编辑器走过的空格数，(&nbsp;) 当值为0时，焦点将移出编辑框
      config.tabSpaces = 4;
      //默认的字体名 plugins/font/plugin.js
      //config.font_defaultLabel = 'Arial';
};
