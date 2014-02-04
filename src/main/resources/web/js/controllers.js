'use strict';

/* Controllers */
var votrControllers = angular.module('votrControllers', []);

votrControllers.controller('QuestionDetailCtrl', ['$scope', '$routeParams', 'Question', 'Answer',
    function ($scope, $routeParams, Question, Answer) {

        $scope.waiting = true;


        $scope.Question = Question.get({talkId: $routeParams.talkId}, function (Question) {

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

            Answer.save({}, { questionId: questionId, answerId: answerId});
            $scope.selected = answerId;
        }
    }]);