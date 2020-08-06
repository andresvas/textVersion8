require(["PluginManager", "DetectIdContext"], function(module, detectIdContext){

    var onReady = function(scope) {
        console.log("onReady block");

        scope.onRegistration = scope.newCallback(function(data){
            $actions.OnRegistrationHandler(data);
        });
        scope.onRegistrationError = scope.newCallback(function(data){
            $actions.OnRegistrationError(data);
        });

        detectIdContext.addDeviceRegCb(scope.onRegistration);
        detectIdContext.addDeviceRegErrorCb(scope.onRegistrationError);
        console.log("onReady block ended");
    };

    var onDestroy = function(scope) {
        console.log("onDestroy block");
        detectIdContext.removeDeviceRegCb(scope.onRegistration);
        detectIdContext.removeDeviceRegErrorCb(scope.onRegistrationError);
        console.log("onDestroy block ended");
    };

    console.log("Create Scope");
    module.createScope("DetectIdScope", onReady, onDestroy);

    $resolve();
});
