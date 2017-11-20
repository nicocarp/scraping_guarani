import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import * as  cheerio  from 'cheerio';

import 'rxjs/add/operator/map';

@Injectable()
export class ScrapingProvider {
  
  base_url = "http://www.dit.ing.unp.edu.ar/v2070/www/";
  
  frames_url = [
    "includes/encabezado.inc.php",
    "includes/barra.inc.php",
    "a_general/operaciones.php",
    "folder.php",
    "a_general/identificarse.php?qs=5a13196fabeef3.46151977"
  ]
  
  constructor(public http: Http) {
    console.log('Hello ScrapingProvider Provider');
  }
  
  login(user:string, passwrod: string){
    console.log("0-LOGINnnnnnn");
    var url_login = this.base_url+this.frames_url[1];
    console.log(url_login);
    
    this.http.get(url_login).subscribe(data => {
      var $ = cheerio.load(data.text());
      var url_form = $("a").map(function (){
        return $(this).attr('href').replace('../', '');
      })
      // obtenemos la url para el form de login
      var url_form_login = this.base_url + url_form[0];
      console.log(url_form_login);
      // AHORA PEDIMOS HTML PARA FORMULARIO DE LOGIN
      this.http.get(url_form_login).subscribe(data => {
          var $ = cheerio.load(data.text());
          $("#fUsuario").val("38147310");
          $("#bClave").val("38147310");
          
      })
      console.log("FIN DE TODOOOOOOOOOO");
      

    });
    console.log("FIN LOGIN SCRAPING")
  }

  probandoHttp(){

    console.log("asds");
    return this.http.get('http://www.dit.ing.unp.edu.ar/v2070/www/');
  }
    
}
