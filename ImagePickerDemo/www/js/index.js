/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    var btnChooseImage = document.getElementById("btnChooseImage");
    var btnHasPermissions = document.getElementById("btnHasPermissions");
    var btnRequestPermissions = document.getElementById("btnRequestPermissions");
    
    btnHasPermissions.addEventListener("click", hasPermissions);
    btnRequestPermissions.addEventListener("click", requestPermissions);
    btnChooseImage.addEventListener("click", chooseImage);
}

function hasPermissions() {
    cordova.plugins.ImagePicker.hasReadPermission(function(successResponse) {
        alert(successResponse);
    }, function(errorResponse) {
        alert(errorResponse);
    });
}

function requestPermissions() {
    cordova.plugins.ImagePicker.requestReadPermission(function(successResponse) {
        alert(successResponse);
    }, function(errorResponse) {
        alert(errorResponse);
    });
}

function chooseImage() {
    var options = {};
    options.width = 320;
    options.height = 640;
    options.outputType = "BASE64_STRING"; // Or BASE64_STRING

    cordova.plugins.ImagePicker.getPictures(function(successResponse) {
        if(options.outputType == "FILE_URI") {
            document.getElementById("file_uri").innerHTML = successResponse;
        } else if(options.outputType == "BASE64_STRING") {
            document.getElementById("image").src = successResponse;
        }
    }, function(errorResponse) {
        alert(errorResponse);
    }, options);
}