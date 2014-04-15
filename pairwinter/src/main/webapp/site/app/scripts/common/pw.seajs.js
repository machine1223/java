/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 3/31/14
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
(function(){
    var staticResources = '/static/';
    seajs.config({
        plugins:['text'],
        paths:{
            components:staticResources+'scripts/components',
            content:'/course/content',
            services:staticResources+'scripts/services',
            controllers:staticResources+'scripts/controllers',
            tpl:staticResources+'views'
        },
        alias:{
            'jquery':'components/jquery/jquery'
        },
        map:[
            [/.js$/,'.js?version='],
            [/.html$/,'.html?version=']
        ]
    });

})()
