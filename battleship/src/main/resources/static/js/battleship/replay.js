const postReplay = () =>{
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
  $("#data").val(json);
  $("#replay-form-submit").click();
};


$(document).ready(function() {
  postReplay();
});
