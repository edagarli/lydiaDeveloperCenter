/**
 * Codz by PortWatcher.
 * Team: Jrooy
 * Date: 13-10-17
 * Time: 下午1:13
 */
var RESULT_SUCCESS = 0;
var RESULT_PARAM_INVALID = 1;
var RESULT_INTERNAL_ERROR = 2;
var RESULT_TOKEN_INVALID = 3;

var STATUS_REVIEW = 0;
var STATUS_APPROVED = 1;
var STATUS_REJECTED = 2;

var services = angular.module('lydiadev.services', ['angularFileUpload']);

services.factory('Apps', function ($http) {
  var apps = {};
  apps.content = [];

  apps.query = function (uid, successCallback, failureCallback) {
    $http.get('http://developer.lydiabox.com/api/apps?devid=' +
	    uid + '&token=' + sessionStorage.getItem('token')
    ).success(function (result, status, headers, config) {
      if (result.status === RESULT_SUCCESS) {
        var apps = [];
        result.apps.forEach(function (app) {
          if (app.status == STATUS_APPROVED) {
            app.class = 'positive';
            app.status = '已上架';
	          app.isApproved = true;
          } else if (app.status == STATUS_REVIEW) {
            app.class = 'warning';
	          app.openTimes = 0;
            app.status = '审核中';
	          app.isReviewing = true;
          } else if (app.status == STATUS_REJECTED) {
            app.class = 'negative';
            app.status = '被拒绝';
	          app.isRejected = true;
          }

          apps.push(app);
        });

        successCallback(apps);
      } else if (result.status === RESULT_PARAM_INVALID) {
        failureCallback('请先登录');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failureCallback('服务器出错');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('获取apps失败，请稍后重试');
    });
  };

  apps.add = function (app, successCallback, failureCallback) {
	  var packet = app;
	  packet.token = sessionStorage.getItem('token');
    $http({
      method: 'POST',
      url: 'http://developer.lydiabox.com/api/apps/newApp',
      data: $.param(packet),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function (result, status, headers, config) {
      if (result.status === RESULT_SUCCESS) {
        successCallback();
      } else if (result.status === RESULT_PARAM_INVALID) {
        failureCallback('app数据非法');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failureCallback('服务器出错，请稍后重试');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('网络错误');
    });
  };

	apps.resubmit = function (appId, successCallback, failureCallback) {
		var packet = {};
		packet.appId = appId;
		packet.token = sessionStorage.getItem('token');

		$http({
			method: 'POST',
			url: 'http://developer.lydiabox.com/api/apps/resubmit',
			data: $.param(packet),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (result, status, headers, config) {
			if (result.status === RESULT_SUCCESS) {
				successCallback();
			} else if (result.status === RESULT_PARAM_INVALID) {
				failureCallback('app数据非法');
			} else if (result.status === RESULT_INTERNAL_ERROR) {
				failureCallback('服务器出错，请稍后重试');
			} else if (result.status === RESULT_TOKEN_INVALID) {
				failureCallback('身份验证出错，请重新登录');
			} else {
				failureCallback('服务器响应出错');
			}
		}).error(function () {
			failureCallback('网络错误');
		});
	};

	apps.update = function (app, successCallback, failureCallback) {
		var packet = {};
		packet.appId = app.appId;
		packet.icon_src = app.icon_src;
		packet.token = sessionStorage.getItem('token');

		if (app.manifestURL) {
			packet.manifestURL = app.manifestURL;
		}

		if (app.keywords) {
			packet.keywords = app.keywords;
		}

		$http({
			method: 'POST',
			url: 'http://developer.lydiabox.com/api/apps/update',
			data: $.param(packet),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (result, status, headers, config) {
			if (result.status === RESULT_SUCCESS) {
				successCallback();
			} else if (result.status === RESULT_PARAM_INVALID) {
				failureCallback('app数据非法');
			} else if (result.status === RESULT_INTERNAL_ERROR) {
				failureCallback('服务器出错，请稍后重试');
			} else if (result.status === RESULT_TOKEN_INVALID) {
				failureCallback('身份验证出错，请重新登录');
			} else {
				failureCallback('服务器响应出错');
			}
		}).error(function () {
			failureCallback('网络错误');
		});
	};

  return apps;
});

services.factory('User', function ($http) {
  var user = {};

  user.getInfo = function (uid, successCallback, failureCallback) {
    $http.get('http://developer.lydiabox.com/api/developer/get?devid=' +
	    uid + '&token=' + sessionStorage.getItem('token')
    ).success(function (result, status, headers, config) {
      if (result.status === RESULT_SUCCESS) {
        successCallback(result.data);
      } else if (result.status === RESULT_PARAM_INVALID) {
        failureCallback('请先登录');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failureCallback('服务器出错');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('网络错误');
    });
  };

  user.getId = function () {
    return localStorage.getItem('devid');
  };

	user.getToken = function () {
		return sessionStorage.getItem('token');
	};

  user.register = function (postData, successCallback, failureCallback) {
    $http({
      method: 'POST',
      url: 'http://developer.lydiabox.com/api/developer/register',
      data: $.param(postData),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function (result, status, headers, config) {
      if (result.status === RESULT_SUCCESS) {
        user.id = result.devid;
        successCallback();
      } else if (result.status === RESULT_PARAM_INVALID) {
        failureCallback('参数非法');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failureCallback('服务器出错');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('网络错误');
	  });
  };

  user.checkMail = function (getData, successCallback, failuerCallback) {
    $http.get('http://developer.lydiabox.com/api/developer/register?email=' +
	    getData.email + '&code=' + getData.code
    ).success(function (result, status, headers, config) {
      console.dir(result);
      if (result.status === RESULT_SUCCESS) {
        successCallback();
      } else if (result.status === RESULT_PARAM_INVALID) {
        failuerCallback('验证码错误');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failuerCallback('服务器出错');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failuerCallback('网络错误');
    });
  };

  user.login = function (postData, successCallback, failureCallback) {
		$http({
			method: 'POST',
			url: 'http://developer.lydiabox.com/api/developer/login',
			data: $.param(postData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (result) {
		  if (result.status === RESULT_SUCCESS) {
			  user.id = result.devid;
			  user.token = result.token;
			  sessionStorage.setItem('token', user.token);
			  localStorage.setItem('devid', user.id);
			  successCallback();
		  } else if (result.status === RESULT_PARAM_INVALID) {
			  failureCallback('登录失败或未验证邮箱');
		  } else if (result.status === RESULT_INTERNAL_ERROR) {
			  failureCallback('服务器出错');
		  } else if (result.status === RESULT_TOKEN_INVALID) {
			  failureCallback('身份验证出错，请重新登录');
		  } else {
			  failureCallback('服务器响应出错');
		  }
	  }).error(function () {
		  failureCallback('网络错误');
	  });
	};

  user.update = function (updateData, successCallback, failureCallback) {
	  updateData.token = sessionStorage.getItem('token');
    $http({
      method: 'POST',
      url: 'http://developer.lydiabox.com/api/developer/update',
      data: $.param(updateData),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function (result) {
      if (result.status === RESULT_SUCCESS) {
        successCallback();
      } else if (result.status === RESULT_PARAM_INVALID) {
        failureCallback('参数非法');
      } else if (result.status === RESULT_INTERNAL_ERROR) {
        failureCallback('服务器内部错误');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('与服务器连接发生了问题')
    });
  };

  user.logout = function (uid, successCallback, failureCallback) {
    $http.get('http://developer.lydiabox.com/api/developer/logout?devid=' +
	    uid + '&token=' + sessionStorage.getItem('token')
    ).success(function () {
      user.id = null;
	    user.token = null;
      localStorage.clear();
	    sessionStorage.clear();
      successCallback();
    }).error(function () {
      failureCallback();
    });
  };

  return user;
});

services.factory('UpYun', function ($http, $upload) {
  var upyun = {};

  upyun.upload = function (file, progressCallback, successCallback, failureCallback) {
    return $upload.upload({
      url: 'http://developer.lydiabox.com/api/apps/icon',
      method: 'POST',
      fileFormDataName: 'file',
      file: file,
	    data: $.param({token: sessionStorage.getItem('token')})
    }).progress(function (event) {
//      console.log(parseInt(100.0 * event.loaded / event.total));
      progressCallback(event.loaded, event.total);
    }).success(function (data) {
	    console.log(data);
      if (data.status == RESULT_SUCCESS) {
        successCallback(data.url);
      } else if (data.status == RESULT_PARAM_INVALID) {
        failureCallback('图片似乎不符合要求');
      } else if (data.status == RESULT_INTERNAL_ERROR) {
        failureCallback('服务器出错，请稍后重试');
      } else if (result.status === RESULT_TOKEN_INVALID) {
	      failureCallback('身份验证出错，请重新登录');
      } else {
	      failureCallback('服务器响应出错');
      }
    }).error(function () {
      failureCallback('网络错误');
    });
  };

  return upyun;
});
