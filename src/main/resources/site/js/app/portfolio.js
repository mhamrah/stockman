angular.module('portfolioServices', ['ngResource']).
  factory('Portfolio', function($resource){
    return $resource('portfolios/:portfolioId', {}, {
      //query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
    });
  });

angular.module('stockman', ['portfolioServices']);

function PortfolioCtrl($scope, Portfolio) {
  $scope.portfolios = Portfolio.query();
  $scope.addPortfolio = function() {
  
    var p = new Portfolio($scope.portfolio);
    p.$save(function(data, status) {
      $scope.portfolios.push(p);
      $scope.portfolio.name = '';
    });
  };
}
