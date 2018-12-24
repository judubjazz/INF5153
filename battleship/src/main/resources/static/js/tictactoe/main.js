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
  name:'tictactoe',
  playerOne:{
    name:'playerOne',
    id:1,
    marks:null,
    map:null,
    sign:1,
    target:{
      x:0,
      y:0,
    },
  },
  playerTwo:{
    name:'playerTwo',
    id:2,
    sign:-1,
    marks:null,
    map:null,
  },
  ai:{
    state:'START',
    strategy:null,
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
    name:'tictactoe',
    playerOne:{
      name:'playerOne',
      id:1,
      sign:1,
      marks:null,
      map:null,
      target:{
        x:0,
        y:0,
      },
    },
    playerTwo:{
      name:'playerTwo',
      id:2,
      sign:-1,
      marks:null,
      map:null,
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


const start = () => {
  const difficulty = $('#difficulty').val();
  data = {
    name:'tictactoe',
    difficulty,
  };
  const json = JSON.stringify(data);
  $('#data').val(json);
  $('#submit-fleet-button').click();
};



const playTurn = (event, x, y) =>{
  event.preventDefault();
  setData();
  game.playerOne.target.x = Number(x);
  game.playerOne.target.y = Number(y);
  const json = JSON.stringify(game);
  console.log(json);
  $("#data").val(json);
  $("#submit-play-button").click();
};


const postRestart = () =>{
  setData();
  const json = JSON.stringify(game);
  console.log(json);
  $("#data").val(json);
  $("#restart-form-submit").click();
};


const setData = () => {
  const players = [P1, P2];
  players.forEach((player)=>{
    game[player].map = $(`#${player}-${PLAYERBOARD}-${MAP}`).val();
    game.memento[player].map = $(`#${MEMENTO}-${player}-${PLAYERBOARD}-${MAP}`).val();
  });

  const playerOneRecorder = $("#recorder-playerOneMoves").val();
  const playerTwoRecorder = $("#recorder-playerTwoMoves").val();
  const gameID = $("#game-id").val();
  const aiState = $("#ai-state").val();
  const aiDifficulty = $("#ai-difficulty").val();
  const aiStartPos = $("#ai-startPosition").val();
  const aiStrategy = $("#ai-strategy").val();
  game.id = Number(gameID);
  game.recorder.playerOneMoves = playerOneRecorder;
  game.recorder.playerTwoMoves = playerTwoRecorder;
  game.ai.state = aiState;
  game.ai.difficulty = aiDifficulty;
  game.ai.startPosition = aiStartPos;
  game.ai.strategy = aiStrategy;

  game.memento.id = Number(gameID);
  game.memento.recorder.playerOneMoves = playerOneRecorder;
  game.memento.recorder.playerTwoMoves = playerTwoRecorder;
  game.memento.ai.state = aiState;
  game.memento.ai.difficulty = aiDifficulty;
  game.memento.ai.startPosition =aiStartPos;
};