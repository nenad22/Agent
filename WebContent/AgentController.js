angular.module('agentApp').controller('AgentController', AgentController);

AgentController.$inject = [ '$http', '$scope', '$location', '$websocket', '$rootScope' ];

function AgentController($http, $scope, $location, $websocket, $rootScope) {	
	// $location.url('/login');
};
