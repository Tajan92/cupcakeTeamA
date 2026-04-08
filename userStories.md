# Must haves
    css variabler for farver og størrelse
    Orden i css filer evt. flere filer?? 


# User Stories

### US-1: Som kunde kan jeg bestille og betale cupcakes med en valgfri bund og top, sådan at jeg senere kan køre forbi butikken i Olsker og hente min ordre.
    Userclass, cupcakeclass(decorator pattern), usercontroller, usermapper
    cupcakecontroller, cupcakemapper, db(user, cupcake), html/css

### US-2 Som kunde kan jeg oprette en konto/profil for at kunne betale og gemme en en ordre.
    Userclass, usermapper, usercontroller, db(user), html/css, validatorclass

### US-3: Som administrator kan jeg indsætte beløb på en kundes konto direkte i Postgres, så en kunde kan betale for sine ordrer.
    Userclass(role for user and admin), usermapper, usercontroller, db(user), html

### US-4: Som kunde kan jeg se mine valgte ordrelinier i en indkøbskurv, så jeg kan se den samlede pris.
    Userclass, usermapper, usercontroller, db(user, basket), html/css

### US-5: Som kunde eller administrator kan jeg logge på systemet med email og kodeord. Når jeg er logget på, skal jeg kunne se min email på hver side (evt. i topmenuen, som vist på mockup’en).
    userclass, usermapper, usercontroller, validatorclass, db(user), html/css

### US-6: Som administrator kan jeg se alle ordrer i systemet, så jeg kan se hvad der er blevet bestilt.
    userclass, usermapper, usercontroller, db(user), basket, html/css

### US-7: Som administrator kan jeg se alle kunder i systemet og deres ordrer, sådan at jeg kan følge op på ordrer og holde styr på mine kunder.
    userclass, usermapper, usercontroller, db(user), basket, html/css

### US-8: Som kunde kan jeg fjerne en ordrelinie fra min indkøbskurv, så jeg kan justere min ordre.
    userclass, usermapper, usercontroller, db(user), basket, html/css

### US-9: Som administrator kan jeg fjerne en ordre, så systemet ikke kommer til at indeholde udgyldige ordrer. F.eks. hvis kunden aldrig har betalt.
    userclass, usermapper, usercontroller, db(user), basket, html/css
