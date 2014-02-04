'use strict';

/* Controllers */

var votrControllers = angular.module('votrControllers', []);


votrControllers.controller('QuestionDetailCtrl', ['$scope', '$routeParams', 'Question',
    function ($scope, $routeParams, Question) {
        $scope.Question = Question.get(function (Question) {
            console.log(Question);
            $scope.title = Question.title;
            $scope.options = Question.options;
        });

    }]);
