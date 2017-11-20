import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { ScrapingProvider} from '../../providers/scraping/scraping'; 
import * as  cheerio  from 'cheerio';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
  providers:[ScrapingProvider]
})
export class HomePage {
  scrapingProvider: ScrapingProvider;

  constructor(public  navCtrl: NavController, scrapingProvider: ScrapingProvider) {
    this.scrapingProvider=scrapingProvider;    
  }
  
  async ngOnInit(){
    this.login();

  }
  async login(){
    this.scrapingProvider.login("usuario", "password");
    
  }
  /* Ejemplo minimo de prueba de scraping. BORRAR */ 
  guarani(){
    console.log("0-por pedir detras");    
    this.scrapingProvider.probandoHttp().subscribe(data => {
     
      var $ = cheerio.load(data.text());
      var arreglo = $("frame").map(function(){
        return $(this).attr('src');
      });
      
      console.log("arreglo");
      console.log(arreglo);
    
    }, err=>{console.log("ERROR"+err)});  
  }
     

}
