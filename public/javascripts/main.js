jQuery( document ).ready(function() {
    jQuery.noConflict();
    console.log( "Ready!" );
    jQuery('#firma').click(function()
    {
        jQuery('#firmenname').removeAttr("disabled");
    });

    jQuery('#privat').click(function()
    {
        jQuery('#firmenname').attr("disabled","disabled");
    });
    jQuery('#anrede').fancySelect();
    jQuery('#titel').fancySelect();
    jQuery("#registrationForm").submit(function(e){
        e.preventDefault();
        if(!validate())
            submitForm();
    });
});

function submitForm() {
    var formData = JSON.stringify(objectifyForm(jQuery("#registrationForm").serializeArray()));
    jQuery.ajax({
        type: "POST",
        url: "/saveStock",
        data: formData,
        dataType: 'html',
        contentType: 'application/json; charset=utf-8',
        success: successfullySubmitted,
        error: failSubmit
    });
}


function failSubmit() {
    jQuery('#failure').modal()
}

function successfullySubmitted(){
    jQuery('#success').modal()
}
function objectifyForm(formArray) {//serialize data function

    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}



function validate() {
    return(!(
        validateEmail(document.getElementById("email")) ||
        validatePhone(document.getElementById("telefon")) ||
        validateName(document.getElementById("vorname"), "vornameError") ||
        validateName(document.getElementById("nachname"), "nachnameError") ||
        validateEmpty(document.getElementById("strasse"), "streetError") ||
        validatePlz(document.getElementById("postleitzahl"), "postleitzahlError") ||
        validateEmpty(document.getElementById("ort"), "ortError")
        )
    )
}

function validateEmail(mail)
{
    var value = mail.value;
    var txt = "";
    if (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value))
    {
        return (true);
    }
    txt = "Bitte geben sie eine gültige E-Mail Adresse an.";
    document.getElementById("emailError").innerHTML = txt;
    mail.style.borderColor = "red";
    return (false)
}
function validatePhone(phone){
    var value = phone.value;
    var txt = "";
    var phoneNum = value.replace(/[^\d]/g, '');
    if(phoneNum.length > 1) {
        return true;
    }
    txt = "Bitte geben sie eine gültige Telefonnummer an."
    document.getElementById("telefonError").innerHTML = txt;
    phone.style.borderColor = "red";
    return (false);
}
function validateName(name, error)
{
    var value = name.value;
    var txt = "";
    if (/^[a-zA-Z ]+$/.test(value))
    {
        return (true);
    }
    txt = "Bitte geben sie einen gültigen Namen an.";
    document.getElementById(error).innerHTML = txt;
    name.style.borderColor = "red";
    return (false)
}
function validateEmpty(string, error)
{
    var txt = "";
    if (string.value != "")
    {
        return (true);
    }

    txt = "Bitte geben sie einen gültigen Wert an.";
    document.getElementById(error).innerHTML = txt;
    string.style.borderColor = "red";
    return (false)
}
function validatePlz(string, error)
{
    var txt = "";
    if (string.value != "" && string.value.length < 10)
    {
        return (true);
    }

    txt = "Bitte geben sie eine gültige Postleitzahl an.";
    document.getElementById(error).innerHTML = txt;
    string.style.borderColor = "red";
    return (false)
}
function myMap() {
    var oneday = {lat: 47.668637, lng: 9.168608};
    var focus = {lat: 47.6682692, lng: 9.1712247};
    var mapOptions = {
        center: focus,
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.HYBRID
    }
    var map = new google.maps.Map(document.getElementById("map"), mapOptions);
    var marker = new google.maps.Marker({
        position: oneday,
        map: map
    });
}
