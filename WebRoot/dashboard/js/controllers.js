/**
 * Codz by PortWatcher. Team: Jrooy Date: 13-10-16 Time: 下午10:36
 */

var app = angular.module('lydiadev', ['lydiadev.services']);

app.config(['$routeProvider', function ($routeProvider) {
	$routeProvider.when('/', {
    controller: 'mainController'
  }).when('/login', {
    controller: 'loginController',
    templateUrl: 'views/login.html'
  }).when('/register', {
		controller: 'regController',
		templateUrl: 'views/reg.html'
	}).when('/manage', {
		controller: 'manageController',
		templateUrl: 'views/manage.html'
	}).when('/developer', {
		controller: 'userController',
		templateUrl: 'views/user.html'
	}).when('/add', {
		controller: 'addController',
		templateUrl: 'views/add.html'
	}).when('/claim', {
		controller: 'claimController',
		templateUrl: 'views/claim.html'
	}).otherwise({
		redirectTo : '/login'
	});
}]);

app.controller('mainController', ['$scope', 'User', function($scope, User) {
  if (sessionStorage.getItem('token')) {
    top.location = '#manage';
  } else {
    top.location = '#login';
  }

	$scope.logout = function () {
		User.logout(User.getId(), function () {
      top.location = '#login';
	  }, function () {
      //error callback
	  });
	};

	$scope.showNotification = function (message, style) {
		$scope.notificationStyle = style;
		$scope.notificationMessage = message;
		$scope.notificationHidden = false;
	};

	$scope.closeNotification = function () {
		$scope.notificationHidden = true;
	};

	$scope.showIndicator = function () {
		$scope.loaderStatus = 'active';
	};

	$scope.closeIndicator = function () {
		$scope.loaderStatus = 'disabled';
	};

	$scope.notificationHidden = true;
	$scope.loaderStatus = 'disabled';
}]);

app.controller('loginController', ['$scope', 'User', function ($scope, User) {
  //initialize form validation
  $('#loginForm').form({
    password: {
      identifier: 'password',
      rules: [{
        type: 'empty',
        prompt: '密码不能为空'
      }]
    },
    email: {
      identifier: 'email',
      rules: [{
        type: 'empty',
        prompt: '邮箱不能为空'
      }, {
        type: 'email',
        prompt: '必须是标准格式的邮箱'
      }]
    }
  }, {
    on: 'blur',
    inline: 'true'
  });

  $scope.login = function () {
    if ($scope.loginForm.$invalid) {
      return;
    }

	  $scope.showIndicator();

    var postData = {
      "email" : $scope.email,
      "password" : $scope.password
    };

    User.login(postData, function() {
      top.location = '#manage';
	    $scope.closeIndicator();
    }, function(msg) {
	    $scope.closeIndicator();
	    $scope.showNotification(msg, 'error');
    });
  };
}]);

app.controller('manageController', ['$scope', 'Apps', 'User', 'UpYun', function ($scope, Apps, User, UpYun) {
  $scope.notificationHidden = true;

  if (User.getId()) {
	  $scope.showIndicator();

    Apps.query(User.getId(), function (apps) {
      $scope.apps = apps;
	    $scope.closeIndicator();
    }, function (msg) {
	    $scope.showNotification(msg, 'error');
	    $scope.closeIndicator();
    });
  }

	$scope.showUpdateModal = function (appId) {
		$scope.appId = appId;
		$('.ui.modal').modal('show');
	};

	$scope.loaded = 0;
	$scope.total = 0;

	$scope.onFileSelect = function ($files) {
		$scope.upload = UpYun.upload($files[0], function (loaded, total) {
			$scope.loaded = loaded;
			$scope.total = total;
		}, function (url, appId) {
			$scope.appId = appId;
			$scope.icon_src = url;
		}, function (msg) {
			$scope.showNotification(msg, 'error');
		});
	};

	$scope.update = function () {
		$scope.showIndicator();

		Apps.update({
			appId: $scope.appId,
			icon_src: $scope.icon_src,
			manifestURL: $scope.manifestURL,
			keywords: $scope.keywords
		}, function () {
			$scope.closeIndicator();
		}, function (msg) {
			alert(msg);
			$scope.closeIndicator();
		});
	};

	$scope.resubmit = function (appId) {
		$scope.showIndicator();

		Apps.resubmit(appId, function () {
			$scope.showNotification('提交成功', 'success');
			$scope.closeIndicator();
		}, function (msg) {
			$scope.showNotification(msg, 'error');
			$scope.closeIndicator();
		});
	};
}]);

