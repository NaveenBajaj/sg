'use strict';
angular.module('sgApp')
    .controller('SalaryCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window, $stateParams) {
    	console.log($stateParams.employeeId);
    	
    	$http.get('/api/employee/'+$stateParams.employeeId)
    	.success(function(response) {
    		console.log(response);
    		$scope.employeeDetails = response;
    	});
    })
