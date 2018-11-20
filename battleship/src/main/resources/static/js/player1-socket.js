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
  shipsRemaining: 17,
  winner:false
};

// TODO make constants for socket events name

const createGame = () => {
  if (isValidGame()) socket.emit('playerWillCreateGame', JSON.stringify(game));
  else alert('ships emplacement invalid');
};

// TODO validate game
const isValidGame = () => {
  return true;
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
  const json = JSON.parse(res);
  game.id = json.id;
  game.map = json.map;
  hideDraggableMap();
  showLoader();
});

socket.on('playerDidJoinGame', (res) => {
  console.log(res);
  const json = JSON.parse(res);
  renderTargetsMap();
  renderPlayerMap();
  hideLoader();
  $('#dinamic-player-map').css('display','block');
  $('#dinamic-player-targets').css('display', 'block');

});

socket.on('playerOneDidPlay', (res) => {
  console.log(res);
  const json = JSON.parse(res);
  const x = json.x;
  const y = json.y;
  game.targets[x][y] = -1;
  $('#targets-table').remove();
  renderTargetsMap();
  hideLoader();
});

socket.on('playerTwoDidPlay', (res) => {
  console.log(res);
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
  hideLoader();
});

const hideDraggableMap = () => {
  $('.fleet-container').css('display', 'none');
  $('.player-one-map').css('display', 'none');
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

const hideLoader = () => {
  $('.loader-container').css('display', 'none');
  $('.map-container').css('filter', 'blur(0px)');
  $('.fleet-container').css('filter', 'blur(0px)');
};

const showLoader = () => {
  $('.loader-container').css('display', 'block');
  $('.map-container').css('filter', 'blur(13px)');
  $('.fleet-container').css('filter', 'blur(13px)');
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
