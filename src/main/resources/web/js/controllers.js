'use strict';

/* Controllers */
var votrControllers = angular.module('votrControllers', []);

votrControllers.controller('QuestionDetailCtrl', ['$scope', '$routeParams', 'Question', 'Answer',
    function ($scope, $routeParams, Question, Answer) {
        $scope.Question = Question.get({talkId: $routeParams.talkId}, function (Question) {
            $scope.title = Question.title;
            $scope.options = Question.options;
            $scope.selected = 0;
        }), function () {
            console.log('404')
        };

        $scope.select = function (questionId, answerId) {
            Answer.save({}, { questionId: questionId, answerId: answerId});
            $scope.selected = answerId;
        }
    }]);