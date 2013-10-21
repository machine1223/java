CKEDITOR.plugins.add( 'myAddImage',{
    init : function( editor )
    {	
        CKEDITOR.dialog.add( 'myAddImage', function( editor )
        {
            return {
                    title : '添加图片',
                    minWidth : 400,
                    minHeight : 200,
                    contents : 
                    [
                        {
                            id : 'addImage',
                            label : '添加图片',
                            title : '添加图片',
                            filebrowser : 'uploadButton',
                            elements :
                            [
                              {    
                                  id : 'txtUrl',
                                  type : 'text',
                                  label : '图片网址',
                                  required: true
                              },
                              {
                                    id : 'upload',
                                    type : 'file',
                                    label : '上传图片',
                                    style: 'height:40px',
                                    size : 38,
                                    onChange: function(){
                            	  		
                              		}
                              },
                              {
                                   type : 'fileButton',
                                   id : 'uploadButton',
                                   label : '上传',
                                   style : 'cursor:pointer;',
                                   filebrowser :
                                   {
                                        action : 'QuickUpload',
                                        target : 'addImage:txtUrl',
                                        onSelect:function(fileUrl, errorMessage){
                                            //在这里可以添加其他的操作
                                        }
                                   },
                                   onClick: function(){
                                        var d = this.getDialog();
                                        var _photo =  d.getContentElement('addImage','upload');
                                        var imgName = _photo.getValue();
                                        var types = "jpg,png,gif,bmp";
                                        var imgType = imgName.substring(imgName.lastIndexOf('.')+1);
                                        if(types.indexOf(imgType.toLowerCase()) == -1){
                                        	alert("只能上传jpg,png,gif,bmp类型的图片");
                                        	return false;
                                        }
                                   },
                                   'for' : [ 'addImage', 'upload']
                              }
                            ]
                        }
                   ],
                   onOk : function(){
                       _src = this.getContentElement('addImage', 'txtUrl').getValue();
                       if (_src.match(/(^\s*(\d+)((px)|\%)?\s*$)|^$/i)) {
                           alert('请输入网址或者上传文件');
                           return false;
                       }
                       this.imageElement = editor.document.createElement( 'img' );
                       this.imageElement.setAttribute( 'alt', '' );
                       this.imageElement.setAttribute( 'src', _src );
                       //图片插入editor编辑器
                       editor.insertElement( this.imageElement );
                   },
                   onCancel : function(){
                	   wm_search_redundancy_attachments();
                   }
            };
        });
        editor.addCommand( 'myImageCmd', new CKEDITOR.dialogCommand( 'myAddImage' ) );
        editor.ui.addButton( 'AddImage',
        {
                label : '图片',
                icon:'images/images.png', //toolbar上icon的地址,要自己上传图片到images下
                command : 'myImageCmd'
        });
    },
    requires : [ 'dialog' ]
});