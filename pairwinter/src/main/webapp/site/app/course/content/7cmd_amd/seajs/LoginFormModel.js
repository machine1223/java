/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 14-7-16
 * Time: 下午1:19
 * To change this template use File | Settings | File Templates.
 */
;define(function(require,exports,module){
    var template = require('example/loginFormtemplate.html');
    function LoginFormModel(options){
        var jContainer = options.container;
        jContainer.html(template);
        this.$form = jContainer.find('#form');
        this.$name = jContainer.find('#name');
        this.$password = jContainer.find('#password');
        this.$submit = jContainer.find('#submit');
        this.$submit.click(function(){
            options.callback.call(this,this.$form);
        });
    }
    LoginFormModel.prototype.getName=function(){
        return this.$name.val();
    }
    LoginFormModel.prototype.getPassword=function(){
        return this.$name.val();
    }
    return LoginFormModel;
});