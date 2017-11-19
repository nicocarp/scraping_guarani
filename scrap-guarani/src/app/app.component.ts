import { Component, ViewChild } from '@angular/core';
import { Platform, Nav } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { HomePage } from '../pages/home/home';
import { LoginPage } from '../pages/login/login';
import { ScrapingProvider } from '../providers/scraping/scraping'

@Component({
  templateUrl: 'app.html'  
})
export class MyApp {

  @ViewChild('NAV') nav: Nav;
  // atributos de nuestra clase.
  public rootPage:any;
  public pages: Array<{ titulo: String, component: any, icon: String }> ;

  constructor( platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen) {

    //console.log("Contructor "+scraping.getLog());

    this.rootPage = HomePage;
    this.pages = [
      {titulo: 'Inicio', component: HomePage, icon:'home'},
      {titulo: 'Login', component: LoginPage, icon:'log-in'},
      {titulo: 'Acerca de', component: LoginPage, icon:'information-circle'},
    ]

    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });
  }

  goToPage(page){
    this.nav.setRoot(page);
  }
 
}

