'use strict';

describe('Service: portfolio', function () {

  // load the service's module
  beforeEach(module('stockmanApp'));

  // instantiate service
  var portfolio;
  beforeEach(inject(function (_portfolio_) {
    portfolio = _portfolio_;
  }));

  it('should do something', function () {
    expect(!!portfolio).toBe(true);
  });

});
