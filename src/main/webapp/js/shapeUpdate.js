let baseUrl = "http://localhost:8080/"
let classificationDropDown;
let importedShapes;

$(document).ready(function() {
    getOHDMInformation();
});


function getOHDMInformation()
{
    let xhr = new XMLHttpRequest();
    let url = baseUrl + "OHDMInformation";
    xhr.open("GET", url, true);

    xhr.onload = function (e)
    {
        if(this.status === 200)
        {
            let data = this.response;
            let dataObj = JSON.parse(data);

            console.log(dataObj);

            classificationDropDown = document.createElement("select");


            for(let i = 0; i < dataObj.length; i++)
            {
                let currentData = dataObj[i];
                let currentOption = document.createElement("option");

                currentOption.value = currentData.id;
                currentOption.text = currentData.subclassName;
                classificationDropDown.append(currentOption);
            }
        }
    };
    xhr.send();

}

function sendData()
{
    let key = $("#key").val();
    let xhr = new XMLHttpRequest();
    let url = baseUrl + "ShapeUpdate?tableKey="+key;

    xhr.open("GET", url, true);

    xhr.onload = function (e)
    {
        if(this.status === 200)
        {
            let data = this.response;
            let dataObj = JSON.parse(data);

            console.log(dataObj);
            $("#importedShapes").empty();
            displayImportedShapes(dataObj.importedShapes);
            importedShapes = dataObj.importedShapes;
        }
    };
    xhr.send();
}

function displayImportedShapes(importedShapes)
{
    for(let i = 0; i < importedShapes.length; i++)
    {
        let id = importedShapes[i].id;
        let name = importedShapes[i].name;
        let validSince, validUntil = "";
        if(importedShapes[i].validSince)
        {
            validSince = getDate(importedShapes[i].validSince);
        }

        if(importedShapes[i].validUntil)
        {
            validUntil = getDate(importedShapes[i].validUntil);
        }

        let classificationId = importedShapes[i].classificationId;
        let geoJson = importedShapes[i].geoJson;

        let shapeId = "Shape-" + id;
        let mapId = "map-"+shapeId;
        classificationDropDown.setAttribute("id", "class" + i);
        let importedShapeForm =
        $("<form id=\""+ i +"\">" +
            "<div class='form-group row'>" +
                "<label>ID</label>" +
                "<input type='text' readonly value=\""+id+"\"/>" +
            "</div>" +
            "<div class='form-group row'>" +
                "<label>Name</label>" +
                "<input type='text' value=\""+name+"\" id=\"name"+i+"\"/>" +
            "</div>" +
            "<div class='form-group row'>" +
                "<label>Gültig von...</label>" +
                "<input type='date' value=\""+validSince+"\" id=\"validSince"+i+"\"/>" +
            "</div>" +
            "<div class='form-group row'>" +
                "<label>Gültig bis...</label>" +
                "<input type='date' value=\""+validUntil+"\" id=\"validUntil"+i+"\"/>" +
            "</div>" +
            "<div class='form-group row'>" +
                "<label>Klassifizierung</label>" +
                classificationDropDown.outerHTML +
            "</div>" +
            "<div id=\""+mapId+"\" class=\"map\">"+

            "</div>"+
            "<button type=\"button\" class=\"btn btn-primary\" id=\"update"+ shapeId +"\">Update</button>" +
        "</form><hr>");

        $("#importedShapes").append(importedShapeForm);
        $("#update"  + shapeId).click(function() {
            updateShape(i);
        });

        var geojsonObject =
        {
            "type": "FeatureCollection",
            "crs": {
                "type": "name",
                "properties": {
                    "name": "EPSG:4326"
                }
            },
            "features": [{"type":"Feature", "properties":{}, "geometry": JSON.parse(geoJson) }]
        };

        var vectorSource = new ol.source.Vector({
            features: (new ol.format.GeoJSON()).readFeatures(geojsonObject)
        });


        var vectorLayer = new ol.layer.Vector({
            source: vectorSource
        });

        var map = new ol.Map({
            target: mapId,
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                }),
                vectorLayer
            ],
            view: new ol.View({
                projection: 'EPSG:4326',
                center: [0,0],
                zoom: 2
            })
        });
    }
}

function getDate(stringDate)
{
    let parsedDate = new Date(stringDate).toISOString();

    return parsedDate.split("T")[0];
}

function updateShape(arrayId)
{
    let importedShape = importedShapes[arrayId];

    let id = importedShapes[arrayId].id;
    let newName = $("#name" + arrayId).val();
    let newSince = $("#validSince" + arrayId).val();
    let newUntil = $("#validUntil" + arrayId).val();
    let newClass = $("#class" + arrayId).val();

    let updateObj = {};
    updateObj.id = id;
    updateObj.name = newName;
    updateObj.validSince = newSince;
    updateObj.validUntil = newUntil;
    updateObj.classificationId = newClass;
    sendUpdateObj(updateObj);
}

function sendUpdateObj(obj)
{
    let key = $("#key").val();
    let xhr = new XMLHttpRequest();
    let url = baseUrl + "ShapeUpdate";

    let formData = new FormData();

    formData.append("tableName", key);
    formData.append("shapeData", JSON.stringify(obj));

    xhr.open("POST", url, true);

    xhr.onload = function (e)
    {
        if(this.status === 200)
        {
            let data = this.response;
            let dataObj = JSON.parse(data);

            console.log(dataObj);
        }
    };
    xhr.send(formData);
}