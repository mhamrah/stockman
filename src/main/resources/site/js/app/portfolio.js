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
    console.log($scope.portfolio);
  
    var p = new Portfolio($scope.portfolio);
    p.$save(function(data, status, headers, config) {
      console.log(data);
      console.log(status);
      console.log(headers);
      console.log(config);
    });
  };
}
