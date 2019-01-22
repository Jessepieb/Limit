function getStationInfo() {
    var settings = {
      "async": true,
      "crossDomain": true,
      "url": "https://cors-anywhere.herokuapp.com/https://webservices.ns.nl/ns-api-avt?station="
       + prompt("Which station?", "Leeuwarden"), //Verander prompt() naar een andere input methode.
      "method": "GET",
      "headers": {
        "Authorization": "Basic aGllbTE1MDVAc3R1ZGVudC5uaGwubmw6SUxDcVVCLWRocFhsVU9UTnhJQWdvd1VOX05LOEM2MmhhMkowaTE4ZDV1aWZ4eXVoVFNQd0p3",
        "cache-control": "no-cache",
        "Postman-Token": "1b2678c7-ed08-403f-bf14-7db0a5ef205e"
      }
    }

    function printtrein(trein) {
        var ritnummer = trein.children[0].textContent;
        var vertrekTijd = trein.children[1].textContent
        var eindbestemming = trein.children[2].textContent;
        var treinsoort = trein.children[3].textContent;
        var routetekst;
        var vervoerder;
        var vertrekspoor;
        if(trein.children[6] != undefined) {
            routetekst = trein.children[4].textContent;
            vervoerder = trein.children[5].textContent;
            vertrekspoor = trein.children[6].textContent;
        } else {
            vervoerder = trein.children[4].textContent;
            vertrekspoor = trein.children[5].textContent;
        }
        console.log(eindbestemming + " / " + vertrekTijd + " / " + vertrekspoor);
    }

    $.ajax(settings).done(function (response) {
        var treins = $(response).context.children[0];
        //console.log(treins);
        console.log("Eindbestemming / VertrekTijd  / Spoor");
        var i = 0;
        var trein = treins.children[i];
        
        while (trein != undefined) {
            printtrein(trein);
            i++;
            trein = treins.children[i];
        }
    });
}

function getStationErrorInfo() {
    var settings = {
      "async": true,
      "crossDomain": true,
      "url": "https://cors-anywhere.herokuapp.com/http://webservices.ns.nl/ns-api-storingen?station="
       + prompt("Which station?", "Leeuwarden"), //Verander prompt() naar een andere input methode.
      "method": "GET",
      "headers": {
        "Authorization": "Basic aGllbTE1MDVAc3R1ZGVudC5uaGwubmw6SUxDcVVCLWRocFhsVU9UTnhJQWdvd1VOX05LOEM2MmhhMkowaTE4ZDV1aWZ4eXVoVFNQd0p3",
        "cache-control": "no-cache",
        "Postman-Token": "1b2678c7-ed08-403f-bf14-7db0a5ef205e"
      }
    }

    function printError(error) {
        var id = error.children[0].textContent;
        var traject = error.children[1].textContent;
        var periode = error.children[2].textContent;
        var bericht = error.children[3].textContent;

        console.log(id + " / " + traject + " / " + periode);
        console.log(bericht);
    }

    $.ajax(settings).done(function (response) {
        var errors = $(response).context.children[0];
        var i = 0;
        var j = 0;
        var gepland = errors.children[0].children[i];
        var ongepland = errors.children[1].children[j];
        console.log(ongepland);
        while (gepland != undefined) {
            printError(gepland);
            i++;
            gepland = errors.children[0].children[i];
        }
        if (ongepland != undefined) {
            printError(ongepland);
            j++;
            var ongepland = errors.children[1].children[j];
        }

    });
}

