var login = () => {
	console.log("yes");
	$.post('localhost:8080/login',{username: 'hello', password: 'world'}, (data) =>{
		console.log(data);
	});
}