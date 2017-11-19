FROM node:8.8.1

RUN mkdir code

WORKDIR code

RUN npm install -g cordova ionic
RUN npm install -g bower
RUN npm install -g gulp

#RUN useradd -ms /bin/bash user
#RUN chown -R user:user /code

RUN npm install
# UID en contenedor igual al UID fuera.
USER 1000 

EXPOSE 8100 35729

CMD ["ionic", "serve", "--address", "0.0.0.0"]

