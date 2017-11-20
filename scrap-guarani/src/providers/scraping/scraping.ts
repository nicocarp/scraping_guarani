import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the ScrapingProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class ScrapingProvider {
  
  url = "http://www.dit.ing.unp.edu.ar/v2070/www/";
  constructor(public http: Http) {
    console.log('Hello ScrapingProvider Provider');
  }

  getLog(){
    return "hola";
  }

  probandoHttp(){
    console.log("asds");
    this.http.get('http://www.dit.ing.unp.edu.ar/v2070/www/').subscribe(data => {
        console.log(data);
      }, err=>{console.log("ERROR"+err)});
    }
}
