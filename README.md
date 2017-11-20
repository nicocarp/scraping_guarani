# Trabajo Final Taller de Nuevas Tecnologias 2017 #

Aplicacion movil desarrollada con [Ionic][ionic-path] para realizar Scraping sobre [siu-guarani][guarani-path]. Ver [tablero][tablero-path] en Trello

[ionic-path]:https://ionicframework.com/
[guarani-path]: http://www.dit.ing.unp.edu.ar/v2070/www/

[tablero-path]:https://trello.com/b/b76gl2IC/scraping-guarani


Alumnos
  - Calfuquir, Jorge Nicolas
  - Parra, Ivan
  - Santos, Carla

## Requisitos ##

  - Node
  - cordova
  - ionic

## Para ejecutar ##

### Con Docker ###
Hay una issue: no se reflejan los cambios de codigo dinamicamente.

Crear la imagen:

    $ docker build --tag nombre_imagen_ionic

Iniciar contenedor:

    $ docker run -d -p 8100:8100 -p 35729:35729 -v $(pwd)/scrap-guarani/:/code nombre_imagen_ionic

En el navegador:

  - 0.0.0.0:8100
  
### Sin Docker ###

Ver doc de ionic:
  - https://ionicframework.com/

Instalamos dependencias:
  
    $ curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
    $ sudo apt-get install -y nodejs
    $ npm install -g cordova ionic

Dentro de la carpeta del proyecto:
    
    $ npm install
    $ ionic serve

En el navegador:
  
  - localhost:8100





