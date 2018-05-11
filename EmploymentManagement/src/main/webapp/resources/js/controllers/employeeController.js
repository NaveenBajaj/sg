'use strict';
angular.module('sgApp')
    .controller('EmployeeCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window) {
    	$scope.addEmployee = {}
    	$scope.addEmployee.salaryBasis = "package";
    	$scope.addEmployee.isPf = true;
    	$scope.addEmployee.isEsic = true;
    	
    	$scope.allEmployees = [];
    	$http.get('/api/employee/')
	    	.success(function(response) {
	    		$scope.allEmployees = response;
	    	});
    	
    	$scope.saveEmployee = function(){
    		console.log($scope.addEmployee);
    		$http.post('/api/employee/', $scope.addEmployee, {
    			headers : {
    				"content-type" : "application/json"
    			}
    		}).success(function(data) {
    			console.log(data);
    			Notification.success("Added " + $scope.addEmployee.firstName + " "+$scope.addEmployee.lastName+" relation.");
    			$state.go('app.employee', null, {reload: true});
    			
    		}).error(function(data) {
    			console.log(data);
    		})
    	}
    	if(angular.isDefined($cookieStore.get("editEmployeeId"))){
    		$http.get('/api/employee/'+$cookieStore.get("editEmployeeId"))
	    	.success(function(response) {
	    		console.log(response);
	    		$scope.editEmployee = response;
	    	});
    	
    	}
    	$scope.editEmployeeDetails = function(employeeId){
    		console.log(employeeId);
    		$cookieStore.put("editEmployeeId", employeeId);
    		$state.go('app.editEmployee');
    	}
    	
    	
    	$scope.calculateSalary = function(employeeId){
    		console.log(employeeId);
    		$window.open('#/app/salary/' + employeeId, "_self");
    	}
    	
    	
    });