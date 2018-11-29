/*GLOBAL VARS*/
const {
  ID,
  SIZE,
  NAME,
  P1,
  P2,
  CARRIER,
  SUBMARINE,
  DESTROYER,
  CRUISER,
  BATTLESHIP,
  MAP,
  ENNEMYBOARD,
  PLAYERBOARD,
  SHIP_REM,
  AI,
  REC,
  STEMX,
  STEMY,
  BOWX,
  BOWY,
  WIDTH,
  HEIGHT,
  STATE,
  DIFF,
  START_POS,
  HOME_URL
} = constants;

var game = {
  id: 1,
  name:'battleship',
  playerOne:{
    name:'playerOne',
    id:1,
    fleet: {
      carrier: {stem: {x: 0, y: 0}, bow: {x: 0, y: 4}, size: 5},
      destroyer: {stem: {x: 1, y: 0}, bow: {x: 1, y: 1}, size: 2},
      battleship: {stem: {x: 2, y: 0}, bow: {x: 2, y: 3}, size: 3},
      cruiser: {stem: {x: 3, y: 0}, bow: {x: 3, y: 2}, size: 3},
      submarine: {stem: {x: 4, y: 0}, bow: {x: 4, y: 3}, size: 4}
    },
    map:null,
    target:{
      x:0,
      y:0,
    },
    shipsRemaining:17,
  },
  playerTwo:{
    name:'playerTwo',
    id:2,
    fleet: {
      carrier: {stem: {x: 0, y: 0}, bow: {x: 0, y: 4}, size: 5},
      destroyer: {stem: {x: 1, y: 0}, bow: {x: 1, y: 1}, size: 2},
      battleship: {stem: {x: 2, y: 0}, bow: {x: 2, y: 3}, size: 3},
      cruiser: {stem: {x: 3, y: 0}, bow: {x: 3, y: 2}, size: 3},
      submarine: {stem: {x: 4, y: 0}, bow: {x: 4, y: 3}, size: 4}
    },
    map:null,
    shipsRemaining:17,
  },
  ai:{
    state:'START',
    difficulty:false,
    startPosition:null,
  },
  recorder:{
    playerOneMoves:null,
    playerTwoMoves:null,
    index:0,
  },
};

const postRestart = () =>{
  const playerOneMoves = $("#recorder-playerOneMoves").val();
  const playerTwoMoves = $("#recorder-playerTwoMoves").val();
  const name = BATTLESHIP;
  const index = 0;
  const data = {
    name,
    playerOneMoves,
    playerTwoMoves,
    index,
  };
  const json = JSON.stringify(data);
  console.log(json);
  $("#data").val(json);
  $("#restart-form-submit").click();
};




