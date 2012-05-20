
function addDataForTodo(data)
{
	for(var i=0; i< data.length; i++){
		element = $("#listTemplate").render( data[i] );
		$("#listTodo").append(element);		
	}
	$("#listTodo").listview('refresh');
}

function addDataForDone(data)
{
	for(var i=0; i< data.length; i++){
		element = $("#listTemplate").render( data[i] );
		$("#listDone").append(element);		
	}
	$("#listDone").listview('refresh');
}

$(document).ready( function() {
	addDataForTodo([{"nameReceiver": "David", "address": "1er rue de la Defense"},
					{"nameReceiver": "Etienne", "address": "5er rue de la Fargue"}]);
	//addDataForTodo("Yang", "1er rue de la Defense", "- Le 12 Mars 2012: Recu par la poste");
	
	//addDataForDone("Yang", "1er rue de la Defense", "- Le 12 Mars 2012: Recu par la poste");
});


realValue = "";
$(document).ready(function() {
	$('input[type="button"]').live('click',function() {
		var input = $(this).val();
		if( parseInt(input) >= 0 && parseInt(input) <=9 ){
			if( $("#typeMessage")[0].innerHTML.length < 4 ){
				$("#typeMessage")[0].innerHTML += "*";
				realValue += $(this).val();
			}else{
				alert( realValue );
			}
		}else{
			if( input == "Delete" ){
				$("#typeMessage").empty();
				realValue = "";
			}
		}
	});
});

pool = [1,2,3,4,5,6,7,8,9,0];
function consumeRandomValueFromPool(){
	if( pool.length > 0 ){
		var randomPosition = Math.floor( Math.random() * pool.length );
		var value = pool[randomPosition];
		pool.splice(randomPosition, 1);
		return value;
	}
	
}

$(document).ready( function() {
	$.each( $('input[type="button"]'), function (){
		var input = $(this).val();
		if( parseInt(input) >= 0 && parseInt(input) <=9 ){
			$(this).attr("value", consumeRandomValueFromPool());
		}
		
		$(this).addClass('ui-icon-check').removeClass('ui-icon-delete');
	});
	$('input[type="button"]').button('refresh');
});