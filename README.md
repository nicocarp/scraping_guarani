# Trabajo Final Taller de Nuevas Tecnologias 2017 #

Scraping sobre [siu-guarani][guarani-path]. Ver [tablero][tablero-path] en Trello

[guarani-path]: http://www.dit.ing.unp.edu.ar/v2070/www/

[tablero-path]:https://trello.com/b/b76gl2IC/scraping-guarani


Alumnos
  - Calfuquir, Jorge Nicolas
  - Parra, Ivan
  - Santos, Carla

## Para ejecutar ##

### Con Docker ###

Crear la imagen:

    $ docker build --tag nombre_imagen_ionic

Iniciar contenedor:

    $ docker run -d -p 8100:8100 -p 35729:35729 -v $(pwd)/scrap-guarani/:/code nombre_imagen_ionic

En el navegador:

  - 0.0.0.0:8100
  
### Sin Docker ###

No se D: Ver la doc de Ionic
  
  - https://ionicframework.com/

