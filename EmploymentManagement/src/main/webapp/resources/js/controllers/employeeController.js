'use strict';
angular.module('sgApp')
    .controller('EmployeeCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window) {
    	$scope.allEmployee = [{"empCode":"45","empName":"Kamal Yadav","position":""},
    	                      {"empCode":"03","empName":"MUKESH KUMAR YADAV","position":""}];
    	console.log($scope.allEmployee);
    	
    	
    	$scope.editEmployeeDetails = function(empObject){
    		console.log(empObject);
    		$cookieStore.put("editEmployee")
    	}
    	
    	
    	
    });