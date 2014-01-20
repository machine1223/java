/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	config.uiColor = '#EEF7FE';
	config.skin = 'kama';
	config.toolbarCanCollapse = false; 
	config.toolbar = 'MyToolbar';
	config.extraPlugins ='myAddImage,more'; //就是这里  
	config.toolbar_PersonToolbar = [
        ['Bold','Italic','Underline','TextColor'],
        ['Font','FontSize']
    ];
    config.resize_enabled = false;
   // config.height = ckheight;
    config.filebrowserUploadUrl = 'doUpload.action';
    config.toolbar_MyToolbar = [
        ['Cut','Copy','Paste','PasteText','PasteFromWord','Undo','Redo','SelectAll','RemoveFormat','Table','HorizontalRule','Smiley','SpecialChar','Subscript','Superscript','NumberedList','BulletedList','Outdent','Indent'],'/',
		['Bold','Italic','Underline','JustifyLeft','JustifyCenter','JustifyRight','Link','Unlink','AddImage','TextColor','BGColor','Format','Font','FontSize'],
		['Maximize','more']
       ];
   //编辑器中回车产生的标签
    config.enterMode = CKEDITOR.ENTER_BR; //可选：CKEDITOR.ENTER_BR或CKEDITOR.ENTER_DIV
    //  config.startupFocus = true;
    //当用户键入TAB时，编辑器走过的空格数，(&nbsp;) 当值为0时，焦点将移出编辑框
    config.tabSpaces = 4;
      
    //字体编辑时的字符集 可以添加常用的中文字符：宋体、楷体、黑体等 plugins/font/plugin.js
    config.font_names = '宋体;新宋体;仿宋_GB2312;楷体_GB2312;黑体;微软雅黑;隶书;Arial;Arial Black;Times New Roman;Courier New;Tahoma;Verdana;';

      //默认的字体名 plugins/font/plugin.js
    config.font_defaultLabel = '宋体';
      //字体默认大小 plugins/font/plugin.js
//    config.fontSize_defaultLabel = '12px';


};
