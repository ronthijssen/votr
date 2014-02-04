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


votrControllers.controller('ReportCtrl', ['$scope', '$routeParams', 'reportPoller',
    function ($scope, $routeParams, reportPoller) {

        $scope.options = ["Pie", "Doughnut" ]
        $scope.graph = "Doughnut";


        $scope.colors = [ "#F7464A", "#E2EAE9" , "#D4CCC5" , "#949FB1", "#4D5360"];


        reportPoller.startPolling($routeParams.talkId, function (Report) {

            var answers = _.map(Report.answers, function (answer, index) {
                return {
                    value: parseInt(answer.result),
                    color: $scope.colors[index]
                };

            });
            ;
            console.log(answers);


//            [
//                {
//                    value: 30,
//                    color: "#F7464A"
//                },
//                {
//                    value: 50,
//                    color: "#E2EAE9"
//                },
//                {
//                    value: 100,
//                    color: "#D4CCC5"
//                },
//                {
//                    value: 40,
//                    color: "#949FB1"
//                },
//                {
//                    value: 120,
//                    color: "#4D5360"
//                }
//
//            ]

            $scope.MyChart = {
                width: 200,
                height: 200,
                options: {},
                data: answers
            }

            console.log($scope.MyChart);

        }), function () {

            $scope.waiting = true;
            console.log('404')
        };


        $scope.selectGraph = function (graph) {
            $scope.graph = graph;
        }


    }
])
;
