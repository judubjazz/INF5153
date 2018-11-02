var submarineRotated = false;
var destroyerRotated = false;
var cruiserRotated = false;
var battleshipRotated = false;
var carrierRotated = false;

function rotateShip(id){
  console.log(id);
  let degree = 0;
  const element = document.getElementById(id);
  switch (id) {
    case 'submarine':
      degree = submarineRotated? 90 : 0;
      submarineRotated = !submarineRotated;
      break;
    case 'destroyer':
      degree = destroyerRotated? 90 : 0;
      destroyerRotated = !destroyerRotated;
      break;
    case 'cruiser':
      degree = cruiserRotated? 90 : 0;
      cruiserRotated = !cruiserRotated;
      break;
    case 'battleship':
      degree = battleshipRotated? 90 : 0;
      battleshipRotated = !battleshipRotated;
      break;
    case 'carrier':
      degree = carrierRotated? 90 : 0;
      carrierRotated = !carrierRotated;
      break;
  }
  element.style.transform = `rotate(${degree}deg)`;
}

function allowDrop(ev) {
  ev.preventDefault();
  ev.dataTransfer.dropEffect = "move"
}

function drag(ev) {
  console.log(ev);
  console.log(ev.target.id);
  ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  console.log('drop', ev);
  var data = ev.dataTransfer.getData("text");
  ev.target.appendChild(document.getElementById(data));
}



function handleResponse(response) {
  if (!response.ok) {
    return Promise.reject(response.statusText);
  }
  console.log('response', response);
  return response._bodyText;
}

function torpedo(x, y){
  const torpedo = {
    x,
    y,
  };
  $(".torpedo-form-data").eq(0).html(JSON.stringify(torpedo));
  $(".torpedo-form-submit").eq(0).click();
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
  // // TODO draw canvases
}

