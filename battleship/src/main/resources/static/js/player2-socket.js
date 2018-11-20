'use strict';

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
  shipsRemaining: 17
};


const torpedo = (event, x, y) => {
  event.preventDefault();
  const target = {
    x,
    y,
  };
  game.targets[x][y] = -1;
  socket.emit('playerTwoWillPlayTurn', JSON.stringify(target));
};

socket.on('playerTwoDidPlay', (res) =>{
  const data= JSON.parse(res);
  const x = data.x;
  const y = data.y;
  game.targets[x][y] = -1;
  $('#targets-table').remove();
  renderTargetsMap();
  showLoader('waiting for opponent to play turn');
  console.log('player two did play', data);
});

socket.on('playerOneDidPlay', (res) =>{
  const json= JSON.parse(res);
  const x = json.x;
  const y = json.y;
  if(game.map[x][y]){
    game.shipsRemaining--;
  }
  game.map[x][y] = -1;
  $('#player-map-table').remove();
  hideLoader('target an opponent ship');
  renderPlayerMap();
  console.log('player one did play', json);
});

socket.on('playerDidJoinGame', (res)=>{
  const data= JSON.parse(res);
  game.map = data.map;
  renderTargetsMap();
  renderPlayerMap();
  console.log('player did join game', res);
  hideOptions();
  showLoader('waiting for opponent to play turn')
});


const joinGame = () =>{
  if(isValidGame())socket.emit('playerWillJoinGame', JSON.stringify(game));
  else alert('ship emplacement invalid');
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

