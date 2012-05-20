function addDataForEnvoye(data)
{
	for(var i=0; i< data.length; i++){
		element = $("#listTemplate").render( data[i] );
		$("#listEnvoye").append(element);		
	}
	$("#listEnvoye").listview('refresh');
}

function addDataForRec(data)
{
	for(var i=0; i<data.length; i++){
		element = $("#listTemplate").render( data[i] );
		$("#listRec").append(element);		
	}
	$("#listRec").listview('refresh');
}

function removeAllRec()
{
	$("#listRec").children().remove('li');
}

function removeAll()
{
	$("#listEnvoye").children().remove('li');
}
$(document).ready( function() {
	//addDataForEnvoye("Yang", "1er rue de la Defense", "- Le 12 Mars 2012: Recu par la poste");
});
	