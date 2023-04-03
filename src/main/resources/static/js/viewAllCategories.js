$(getAllCategories);
		
function getAllCategories(){
	let url = "/api/viewAllCategories";
	
	//Do not use GET, use POST
	postIt(url, {}, processAllCategories);
}

function processAllCategories(response){

	if(response.status=="Success"){
		let categories = response.data;
		let contentDiv = $("#divContent");
		
		let str = "";
		
		for(let i=0;i<categories.length;i++){
			let colorStr = "#"+getColorNumber()+getColorNumber()+getColorNumber();
			
			let divStr = `<div class="col-2">
				<div class="ik-category-label text-center" style="background-color: ${colorStr}">
					<span>${categories[i].name}</span>
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