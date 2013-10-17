angular.module('stockmanApp')
  .controller('PortfolioCtrl', function ($scope, Portfolio) {
    $scope.portfolios = Portfolio.query();

    $scope.addPortfolio = function() {
      var p = new Portfolio($scope.portfolio);
      p.$save(function(data, status) {
        $scope.portfolios.push(p);
        $scope.portfolio.name = '';
      });
    };
  });
