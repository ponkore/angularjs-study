angular.module('myapp', ['ui.bootstrap']);
function TypeaheadCtrl($scope, $http) {
  // Any function returning a promise object can be used to load values asynchronously
  $scope.getLocation = function(val) {
    var url = '/foo';
    return $http.get(url, {
      params: {
        address: val,
        sensor: false // TODO: sensor プロパティの意味は？
      }
    }).then(function(res){
      var records = [];
      // TODO: View からの指定により絞込、ハイライト等
      // TODO: res.data.status : "OK" じゃない場合
      angular.forEach(res.data, function(item){
        records.push(item.name + " " + item.allowance + " " + item.paid);
      });
      return records;
    });
  };
}
