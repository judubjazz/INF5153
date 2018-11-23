/*GLOBAL VARS*/
var gameSettings = {
  difficulty:false,
  fleet:null,
};

const home_url = 'http://localhost:8090';
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
  $("#targetX").val(x);
  $("#targetY").val(y);
  $("#torpedo-form-submit").click();
}

function triggerHiddenSubmit(){
  const fleetStringified = JSON.stringify(fleet);
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
  const url = `${home_url}/load/${id}`;
  location.href = url;
}

