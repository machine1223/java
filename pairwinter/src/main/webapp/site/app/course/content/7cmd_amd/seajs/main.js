(function(){
    seajs.config({
        paths:{
            'example':'/static/course/content/7cmd_amd/seajs'
        },
        alias:{
            a:'example/a',
            b:'example/b',
            c:'example/c',
            def:'example/def',
            'jquery':'/static/scripts/components/jquery/jquery'
        },
        vars:{
            version:"sdfsdf"
        },
        map:{
            ".js$":".js{{version}}"
        }
    });
})();