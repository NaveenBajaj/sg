'use strict';
angular.module('sgApp')
    .controller('EmployeeCtrl', function($scope, $http, $state, $cookieStore, $rootScope, $location, Notification, $window) {
    	$scope.allEmployee = [{"empCode":"45","empName":"Kamal Yadav","position":""},
    	                      {"empCode":"03","empName":"MUKESH KUMAR YADAV","position":""}];
    	console.log($scope.allEmployee);
    	$scope.addEmployee = {};
    	$scope.saveEmployee = function(){
    		console.log($scope.addEmployee);
    		//delete $scope.errorMessage;
    		$http.post('/api/employee/', $scope.addEmployee, {
    			headers : {
    				"content-type" : "application/json"
    			}
    		}).success(function(data) {
    			console.log(data);
    			Notification.success("Added " + $scope.addEmployee.firstName + " "+$scope.addEmployee.lastName+" relation.");
    			//$state.go('app.appDesign.relation', null, {reload: true});
    			
    		}).error(function(data) {
    			console.log(data);
//    			if(isJsonString(data.message)){
//    				$scope.errorMessage = $.parseJSON(data.message);
//    			}
//    			else{
//    				$scope.errorMessage=[];
//    				$scope.errorMessage.push(data.message);
//    			}	
//    			$('#epopup').modal('show');


    		})
    	}
    	
    	$scope.editEmployeeDetails = function(empObject){
    		console.log(empObject);
    		$cookieStore.put("editEmployee")
    	}
    	
    	
    	
    });