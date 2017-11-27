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