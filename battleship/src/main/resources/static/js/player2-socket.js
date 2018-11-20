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
  const json= JSON.parse(res);
  const x = json.x;
  const y = json.y;
  game.targets[x][y] = -1;
  $('#targets-table').remove();
  renderTargetsMap();
  console.log('playero two did play', json);
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
  renderPlayerMap();
  console.log('playerone did play', json);
});

socket.on('playerDidJoinGame', (res)=>{
  const json= JSON.parse(res);
  game.map = json.map;
  renderTargetsMap();
  renderPlayerMap();
  console.log('player did join game', res);
  hideOptions();
});


const joinGame = () =>{
  if(isValidGame())socket.emit('playerWillJoinGame', JSON.stringify(game));
  else alert('ship emplacement invalid');
};

const isValidGame = () =>{
  return true;
};

const hideOptions = () =>{
  $('.options-container').remove();
  $('.player-one-map').remove()
};

const renderPlayerMap = () => {
  let table = $('<table/>');
  table.attr({'id': 'player-map-table'});
  for (let i = 0; i < 9; ++i) {
    let tr = $('<tr/>');
    table.append(tr);
    for (let j = 0; j < 9; ++j) {
      let td = $('<td/>');
      let div = $('<div/>');
      const shipID = game.map[i][j];
      div.html(shipID);
      div.attr({'name': shipID});
      td.append(div);
      tr.append(td);
    }
  }
  $('#dinamic-player-map').append(table);
};

const renderTargetsMap = () => {
  let table = $('<table/>');
  table.attr({'id': 'targets-table'});
  for (let i = 0; i < 9; ++i) {
    let tr = $('<tr/>');
    table.append(tr);
    for (let j = 0; j < 9; ++j) {
      let td = $('<td/>');
      let div = $('<div/>');
      const shipID = game.targets[i][j];
      div.html(shipID);
      div.attr({'name': shipID});
      div.attr({'id': `${i}, ${j}`});
      div.attr({'onclick': `torpedo(event, ${i}, ${j})`});
      td.append(div);
      tr.append(td);
    }
  }
  $('#dinamic-player-targets').append(table);
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

