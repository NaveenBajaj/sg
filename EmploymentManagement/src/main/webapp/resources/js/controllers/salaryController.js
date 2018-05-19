'use strict';
angular.module('sgApp')
    .controller('SalaryCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window, $stateParams) {
    	console.log($stateParams.employeeId);
    	$scope.employeeId = $stateParams.employeeId;
    	$http.get('/api/employee/'+$scope.employeeId )
    	.success(function(response) {
    		$scope.editEmployee = response;
    	});
    	var month = moment().format('MM');
		var year = moment().format('Y');

    	$http.get('/api/employee/'+$scope.employeeId+'/calculate-salary/'+month+'/'+year)
    	.success(function(response) {
    		console.log(response);
    		$scope.editEmployeeSalary = response;
    		$scope.extraIncome = (Number($scope.editEmployeeSalary.extraHours) / 10) * Number($scope.editEmployeeSalary.ratePerDay);
    		$scope.leavesDeduction = Number($scope.editEmployeeSalary.leaves) * Number($scope.editEmployeeSalary.ratePerDay);
    		$scope.totalDeduction = Number($scope.editEmployeeSalary.pfAmount) + Number($scope.editEmployeeSalary.esicAmount) + $scope.leavesDeduction;
    	});
    	
    	
    	$scope.updateLeaves = function(){
    		console.log($scope.editEmployeeSalary)
    		$scope.editEmployeeSalary.employeeId = $scope.editEmployee.employeeId;
    		$http.post('/api/employee/'+$scope.employeeId+'/update-salary', $scope.editEmployeeSalary, {
    			headers : {
    				"content-type" : "application/json"
    			}
    		}).success(function(data) {
    			console.log(data);
    			$scope.editEmployeeSalary = data;
    			$scope.leavesDeduction = Number($scope.editEmployeeSalary.leaves) * Number($scope.editEmployeeSalary.ratePerDay);
        		$scope.totalDeduction = Number($scope.editEmployeeSalary.pfAmount) + Number($scope.editEmployeeSalary.esicAmount) + $scope.leavesDeduction;
    			
    		}).error(function(data) {
    			console.log(data);
    		})
    	}
    	
    })
