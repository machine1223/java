/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 14-5-30
 * Time: 下午4:16
 * To change this template use File | Settings | File Templates.
 */
var Deferred = function(func){
    var deferred = _Deferred();
    var failDeferred = _Deferred();
    var promise;
    var extendMethods = {
        then:function(doneCallbacks, failCallbacks){
            deferred.done(doneCallbacks).fail(failCallbacks);
            return this;
        },
        always:function(){
            return deferred.done.apply(deferred,arguments).fail.apply(this,arguments);
        },
        fail:failDeferred.done,
        rejectWith:failDeferred.resolveWith,
        reject:failDeferred.resolve,
        isReject:failDeferred.isResolved,
        pipe:function(fnDone,fnFail){
            var newDeferFunction = function(newDefer){
                var o = {
                    done:[fnDone,'resolve'],
                    fail:[fnFail,'reject']
                };
                $.each(o,function(handler,data){
                    var fn = data[0];
                    var action = data[1];
                    var returned;
                    if($.isFunction(fn)){
                        var ff = function(){
                            returned = fn.apply(this,arguments);
                            if(returned && $.isFunction(returned.promise)){
                                returned.promise().then(newDefer.resolve,newDefer.reject);
                            }else{
                                newDefer[action + 'With'](this === deferred?newDefer:this,[returned]);
                            }
                        }
                        deferred[handler](ff);
                    }else{
                        deferred[handler](newDefer[action]);
                    }
                })
            }
            return Deferred(newDeferFunction).promise();
        },
        promise:function(obj){
            if(obj == null){
                if(promise){
                    return promise;
                }
                promise = obj = {};
            }
            var promiseMethods = "done fail isResolved isRejected promise then always pipe".split(" ");
            var i = promiseMethods.length;
            while(i--){
                obj[promiseMethods[i]] = deferred[promiseMethods[i]];
            }
            return obj;
        }
    };

    $.extend(deferred,extendMethods);
    deferred.done(failDeferred.cancel).fail(deferred.cancel);
    delete deferred.cancel;

    if(func){
        func.call(deferred,deferred);
    }
    return deferred;
}