/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/2/14
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
;define(function(require,exports,module){
    var bindVideoEvent = function($){
        $('#video').bind("ended",function(){
            console.log("ended");
        }).bind("loadedmetadata",function(){
            console.log("loadedmetadata");
        }).bind("canplaythrough",function(){
            console.log("canplaythrough");
        }).bind("abort",function(){
            console.log("abort");
        }).bind("error",function(){
            console.log("error");
        }).bind("emptied",function(){
            console.log("emptied");
        }).bind("pause",function(){
            console.log("pause");
        }).bind("play",function(){
            console.log("play");
        }).bind("playing",function(){
            console.log("playing");
        }).bind("timeupdate",function(){
            console.log("timeupdate");
        });
    };
    exports.bindVideoEvent = bindVideoEvent;
    var bindAudioEvent = function($){
        $('#audio').bind("ended",function(){
            console.log("ended");
        }).bind("loadedmetadata",function(){
            console.log("loadedmetadata");
        }).bind("canplaythrough",function(){
            console.log("canplaythrough");
        }).bind("abort",function(){
            console.log("abort");
        }).bind("error",function(){
            console.log("error");
        }).bind("emptied",function(){
            console.log("emptied");
        }).bind("pause",function(){
            console.log("pause");
        }).bind("play",function(){
            console.log("play");
        }).bind("playing",function(){
            console.log("playing");
        }).bind("timeupdate",function(){
            console.log("timeupdate");
        });
    }
    exports.bindAudioEvent = bindAudioEvent;
});
