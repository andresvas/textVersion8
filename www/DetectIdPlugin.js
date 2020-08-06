var exec = require('cordova/exec');

//REGISTER METHODS
exports.init = function (success, error, serverUrl) {
    exec(success, error, 'DetectIdPlugin', 'INIT', [serverUrl]);
};
exports.registerDeviceByCode = function (success, error, code) { //P0
    exec(success, error, 'DetectIdPlugin', 'REGISTER_DEVICE_BY_CODE', [code]);
};
exports.enableRegistrationAlert = function (success, error, enable) {
    exec(success, error, 'DetectIdPlugin', 'ENABLE_REGISTRATION_ALERT', [enable]);
};

//GET METHODS
exports.getSharedDeviceId = function (success, error) {
    exec(success, error, 'DetectIdPlugin', 'GET_SHARED_DEVICE_ID');
};
exports.getDeviceId = function (success, error) {
    exec(success, error, 'DetectIdPlugin', 'GET_DEVICE_ID');
};
exports.getAccounts = function (success, error) { //P0
    exec(success, error, 'DetectIdPlugin', 'GET_ACCOUNTS');
};
exports.removeAccount = function (success, error, accountUsername) { //P0
    exec(success, error, 'DetectIdPlugin', 'REMOVE_ACCOUNT', [accountUsername]);
};
exports.setAccountUsername = function (success, error, oldAccountUsername, newAccountUsername) { //P0
    exec(success, error, 'DetectIdPlugin', 'SET_ACCOUNT_USERNAME', [oldAccountUsername, newAccountUsername]);
};
exports.hasAccounts = function (success, error) { //P0
    exec(success, error, 'DetectIdPlugin', 'HAS_ACCOUNTS');
};

//OTP METHODS
exports.otpGetTokenValue = function (success, error, accountUsername) { //P0
    exec(success, error, 'DetectIdPlugin', 'OTP_GET_TOKEN_VALUE', [accountUsername]);
};
exports.otpGetTokenTimeleft = function (success, error, accountUsername) { //P0
    exec(success, error, 'DetectIdPlugin', 'OTP_GET_TOKEN_TIMELEFT', [accountUsername]);
};
