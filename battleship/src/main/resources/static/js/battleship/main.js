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
  HOME_URL,
  MEMENTO,
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
  memento:{
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
  },
};


// TODO possibile to reunite many js file in the main
// TODO same applies with css
// TODO add a getData function
/*GAME RELATED*/

const torpedo = (event, x, y) =>{
  event.preventDefault();

  const shipNames = [CARRIER, BATTLESHIP, CRUISER, DESTROYER, SUBMARINE];
  const players = [P1, P2];
  players.forEach((player)=>{
    shipNames.forEach((ship)=>{
      game[player].fleet[ship].bow.x = $(`#${player}-${ship}-${BOWX}`).val();
      game[player].fleet[ship].bow.y = $(`#${player}-${ship}-${BOWY}`).val();
      game[player].fleet[ship].stem.x = $(`#${player}-${ship}-${STEMX}`).val();
      game[player].fleet[ship].stem.y = $(`#${player}-${ship}-${STEMY}`).val();

      game.memento[player].fleet[ship].bow.x = $(`#${MEMENTO}-${player}-${ship}-${BOWX}`).val();
      game.memento[player].fleet[ship].bow.y = $(`#${MEMENTO}-${player}-${ship}-${BOWY}`).val();
      game.memento[player].fleet[ship].stem.x = $(`#${MEMENTO}-${player}-${ship}-${STEMX}`).val();
      game.memento[player].fleet[ship].stem.y = $(`#${MEMENTO}-${player}-${ship}-${STEMY}`).val();
    });
    game[player].map = $(`#${player}-${PLAYERBOARD}-${MAP}`).val();
    game[player].shipsRemaining = $(`#${player}-${SHIP_REM}`).val();

    game.memento[player].map = $(`#${MEMENTO}-${player}-${PLAYERBOARD}-${MAP}`).val();
    game.memento[player].shipsRemaining = $(`#${MEMENTO}-${player}-${SHIP_REM}`).val();
  });
  const playerOneRecorder = $("#recorder-playerOneMoves").val();
  const playerTwoRecorder = $("#recorder-playerTwoMoves").val();
  const gameID = $("#game-id").val();
  const aiState = $("#ai-state").val();
  const aiDifficulty = $("#ai-difficulty").val();
  const aiStartPos = $("#ai-startPosition").val();
  const recorderIndex = $("#recorder-index").val();

  game.id = Number(gameID);
  game.recorder.playerOneMoves = playerOneRecorder;
  game.recorder.playerTwoMoves = playerTwoRecorder;
  game.recorder.index = Number(recorderIndex);
  game.ai.state = aiState;
  game.ai.difficulty = aiDifficulty;
  game.ai.startPosition = aiStartPos;
  game.playerOne.target.x = x;
  game.playerOne.target.y = y;


  game.memento.id = Number(gameID);
  game.memento.recorder.playerOneMoves = playerOneRecorder;
  game.memento.recorder.playerTwoMoves = playerTwoRecorder;
  game.memento.recorder.index = Number(recorderIndex);
  game.memento.ai.state = aiState;
  game.memento.ai.difficulty = aiDifficulty;
  game.memento.ai.startPosition =aiStartPos;

  const json = JSON.stringify(game);
  console.log(json);
  $("#data").val(json);
  $("#submit-play-button").click();
};

const sendGameID = ()=>{
  const id = $('#game-id').val();
  const url = `${HOME_URL}/load/${id}`;
  location.href = url;
};

const save = () => {
  const shipNames = [CARRIER, BATTLESHIP, CRUISER, DESTROYER, SUBMARINE];
  const players = [P1, P2];
  players.forEach((player)=>{
    shipNames.forEach((ship)=>{
      game[player].fleet[ship].bow.x = $(`#${player}-${ship}-${BOWX}`).val();
      game[player].fleet[ship].bow.y = $(`#${player}-${ship}-${BOWY}`).val();
      game[player].fleet[ship].stem.x = $(`#${player}-${ship}-${STEMX}`).val();
      game[player].fleet[ship].stem.y = $(`#${player}-${ship}-${STEMY}`).val();

      game.memento[player].fleet[ship].bow.x = $(`#${MEMENTO}-${player}-${ship}-${BOWX}`).val();
      game.memento[player].fleet[ship].bow.y = $(`#${MEMENTO}-${player}-${ship}-${BOWY}`).val();
      game.memento[player].fleet[ship].stem.x = $(`#${MEMENTO}-${player}-${ship}-${STEMX}`).val();
      game.memento[player].fleet[ship].stem.y = $(`#${MEMENTO}-${player}-${ship}-${STEMY}`).val();
    });
    game[player].map = $(`#${player}-${PLAYERBOARD}-${MAP}`).val();
    game[player].shipsRemaining = $(`#${player}-${SHIP_REM}`).val();

    game.memento[player].map = $(`#${MEMENTO}-${player}-${PLAYERBOARD}-${MAP}`).val();
    game.memento[player].shipsRemaining = $(`#${MEMENTO}-${player}-${SHIP_REM}`).val();
  });
  const playerOneRecorder = $("#recorder-playerOneMoves").val();
  const playerTwoRecorder = $("#recorder-playerTwoMoves").val();
  const gameID = $("#game-id").val();
  const aiState = $("#ai-state").val();
  const aiDifficulty = $("#ai-difficulty").val();
  const aiStartPos = $("#ai-startPosition").val();
  const recorderIndex = $("#recorder-index").val();

  game.id = Number(gameID);
  game.recorder.playerOneMoves = playerOneRecorder;
  game.recorder.playerTwoMoves = playerTwoRecorder;
  game.recorder.index = Number(recorderIndex);
  game.ai.state = aiState;
  game.ai.difficulty = aiDifficulty;
  game.ai.startPosition = aiStartPos;

  game.memento.id = Number(gameID);
  game.memento.recorder.playerOneMoves = playerOneRecorder;
  game.memento.recorder.playerTwoMoves = playerTwoRecorder;
  game.memento.recorder.index = Number(recorderIndex);
  game.memento.ai.state = aiState;
  game.memento.ai.difficulty = aiDifficulty;
  game.memento.ai.startPosition =aiStartPos;

  const json = JSON.stringify(game);
  console.log(json);
  $("#save-data").val(json);
  $("#submit-save-button").click();
};

