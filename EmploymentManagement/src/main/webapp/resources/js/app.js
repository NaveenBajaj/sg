'use strict';
angular.module('sgApp', ['ui.router', 'oc.lazyLoad', 'ngCookies', 'ui-notification', 'ngTable', 'ngAnimate', 'ui.bootstrap.datetimepicker']).config(['$ocLazyLoadProvider', '$stateProvider', function($ocLazyLoadProvider, $stateProvider) {
    $ocLazyLoadProvider.config({
        debug: false,
        events: true
    });
    var path = $stateProvider;

    path.state('app', {
        url: '/app',
        controller: 'MainCtrl',
        templateUrl: 'views/main.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/mainController.js'
                    ]
                })
            }
        }
    });


    path.state('app.admin', {
        url: '/admin',
        controller: 'AdminCtrl',
        templateUrl: 'views/admin.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/adminController.js',
                    ]
                })
            }
        }
    });
    
    path.state('app.employee', {
        url: '/employee',
        controller: 'EmployeeCtrl',
        templateUrl: 'views/employee/employee.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/employeeController.js',
                    ]
                })
            }
        }
    });
    
    path.state('app.addEmployee', {
        url: '/add-employee',
        controller: 'EmployeeCtrl',
        templateUrl: 'views/employee/addEmployee.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/employeeController.js',
                    ]
                })
            }
        }
    });
    
    path.state('app.editEmployee', {
        url: '/edit-employee',
        controller: 'EmployeeCtrl',
        templateUrl: 'views/employee/editEmployee.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/employeeController.js',
                    ]
                })
            }
        }
    });
    
    path.state('app.salary', {
        url: '/salary/:employeeId',
        controller: 'SalaryCtrl',
        templateUrl: 'views/employee/salary.html',
        resolve: {
            loadMyFiles: function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'tandemApp',
                    files: [
                        'js/directives/header.js',
                        'js/controllers/salaryController.js',
                    ]
                })
            }
        }
    });

   
}]).factory([
    function() {
        /*your code */
    }
]).run(['$location', '$rootScope', '$state',
    function($location, $rootScope, $state) {
        if (!$rootScope.hasOwnProperty("authenticated") || !$rootScope.authenticated) {
            $state.go('app.employee');
        }
    }
]).run(['$cookieStore', function($cookieStore) {
    $cookieStore.defaultConfig = {
        regions: []
    };
}]).config(function($provide, $httpProvider) {
    //global exception handler
    $provide.factory('httpInterceptor', ['$q', '$location', function($q, $location) {
        return {
            response: function(response) {
                return response || $q.when(response);
            },
            responseError: function(rejection) {
                if (rejection.status === 401 || rejection.status === 403) {
                    $location.path('/admin');
                }
                return $q.reject(rejection);
            }
        };
    }]);
    $httpProvider.interceptors.push('httpInterceptor');
}).config(function(NotificationProvider) {
    NotificationProvider.setOptions({
        delay: 5000,
        startTop: 20,
        startRight: 10,
        verticalSpacing: 20,
        horizontalSpacing: 20,
        positionX: 'right',
        positionY: 'top'
    });
});
