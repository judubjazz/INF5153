const hideDraggableMap = () => {
  $('.fleet-container').remove();
  $('.player-one-map').remove();
  $('.options-container').remove();
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
      div.addClass('tiles');
      td.append(div);
      tr.append(td);
    }
  }
  $('#dinamic-player-targets').append(table);
};

const hideLoader = (msg) => {
  $('#message-header').html(msg);
  $('.loader-container').css('display', 'none');
  $('.map-container').css('filter', 'blur(0px)');
  $('.fleet-container').css('filter', 'blur(0px)');
};

const showLoader = (msg) => {
  $('#message-header').html(msg);
  $('.loader-container').css('display', 'block');
  $('.map-container').css('filter', 'blur(13px)');
  $('.fleet-container').css('filter', 'blur(13px)');
};

// TODO validate game
const isValidGame = () => {
  return true;
};

const hideOptions = () =>{
  $('.options-container').remove();
  $('.player-one-map').remove()
};



