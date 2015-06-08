var app = angular.module('myApp', []);
app.controller('personCtrl', function($scope) {
//    $scope.firstName = "Johngdg";
    $scope.lastName = "Doesgsg";
    $scope.myVar = false;
    $scope.toggle = function() {

        $scope.firstName = document.getElementByName('First').value;
        $scope.myVar = !$scope.myVar;
    };
});