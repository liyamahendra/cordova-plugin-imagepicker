var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'ImagePicker', 'coolMethod', [arg0]);
};

exports.validateOutputType = function (success, error, options) {
    exec(success, error, 'ImagePicker', 'validateOutputType', [options]);
};

exports.hasReadPermission = function (success, error) {
    exec(success, error, 'ImagePicker', 'hasReadPermission');
};

exports.requestReadPermission = function (success, error) {
    exec(success, error, 'ImagePicker', 'requestReadPermission');
};

exports.getPictures = function (success, error, options) {
    exec(success, error, 'ImagePicker', 'getPictures', [options]);
};