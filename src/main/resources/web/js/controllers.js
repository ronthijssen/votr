'use strict';

/* Controllers */
var votrControllers = angular.module('votrControllers', []);

votrControllers.controller('QuestionDetailCtrl', ['$scope', '$routeParams', 'Question', 'Answer', 'questionPoller',
    function ($scope, $routeParams, Question, Answer, questionPoller) {

        $scope.waiting = true;
        $scope.talkId = $routeParams.talkId;
        $scope.lastQuestionId = 0;

        questionPoller.startPolling($scope.talkId, function (Question) {

            if (Question.status == 'QUESTION' && Question.question.id != $scope.lastQuestionId) {
                $scope.waiting = false;
                $scope.id = Question.question.id;
                $scope.title = Question.question.title;
                $scope.options = Question.question.options;
                $scope.selected = 0;
                $scope.lastQuestionId = Question.question.id;
            } else if (Question.status == 'WAITING') {

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

        $scope.$on('$destroy', function iVeBeenDismissed() {
            questionPoller.stopPolling();
        });

    }]);