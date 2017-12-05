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
        dataType: 'html',
        contentType: 'application/json; charset=utf-8',
        success: successfullySubmitted
    });
}

function successfullySubmitted(){
    let p = $('#registrierung');
    p.modal('toggle');
    p.after("<div class='alert alert-success alert-dismissable'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a> <strong>Resgistrierung erfolgreich!</strong> Wir freuen uns auf Ihr kommen. </div>")

}
function objectifyForm(formArray) {//serialize data function

    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}