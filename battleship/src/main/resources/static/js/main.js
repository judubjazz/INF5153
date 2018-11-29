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

var fleet = {
  carrier:   {stem:{x:0,y:0},bow:{x:0,y:4}, size:5, rotated:false},
  destroyer: {stem:{x:1,y:0},bow:{x:1,y:1}, size:3, rotated:false},
  battleship:{stem:{x:2,y:0},bow:{x:2,y:3}, size:4, rotated:false},
  cruiser:   {stem:{x:3,y:0},bow:{x:3,y:2}, size:3, rotated:false},
  submarine: {stem:{x:4,y:0},bow:{x:4,y:3}, size:2, rotated:false},
};

/*DRAGGABLE DIVS*/
function allowDrop(event) {
  event.preventDefault();
  event.dataTransfer.dropEffect = "move";
}

function drag(event) {
  event.dataTransfer.setData("text", event.target.id);
}

function drop(event) {
  event.preventDefault();
  let data = event.dataTransfer.getData("text");
  event.target.appendChild(document.getElementById(data));
}


/*GAME RELATED*/
function startGame(){
  setFleetData();
  triggerHiddenSubmit();
}

function setDifficulty(event){
  event.target.value === 'hard' ? gameSettings.difficulty = true : gameSettings.difficulty = false;
}

/*SHIP RELATED*/
function rotateShip(id){
  console.log(id);
  let degree = 0;
  let halfTileLength = -20;
  const element = document.getElementById(id);
  fleet[id].rotated = !fleet[id].rotated;
  degree = fleet[id].rotated? 90 : 0;
  element.style.transform = `rotate(${degree}deg)`;
}

function torpedo(event, x, y){
  event.preventDefault();

  const shipNames = [CARRIER, BATTLESHIP, CRUISER, DESTROYER, SUBMARINE];
  const players = [P1, P2];
  players.forEach((player)=>{
    shipNames.forEach((ship)=>{
      game[player].fleet[ship].bow.x = $(`#${player}-${ship}-${BOWX}`).val();
      game[player].fleet[ship].bow.y = $(`#${player}-${ship}-${BOWY}`).val();
      game[player].fleet[ship].stem.x = $(`#${player}-${ship}-${STEMX}`).val();
      game[player].fleet[ship].stem.y = $(`#${player}-${ship}-${STEMY}`).val();
    });
    game[player].map = $(`#${player}-${PLAYERBOARD}-${MAP}`).val();
    game[player].shipsRemaining = $(`#${player}-${SHIP_REM}`).val();
  });
  const playerOneRecorder = $("#recorder-playerOneMoves").val();
  const playerTwoRecorder = $("#recorder-playerTwoMoves").val();
  game.recorder.playerOneMoves = playerOneRecorder;
  game.recorder.playerTwoMoves = playerTwoRecorder;
  game.ai.state = $("#ai-state").val();
  game.ai.difficulty = $("#ai-difficulty").val();
  game.ai.startPosition =$("#ai-startPosition").val();
  game.playerOne.target.x = x;
  game.playerOne.target.y = y;

  const json = JSON.stringify(game);
  console.log(json);
  $("#data").val(json);
  $("#submit-play-button").click();
}


const buildFleet = (shipName) =>{

};
function triggerHiddenSubmit(){
  gameSettings.fleet = fleet;
  const data = JSON.stringify(gameSettings);
  $("#fleet-data-input").val(data);
  $("#submit-fleet-button").click();
}

function setFleetData(){
  for(let x = 0; x < 10; ++x){
    for(let y = 0; y < 10; ++y){
      let divID = `${x},${y}`;
      let tiles = document.getElementById(divID);
      let childNodes = tiles.childNodes;
      if(childNodes.length){
        let ship = childNodes[0];
        let shipSize = fleet[ship.id].size;
        fleet[ship.id].stem.x = x;
        fleet[ship.id].stem.y = y;
        if(fleet[ship.id].rotated){
          fleet[ship.id].bow.x = x + shipSize ;
          fleet[ship.id].bow.y = y;
        } else {
          fleet[ship.id].bow.x = x;
          fleet[ship.id].bow.y = y + shipSize;
        }
      }
    }
  }
}



/*HTTP REQUEST*/
function handleResponse(response) {
  if (!response.ok) {
    return Promise.reject(response.statusText);
  }
  console.log('response', response);
  return response._bodyText;
}

function sendMove(options){
  const url = 'http://localhost:8090/start/';
  const params = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(options),
  };

  fetch(url, params);
  //   // .then(handleResponse);
  //   // .then(res => res);
}

function sendGameID(){
  const id = $('#game-id').val();
  const url = `${HOME_URL}/load/${id}`;
  location.href = url;
}

