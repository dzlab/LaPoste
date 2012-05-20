function addPin()
{
	var pinIcon = new CM.Icon(); 
	pinIcon.image = "http://icons.iconarchive.com/icons/designkindle/build/32/Pin-icon.png"
	var pinLocation = new CM.LatLng(48.985850, 1.668205);
	var pinMarker = new CM.Marker(truckLocation, {
		title: "Delivery address",
		icon: pinIcon
	}); 
	map.addOverlay(pinMarker);
}

function initMap()
{
    cloudmade = new CM.Tiles.CloudMade.Web({key: '8ee2a50541944fb9bcedded5165f09d9'});
    map = new CM.Map('cm-example', cloudmade);    
    //map.setCenter(new CM.LatLng(51.514, -0.137), 15);
    
    var TruckIcon = new CM.Icon();
    //TruckIcon.image = "http://icons.iconarchive.com/icons/icons-land/transport/48/Truck-icon.png";
    TruckIcon.image = "C:\\PhD\\workspace\\SupV\\res\\truck.png";
    
    var truckLocation = new CM.LatLng(48.985850, 1.668205);
    var truckMarker = new CM.Marker(truckLocation, {
    	title: "Delivery truck current location",
    	icon: TruckIcon
    });
    var departLocation = new CM.LatLng(48.822618, 2.269685);
    var departMarker = new CM.Marker(departLocation, {title: "Delivery truck departure location"});    
    
    var arrivalLocation = new CM.LatLng(49.178926, -0.392804);
    var arrivalMarker = new CM.Marker(arrivalLocation, {title: "Delivery truck arrival location"});
    
	var centerLocation = new CM.LatLng(49.027063, 1.148071);
	map.setCenter(centerLocation, 8);
	map.addOverlay(truckMarker);
	map.addOverlay(departMarker);
	map.addOverlay(arrivalMarker);
    
    var topRight = new CM.ControlPosition(CM.TOP_RIGHT, new CM.Size(50, 20));
    map.addControl(new CM.SmallMapControl(), topRight);
    map.addControl(new CM.LargeMapControl());
    map.addControl(new CM.ScaleControl());
    map.addControl(new CM.OverviewMapControl());
}

function addDataForEnvoye(name, address, status)
{
	data = {"name": name, 
			 "status": status};
	element = $("#listTemplate").render( data );
	$("#listEnvoye").append(element);
	$("#listEnvoye").listview('refresh');
}

$(document).ready( function() {
	addDataForEnvoye("Yang", "1er rue de la Defense", "- Le 12 Mars 2012: Recu par la poste");
})

function appelerCodeNatif(ecran, action, params) {
	//Construction de l'URL
 var url = 'laposte://client/' + ecran + '/'; // A changer en fonction de l'application
 var query = '';
 var fragment = '$' + action + '/';
 for (param in params) {
     var separator = '|';
     query = query + param + "=" + params[param] + separator;
 }
	
 // Appel du code natif
 window.location = url + query + fragment;
}