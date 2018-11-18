'use strict';

const socket = io(window.location.protocol + '//localhost:9291');
const fleet = {
  "carrier":   {"stem":{"x":0,"y":0},"bow":{"x":0,"y":4}},
  "destroyer": {"stem":{"x":1,"y":0},"bow":{"x":1,"y":1}},
  "battleship":{"stem":{"x":2,"y":0},"bow":{"x":2,"y":3}},
  "cruiser":   {"stem":{"x":3,"y":0},"bow":{"x":3,"y":2}},
  "submarine": {"stem":{"x":4,"y":0},"bow":{"x":4,"y":3}}
};

function startGameVsHuman(){
  socket.emit('createGame', JSON.stringify(fleet));
  socket.on('toClient', (msg) =>{
    console.log(msg);
  });
}



socket.on('connect_failed', function(data)
{
  console.log('connect_failed');
});
socket.on('connecting', function(data)
{
  console.log('connecting');
});
socket.on('disconnect', function(data)
{
  console.log('disconnect');
});
socket.on('error', function(reason)
{
  console.log('error');
});
socket.on('reconnect_failed', function(data)
{
  console.log('reconnect_failed');
});
socket.on('reconnect', function(data)
{
  console.log('reconnect');
});
socket.on('reconnecting', function(data)
{
  console.log('reconnecting');
});

// $(function () {
//   var socket = io();
//   $('form').submit(function(){
//     socket.emit('chat message', $('#m').val());
//     $('#m').val('');
//     return false;
//   });
//   socket.on('chat message', function(msg){
//     $('#messages').append($('<li>').text(msg));
//   });
// });