function planJourney() {
    function fixInt(int) {
        var toReturn = "";
        if(int < 10) {
            toReturn += "0";
        }
        return (toReturn + int.toString());
    }
    var date = new Date();
    var now = date.getFullYear() + "-" + fixInt(date.getMonth() + 1) + "-" + fixInt(date.getDate()) + "T"
    + fixInt(date.getHours()) + ":" + fixInt(date.getMinutes());
    console.log(now);
    var from = prompt("From?", "Leeuwarden");
    var to = prompt("To?", "Heerenveen");
    var dep = prompt("Vertrektijd = true. Aankomsttijd = false", "true");
    var time = prompt("Datetime in YYYY-MM-DDTHH:MM", now);
    var settings = {
          "async": true,
          "crossDomain": true,
          "url": "https://cors-anywhere.herokuapp.com/http://webservices.ns.nl/ns-api-treinplanner?fromStation="
           + from + "&toStation=" + to + "&Departure=" + dep + "&dateTime=" + time, //Verander prompt() naar een andere input methode.
          "method": "GET",
          "headers": {
            "Authorization": "Basic aGllbTE1MDVAc3R1ZGVudC5uaGwubmw6SUxDcVVCLWRocFhsVU9UTnhJQWdvd1VOX05LOEM2MmhhMkowaTE4ZDV1aWZ4eXVoVFNQd0p3",
            "cache-control": "no-cache",
            "Postman-Token": "1b2678c7-ed08-403f-bf14-7db0a5ef205e"
          }
    }

    function Reis(reis) {
        this.aantalOverstappen = reis.children[0].textContent;
        this.geplandeReisTijd = reis.children[1].textContent;
        this.actueleReisTijd = reis.children[2].textContent;
        this.optimaal = reis.children[3].textContent;
        this.geplandeVertrekTijd = reis.children[4].textContent;
        this.actueleVertrekTijd = reis.children[5].textContent;
        this.geplandeAankomstTijd = reis.children[6].textContent;
        this.actueleAankomstTijd = reis.children[7].textContent;
        this.status = reis.children[8].textContent;
        this.reisdeel = reis.children[9];

        if(this.reisdeel.children[0] != undefined) {
            
            this.vervoerder = this.reisdeel.children[0].textContent;
            this.vervoerType = this.reisdeel.children[1].textContent;
            this.ritNummer = this.reisdeel.children[2].textContent;
            this.status = this.reisdeel.children[3].textContent;

            this.reisStops = [];
            var i = 4;
            //0: Naam van tussenstation
            //1: Tijd van aankomst tussenstation
            //2: Perron (Niet altijd van toepassing)
            while(this.reisdeel.children[i] != undefined) {
                if(this.reisdeel.children[i].children[2] != undefined) {
                    this.reisStops[i] = (
                    this.reisdeel.children[i].children[0].textContent,
                    this.reisdeel.children[i].children[1].textContent,
                    this.reisdeel.children[i].children[2].textContent);
                } else {
                    this.reisStops[i] = (
                    this.reisdeel.children[i].children[0].textContent,
                    this.reisdeel.children[i].children[1].textContent);
                }
                i++;
            }
        }
        this.printTijd = function () {
            if(dep) {
                var hour = this.actueleVertrekTijd.substring(11, 13);
                var min = this.actueleVertrekTijd.substring(14, 16);
            }
            else {
                var hour = this.actueleAankomstTijd.substring(11, 13);
                var min = this.actueleAankomstTijd.substring(14, 16);
            }
            return (hour + ":" + min);
        }
        this.toString = function () {
            return from + ": " + this.geplandeVertrekTijd + " " + to + ": " + this.geplandeAankomstTijd;
        }
        this.hasLeft = function () {
            var year = this.actueleVertrekTijd.substring(0, 4);
            var month = this.actueleVertrekTijd.substring(5, 7);
            var day = this.actueleVertrekTijd.substring(8, 10);
            var hour = this.actueleVertrekTijd.substring(11, 13);
            var min = this.actueleVertrekTijd.substring(14, 16);

            if (year >= date.getFullYear() && month >= fixInt(date.getMonth() + 1) && day >= fixInt(date.getDate())
            && (hour > fixInt(date.getHours()) || hour == fixInt(date.getHours()) && min >= fixInt(date.getMinutes()))) {
                console.log("Test");
                return false;
            }
            return true;
        }
        //console.log(actueleVertrekTijd + " / " + actueleAankomstTijd);
    }

    $.ajax(settings).done(function (response) {
        var reismogelijkheidLijst = $(response).context.children[0];
        var i = 0;
        var j = 0;
        var reizen = [];

        while (reismogelijkheidLijst.children[i] != undefined) {
            reizen[i] = new Reis(reismogelijkheidLijst.children[i]);
            //Eerste trein die niet vertrokken is bijhouden.
            if (reizen[i].hasLeft()) {
                j++;
            } else if (j == 0) {
                j == i;
            }
            console.log(reizen[i].toString());
            i++;
        }

        var eersteReis = reizen[j];
        var tweedeReis = reizen[j + 1];

        console.log("Take the " + eersteReis.actueleVertrekTijd + " train or the " + tweedeReis.actueleVertrekTijd +
        "train.");
        document.getElementById('advice').innerHTML = "Take the " + eersteReis.printTijd() + " train or the " + tweedeReis.printTijd() +
        " train.";
    });
}

//getStationInfo();
//getStationErrorInfo();
planJourney();