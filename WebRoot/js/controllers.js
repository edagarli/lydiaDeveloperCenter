/**
 * Codz by PortWatcher. Team: Jrooy Date: 13-10-16 Time: 下午10:36
 */

var app = angular.module('lydiadev',
		[ 'lydiadev.services', 'angularFileUpload' ]);

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		controller : '',
		templateUrl : 'views/hero.html'
	}).when('/register', {
		controller : 'regController',
		templateUrl : 'views/reg.html'
	}).when('/manage', {
		controller : 'manageController',
		templateUrl : 'views/manage.html'
	}).when('/developer', {
		controller : 'userController',
		templateUrl : 'views/user.html'
	}).when('/add', {
		controller : 'addController',
		templateUrl : 'views/add.html'
	}).when('/faq', {
		controller : 'faqController',
		templateUrl : 'views/faq.html'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

app.controller('mainController', [ '$scope', 'User', function($scope, User) {
	$scope.isLoggedIn = false;

	$scope.register = function() {
		top.location = '#register';
	};

	$scope.login = function() {
		var postData = {
			"email" : $scope.email,
			"password" : $scope.password
		};

		User.login(postData, function() {
			$scope.isLoggedIn = true;
		}, function() 
		{
			alert('邮箱或密码错误');
		});
	};

	$scope.logout = function() {
		User.logout();
		$scope.isLoggedIn = false;
	}

	$scope.btnUploadClick = function() {
		if ($scope.isLoggedIn) {
			top.location = '#add';
		} else {
			$('.sidebar').sidebar('toggle');
		}
	};
} ]);

app.controller('manageController', [ '$scope', 'Apps', function($scope, Apps) {
	$scope.apps = Apps.query();
} ]);

app.controller('userController', [ '$scope', 'User', function($scope, User) {
	$scope.user = User.getInfo();

	$scope.update = function() {
		var updateData = {};
		updateData.name = $scope.user.name;
		updateData.email = $scope.user.email;
		updateData.password = $scope.user.password;
		updateData.website = $scope.user.website;
	};
} ]);

app.controller('addController', ['$scope', 'Apps', 'User', '$http', function($scope, Apps, User, $http) {
			$scope.onFileSelect = function($files) {
				for ( var i = 0; i < $files.length; i++) {
					var $file = $files[i];
					$http.uploadFile({
						url : '/api/apps/icon/upload',
						data : {},
						file : $file
					}).progress(
							function(evt) {
								console.log('percent: '
										+ parseInt(100.0 * evt.loaded
												/ evt.total));
							}).then(function(data, status, headers, config) {
						var result = JSON.parse(data);
						if (result.status === RESULT_SUCCESS) {
							$scope.icon_src = result.icon_src;
						}
					});
				}
			};

			$scope.steps = [ {
				status : 'active',
				show : true,
				next : function() {
					// TODO: validate the user form first, then:
					this.status = '';
					this.show = false;
					$scope.steps[1].status = 'active';
					$scope.steps[1].show = true;
				}
			}, {
				status : 'disabled',
				show : false,
				next : function() {
					// TODO: validate the user form first, then:
					var appToAdd = {};
					appToAdd.name = $scope.appName;
					appToAdd.url = $scope.appURL;
					appToAdd.description = $scope.appDescription;
					appToAdd.devid = User.getId();
					appToAdd.category = $scope.appCategory;
					appToAdd.icon_src = $scope.icon_src || 'http://www.pwhack.me/icon_moment.png';
					Apps.add(appToAdd, function() {
						this.status = '';
						this.show = false;
						$scope.steps[2].status = 'active';
						$scope.steps[2].show = true;
					}, function() {
						alert('添加失败，大侠请再来一次');
					});
				}
			}, {
				status : 'disabled',
				show : false
			} ];

			$scope.complete = function() {
				$scope.addName = '';
				$scope.appURL = '';
				$scope.appDescription = '';
				top.location = '#manage';
			};
		} ]);

app.controller('faqController', [ '$scope', function($scope) {
	// currently empty
} ]);

app.controller('regController', [ '$scope', 'User', function($scope, User) {
	$scope.steps = [ {
		status : 'active',
		show : true
	}, {
		status : 'disabled',
		show : false
	} ];

	$scope.next = function() {
		// TODO: submit to the server, then:
		User.reg();
		$scope.steps[0].status = '';
		$scope.steps[0].show = false;
		$scope.steps[1].status = 'active';
		$scope.steps[1].show = true;
	}
} ])