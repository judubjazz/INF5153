const hideDraggableMap = () => {
  $('.fleet-container').remove();
  $('.player-one-map').remove();
  $('.options-container').remove();
};

const renderPlayerMap = () => {
  let table = $('<table/>');
  table.attr({'id': 'player-map-table'});
  for (let i = 0; i < 10; ++i) {
    let tr = $('<tr/>');
    table.append(tr);
    for (let j = 0; j < 10; ++j) {
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
  for (let i = 0; i < 10; ++i) {
    let tr = $('<tr/>');
    table.append(tr);
    for (let j = 0; j < 10; ++j) {
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
  $('.loader-container').css('display', 'flex');
  $('.map-container').css('filter', 'blur(2px)');
  $('.fleet-container').css('filter', 'blur(2px)');
};

// TODO validate game
const isValidGame = () => {
  return true;
};

const hideOptions = () =>{
  $('.options-container').remove();
  $('.player-one-map').remove();
  $('.rules-container').remove();
};



// amine functions

var idShip = 5;
var carrier;
var battleship;
var cruiser;
var submarine;
var destroyer;

/*all white background*/
function whiteBackground() {
  for (var i = 0; i <= 9; i++) {
    for (var j = 0; j <= 9; j++) {
      document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = "#ffffff";
    }
  }
}

/*grey background except black or red cases*/
function greyBackground() {
  for (var i = 0; i <= 9; i++) {
    for (var j = 0; j <= 9; j++) {
      if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != 'rgb(244, 67, 54)'
        && document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != 'rgb(219, 60, 48)'
        && document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != 'rgb(26, 26, 26)') {
        document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = "#f2f2f2";
      }
    }
  }
}

/*grey background except black or red cases*/
function shipOnGrid() {
  for (var i = 0; i <= 9; i++) {
    for (var j = 0; j <= 9; j++) {
      if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != 'rgb(26, 26, 26)') {
        document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = "#ffffff";
      }
    }
  }
}

/*verified if ships coordinates or not empty or null*/

/*if empty, null or undefined. Attribute a new value*/
function validateShipsCoord(str, id) {
  if (!str || !str.length) {
    switch (id) {
      case 5:
        carrier = {stem:{x:0,y:0},bow:{x:4,y:0},size:5};
        break;
      case 4:
        battleship = {stem:{x:0,y:1},bow:{x:3,y:1},size:4};
        break;
      case 3:
        cruiser = {stem:{x:0,y:2},bow:{x:2,y:2},size:3};
        break;
      case 2:
        submarine = {stem:{x:0,y:3},bow:{x:2,y:3},size:3};
        break;
      case 1:
        destroyer = {stem:{x:0,y:4},bow:{x:1,y:4},size:2};
        break;
    }
  }
}

/*clean grid*/
whiteBackground();

/*All ships*/
function shipsChooser(coord) {
  var tr = coord.parentNode.rowIndex;
  var td = coord.cellIndex;

  if (idShip === 5) {
    carrier = position("carrier", tr, td, 5);
  }
  else if (idShip === 4) {
    battleship = position("battleship", tr, td, 4);
  }
  else if (idShip === 3) {
    cruiser = position("cruiser", tr, td, 3);
  }
  else if (idShip === 2) {
    submarine = position("submarine", tr, td, 3);
  }
  else if (idShip === 1) {
    destroyer = position("destroyer", tr, td, 2);
  }

  const {fleet} = game;
  fleet.carrier = carrier;
  fleet.cruiser = cruiser;
  fleet.battleship = battleship;
  fleet.submarine = submarine;
  fleet.destroyer = destroyer;
  game.fleet = fleet;
}

function position(shipName, tr, td, size) {
  var stemX, stemY, bowX, bowY;
  var trR = 0;
  var tdR = 0;
  var validate = true;
  var i;
  var j;

  if (document.getElementById(tr + "," + td).style.backgroundColor === 'rgb(255, 255, 255)') {
    document.getElementById(tr + "," + td).style.backgroundColor = "#f44336";
    if ((tr + (size - 1)) < 10) {
      for (i = tr; i <= tr + (size - 1); i++) {
        if (document.getElementById(i + "," + td).style.backgroundColor === 'rgb(26, 26, 26)') {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById((tr + (size - 1)) + "," + td).style.backgroundColor = "#DB3C30";
      }
    }
    validate = true;
    if ((td + (size - 1)) < 10) {
      for (i = td; i <= td + (size - 1); i++) {
        if (document.getElementById(tr + "," + i).style.backgroundColor === 'rgb(26, 26, 26)') {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById(tr + "," + (td + (size - 1))).style.backgroundColor = "#DB3C30";
      }
    }
    validate = true;
    if ((tr - (size - 1)) >= 0) {
      for (i = tr; i >= tr - (size - 1); i--) {
        if (document.getElementById(i + "," + td).style.backgroundColor === 'rgb(26, 26, 26)') {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById((tr - (size - 1)) + "," + td).style.backgroundColor = "#DB3C30";
      }
    }
    validate = true;
    if ((td - (size - 1)) >= 0) {
      for (i = td; i >= td - (size - 1); i--) {
        if (document.getElementById(tr + "," + i).style.backgroundColor === 'rgb(26, 26, 26)') {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById(tr + "," + (td - (size - 1))).style.backgroundColor = "#DB3C30";
      }
    }
    greyBackground();
  }
  else if (document.getElementById(tr + "," + td).style.backgroundColor === 'rgb(244, 67, 54)') {
    document.getElementById(tr + "," + td).style.backgroundColor = "#ffffff";
    shipOnGrid();
  }
  else if (document.getElementById(tr + "," + td).style.backgroundColor === 'rgb(219, 60, 48)') {
    document.getElementById(tr + "," + td).style.backgroundColor = "#1a1a1a";

    for (i = 0; i <= 9; i++) {
      for (j = 0; j <= 9; j++) {
        if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor === 'rgb(244, 67, 54)') {
          trR = i;
          tdR = j;
          document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = "#1a1a1a";
          break;
        }
      }
    }
    if (trR === tr) {
      if (tdR < td) {
        for (i = tdR; i <= td; i++) {
          document.getElementById("" + [tr] + "," + [i] + "").style.backgroundColor = "#1a1a1a";
          if (i === tdR) {
            stemX = tr;
            stemY = i;
          }
          if (i === td) {
            bowX = tr;
            bowY = i;
          }
        }
      }
      if (td < tdR) {
        for (i = td; i <= tdR; i++) {
          document.getElementById("" + [tr] + "," + [i] + "").style.backgroundColor = "#1a1a1a";
          if (i === tdR) {
            bowX = tr;
            bowY = i;
          }
          if (i === td) {
            stemX = tr;
            stemY = i;
          }
        }
      }
    }
    if (tdR === td) {
      if (trR < tr) {
        for (i = trR; i <= tr; i++) {
          document.getElementById("" + [i] + "," + [td] + "").style.backgroundColor = "#1a1a1a";
          if (i === trR) {
            stemX = i;
            stemY = td;
          }
          if (i === tr) {
            bowX = i;
            bowY = td;
          }
        }
      }
      if (tr < trR) {
        for (i = tr; i <= trR; i++) {
          document.getElementById("" + [i] + "," + [td] + "").style.backgroundColor = "#1a1a1a";
          if (i === trR) {
            bowX = i;
            bowY = td;
          }
          if (i === tr) {
            stemX = i;
            stemY = td;
          }
        }
      }
    }
    shipOnGrid();
    idShip = idShip - 1;
  }
  return {stem:{x:stemX,y:stemY },bow:{x:bowX,y:bowY},size:size};
}