app.controller('userController', [ '$scope', 'User', function($scope, User) {
  //initial form validation
  $('#userEditForm').form({
    name: {
      identifier: 'name',
      rules: [{
        type: 'empty',
        prompt: '请输入开发者／团队名'
      }]
    },
    website: {
      identifier: 'website',
      rules: [{
        type: 'empty',
        prompt: '请输入开发者／团队的网站'
      }, {
        type: 'url',
        prompt: '必须是以http://开头的网址'
      }]
    }
  }, {
    on: 'blur',
    inline: 'true'
  });

	User.getInfo(User.getId(), function (user) {
    $scope.user = user;
    $scope.user.password = '';
		$scope.closeIndicator();
  }, function (msg) {
		$scope.showNotification(msg, 'error');
		$scope.closeIndicator();
  });

	$scope.update = function () {
    if ($scope.userEditForm.$invalid) {
      return;
    }

		$scope.showIndicator();

    var updateData = {};
    updateData.devid = User.getId();
    updateData.name = $scope.user.name;
    updateData.email = $scope.user.email;

    if ($scope.user.password) {
      updateData.password = $scope.user.password;
    }

    updateData.website = $scope.user.website;

    User.update(updateData, function () {
	    $scope.showNotification('修改成功', 'success');
	    $scope.closeIndicator();
    }, function (msg) {
	    $scope.showNotification(msg, 'error');
	    $scope.closeIndicator();
    });
	};
}]);

app.controller('addController', ['$scope', 'Apps', 'User', 'UpYun', function ($scope, Apps, User, UpYun) {
  //initialize dropdown widget
  $('.ui.dropdown').dropdown({
    onChange: function (value) {
      $scope.appCategory = value;
    }
  });
  //initialize form validation
  $('#addForm').form({
    name: {
      identifier: 'appName',
      rules: [{
        type: 'empty',
        prompt: '请输入app名字'
      }]
    },
    url: {
      identifier: 'appURL',
      rules: [{
        type: 'empty',
        prompt: '请输入app url'
      }, {
        type: 'url',
        prompt: '必须是以http://开头的网址'
      }]
    },
    category: {
      identifier: 'appCategory',
      rules: [{
        type: 'empty',
        prompt: '请选择app分类'
      }]
    },
    description: {
      identifier: 'appDescription',
      rules: [{
        type: 'empty',
        prompt: '请输入app的描述'
      }]
    },
	  keywords: {
		  identifier: 'appKeywords',
		  rules: [{
			  type: 'empty',
			  prompt: '请输入关键字'
		  }]
	  }
  }, {
    on: 'blur',
    inline: 'true'
  });

  $scope.loaded = 0;
  $scope.total = 0;

  $scope.onFileSelect = function ($files) {
    $scope.upload = UpYun.upload($files[0], function (loaded, total) {
      $scope.loaded = loaded;
      $scope.total = total;
    }, function (url, appId) {
	    $scope.appId = appId;
      $scope.icon_src = url;
    }, function (msg) {
	    $scope.showNotification(msg, 'error');
    });
  };

	$scope.steps = [{
		status : 'active',
		show : true,
		next : function () {
      if (!$scope.icon_src) {
	      $scope.showNotification('请上传图标', 'error');
        return;
      }

      if ($scope.addForm.$invalid) {
        return;
      }

			this.status = '';
			this.show = false;
			$scope.steps[1].status = 'active';
			$scope.steps[1].show = true;
		}
	}, {
		status : 'disabled',
		show : false,
		next : function () {
			var appToAdd = {};
			appToAdd.appId = $scope.appId;
			appToAdd.name = $scope.appName;
			appToAdd.url = $scope.appURL;
			appToAdd.description = $scope.appDescription;
			appToAdd.devid = User.getId();
			appToAdd.category = $scope.appCategory;
			appToAdd.icon_src = $scope.icon_src;
			appToAdd.manifest = $scope.appURL + '/manifest.webapp';
			appToAdd.keywords = $scope.appKeywords;

			$scope.showIndicator();

			Apps.add(appToAdd, function () {
				$scope.closeIndicator();

				$scope.steps[1].status = '';
				$scope.steps[1].show = false;

				$scope.steps[2].status = 'active';
				$scope.steps[2].show = true;
			}, function(msg) {
				$scope.showNotification(msg, 'error');
			});
		}
	}, {
		status : 'disabled',
		show : false
	}];

	$scope.complete = function () {
		$scope.addName = '';
		$scope.appURL = '';
		$scope.appDescription = '';
		top.location = '#manage';
	};

	User.getInfo(User.getId(), function (user) {
		$scope.developer = user;
	}, function (msg) {
		$scope.showNotification(msg, 'error');
	});
}]);

