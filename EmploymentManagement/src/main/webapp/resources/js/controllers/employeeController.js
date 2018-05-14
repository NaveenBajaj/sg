'use strict';
angular.module('sgApp')
    .controller('EmployeeCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window) {
    	$scope.addEmployee = {};
    	$scope.addEmployeeSalary = {};
    	$scope.addEmployee.salaryBasis = "package";
    	$scope.addEmployee.isPf = true;
    	$scope.addEmployee.isEsic = true;
    	
    	$scope.allEmployees = [];
    	$http.get('/api/employee/')
	    	.success(function(response) {
	    		$scope.allEmployees = response;
	    	});
    	
    	$scope.saveEmployee = function(){
    		$scope.addEmployeeSalary.employeeId = $scope.addEmployee.employeeId;
    		$scope.addEmployeeSalary.month = moment().format('M');
    		$scope.addEmployeeSalary.year = moment().format('Y');
    		var obj = {"employee": $scope.addEmployee, "salary":$scope.addEmployeeSalary}
    		$http.post('/api/employee/', obj, {
    			headers : {
    				"content-type" : "application/json"
    			}
    		}).success(function(data) {
    			console.log(data);
    			Notification.success("Added " + $scope.addEmployee.firstName + " "+$scope.addEmployee.lastName+" employee.");
    			$state.go('app.employee', null, {reload: true});
    			
    		}).error(function(data) {
    			console.log(data);
    		})
    	}
    	
    	$scope.updateEmployee = function(){
    		$scope.editEmployeeSalary.employeeId = $scope.editEmployee.employeeId;
    		$scope.editEmployeeSalary.month = moment().format('M');
    		$scope.editEmployeeSalary.year = moment().format('Y');
    		var obj = {"employee": $scope.editEmployee, "salary":$scope.editEmployeeSalary}
    		$http.post('/api/employee/'+$scope.editEmployee.employeeId, obj, {
    			headers : {
    				"content-type" : "application/json"
    			}
    		}).success(function(data) {
    			console.log(data);
    			Notification.success("Updated " + $scope.editEmployee.firstName + " "+$scope.editEmployee.lastName+" employee.");
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
	    		$scope.editEmployee.isPf = ($scope.editEmployee.isPf == 'true');
	    		$scope.editEmployee.isEsic = ($scope.editEmployee.isEsic == 'true');
	    	});
    		var month = moment().format('M');
    		var year = moment().format('Y');
    		$http.get('/api/employee/'+$cookieStore.get("editEmployeeId")+'/salary/'+month+'/'+year)
	    	.success(function(response) {
	    		console.log(response);
	    		$scope.editEmployeeSalary = response;
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