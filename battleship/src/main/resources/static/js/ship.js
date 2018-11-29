	var idShip = 5;
	var carrier = "";
	var battleship = "";
	var cruiser = "";
	var submarine = "";
	var destroyer = "";
    
	var data = "";

	/*all white background*/
	for(i = 0; i <= 9; i++){ 
		for(j = 0; j <= 9; j++){ 
			document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor = "#ffffff";
		}
	}

    function run() {
        return document.getElementById("ddlViewBy").value;
    }
    
	function myFunction(x) {
        var tr = x.parentNode.rowIndex;
        var td = x.cellIndex;  

        if(idShip == 5){
        	carrier = position("carrier", tr, td,5);
        }
        else if(idShip == 4){
        	battleship = position("battleship", tr, td,4);
        }
		else if(idShip == 3){
			cruiser = position("cruiser", tr, td,3);
		}
		else if(idShip == 2){
			submarine = position("submarine", tr, td,3);
		}
		 else if(idShip == 1){
			 destroyer = position("destroyer", tr, td,2);
		}



		data = '{"difficulty":'+ run() +','+
      '"name":"battleship",'+
	       '"fleet":{"carrier":{'+carrier+'},'+
	       '"cruiser":{'+cruiser+'},'+
	       '"battleship":{'+battleship+'},'+
	       '"submarine":{'+submarine+'},'+
	       '"destroyer":{'+destroyer+'}}}';          

       	//console.log(data);
    	document.getElementById("data").value = data;  
	}

	function position(shipName,tr, td, size) {

		var stemX, stemY, bowX, bowY;
	    var trR = 0;
	    var tdR = 0;

		if (document.getElementById(tr+"," +td).style.backgroundColor === 'rgb(255, 255, 255)') {
			document.getElementById(tr+"," +td).style.backgroundColor = "#f44336";
			if((tr+(size-1))<10){
				document.getElementById((tr+(size-1))+"," +td).style.backgroundColor = "#DB3C30";
			}
			if((td+(size-1))<10){
				document.getElementById((tr)+"," +(td+(size-1))).style.backgroundColor = "#DB3C30";
			}
			if((tr-(size-1))>=0){
				document.getElementById((tr-(size-1))+"," +(td)).style.backgroundColor = "#DB3C30";
			}	
			if((td-(size-1))>=0){
				document.getElementById((tr)+"," +(td-(size-1))).style.backgroundColor = "#DB3C30";
			}
			for(i = 0; i <= 9; i++){ 
				for(j = 0; j <= 9; j++){ 
					if(document.getElementById(""+[i]+"," +[j] +"") != document.getElementById(tr+"," +td)
					&& document.getElementById(""+[i]+"," +[j] +"") != document.getElementById((tr+(size-1))+"," +td)
					&& document.getElementById(""+[i]+"," +[j] +"") != document.getElementById((tr)+"," +(td+(size-1)))	
					&& document.getElementById(""+[i]+"," +[j] +"") != document.getElementById((tr-(size-1))+"," +(td))	
					&& document.getElementById(""+[i]+"," +[j] +"") != document.getElementById((tr)+"," +(td-(size-1)))
					&& document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor != 'rgb(26, 26, 26)'){
						document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor = "#f2f2f2";
					}
				}
			}
		}
		else if(document.getElementById(tr+"," +td).style.backgroundColor === 'rgb(244, 67, 54)'){
			document.getElementById(tr+"," +td).style.backgroundColor = "#ffffff";
			if((tr+(size-1))<10){
				document.getElementById((tr+(size-1))+"," +td).style.backgroundColor = "#ffffff";
			}
			if((td+(size-1))<10){
				document.getElementById((tr)+"," +(td+(size-1))).style.backgroundColor = "#ffffff";
			}
			if((tr-(size-1))>=0){
				document.getElementById((tr-(size-1))+"," +(td)).style.backgroundColor = "#ffffff";
			}	
			if((td-(size-1))>=0){
				document.getElementById((tr)+"," +(td-(size-1))).style.backgroundColor = "#ffffff";
			}
			for(i = 0; i <= 9; i++){ 
				for(j = 0; j <= 9; j++){ 
					document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor = "#ffffff";
				}
			}
		}		
		else if(document.getElementById(tr+"," +td).style.backgroundColor === 'rgb(219, 60, 48)'){
			document.getElementById(tr+"," +td).style.backgroundColor = "#1a1a1a";
			
			for(i = 0; i <= 9; i++){ 
				for(j = 0; j <= 9; j++){ 
					if(document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor === 'rgb(244, 67, 54)'){
						trR = i;
						tdR = j;
						document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor = "#1a1a1a";
						break;
					}
				}
			}
			if(trR == tr ){
				if(tdR < td){
					for(i = tdR; i <= td; i++){ 
						document.getElementById(""+[tr]+"," +[i] +"").style.backgroundColor = "#1a1a1a";
						if(i==tdR){
							stemX = tr;
							stemY = i;
						}
						if(i==td){
							bowX = tr;
							bowY = i;
						}		
					}
				}
				if(td < tdR){
					for(i = td; i <= tdR; i++){ 
						document.getElementById(""+[tr]+"," +[i] +"").style.backgroundColor = "#1a1a1a";
						if(i==tdR){
							bowX = tr;
							bowY = i;
						}
						if(i==td){
							stemX = tr;
							stemY = i;
						}
					}
				}
			}
			if(tdR == td ){
				if(trR < tr){
					for(i = trR; i <= tr; i++){ 
						document.getElementById(""+[i]+"," +[td] +"").style.backgroundColor = "#1a1a1a";
						if(i==trR){
							stemX = i;
							stemY = td;
						}
						if(i==tr){
							bowX = i;
							bowY = td;
						}
					}
				}
				if(tr < trR){
					for(i = tr; i <= trR; i++){ 
						document.getElementById(""+[i]+"," +[td] +"").style.backgroundColor = "#1a1a1a";
						if(i==trR){
							bowX = i;
							bowY = td;
						}
						if(i==tr){
							stemX = i;
							stemY = td;
						}
					}
				}
			}
			for(i = 0; i <= 9; i++){ 
				for(j = 0; j <= 9; j++){ 
					if(document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor != 'rgb(26, 26, 26)'){
						document.getElementById(""+[i]+"," +[j] +"").style.backgroundColor = "#ffffff"; 
					}
				}
			}	
			idShip = idShip - 1;
		}
	
		return '"stem":{"x":'+stemX+',"y":'+stemY+'},"bow":{"x":'+bowX+',"y":'+bowY+'},"size":'+size;     
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	