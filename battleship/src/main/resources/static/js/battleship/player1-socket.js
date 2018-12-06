'use strict';

const {WAITING_OPP_TURN, SHIP_LOCATION_ERR, WAITING_GAME_CONNECTION, TARGET_OPP_SHIP, YOU_WON, YOU_LOST, QUIT_GAME_MSG, OPP_DID_QUIT} = constants;
const socket = io(window.location.protocol + '//localhost:9291');
var idShip = 5;
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
  isWaiting: false,
};

const createGame = () => {
  if (isValidGame()) socket.emit('playerWillCreateGame', JSON.stringify(game));
  else alert(SHIP_LOCATION_ERR);
};


const torpedo = (event, x, y) => {
  event.preventDefault();
  const {isWaiting, id} = game;
  if(!isWaiting){
    const target = {
      id,
      x,
      y,
    };
    socket.emit('playerOneWillPlayTurn', JSON.stringify(target));
  }
};

socket.on('playerDidCreateGame', (res) => {
  const {id, map} = res;
  game.id = id;
  game.map = map;
  hideDraggableMap();
  hideOptions();
  showLoader(WAITING_GAME_CONNECTION);
});

socket.on('playerDidJoinGame', (res) => {
  console.log(res);
  renderTargetsMap();
  renderPlayerMap();
  hideLoader(TARGET_OPP_SHIP);
  $('#dinamic-player-map').css('display','block');
  $('#dinamic-player-targets').css('display', 'block');

});

socket.on('playerOneDidPlay', (res) => {
  console.log('player one did play', res);
  const {x, y, winner, hit} = res;
  if(hit > 0){
    game.targets[x][y] = -1;
  } else {
    game.targets[x][y] = -2;
  }

  if(winner){
    $('#message-header').html(YOU_WON);
    // showLoader(YOU_WON);
  } else {
    $('#targets-table').remove();
    renderTargetsMap();
    showLoader(WAITING_OPP_TURN);
    game.isWaiting = true;
  }
});

socket.on('playerTwoDidPlay', (res) => {
  const {x, y, winner, hit} = res;
  if(hit > 0){
    game.map[x][y] = -1;
  } else {
    game.map[x][y] = -2;
  }
  game.isWaiting = false;

  if(winner){
    $('#message-header').html(YOU_LOST);
    // showLoader(YOU_LOST)
    // TODO show end of game
  } else {
    $('#player-map-table').remove();
    renderPlayerMap();
    hideLoader(TARGET_OPP_SHIP);
    console.log('player two did play', res);
  }
});

socket.on('playerTwoDidLeave', (res) => {
  console.log('player did leave', res);
  showLoader(OPP_DID_QUIT);
});


$(window).bind('beforeunload', function(){
  return QUIT_GAME_MSG;
});

window.onunload = () => {
  socket.emit('playerOneDidLeave', JSON.stringify({id:Number(game.id)}));
};

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


