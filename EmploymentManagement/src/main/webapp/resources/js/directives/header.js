'use strict';

angular.module('sgApp').directive('header', function(){
    return {
            templateUrl: 'views/header.html',
            restrict: 'E',
            replace: true,
            controller: ['$http', '$scope','$rootScope', '$cookieStore', function ($http, $scope, $rootScope, $cookieStore) {
                //$scope.authenticated = $rootScope.authenticated;
                $scope.authenticated = $cookieStore.get('authenticated');
                $scope.currentUser = $cookieStore.get('currentUser');
                $scope.role = $cookieStore.get('role');
            }]
        }
});
