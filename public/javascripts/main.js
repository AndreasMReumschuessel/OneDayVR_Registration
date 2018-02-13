jQuery(document).ready(function () {
    jQuery.noConflict();
    console.log("Ready!");
    jQuery('#firma').click(function () {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz 50€\">Konferenz 50€</option>\n" +
            "<option value=\"Workshop + Konferenz 75€\">Workshop + Konferenz 75€ / AUSGEBUCHT</option>")
    });

    jQuery('#privat').click(function () {
        jQuery('#firmenname').attr("disabled", "disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz 50€\">Konferenz 50€</option>\n" +
            "<option value=\"Workshop + Konferenz 75€\">Workshop + Konferenz 75€ / AUSGEBUCHT</option>")
    });

    jQuery("#student").click(function() {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz 0€\">Konferenz 0€</option>\n" +
            "<option value=\"Workshop + Konferenz 0€\">Workshop + Konferenz 0€ / AUSGEBUCHT</option>")
    })

    jQuery("#premium").click(function() {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Workshop + Konferenz (1 Pers - 150 €)\">Workshop + Konferenz 150€ (1 Pers - 150 €)</option>\n" +
            "<option value=\"Workshop + Konferenz 280€ (2 Pers - 280 €)\">Workshop + Konferenz (2 Pers - 280 €)</option>" +
            "<option value=\"Workshop + Konferenz 500€ (4 Pers - 500 €)\">Workshop + Konferenz (4 Pers - 500 €)</option>")
    })



    jQuery('#email').on('keypress keyup keydown', function () {
        validateEmail(document.getElementById("email"))
    });
    jQuery('#vorname').on('keypress keyup keydown', function () {
        validateName(document.getElementById("vorname"), "vornameError")
        jQuery(document.getElementById("vorname").parentElement).css("margin-bottom", "0");
    });
    jQuery('#nachname').on('keypress keyup keydown', function () {
        validateName(document.getElementById("nachname"), "nachnameError")
    });
    jQuery('#strasse').on('keypress keyup keydown', function () {
        validateStreet(document.getElementById("strasse"), "streetError")
    });
    jQuery('#postleitzahl').on('keypress keyup keydown', function () {
        validatePlz(document.getElementById("postleitzahl"), "postleitzahlError")
    });
    jQuery('#ort').on('keypress keyup keydown', function () {
        validateOrt(document.getElementById("ort"), "ortError")
    });

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

function successfullySubmitted() {
    jQuery('#success').modal()
    var frm = document.getElementsByName('registrationForm')[0];
    frm.reset();
}

function objectifyForm(formArray) {//serialize data function

    var returnArray = {};
    for (var i = 0; i < formArray.length; i++) {
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}


function validate() {
    return(!(
            validateEmail(document.getElementById("email")) ||
            validateName(document.getElementById("vorname"), "vornameError") ||
            validateName(document.getElementById("nachname"), "nachnameError") ||
            validateStreet(document.getElementById("strasse"), "streetError") ||
            validatePlz(document.getElementById("postleitzahl"), "postleitzahlError") ||
            validateOrt(document.getElementById("ort"), "ortError")
        )
    )
}

function validateEmail(mail) {
    var value = mail.value;
    var txt = "";
    if (/(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/.test(value)) {
        jQuery(document.getElementById("email")).css("margin-bottom", "1em");
        txt = "";
        document.getElementById("emailError").innerHTML = txt;
        mail.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(document.getElementById("email")).css("margin-bottom", "0");
        txt = "Bitte geben sie eine gültige E-Mail Adresse an.";
        document.getElementById("emailError").innerHTML = txt;
        mail.style.borderColor = "red";
        return (false)
    }

}

function validateName(name, error) {
    var value = name.value;
    var txt = "";
    if (/^[A-z|üÜ|öÖ|äÄ|\s|\-\.]{2,}/.test(value)) {
        jQuery(jQuery(name).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(name).css("margin-bottom", "0"));
        txt = "Bitte geben sie einen gültigen Namen an.";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "red";
        return (false)
    }

}
function validateStreet(name, error) {
    var value = name.value;
    var txt = "";
    if (/^[A-z|üÜ|öÖ|äÄ|ß|\s|\-\.|0-9]{2,}/.test(value)) {
        jQuery(jQuery(name).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(name).css("margin-bottom", "0"));
        txt = "Bitte geben sie einen gültigen Straßenamen an.";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "red";
        return (false)
    }

}
function validateOrt(name, error) {
    var value = name.value;
    var txt = "";
    if (/^[A-z|üÜ|öÖ|äÄ|\s|\-\.]{2,}/.test(value)) {
        jQuery(jQuery(name).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(name).css("margin-bottom", "0"));
        txt = "Bitte geben sie einen gültige Ortsamen an.";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "red";
        return (false)
    }

}


function validatePlz(name, error) {
    var value = name.value;
    var txt = "";
    if (/^[0-9]+/.test(value)) {
        jQuery(jQuery(name).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(name).css("margin-bottom", "0"));
        txt = "Bitte geben sie eine gültige Postleitzahl an.";
        document.getElementById(error).innerHTML = txt;
        name.style.borderColor = "red";
        return (false)
    }

}

function myMap() {
    var oneday = {lat: 47.668637, lng: 9.168608};
    var focus = {lat: 47.6682692, lng: 9.1712247};
    var mapOptions = {
        center: focus,
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(document.getElementById("map"), mapOptions);
    var marker = new google.maps.Marker({
        position: oneday,
        map: map
    });
}
