var NPNPlugin = function() {
};

NPNPlugin.errorHandler = function() {};

NPNPlugin.prototype.activateNotification = function(success,error) {
    PhoneGap.exec(success, error, "NPNPlugin", "activatePush", []);
};

NPNPlugin.prototype.deactivateNotification = function(success,error) {
    PhoneGap.exec(success, error, "NPNPlugin", "deactivatePush", []);
};

NPNPlugin.prototype.notificationCallback = function(e){
    alert("notification: " + JSON.stringify(e));
};

PhoneGap.addConstructor(function(){
    console.log("register NativePushNotification plugin @ PG");
    if(!window.plugins){
        window.plugins = {};
    }
    window.plugins.NPNPlugin = new NPNPlugin();

    // bind plugin to js to allow processing of incoming notifications
    PhoneGap.exec(null,null,"NPNPlugin","bootstrap",[]);
});
