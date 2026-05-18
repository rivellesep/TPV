# Projecte Final - TPV

## Descripció del Projecte
Aquesta és una aplicació de Terminal de Punt de Venda (TPV) dissenyada per gestionar una petita botiga de barri especialitzada exclusivament en la venda de pantalons i camises. Aquest projecte s'ha desenvolupat per al mòdul MP0485 de Programació.

## Funcionalitats Principals
El sistema permet realitzar les següents operacions:

* **Inicialització d'articles:** Càrrega automàtica de l'estoc mitjançant la lectura d'un fitxer JSON (`articles.json`).
* **Gestió de dades (CRUD):** Administració d'articles i clients, permetent altes, baixes, modificacions i consultes.
* **Sistema de Vendes (TPV):** Registre de compres, generació de línies de factura, actualització d'estoc en temps real i simulació d'impressió del tiquet final.
* **Consultes i Estadístiques:** Visualització de l'historial de vendes filtrat per client o per article.
* **Càlcul de Beneficis:** Generació d'informes de costos de producció, vendes i beneficis obtinguts per cada article.
* **Recompra Automàtica:** Sistema que genera una proposta de compra en format JSON per als articles que tenen l'estoc per sota d'un llindar establert.