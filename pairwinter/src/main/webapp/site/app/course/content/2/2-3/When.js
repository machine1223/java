/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 14-6-3
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
var When = function(firstParam){
    var args = arguments;
    var i = 0;
    var length = args.length;
    var count = length;
    var deferred;
    if(length<=1 && firstParam && $.isFunction(firstParam.promise)){
        deferred = firstParam;
    }else{
        deferred = Deferred();
    }

    function resolveFunc(i){
        var fu = function(value){
            var slice = [].slice;
            args[i] = arguments.length>1?slice.call(arguments,0):value;
            if(!(--count)){
                deferred.resolveWith(deferred,slice.call(args,0));
            }
        }
        return fu;
    }

    if(length>1){
        for(;i<length;i++){
            if(args[i] && $.isFunction(args[i].promise)){
                args[i].promise().then(resolveFunc(i),deferred.reject);
            }else{
                --count;
            }
        }
        if(!count){
            deferred.resolveWith(deferred,args);
        }
    }else if(deferred !== firstParam){
        deferred.resolveWith(deferred,length ? [firstParam] : []);
    }

    return deferred.promise();
}