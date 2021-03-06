'use strict';
const {WAITING_OPP_TURN, SHIP_LOCATION_ERR, WAITING_GAME_CONNECTION, TARGET_OPP_SHIP, YOU_WON, YOU_LOST, QUIT_GAME_MSG, OPP_DID_QUIT} = constants;
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
  isWaiting: true,
};


const torpedo = (event, x, y) => {
  event.preventDefault();
  const {id, isWaiting} = game;
  if(!isWaiting){
    const target = {
      id,
      x,
      y,
    };
    socket.emit('playerTwoWillPlayTurn', JSON.stringify(target));
  }
};

socket.on('playerTwoDidPlay', (res) =>{
  const {x, y, winner, hit} = res;
  if(hit > 0){
    game.targets[x][y] = -1;
  } else {
    game.targets[x][y] = -2;
  }
  if(winner){
    showLoader(YOU_WON);
  } else {
    $('#targets-table').remove();
    renderTargetsMap();
    showLoader(WAITING_OPP_TURN);
    console.log('player two did play', res);
    game.isWaiting = true;
  }
});

socket.on('playerOneDidPlay', (res) =>{
  const {x, y, winner, hit} = res;
  if(hit > 0){
    game.map[x][y] = -1;
  } else {
    game.map[x][y] = -2;
  }
  game.isWaiting = false;

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
  console.log('player did join game', res);
  const {map} = res;
  game.map = map;
  renderTargetsMap();
  renderPlayerMap();
  hideOptions();
  hideDraggableMap();
  showLoader(WAITING_OPP_TURN);
});

socket.on('playerOneDidLeave', (res) => {
  console.log('player did leave', res);
  showLoader(OPP_DID_QUIT);
});

$(window).bind('beforeunload', function(){
  return 'Are you sure you want to leave?';
});

window.onunload = () => {
  socket.emit('playerTwoDidLeave', JSON.stringify({id:Number(game.id)}));
};


const joinGame = () =>{
  const id = location.href.substr(location.href.lastIndexOf('/') + 1);
  game.id = Number(id);
  console.log(JSON.stringify(game));
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


