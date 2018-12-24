/* Initial values*/
let idShip = 5;
const black = 'rgb(26, 26, 26)';
const white = 'rgb(255, 255, 255)';
const grey = 'rgb(242, 242, 242)';
const redError = 'rgb(244, 67, 54)';
const redShipChooser = 'rgb(219, 60, 48)';
let data = {
  difficulty: false,
  name : 'battleship',
  fleet: {
    carrier: {stem: {x: 0, y: 0}, bow: {x: 0, y: 4}, size: 5},
    destroyer: {stem: {x: 1, y: 0}, bow: {x: 1, y: 1}, size: 2},
    battleship: {stem: {x: 2, y: 0}, bow: {x: 2, y: 3}, size: 3},
    cruiser: {stem: {x: 3, y: 0}, bow: {x: 3, y: 2}, size: 3},
    submarine: {stem: {x: 4, y: 0}, bow: {x: 4, y: 3}, size: 4}
  },
};

/*all white background*/
function whiteBackground() {
  for (let i = 0; i <= 9; i++) {
    for (let j = 0; j <= 9; j++) {
      document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = white;
    }
  }
}

/*grey background except black or red cases*/
function greyBackground() {
  for (let i = 0; i <= 9; i++) {
    for (let j = 0; j <= 9; j++) {
      if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != redError
        && document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != redShipChooser
        && document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != black) {
        document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = grey;
      }
    }
  }
}

/*grey background except black or red cases*/
function shipOnGrid() {
  for (let i = 0; i <= 9; i++) {
    for (let j = 0; j <= 9; j++) {
      if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor != black) {
        document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = white;
      }
    }
  }
}

/*verified if ships coordinates or not empty or null*/

/*if empty, null or undefined. Attribute a new value*/
function validateShipsCoord(str, id) {
  if (!str || 0 === str.length) {
    switch (id) {
      case 5:
        let carrier = {stem:{x:0,y:0},bow:{x:4,y:0},size:5};
        fleet.carrier= carrier;
        break;
      case 4:
        let battleship = {stem:{x:0,y:1},bow:{x:3,y:1},size:4};
        fleet.battleship = battleship;
        break;
      case 3:
        let cruiser = {stem:{x:0,y:2},bow:{x:2,y:2},size:3};
        fleet.cruiser = cruiser;
        break;
      case 2:
        let submarine = {stem:{x:0,y:3},bow:{x:2,y:3},size:3};
        fleet.submarine = submarine;
        break;
      case 1:
        let destroyer = {stem:{x:0,y:4},bow:{x:1,y:4},size:2};
        fleet.destroyer = destroyer;
        break;
    }
  }
}

/*clean grid*/
whiteBackground();

/*All ships*/
function shipsChooser(coord) {
  let tr = coord.parentNode.rowIndex;
  let td = coord.cellIndex;

  if (idShip === 5) {
    data.fleet.carrier = position("carrier", tr, td, 5);
  }
  else if (idShip === 4) {
    data.fleet.battleship = position("battleship", tr, td, 4);
  }
  else if (idShip === 3) {
    data.fleet.cruiser = position("cruiser", tr, td, 3);
  }
  else if (idShip === 2) {
    data.fleet.submarine = position("submarine", tr, td, 3);
  }
  else if (idShip === 1) {
    data.fleet.destroyer = position("destroyer", tr, td, 2);
  }
}

function start() {
  if(idShip === 0){
    let difficulty =  document.getElementById("difficulty").value;
    data.difficulty = difficulty;
    document.getElementById("data").value = JSON.stringify(data);
    $('#submit-fleet-button').click();
  }
}


function position(shipName, tr, td, size) {

  let stemX, stemY, bowX, bowY;
  let trR = 0;
  let tdR = 0;
  let validate = true;

  if (document.getElementById(tr + "," + td).style.backgroundColor === white) {
    document.getElementById(tr + "," + td).style.backgroundColor = redError;
    if ((tr + (size - 1)) < 10) {
      for (let i = tr; i <= tr + (size - 1); i++) {
        if (document.getElementById(i + "," + td).style.backgroundColor === black) {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById((tr + (size - 1)) + "," + td).style.backgroundColor = redShipChooser;
      }
    }
    validate = true;
    if ((td + (size - 1)) < 10) {
      for (let i = td; i <= td + (size - 1); i++) {
        if (document.getElementById(tr + "," + i).style.backgroundColor === black) {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById(tr + "," + (td + (size - 1))).style.backgroundColor = redShipChooser;
      }
    }
    validate = true;
    if ((tr - (size - 1)) >= 0) {
      for (let i = tr; i >= tr - (size - 1); i--) {
        if (document.getElementById(i + "," + td).style.backgroundColor === black) {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById((tr - (size - 1)) + "," + td).style.backgroundColor = redShipChooser;
      }
    }
    validate = true;
    if ((td - (size - 1)) >= 0) {
      for (let i = td; i >= td - (size - 1); i--) {
        if (document.getElementById(tr + "," + i).style.backgroundColor === black) {
          validate = false;
          break;
        }
      }
      if (validate === true) {
        document.getElementById(tr + "," + (td - (size - 1))).style.backgroundColor = redShipChooser;
      }
    }
    greyBackground();
  }
  else if (document.getElementById(tr + "," + td).style.backgroundColor === redError) {
    document.getElementById(tr + "," + td).style.backgroundColor = white;
    shipOnGrid();
  }
  else if (document.getElementById(tr + "," + td).style.backgroundColor === redShipChooser) {
    document.getElementById(tr + "," + td).style.backgroundColor = black;

    for (let i = 0; i <= 9; i++) {
      for (let j = 0; j <= 9; j++) {
        if (document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor === redError) {
          trR = i;
          tdR = j;
          document.getElementById("" + [i] + "," + [j] + "").style.backgroundColor = black;
          break;
        }
      }
    }
    if (trR === tr) {
      if (tdR < td) {
        for (let i = tdR; i <= td; i++) {
          document.getElementById("" + [tr] + "," + [i] + "").style.backgroundColor = black;
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
        for (let i = td; i <= tdR; i++) {
          document.getElementById("" + [tr] + "," + [i] + "").style.backgroundColor = black;
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
        for (let i = trR; i <= tr; i++) {
          document.getElementById("" + [i] + "," + [td] + "").style.backgroundColor = black;
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
        for (let i = tr; i <= trR; i++) {
          document.getElementById("" + [i] + "," + [td] + "").style.backgroundColor = black;
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
    --idShip;
  }
  let ship = {
    stem:{x:stemX, y:stemY},
    bow:{x:bowX, y:bowY},
    size,
  };
  return ship;
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	