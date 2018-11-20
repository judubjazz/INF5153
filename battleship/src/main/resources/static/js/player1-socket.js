'use strict';
// import constants from './constants/constants';
// console.log(constants);

const constants = {
  WAITING_OPP_TURN: 'waiting for opponent to play turn',
  TARGET_OPP_SHIP: 'target an opponent ship',
  WAITING_GAME_CONNECTION: 'waiting for a player to connect game with id',
  SHIP_LOCATION_ERR: 'ships locations invalid',
  HOME_URL: 'http://localhost:8090',
  START_GAME: 'Start Game',
  CREATE_GAME: 'Create Game',
  JOIN_GAME: 'Join Game',
};
const {WAITING_OPP_TURN, SHIP_LOCATION_ERR, WAITING_GAME_CONNECTION, TARGET_OPP_SHIP} = constants;


const socket = io(window.location.protocol + '//localhost:9291');
var game = {
  id: null,
  playerID:1,
  map: [
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  ],
  fleet: {
    carrier: {stem: {x: 0, y: 0}, bow: {x: 0, y: 4}, size: 5},
    destroyer: {stem: {x: 1, y: 0}, bow: {x: 1, y: 1}, size: 2},
    battleship: {stem: {x: 2, y: 0}, bow: {x: 2, y: 3}, size: 3},
    cruiser: {stem: {x: 3, y: 0}, bow: {x: 3, y: 2}, size: 3},
    submarine: {stem: {x: 4, y: 0}, bow: {x: 4, y: 3}, size: 4}
  },
  targets: [
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  ],
  shipsRemaining: 17,
  winner:false
};

// TODO make constants for socket events name

const createGame = () => {
  if (isValidGame()) socket.emit('playerWillCreateGame', JSON.stringify(game));
  else alert(SHIP_LOCATION_ERR);
};



const torpedo = (event, x, y) => {
  event.preventDefault();
  const target = {
    x,
    y,
  };
  game.targets[x][y] = -1;
  socket.emit('playerOneWillPlayTurn', JSON.stringify(target));
};

socket.on('playerDidCreateGame', (res) => {
  const data = JSON.parse(res);
  game.id = data.id;
  game.map = data.map;
  hideDraggableMap();
  showLoader(WAITING_GAME_CONNECTION);
});

socket.on('playerDidJoinGame', (res) => {
  console.log(res);
  const json = JSON.parse(res);
  renderTargetsMap();
  renderPlayerMap();
  hideLoader('target an opponnent ship');
  $('#dinamic-player-map').css('display','block');
  $('#dinamic-player-targets').css('display', 'block');

});

socket.on('playerOneDidPlay', (res) => {
  console.log(res);
  const data = JSON.parse(res);
  const x = data.x;
  const y = data.y;
  game.targets[x][y] = -1;
  $('#targets-table').remove();
  renderTargetsMap();
  showLoader(WAITING_OPP_TURN);
});

socket.on('playerTwoDidPlay', (res) => {
  console.log(res);
  const data= JSON.parse(res);
  const x = data.x;
  const y = data.y;
  if(game.map[x][y]){
    game.shipsRemaining--;
  }
  game.map[x][y] = -1;
  $('#player-map-table').remove();
  renderPlayerMap();
  hideLoader(TARGET_OPP_SHIP);
  console.log('player two did play', data);
});



socket.on('connect_failed', function (data) {
  console.log('connect_failed');
});
socket.on('connecting', function (data) {
  console.log('connecting');
});
socket.on('disconnect', function (data) {
  console.log('disconnect');
});
socket.on('error', function (reason) {
  console.log('error');
});
socket.on('reconnect_failed', function (data) {
  console.log('reconnect_failed');
});
socket.on('reconnect', function (data) {
  console.log('reconnect');
});
socket.on('reconnecting', function (data) {
  console.log('reconnecting');
});
