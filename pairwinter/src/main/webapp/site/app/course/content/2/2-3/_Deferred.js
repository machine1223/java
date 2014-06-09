var _Deferred = function(){
    var callbacks = [];
    var fired;
    var firing;
    var cancelled;
    var deferred = {
        done:function(){
            if(!cancelled){
                var args = arguments;
                var i,length,elem,type,_fired;
                if(fired){
                    _fired = fired;
                    fired = 0;
                }
                for(i = 0,length = args.length;i<length;i++){
                    elem = args[i];
                    type = jQuery.type(elem);
                    if(type == 'array'){
                        deferred.done.apply(deferred,elem);
                    }else if(type == 'function'){
                        callbacks.push(elem);
                    }
                }
                if(_fired){
                    deferred.resolveWith(_fired[0],_fired[1]);
                }
            }
            return this;
        },
        resolveWith:function(context, args){
            if(!cancelled && !fired && !firing){
                args = args || [];
                firing = 1;
                try{
                    while(callbacks[0]){
                        callbacks.shift().apply(context,args);
                    }
                }finally{
                    fired = [context,args];
                    firing = 0;
                }
            }
            return this;
        },
        resolve:function(){
            deferred.resolveWith(this,arguments);
            return this;
        },
        isResolved:function(){
            return !!(fired || firing);
        },
        cancel:function(){
            cancelled = 1;
            callbacks = [];
            return this;
        }
    }
    return deferred;
}