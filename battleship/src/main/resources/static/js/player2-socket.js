'use strict';
const constants = {
  WAITING_OPP_TURN: 'waiting for opponent to play turn',
  TARGET_OPP_SHIP: 'target an opponent ship',
  WAITING_GAME_CONNECTION: 'waiting for a player to connect game with id',
  SHIP_LOCATION_ERR: 'ships locations invalid',
  HOME_URL: 'http://localhost:8090',
  START_GAME: 'Start Game',
  CREATE_GAME: 'Create Game',
  JOIN_GAME: 'Join Game',
  YOU_LOST: 'You Lost',
  YOU_WON: 'You Won'
};
const {WAITING_OPP_TURN, SHIP_LOCATION_ERR, WAITING_GAME_CONNECTION, TARGET_OPP_SHIP, YOU_WON, YOU_LOST} = constants;

const socket = io(window.location.protocol + '//localhost:9291');
var game = {
  id: null,
  playerID:2,
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
};


const torpedo = (event, x, y) => {
  event.preventDefault();
  const id = game.id;
  const target = {
    id,
    x,
    y,
  };
  socket.emit('playerTwoWillPlayTurn', JSON.stringify(target));
};

socket.on('playerTwoDidPlay', (res) =>{
  const {x, y, winner} = res;
  game.targets[x][y] = -1;
  if(winner){
    showLoader(YOU_WON);
  } else {
    $('#targets-table').remove();
    renderTargetsMap();
    showLoader(WAITING_OPP_TURN);
    console.log('player two did play', res);
  }
});

socket.on('playerOneDidPlay', (res) =>{
  const {x, y, winner} = res;
  game.map[x][y] = -1;

  if(winner){
    showLoader(YOU_LOST);
  } else {
    $('#player-map-table').remove();
    hideLoader(TARGET_OPP_SHIP);
    renderPlayerMap();
    console.log('player one did play', res);
  }

});

socket.on('playerDidJoinGame', (res)=>{
  const {map} = res;
  game.map = map;
  renderTargetsMap();
  renderPlayerMap();
  console.log('player did join game', res);
  hideOptions();
  hideDraggableMap();
  showLoader(WAITING_OPP_TURN);
});


const joinGame = () =>{
  const id = location.href.substr(location.href.lastIndexOf('/') + 1);
  game.id = Number(id);
  if(isValidGame())socket.emit('playerWillJoinGame', JSON.stringify(game));
  else alert(SHIP_LOCATION_ERR);
};




socket.on('connect_failed', function(data)
{
  console.log('connect_failed');
});
socket.on('connecting', function(data)
{
  console.log('connecting');
});
socket.on('disconnect', function(data)
{
  console.log('disconnect');
});
socket.on('error', function(reason)
{
  console.log('error');
});
socket.on('reconnect_failed', function(data)
{
  console.log('reconnect_failed');
});
socket.on('reconnect', function(data)
{
  console.log('reconnect');
});
socket.on('reconnecting', function(data)
{
  console.log('reconnecting');
});

