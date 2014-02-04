'use strict';

/* Controllers */
var votrControllers = angular.module('votrControllers', []);

votrControllers.controller('QuestionDetailCtrl', ['$scope', '$routeParams', 'Question', 'Answer',
    function ($scope, $routeParams, Question, Answer) {

        $scope.waiting = true;
        $scope.talkId = $routeParams.talkId;


        $scope.Question = Question.get({talkId: $scope.talkId}, function (Question) {

            if (Question.status == 'QUESTION') {
                $scope.waiting = false;
                $scope.id = Question.question.id;
                $scope.title = Question.question.title;
                $scope.options = Question.question.options;
                $scope.selected = 0;
            } else if (Question.statis = 'WAITING') {

                $scope.waiting = true;
            }

        }), function () {

            $scope.waiting = true;
            console.log('404')
        };

        $scope.select = function (questionId, answerId) {
            console.log(questionId, answerId);

            Answer.save({talkId: $scope.talkId, questionId: questionId}, {optionId: answerId});
            $scope.selected = answerId;
        }
    }]);