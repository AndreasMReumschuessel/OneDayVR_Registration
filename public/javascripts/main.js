jQuery(document).ready(function () {
    jQuery.noConflict();
    console.log("Ready!");
    jQuery('#firma').click(function () {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz + Workshop 75€\">Konferenz + Workshop 75€</option>\n" +
            "<option value=\"Konferenz 50€\">Workshop 50€</option>")
    });

    jQuery('#privat').click(function () {
        jQuery('#firmenname').attr("disabled", "disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz + Workshop 75€\">Konferenz + Workshop 75€</option>\n" +
            "<option value=\"Konferenz 50€\">Workshop 50€</option>")
    });

    jQuery("#student").click(function() {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz + Workshop 0€\">Konferenz + Workshop 0€</option>\n" +
            "<option value=\"Konferenz 0€\">Workshop 0€</option>")
    })

    jQuery("#premium").click(function() {
        jQuery('#firmenname').removeAttr("disabled");
        jQuery('#ticket').children().remove();
        jQuery('#ticket').append("<option value=\"Konferenz + Workshop 150€\">Konferenz + Workshop 150€</option>\n" +
            "<option value=\"Konferenz + Workshop 280€\">Konferenz + Workshop 280€</option>" +
            "<option value=\"Konferenz + Workshop 500€\">Konferenz + Workshop 500€</option>")
    })



    jQuery('#email').on('keypress keyup keydown', function () {
        validateEmail(document.getElementById("email"))
    });
    jQuery('#telefon').on('keypress keyup keydown', function () {
        validatePhone(document.getElementById("telefon"))
    });
    jQuery('#vorname').on('keypress keyup keydown', function () {
        validateName(document.getElementById("vorname"), "vornameError")
        jQuery(document.getElementById("vorname").parentElement).css("margin-bottom", "0");
    });
    jQuery('#nachname').on('keypress keyup keydown', function () {
        validateName(document.getElementById("nachname"), "nachnameError")
    });
    jQuery('#strasse').on('keypress keyup keydown', function () {
        validateEmpty(document.getElementById("strasse"), "streetError")
    });
    jQuery('#postleitzahl').on('keypress keyup keydown', function () {
        validatePlz(document.getElementById("postleitzahl"), "postleitzahlError")
    });
    jQuery('#ort').on('keypress keyup keydown', function () {
        validateEmpty(document.getElementById("ort"), "ortError")
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
    jQuery('#firmenname').attr("disabled", "disabled");
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
            validatePhone(document.getElementById("telefon")) ||
            validateName(document.getElementById("vorname"), "vornameError") ||
            validateName(document.getElementById("nachname"), "nachnameError") ||
            validateEmpty(document.getElementById("strasse"), "streetError") ||
            validatePlz(document.getElementById("postleitzahl"), "postleitzahlError") ||
            validateEmpty(document.getElementById("ort"), "ortError")
        )
    )
}

function validateEmail(mail) {
    var value = mail.value;
    var txt = "";
    if (/^([\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00dfa-zA-Z0-9_\.\-])+\@(([\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00dfa-zA-Z0-9\-])+\.)+([\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00dfa-zA-Z0-9]{2,4})+$/.test(value)) {
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

function validatePhone(phone) {
    var value = phone.value;
    var txt = "";
    var phoneNum = value.replace(/[^\d]/g, '');
    if (phoneNum.length > 1) {
        jQuery(document.getElementById("telefon")).css("margin-bottom", "1em");
        txt = "";
        document.getElementById("telefonError").innerHTML = txt;
        phone.style.borderColor = "#ced4da";
        return true;
    } else {
        jQuery(document.getElementById("telefon")).css("margin-bottom", "0");
        txt = "Bitte geben sie eine gültige Telefonnummer an."
        document.getElementById("telefonError").innerHTML = txt;
        phone.style.borderColor = "red";
        return (false);
    }

}

function validateName(name, error) {
    var value = name.value;
    var txt = "";
    if (/^[a-zA-Z -\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00df]+$/.test(value)) {
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

function validateEmpty(string, error) {
    var txt = "";
    if (string.value != "") {
        jQuery(jQuery(string).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        string.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(string).css("margin-bottom", "0"));
        txt = "Bitte geben sie einen gültigen Wert an.";
        document.getElementById(error).innerHTML = txt;
        string.style.borderColor = "red";
        return (false)
    }


}

function validatePlz(string, error) {
    var txt = "";
    if (string.value != "" && string.value.length < 10) {
        jQuery(jQuery(string).css("margin-bottom", "1em"));
        txt = "";
        document.getElementById(error).innerHTML = txt;
        string.style.borderColor = "#ced4da";
        return (true);
    } else {
        jQuery(jQuery(string).css("margin-bottom", "0"));
        txt = "Bitte geben sie eine gültige Postleitzahl an.";
        document.getElementById(error).innerHTML = txt;
        string.style.borderColor = "red";
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
