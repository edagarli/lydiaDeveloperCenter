/**
 * Codz by PortWatcher.
 * Team: Jrooy
 * Date: 13-10-17
 * Time: 下午1:13
 */
var RESULT_SUCCESS = 0;
var RESULT_PARAM_INVALID = 1;
var RESULT_INTERNAL_ERROR = 2;

var services = angular.module('lydiadev.services', []);

services.factory('Apps', function ($http) {
    var apps = {};

    apps.content = [
        {
            id: 0,
            name: 'Smart QQ',
            url: 'http://w.qq.com',
            status: 'Approved',
            class: 'positive',
            openTimes: '0',
            description: ''
        },
        {
            id: 1,
            name: '百度贴吧',
            url: 'http://tieba.baidu.com',
            status: 'Approved',
            class: 'positive',
            openTimes: '0',
            description: ''
        },
        {
            id: 2,
            name: 'Moment',
            url: 'http://moment.aliapp.com',
            status: 'Reviewed',
            class: 'warning',
            openTimes: '99999',
            description: ''
        }
    ];

    apps.query = function (uid) {
        /*$http.get('/api/apps/?devid=' + uid).success(function (result, status, headers, config) {
            
            if (result.status == RESULT_SUCCESS) {
                apps.content = result.apps;
            } else {
                console.log('get data error, error code:' + result.status);
            }
        }).error(function () {
            console.log('get apps fail');
        });
        var userApps = [];
        for (var app in apps.content) {
            if (app.status === 'Approved') {
                app.class = 'positive';
            } else if (app.status == 'Reviewed') {
                app.class = 'warning';
            } else {
                app.class = 'negative';
            }
            userApps.push(app);
        }

        return userApps;*/
        return apps.content;
    };

    apps.add = function (app, successCallback, failureCallback) {
        /*$http.get('/api/apps/newApp/?name=' + app.name + '&category=' + app.category + '&devid=' + app.devid + '&url=' + app.url + '&icon=' + app.icon + '&description=' + app.description).success(function (data) {
            if (result.status === RESULT_SUCCESS) {
               successCallback();
            }
        }).error(function () {
            failureCallback();
        });*/

        apps.content.push(app);
    };

    return apps;
});

services.factory('User', function ($http) {
    var user = {};

    user.getInfo = function (uid) {
        /*$http.get('/api/developer/get/?devid=' + uid).success(function (result, status, headers, config) {
            user.info = JSON.parse(data);
        }).error(function () {
            console.log('get user info fail.');
        });

        return user.info*/
        return {
            name: 'GeeKlub',
            email: 'creator@geeklub.org',
            website: 'http://www.geeklub.org'
        };
    };

    user.getId = function () {
        return user.id;
    }

    user.reg = function (postData, successCallback, failureCallback) {
        $http.post('/api/developer/register', JSON.stringify(postData)).success(function (result, status, headers, config) {
            //congratulations
            user.id = result.devid;
            successCallback();
        }).error(function () {
            failureCallback();
        });
    };

    user.login = function (postData, successCallback, failureCallback) {
        $http.get('/api/developer/login?email='+postData.email+'&password='+postData.password).success(function (result, status, headers, config) {
            if (result.status === RESULT_SUCCESS) {
                user.id = result.devid;
                successCallback();
            } else {
               failureCallback();
            }
        }).error(function () {
            failureCallback();
        });
    };

    user.update = function (updateData, successCallback, failureCallback) {
        $http.post('/api/developer/update', JSON.stringify(updateData)).success(function (result, status, headers, config) {
            if (result.status === RESULT_SUCCESS) {
                successCallback();
            }
        }).error(function () {
            failureCallback();
        });
    };

    user.logout = function () {
        user.id = undefined;
    };

    return user;
});