app.controller('claimController', ['$scope', 'User', 'Apps', function ($scope, User, Apps) {

}]);

app.controller('regController', [ '$scope', 'User', function ($scope, User) {
  //initial form validation
  $('#regForm').form({
    email: {
      identifier: 'email',
      rules: [{
        type: 'empty',
        prompt: '请输入邮箱'
      }, {
        type: 'email',
        prompt: '必须是标准格式的邮箱'
      }]
    },
    password: {
      identifier: 'password',
      rules: [{
        type: 'empty',
        prompt: '请输入密码'
      }]
    },
    name: {
      identifier: 'name',
	    rules: [{
		    type: 'empty',
		    prompt: '请输入开发者／团队名'
	    }]
    },
    website: {
      identifier: 'website',
      rules: [{
        type: 'empty',
        prompt: '请输入开发者／团队的网站'
      }, {
        type: 'url',
        prompt: '必须是一个以http://开头的网址'
      }]
    }
  }, {
    on: 'blur',
    inline: 'true'
  });

  $('#codeForm').form({
    code: {
      identifier: 'code',
      rules: [{
        type: 'empty',
        prompt: '请输入验证码'
      }]
    }
  }, {
    on: 'blur',
    inline: 'true'
  });

	$scope.steps = [{
		status : 'active',
		show : true
	}, {
		status : 'disabled',
		show : false
	}];

	$scope.next = function () {
    if ($scope.regForm.$invalid) {
      return;
    }

		$scope.showIndicator();

    var postData = {};
    postData.email = $scope.newEmail;
    postData.password = $scope.newPassword;
    postData.name = $scope.newName;
    postData.website = $scope.newWebsite;

    User.register(postData, function () {
	    $scope.closeIndicator();
      $scope.steps[0].status = '';
      $scope.steps[0].show = false;
      $scope.steps[1].status = 'active';
      $scope.steps[1].show = true;
    }, function (msg) {
	    $scope.closeIndicator();
	    $scope.showNotification(msg, 'error');
    });
	};

  $scope.checkMail = function () {
    if ($scope.codeForm.$invalid) {
      return;
    }

    var getData = {};
    getData.email = $scope.newEmail;
    getData.code = $scope.code;

    User.checkMail(getData, function () {
      top.location = '#manage';
    }, function (msg) {
	    $scope.showNotification(msg, 'error');
    });
  };
}]);