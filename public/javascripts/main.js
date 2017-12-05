$( document ).ready(function() {
    console.log( "Ready!" );
    $('#firma').click(function()
    {
        $('#firmenname').removeAttr("disabled");
    });

    $('#privat').click(function()
    {
        $('#firmenname').attr("disabled","disabled");
    });
});

function submitForm() {
    var formData = JSON.stringify(objectifyForm($("#registrationForm").serializeArray()));
    $.ajax({
        type: "POST",
        url: "/saveStock",
        data: formData,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    });
}
function objectifyForm(formArray) {//serialize data function

    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}