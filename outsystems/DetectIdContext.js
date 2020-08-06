define("DetectIdContext", ["exports"], function (exports) {

    var deviceRegCb = [];
    var deviceRegErrorCb = [];

    var localDeviceRegCb = function (data) {
        deviceRegCb.forEach(function (cb) {
            console.log("Success: " + data);
            cb(data);
        })
    };
    var localDeviceRegErrorCb = function (data) {
        deviceRegErrorCb.forEach(function (cb) {
            cb(data);
        })
    };

    exports.startListening = function (registrationCode) {
        cordova.plugins.DetectIdPlugin.silentRegister(localDeviceRegCb, localDeviceRegErrorCb, registrationCode);
    };

    exports.addDeviceRegCb = function (cb) {
        deviceRegCb.push(cb)
    };

    exports.removeDeviceRegCb = function (cb) {
        var index = deviceRegCb.indexOf(cb);
        deviceRegCb.splice(index, 1)
    };

    exports.addDeviceRegErrorCb = function (cb) {
        deviceRegErrorCb.push(cb)
    };

    exports.removeDeviceRegErrorCb = function (cb) {
        var index = deviceRegErrorCb.indexOf(cb);
        deviceRegErrorCb.splice(index, 1)
    };

});
