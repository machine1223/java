/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 3/31/14
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
(function(){
    var staticResources = window.location.host+'/statics';
    seajs.config({
        plugins:['text'],
        paths:{
            plugins:staticResources+'/site/app/bower_components',
            jsviews:staticResources+'/site/app/views',
            tplviews:staticResources+'/site/app/views'
        },
        alias:{

        },
        map:[
            [/.js$/,'.js?version='],
            [/.html$/,'.html?version=']
        ]
    });

})()
