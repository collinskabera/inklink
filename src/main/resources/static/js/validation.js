function validateUsername(username, arr){
	if(username==null||username===""){
		arr.push("Username cannot be empty.");
	}
	else if(username.length<3){
		arr.push("Username is too short. Minimum is 3 characters.");
	}
	else if(username.length>16){
		arr.push("Username is too long. Maximum is 16 characters.");
	}
}

function validatePassword(pwd, arr){
	if(pwd==null||pwd===""){
		arr.push("Password cannot be empty.");
	}
	else if(pwd.length<5){
		arr.push("Password must have 5 or more characters. Yours has "+pwd.length+".");
	}
	else if(pwd.length>20){
		arr.push("Password length has exceeded limit which is 20. Yours has "+pwd.length+".");
	}
}

function validateFirstName(name, arr){
	if(name==""){
		
	}else{
		if(name.length<=3){
			arr.push("First name must include at minium, 3 characters.");
		}
	}
}

function validateLastName(name, arr){
	if(name===""){
		
	}else{
		if(name.length<=3){
			arr.push("Last name must include at minium, 3 characters.");
		}
	}
}

function validateEmailAddress(email, erm){
	if(email===""){
		return;
	}else{
		//do email validation since some browsers do not understand HTML input type or the browser does not know how to validate an email address
	}
}

function validateBookId(id, arr){
	if(id==null||id==""){
		arr.push("Book ID cannot be empty.");
	}else{
		try{
			let num = parseInt(id);
			if(num<=0){
				arr.push("Book ID should be greater than 0.");
			}
		}catch(e){
			arr.push("Book ID should be a positive integer.");
		}
	}
}

function validateBookName(name, arr){
	if(name==null||name===""){
		arr.push("The name of the book cannot be empty.");
	}
}

function validateCover(cover, arr){
	if(cover===null||cover===undefined){
		arr.push("Please select a cover for the book.");
	}else{
		let size = cover.size;
		const MAX_BOOK_COVER_FILE_SIZE = 4 * 1024 * 1024;
		if(size>MAX_BOOK_COVER_FILE_SIZE){
			arr.push("The image you have selected exceeds the size limit which is 4MB.");
		}
		
		let coverName = cover.name;
		let indexDot = coverName.lastIndexOf(".");
		if(indexDot<0){
			arr.push("Select a valid image format.");
		}
	}
}

function validateBookFile(bookFile, arr){
	if(bookFile===null||bookFile===undefined){
		arr.push("Please select book file.");
	}else{
		let size = bookFile.size;
		/*const MAX_BOOK_FILE_SIZE = 4 * 1024 * 1024;
		if(size>MAX_BOOK_FILE_SIZE){
			arr.push("The image you have selected exceeds the size limit which is 4MB.");
		}*/
		
		let bookFileName = bookFile.name;
		let indexDot = bookFileName.lastIndexOf(".");
		if(indexDot<0){
			arr.push("Select a valid file for the book.");
		}
	}
}

function validateDescription(description, arr){
	if(description==null||description===""){
		arr.push("The description cannot be empty.");
	}
	else if(description.length<20){
		arr.push("Book description must have 20 or more characters. Current count is "+description.length+".");
	}
}

function validateBookPrice(price, arr){
	if(price==null||price===""){
		arr.push("Book price cannot be empty.");
	}
	else{
		try{
			let bp = new Number(price);
			if(bp<0){
				arr.push("Book price cannot be a negative number.");
			}
			else if(bp>10000){
				arr.push("Book price is too high. Maximum price is set to 10,000.");
			}
		}catch(err){
			arr.push("Use numbers to specify book price.");
		}
	}
}

function validatePublisherName(publisher, arr){
	if(publisher==null||publisher===""){
		arr.push("Publisher name cannot be empty.");
	}
}

