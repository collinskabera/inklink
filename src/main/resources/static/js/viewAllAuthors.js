$(getAllAuthors);
		
function getAllAuthors(){
	let url = "/api/viewAllAuthors";
	
	//Do not use GET, use POST
	postIt(url, {}, processAllAuthors);
}

function processAllAuthors(response){

	if(response.status=="Success"){
		let authors = response.data;
		let contentDiv = $("#divContent");
		
		let str = "";
		
		for(let i=0;i<authors.length;i++){
			let colorStr = "#"+getColorNumber()+getColorNumber()+getColorNumber();
			
			let divStr = `<div class="col-2">
				<div class="ik-author-label text-center" style="background-color: ${colorStr}">
					<span>${authors[i].name}</span>
				</div>
			</div>`;

			
			str+=divStr;
		}
		
		contentDiv.html(str);
		
	}else{
		
	}
}

function getColorNumber(){
	let num = Math.ceil(Math.random()*51);
	if(num<10){
		return "0"+num;
	}else{
		return ""+num;
	}
}