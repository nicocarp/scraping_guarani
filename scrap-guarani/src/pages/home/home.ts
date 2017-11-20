import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { ScrapingProvider} from '../../providers/scraping/scraping'; 

@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
  providers:[ScrapingProvider]
})
export class HomePage {
  scrapingProvider: ScrapingProvider;

  constructor(public navCtrl: NavController, scrapingProvider: ScrapingProvider) {
    this.scrapingProvider=scrapingProvider;
    this.guarani();
  }

  guarani(){
    console.log("0-por pedir detras");
    this.scrapingProvider.probandoHttp();
  }
     

}