function validatePublicationYear(puby, arr){
	if(puby==null||puby===""){
		arr.push("Publication year cannot be empty.");
	}
	else{
		try{
			let year = parseInt(new Number(puby));

			let currentYear = (new Date).getFullYear();

			if(year<1600){
				arr.push("Publication year is below the 1600 limit.");
			}
			else if(year>currentYear){
				arr.push("The publication year you have entered is greater than the current year: "+currentYear+".");
			}
		}catch(err){
			arr.push("Use natural numbers to publication year.");
		}
	}
}

function validateEditionNumber(edition, arr){
	if(edition==null||edition===""){
		arr.push("Edition number cannot be empty.");
	}
	else{
		try{
			let ed = parseInt(new Number(edition));

			if(ed<=0){
				arr.push("Edition number cannot be 0 or a negative number.");
			}
			else if(ed>1000){
				arr.push("Edition number is too high. Maximum price is set to 1000.");
			}
		}catch(err){
			arr.push("Use natural numbers to edition number.");
		}
	}
}

function validatePublicationCountry(country, arr){
	if(country==null||country===""){
		arr.push("Publication country cannot be empty.");
	}
}

function validateIsbn(isbn, arr){
	if(isbn==null||isbn===""){
		arr.push("ISBN cannot be empty.");
	}
}

function validateLanguage(language, arr){
	if(language==null||language===""){
		arr.push("The language the book is written in cannot be empty.");
	}
}

function validateAuthors(authors, arr){
	if(authors.length==0){
		arr.push("You need to have one or more authors for the book.");
	}else{
		for(let i=0;i<authors.length;i++){
			if(authors[i].length<3){
				arr.push("Author "+(i+1)+" error: Author's name must have 3 or more characters.");
			}
		}
	}
}

function validateCategories(categories, arr){
	if(categories.length==0){
		arr.push("Specify at least one category to which this book belongs.")
	}else{
		for(let i=0;i<categories.length;i++){
			if(categories[i].length<3){
				arr.push("Category "+(i+1)+" error: Category name should have 3 or more characters.");
			}
		}
	}
}

function validateBookWidth(wid, arr){
	if(wid==null||wid===""){
		arr.push("Book width cannot be empty.");
	}
	else{
		try{
			let width = new Number(wid);
			if(width<=0){
				arr.push("Book width cannot be 0 or a negative number.");
			}
			else if(width>1000){
				arr.push("Book width is too big. Maximum width is set to 1,000mm.");
			}
		}catch(err){
			arr.push("Use numbers to specify book width.");
		}
	}
}

function validateBookHeight(hei, arr){
	if(hei==null||hei===""){
		arr.push("Book height cannot be empty.");
	}
	else{
		try{
			let height = new Number(hei);
			if(height<=0){
				arr.push("Book height cannot be 0 or a negative number.");
			}
			else if(height>1000){
				arr.push("Book height is too big. Maximum height is set to 1,000mm.");
			}
		}catch(err){
			arr.push("Use numbers to specify book height.");
		}
	}
}

function validateNumberOfPages(nop, arr){
	if(nop==null||nop===""){
		arr.push("Number of pages cannot be empty.");
	}
	else{
		try{
			let numberOfPages = parseInt(new Number(nop));

			if(numberOfPages<=0){
				arr.push("Number of pages cannot be 0 or a negative number.");
			}
			else if(numberOfPages>100000){
				arr.push("Number of pages is too high. Maximum price is set to 100,000.");
			}
		}catch(err){
			arr.push("Use natural numbers to specify book price.");
		}
	}
}

function validatePasswordsEqual(pwd, conf, erm){
	if(pwd!==conf){
		erm.push("The two passwords you entered did not match. They should be the same.");
	}
}

function validatePhoneNumber(phone, erm){
	if(phone==null || phone == ""){
		return;
	}
}

function validatePhoneNumberMust(phone, erm){
	if(phone==null || phone==""){
		erm.push("You must enter a phone number.");
	}
	//Include rege for validating phone number.
}

function validate(b, arr){
	if(b==null||b===""){
		arr.push(" cannot be empty.");
	}
}
