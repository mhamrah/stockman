angular.module('stockmanApp')
  .factory('Portfolio', function ($resource) {
      return $resource('/portfolios/:portfolioId', {}, {
    });
  });
