/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 14-2-5
 * Time: 下午7:59
 * To change this template use File | Settings | File Templates.
 */
(function(){
    if(window.location.hostname==='localhost' || (/\d+\.\d+\.\d+\.\d+/).test(window.location.hostname)){
        document.write('<script src="http://' + (location.host || 'localhost').split(':')[0] + ':35729/livereload.js?snipver=1" type="text/javascript"><\/script>')
    }
})();
